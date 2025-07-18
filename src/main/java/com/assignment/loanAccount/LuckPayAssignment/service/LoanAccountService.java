package com.assignment.loanAccount.LuckPayAssignment.service;

import com.assignment.loanAccount.LuckPayAssignment.dto.LoanResponseDTO;
import com.assignment.loanAccount.LuckPayAssignment.integration.ExternalLoanApiClient;
import com.assignment.loanAccount.LuckPayAssignment.integration.LoanApiResponse;
import com.assignment.loanAccount.LuckPayAssignment.model.LoanAccount;
import com.assignment.loanAccount.LuckPayAssignment.model.LoanEmi;
import com.assignment.loanAccount.LuckPayAssignment.repository.LoanAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class LoanAccountService {

    @Autowired
    private LoanAccountRepository loanAccountRepository;
    @Autowired
    private ExternalLoanApiClient externalLoanApiClient;

    public LoanResponseDTO handleLoanRequest(String loanAccountNumber) {
        LoanApiResponse apiResponse = externalLoanApiClient.fetchLoanDetails(loanAccountNumber);
        Optional<LoanEmi> nextDue = apiResponse.getEmiDetails().stream()
                .filter(LoanEmi::isDueStatus)
                .findFirst();

        if(nextDue.isPresent()) {
            LoanEmi emi = nextDue.get();
            // Example: Convert "April 2024" to "2024-04-01"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy d");
            LocalDate dueDate = LocalDate.parse(emi.getMonth() + " 1", formatter);

            LoanAccount account = new LoanAccount(apiResponse.getLoanAccountNumber(), dueDate, emi.getEmiAmount());
            loanAccountRepository.save(account);

            return new LoanResponseDTO(account.getLoanAccountNumber(), account.getDueDate(), account.getEmiAmount());
        } else {
            // handle case where no due EMI found (throw custom exception, etc.)
            return null;
        }
    }
}
