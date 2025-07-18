package com.assignment.loanAccount.LuckPayAssignment.service;

import com.assignment.loanAccount.LuckPayAssignment.dto.LoanResponseDTO;
import com.assignment.loanAccount.LuckPayAssignment.integration.ExternalLoanApiClient;
import com.assignment.loanAccount.LuckPayAssignment.integration.LoanApiResponse;
import com.assignment.loanAccount.LuckPayAssignment.model.LoanAccount;
import com.assignment.loanAccount.LuckPayAssignment.model.LoanEmi;
import com.assignment.loanAccount.LuckPayAssignment.repository.LoanAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class LoanAccountService {

    private static final Logger logger = LoggerFactory.getLogger(LoanAccountService.class);

    @Autowired
    private LoanAccountRepository loanAccountRepository;

    @Autowired
    private ExternalLoanApiClient externalLoanApiClient;

    public LoanResponseDTO handleLoanRequest(String loanAccountNumber) {
        logger.info("Starting loan request processing for account: {}", loanAccountNumber);

        try {
            // Call external API
            logger.debug("Calling external API for loan account: {}", loanAccountNumber);
            LoanApiResponse apiResponse = externalLoanApiClient.fetchLoanDetails(loanAccountNumber);

            if (apiResponse == null) {
                logger.warn("External API returned null response for account: {}", loanAccountNumber);
                return null;
            }

            logger.debug("Received API response with {} EMI details for account: {}",
                    apiResponse.getEmiDetails().size(), loanAccountNumber);

            // Find next due EMI
            Optional<LoanEmi> nextDue = apiResponse.getEmiDetails().stream()
                    .filter(LoanEmi::isDueStatus)
                    .findFirst();

            if (nextDue.isPresent()) {
                LoanEmi emi = nextDue.get();
                logger.info("Found due EMI for account: {}, month: {}, amount: {}",
                        loanAccountNumber, emi.getMonth(), emi.getEmiAmount());

                // Parse date
                LocalDate dueDate = parseEmiDate(emi.getMonth());
                if (dueDate == null) {
                    logger.error("Failed to parse EMI date: {} for account: {}",
                            emi.getMonth(), loanAccountNumber);
                    return null;
                }

                // Save to database
                logger.debug("Saving loan account to database: {}", loanAccountNumber);
                LoanAccount account = new LoanAccount(
                        apiResponse.getLoanAccountNumber(),
                        dueDate,
                        emi.getEmiAmount()
                );

                LoanAccount savedAccount = loanAccountRepository.save(account);
                logger.info("Successfully saved loan account: {} to database", loanAccountNumber);

                // Create response DTO
                LoanResponseDTO response = new LoanResponseDTO(
                        savedAccount.getLoanAccountNumber(),
                        savedAccount.getDueDate(),
                        savedAccount.getEmiAmount()
                );

                logger.debug("Created response DTO for account: {}", loanAccountNumber);
                return response;

            } else {
                logger.warn("No due EMI found for account: {}", loanAccountNumber);
                return null;
            }

        } catch (Exception e) {
            logger.error("Error in loan request processing for account: {}", loanAccountNumber, e);
            return null;
        }
    }

    private LocalDate parseEmiDate(String monthString) {
        logger.debug("Parsing EMI date string: {}", monthString);

        try {
            // Convert "April 2024" to "2024-04-01"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy d");
            LocalDate date = LocalDate.parse(monthString + " 1", formatter);
            logger.debug("Successfully parsed date: {} to {}", monthString, date);
            return date;

        } catch (DateTimeParseException e) {
            logger.error("Failed to parse EMI date: {}", monthString, e);
            return null;
        }
    }
}
