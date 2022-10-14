package com.nttdata.bootcamp.msbankaccount.infrastructure;

import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {


}
