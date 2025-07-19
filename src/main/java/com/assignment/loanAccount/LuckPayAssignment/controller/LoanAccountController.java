package com.assignment.loanAccount.LuckPayAssignment.controller;

import com.assignment.loanAccount.LuckPayAssignment.dto.LoanResponseDTO;
import com.assignment.loanAccount.LuckPayAssignment.service.LoanAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loanaccount")
public class LoanAccountController {
    private static final Logger logger = LoggerFactory.getLogger(LoanAccountController.class);
    @Autowired
    private LoanAccountService loanAccountService;

    @GetMapping("/{loanAccountNumber}")
    public ResponseEntity<LoanResponseDTO> getLoanDetails(@PathVariable String loanAccountNumber) {
        logger.info("Received loan details request for account: {}", loanAccountNumber);
        logger.info("Processing GET request for loan account: {}", loanAccountNumber);
        try {
            long startTime = System.currentTimeMillis();

            // Validate input
            if (loanAccountNumber == null || loanAccountNumber.trim().isEmpty()) {
                logger.warn("Invalid loan account number provided: {}", loanAccountNumber);
                return ResponseEntity.badRequest().build();
            }

            LoanResponseDTO dto = loanAccountService.handleLoanRequest(loanAccountNumber);

            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;

            if (dto != null) {
                logger.info("Successfully processed loan request for account: {} in {}ms",
                        loanAccountNumber, processingTime);
                logger.info("Response data: loanAccountNumber={}, dueDate={}, emiAmount={}",
                        dto.getLoanAccountNumber(), dto.getDueDate(), dto.getEmiAmount());
                return ResponseEntity.ok(dto);
            } else {
                logger.warn("No loan details found for account: {}", loanAccountNumber);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            logger.error("Error processing loan request for account: {}", loanAccountNumber, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
