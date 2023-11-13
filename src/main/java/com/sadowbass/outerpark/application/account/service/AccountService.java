package com.sadowbass.outerpark.application.account.service;

import com.sadowbass.outerpark.application.account.Account;
import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException;
import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        Account exist = accountRepository.findByEmail(signUpRequest.getEmail());
        if (exist != null) {
            throw new DuplicateEmailException(signUpRequest.getEmail());
        }

        Account account = Account.create(
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getName(),
                signUpRequest.getNickname(),
                signUpRequest.getPhone()
        );

        accountRepository.save(account);

        account.addCreator(account.getId());
        accountRepository.updateCreateData(account);
    }
}
