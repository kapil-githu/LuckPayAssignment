package com.assignment.loanAccount.LuckPayAssignment.repository;

import com.assignment.loanAccount.LuckPayAssignment.model.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanAccountRepository extends JpaRepository<LoanAccount, String> {

}
