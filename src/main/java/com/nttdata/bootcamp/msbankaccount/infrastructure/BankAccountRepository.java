package com.nttdata.bootcamp.msbankaccount.infrastructure;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.bootcamp.msbankaccount.dto.BankAccountDto;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class MobileWalletRepository.
 * MobileWallet microservice class MobileWalletRepository.
 */
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Mono<BankAccount> findByAccountNumber(String accountNumber);

    @Query(value = "{'client.documentNumber' : ?0, accountType: ?1 }")
    Flux<BankAccount> findByAccountClient(String documentNumber, String accountType);

    @Query(value = "{'client.documentNumber' : ?0, accountNumber: ?1 }")
    Mono<BankAccountDto> findByAccountAndDocumentNumber(
            String documentNumber, String accountNumber);

    @Query(value = "{'client.documentNumber' : ?0, 'debitCard.cardNumber' : ?1 }")
    Flux<BankAccount> findByClientAndCard(String documentNumber, String cardNumber);

    @Query(value = "{'client.documentNumber' : ?0, 'debitCard.cardNumber' : ?1, 'debitCard.isMainAccount' : false }")
    Flux<BankAccount> findByClientAndCardAndIsNotMainAccount(
            String documentNumber, String cardNumber);

    @Query(value = "{'debitCard.cardNumber' : ?0, 'debitCard.isMainAccount' : true }")
    Mono<BankAccount> findByCardNumberAndIsMainAccount(String cardNumber);

    @Query(value = "{'client.documentNumber' : ?0, 'debitCard.isMainAccount' : true }")
    Flux<BankAccount> findBankAccountBalanceByDocumentNumber(String documentNumber);
}
