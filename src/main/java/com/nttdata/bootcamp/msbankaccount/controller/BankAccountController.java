package com.nttdata.bootcamp.msbankaccount.controller;

import java.net.URI;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import javax.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.support.WebExchangeBindException;
import com.nttdata.bootcamp.msbankaccount.application.BankAccountService;

@RestController
@RequestMapping("/api/bankaccounts")
public class BankAccountController {
    @Autowired
    private BankAccountService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<BankAccount>>> listBankAccounts() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(service.findAll()));
    }

    @GetMapping("/{idBankAccount}")
    public Mono<ResponseEntity<BankAccount>> viewBankAccountDetails(@PathVariable("idBankAccount") String idClient) {
        return service.findById(idClient).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> saveBankAccount(@Valid @RequestBody Mono<BankAccount> monoClient) {
        Map<String, Object> request = new HashMap<>();
        return monoClient.flatMap(client -> {
            return service.save(client).map(c -> {
                request.put("Cuenta Bancaria", c);
                request.put("mensaje", "Cuenta Bancaria guardado con exito");
                request.put("timestamp", new Date());
                return ResponseEntity.created(URI.create("/api/bankaccounts/".concat(c.getIdBankAccount())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(request);
            });
        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class).flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "El campo: " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList().flatMap(list -> {
                        request.put("errors", list);
                        request.put("timestamp", new Date());
                        request.put("status", HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(request));
                    });
        });
    }

    @PutMapping("/{idBankAccount}")
    public Mono<ResponseEntity<BankAccount>> editBankAccount(@Valid @RequestBody BankAccount bankAccount, @PathVariable("idBankAccount") String idBankAccount) {
        return service.findById(idBankAccount).flatMap(x -> {

                    x.setAccountType(bankAccount.getAccountType());
                    x.setCardNumber(bankAccount.getCardNumber());
                    x.setAccountNumber(bankAccount.getAccountNumber());
                    x.setCommission(bankAccount.getCommission());
                    x.setMovementDate(bankAccount.getMovementDate());
                    x.setMaximumMovement(bankAccount.getMaximumMovement());
                    x.setListHeadline(bankAccount.getListHeadline());
                    x.setListAuthorizedSignatories(bankAccount.getListAuthorizedSignatories());
                    x.setStartingAmount(bankAccount.getStartingAmount());
                    x.setCurrency(bankAccount.getCurrency());
                    return service.save(x);
                }).map(x -> ResponseEntity.created(URI.create("/api/bankaccounts/".concat(x.getIdBankAccount())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(x))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idBankAccount}")
    public Mono<ResponseEntity<Void>> deleteBankAccount(@PathVariable("idBankAccount") String idBankAccount) {
        return service.findById(idBankAccount).flatMap(c -> {
            return service.delete(c).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
