package com.assignment.loanAccount.LuckPayAssignment.controller;

import com.assignment.loanAccount.LuckPayAssignment.dto.LoanResponseDTO;
import com.assignment.loanAccount.LuckPayAssignment.service.LoanAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loanaccount")
public class LoanAccountController {
    @Autowired
    private LoanAccountService loanAccountService;

    @GetMapping("/{loanAccountNumber}")
    public ResponseEntity<LoanResponseDTO> getLoanDetails(@PathVariable String loanAccountNumber) {
        LoanResponseDTO dto = loanAccountService.handleLoanRequest(loanAccountNumber);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
