package com.nttdata.bootcamp.msbankaccount.application;

import com.nttdata.bootcamp.msbankaccount.dto.BankAccountDto;
import com.nttdata.bootcamp.msbankaccount.dto.DebitCardDto;
import com.nttdata.bootcamp.msbankaccount.dto.bean.BankAccountBean;
import com.nttdata.bootcamp.msbankaccount.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbankaccount.infrastructure.*;
import com.nttdata.bootcamp.msbankaccount.model.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MovementRepository movementRepository;
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private DebitCardRepository debitCardRepository;

    @Override
    public Flux<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> findById(String idBankAccount) {
        return Mono.just(idBankAccount)
                .flatMap(bankAccountRepository::findById)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta Bancaria", "idBankAccount", idBankAccount)));
    }

    @Override
    public Mono<BankAccount> findByAccountNumber(String accountNumber) {
        return Mono.just(accountNumber)
                .flatMap(bankAccountRepository::findByAccountNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Número Cuenta Bancaria", "accountNumber", accountNumber)));
    }

    @Override
    public Flux<BankAccount> findByDocumentNumber(String documentNumber, String accountType) {
        return bankAccountRepository.findByAccountClient(documentNumber, accountType)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Número Cuenta Bancaria", "documentNumber", documentNumber)));
    }

    @Override
    public Mono<BankAccountDto> findMovementsByDocumentNumber(String documentNumber, String accountNumber) {
        return bankAccountRepository.findByAccountAndDocumentNumber(documentNumber, accountNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta Bancaria", "accountNumber", accountNumber)))
                .flatMap(d -> movementRepository.findMovementsByAccountNumber(accountNumber)
                        .collectList()
                        .flatMap(m -> {
                            d.setMovements(m);
                            return Mono.just(d);
                        })
                );
    }

    @Override
    public Mono<BankAccount> save(BankAccountDto bankAccountDto) {
        log.info("----save-------bankAccountDto : " + bankAccountDto.toString());
        return Mono.just(bankAccountDto)
                .flatMap(badto -> setDebitCard(badto))
                .flatMap(badto -> {
                    if (badto.getAccountType().equals("Savings-account")) { // cuenta de ahorros.
                        return badto.mapperToSavingAccount();
                    } else if (badto.getAccountType().equals("FixedTerm-account")) { // cuenta plazos fijos.
                        return badto.mapperToFixedTermAccount();
                    } else if (badto.getAccountType().equals("Checking-account")) { // current account.
                        return badto.mapperToCheckingAccount();
                    } else {
                        return Mono.error(new ResourceNotFoundException("Tipo Cuenta", "AccountType", badto.getAccountType()));
                    }
                })
                .flatMap(mba -> clientRepository.findClientByDni(mba.getDocumentNumber())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "DocumentNumber", mba.getDocumentNumber())))
                        .flatMap(clnt -> validateNumberClientAccounts(clnt, mba, "save").then(Mono.just(clnt)))
                        .flatMap(clnt -> mba.validateFields()
                                .flatMap(at -> mba.mapperToBankAccount(clnt))
                                .flatMap(ba -> verifyThatYouHaveACreditCard(clnt, mba.getMinimumAmount())
                                        .flatMap(o -> {
                                            ba.setBalance(ba.getStartingAmount());
                                            if (o.equals(true) && clnt.getClientType().equals("Business") && mba.getAccountType().equals("Checking-account")) {
                                                ba.setCommission(0.0);
                                            }
                                            return Mono.just(ba);
                                        })
                                        .flatMap(bankAccountRepository::save)
                                )
                        )
                );
    }

    public Mono<BankAccountDto> setDebitCard(BankAccountDto bankAccountDto) {
        log.info("---setDebitCard : " + bankAccountDto.toString());
        return Mono.just(bankAccountDto)
                .flatMap(badto -> {
                    if (badto.getCardNumber() != null) {
                        return debitCardRepository.findByCardNumber(badto.getCardNumber())
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Tarjeta de débito", "CardNumber", badto.getCardNumber())))
                                .flatMap(dc -> {
                                    if (dc.getIdDebitCard() != null) {
                                        return bankAccountRepository.findByClientAndCard(badto.getDocumentNumber(), badto.getCardNumber())
                                                .collectList()
                                                .flatMap(dcc -> {
                                                    DebitCardDto debitCardDto = DebitCardDto.builder()
                                                            .idDebitCard(dc.getIdDebitCard())
                                                            .cardNumber(dc.getCardNumber())
                                                            .build();
                                                    Stream<BankAccount> BankAccounts = dcc.stream();
                                                    Long countBA = dcc.stream().count();
                                                    if (countBA > 0) {
                                                        Optional<Integer> finalOrder = BankAccounts
                                                                .map(mp -> mp.getDebitCard() != null ? mp.getDebitCard().getOrder() : 0)
                                                                .max(Comparator.naturalOrder());
                                                        if (finalOrder.isPresent()) {
                                                            debitCardDto.setIsMainAccount(false);
                                                            debitCardDto.setOrder((finalOrder.get() + 1));
                                                            badto.setDebitCard(debitCardDto);
                                                            return Mono.just(badto);
                                                        } else {
                                                            return Mono.just(badto);
                                                        }
                                                    } else {
                                                        debitCardDto.setIsMainAccount(true);
                                                        debitCardDto.setOrder(1);
                                                        badto.setDebitCard(debitCardDto);
                                                        return Mono.just(badto);
                                                    }
                                                });
                                    } else {
                                        return Mono.just(badto);
                                    }
                                })
                                .then(Mono.just(badto));
                    } else {
                        return Mono.just(badto);
                    }
                });
    }

    @Override
    public Mono<BankAccount> update(BankAccountDto bankAccountDto, String idBankAccount) {
        log.info("----update-------bankAccountDto -- idBankAccount: " + bankAccountDto.toString() + " -- " + idBankAccount);
        return Mono.just(bankAccountDto)
                .flatMap(badto -> setDebitCard(badto))
                .flatMap(badto -> {
                    if (badto.getAccountType().equals("Savings-account")) { // cuenta de ahorros.
                        return badto.mapperToSavingAccount();
                    } else if (badto.getAccountType().equals("FixedTerm-account")) { // cuenta plazos fijos.
                        return badto.mapperToFixedTermAccount();
                    } else if (badto.getAccountType().equals("Checking-account")) { // current account.
                        return badto.mapperToCheckingAccount();
                    } else {
                        return Mono.error(new ResourceNotFoundException("Tipo Cuenta", "AccountType", badto.getAccountType()));
                    }
                }).flatMap(mba -> clientRepository.findClientByDni(mba.getDocumentNumber())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "DocumentNumber", mba.getDocumentNumber())))
                        .flatMap(clnt -> validateNumberClientAccounts(clnt, mba, "update").then(Mono.just(clnt)))
                        .flatMap(clnt -> mba.validateFields()
                                .flatMap(at -> mba.mapperToBankAccount(clnt))
                                .flatMap(ba -> verifyThatYouHaveACreditCard(clnt, mba.getMinimumAmount())
                                        .flatMap(o -> {
                                            if (o.equals(true) && clnt.getClientType().equals("Business") && mba.getAccountType().equals("Checking-account")) {
                                                ba.setCommission(0.0);
                                            }
                                            return bankAccountRepository.findById(idBankAccount)
                                                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta Bancaria", "idBankAccount", idBankAccount)))
                                                    .flatMap(x -> {
                                                        ba.setIdBankAccount(x.getIdBankAccount());
                                                        return bankAccountRepository.save(ba);
                                                    });
                                        })
                                )
                        )
                );
    }

    @Override
    public Mono<Void> delete(String idBankAccount) {
        return Mono.just(idBankAccount)
                .flatMap( b -> bankAccountRepository.findById(idBankAccount))
                .switchIfEmpty( Mono.error(new ResourceNotFoundException("Cuenta Bancaria", "idBankAccount", idBankAccount )))
                .flatMap( bankAccountRepository::delete );
    }

    public Mono<Boolean> verifyThatYouHaveACreditCard(Client client, Double minimumAmount) {
        log.info("--verifyThatYouHaveACreditCard-------minimumAmount: " + (minimumAmount == null ? "" : minimumAmount.toString()));
        return creditRepository.findCreditsByDocumentNumber(client.getDocumentNumber()).count()
                .flatMap(cnt -> {
                    String profile = "0";
                    if (cnt > 0) {
                        if (client.getClientType().equals("Personal")) {
                            if (minimumAmount == null) {
                                return clientRepository.updateProfileClient(client.getDocumentNumber(), profile).then(Mono.just(false));
                            } else {
                                profile = "VIP";
                                return clientRepository.updateProfileClient(client.getDocumentNumber(), profile).then(Mono.just(true));
                            }
                        } else if (client.getClientType().equals("Business")) {
                            profile = "PYME";
                            return clientRepository.updateProfileClient(client.getDocumentNumber(), profile).then(Mono.just(true));

                        } else {
                            return clientRepository.updateProfileClient(client.getDocumentNumber(), profile).then(Mono.just(false));
                        }
                    } else {
                        return clientRepository.updateProfileClient(client.getDocumentNumber(), profile).then(Mono.just(false));
                    }
                });
    }

    public Mono<Boolean> validateNumberClientAccounts(Client client, BankAccountBean bankAccountBean, String method) {
        log.info("--validateNumberClientAccounts-------: ");
        if (client.getClientType().equals("Personal")) {
            if (method.equals("save")) {
                return bankAccountRepository.findByAccountClient(client.getDocumentNumber(), bankAccountBean.getAccountType())
                        .count().flatMap(cnt -> {
                            if (cnt >= 1) {
                                return Mono.error(new ResourceNotFoundException("Tipo Cliente", "ClientType", client.getClientType()));
                            } else {
                                return Mono.just(true);
                            }
                        });
            } else {
                return Mono.just(true);
            }
        } else if (client.getClientType().equals("Business")) {
            if (bankAccountBean.getAccountType().equals("Checking-account")) {
                if (bankAccountBean.getListHeadline() == null) {
                    return Mono.error(new ResourceNotFoundException("Titular", "ListHeadline", ""));
                } else {
                    return Mono.just(true);
                }
            } else {
                return Mono.error(new ResourceNotFoundException("Tipo Cliente", "ClientType", client.getClientType()));
            }
        } else {
            return Mono.error(new ResourceNotFoundException("Tipo Cliente", "ClientType", client.getClientType()));
        }
    }

    @Override
    public Flux<BankAccount> findByDocumentNumberAndWithdrawalAmount(String documentNumber, String cardNumber, Double withdrawalAmount) {
        return bankAccountRepository.findByClientAndCardAndIsNotMainAccount(documentNumber, cardNumber)
                .collectList()
                .flatMap(dcc -> {
                    Stream<BankAccount> bankAccounts = dcc.stream();
                    Long countBA = dcc.stream().count();
                    if (countBA > 0) {
                        AtomicReference<Double> missingOutflowAmount = new AtomicReference<>(withdrawalAmount);
                        List<BankAccount> bcAV = bankAccounts
                                .sorted((o1, o2) -> o1.getDebitCard().getOrder().compareTo(o2.getDebitCard().getOrder()))
                                .filter(ft -> {
                                    Double valIni = missingOutflowAmount.get();
                                    if (valIni <= 0) {
                                        return false;
                                    } else {
                                        Double balance = ft.getBalance() != null ? ft.getBalance() : 0;
                                        missingOutflowAmount.set(missingOutflowAmount.get() - balance);
                                        return true;
                                    }
                                })
                                .collect(Collectors.toList());
                        return Mono.just(bcAV);
                    } else {
                        return Mono.just(dcc);
                    }
                })
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<BankAccount> updateBalanceById(String idBankAccount, Double balance) {
        return bankAccountRepository.findById(idBankAccount)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta Bancaria", "idBankAccount", idBankAccount)))
                .flatMap(x -> {
                    x.setBalance(balance);
                    return bankAccountRepository.save(x);
                });
    }

    @Override
    public Mono<BankAccount> findByDebitCardNumberAndIsMainAccount(String debitCardNumber) {
        return bankAccountRepository.findByCardNumberAndIsMainAccount(debitCardNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Tarjeta de débito", "debitCardNumber", debitCardNumber)));
    }

    @Override
    public Flux<BankAccount> findBankAccountBalanceByDocumentNumber(String documentNumber) {
        return bankAccountRepository.findBankAccountBalanceByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "documentNumber", documentNumber)))
                .doOnNext( d -> log.info("--findBankAccountBalanceByDocumentNumber-------: " + documentNumber) )
                .doOnNext( d -> log.info("--findBankAccountBalanceByDocumentNumber-------: " + d) );
    }
}
