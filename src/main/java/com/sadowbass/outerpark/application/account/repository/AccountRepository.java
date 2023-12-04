package com.sadowbass.outerpark.application.account.repository;

import com.sadowbass.outerpark.application.account.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository {

    Account findByEmail(String email);

    Long save(Account account);

    void updateCreateData(Account account);
}
