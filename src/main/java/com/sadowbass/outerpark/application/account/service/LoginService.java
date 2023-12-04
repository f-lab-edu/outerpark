package com.sadowbass.outerpark.application.account.service;

import com.sadowbass.outerpark.application.account.domain.Account;
import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.account.exception.InvalidLoginInformationException;
import com.sadowbass.outerpark.application.account.exception.NoSuchAccountDataException;
import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.infra.session.LoginManager;
import com.sadowbass.outerpark.infra.utils.PasswordUtils;
import com.sadowbass.outerpark.presentation.dto.account.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;
    private final LoginManager loginManager;

    @Transactional(readOnly = true)
    public LoginResult login(LoginRequest loginRequest) {
        Account account = accountRepository.findByEmail(loginRequest.getEmail());
        valid(loginRequest, account);

        LoginResult loginResult = new LoginResult(account.getEmail());
        loginManager.login(loginResult);
        
        return loginResult;
    }

    private void valid(LoginRequest loginRequest, Account account) {
        if (account == null) {
            throw new NoSuchAccountDataException();
        }

        if (!PasswordUtils.verify(loginRequest.getPassword(), account.getPassword())) {
            throw new InvalidLoginInformationException();
        }
    }
}
