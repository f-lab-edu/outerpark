package com.sadowbass.outerpark.infra.mapper;

import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountRepositoryMapper extends AccountRepository {
}
