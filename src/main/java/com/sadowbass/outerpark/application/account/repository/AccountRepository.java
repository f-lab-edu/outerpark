package com.sadowbass.outerpark.application.account.repository;

import com.sadowbass.outerpark.application.account.domain.Account;
import com.sadowbass.outerpark.application.account.dto.AccountInfo;

public interface AccountRepository {

    Account findByEmail(String email);

    AccountInfo findAccountInfoById(Long id);

    Long save(Account account);

    void updateCreateData(Account account);
}
