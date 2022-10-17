package com.nttdata.bootcamp.msbankaccount.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.nttdata.bootcamp.msbankaccount.dto.BankAccountDto;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import com.nttdata.bootcamp.msbankaccount.application.BankAccountService;

@RestController
@RequestMapping("/api/bankaccounts")
@Slf4j
public class BankAccountController {
    @Autowired
    private BankAccountService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<BankAccount>>> listBankAccounts() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(service.findAll()));
    }

    @GetMapping("/{idBankAccount}")
    public Mono<ResponseEntity<BankAccount>> getBankAccountDetails(@PathVariable("idBankAccount") String idClient) {
        return service.findById(idClient).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public Mono<ResponseEntity<BankAccount>> getBankAccountByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return service.findByAccountNumber(accountNumber).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> saveBankAccount(@Valid @RequestBody Mono<BankAccountDto> bankAccountDto) {

        Map<String, Object> request = new HashMap<>();
        return bankAccountDto.flatMap(bnkAcc -> {
            return service.save(bnkAcc).map(baSv -> {
                request.put("bankAccount", baSv);
                request.put("message", "Cuenta Bancaria guardado con exito");
                request.put("timestamp", new Date());
                return ResponseEntity.created(URI.create("/api/bankaccounts/".concat(baSv.getIdBankAccount())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(request);
            });
        });
    }

    @PutMapping("/{idBankAccount}")
    public Mono<ResponseEntity<BankAccount>> editBankAccount(@Valid @RequestBody BankAccountDto bankAccountDto, @PathVariable("idBankAccount") String idBankAccount) {
        return service.update(bankAccountDto,idBankAccount)
                .map(c -> ResponseEntity.created(URI.create("/api/bankaccounts/".concat(idBankAccount)))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(c));
    }

    @DeleteMapping("/{idBankAccount}")
    public Mono<ResponseEntity<Void>> deleteBankAccount(@PathVariable("idBankAccount") String idBankAccount) {
        return service.findById(idBankAccount).flatMap(c -> {
            return service.delete(c).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/documentNumber/{documentNumber}/AccountType/{accountType}")
    public Mono<ResponseEntity<List<BankAccount>>> getBankAccountByDocumentNumberAndAccountType(@PathVariable("documentNumber") String documentNumber, @PathVariable("accountType") String accountType) {
        return service.findByDocumentNumber(documentNumber, accountType)
                .flatMap( mm ->{
                    log.info("--getBankAccountByDocumentNumberAndAccountType-------: " + mm.toString());
                    return Mono.just(mm);
                })
                .collectList()
                .map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/documentNumber/{documentNumber}/accountNumber/{accountNumber}/movements")
    public Mono<ResponseEntity<BankAccountDto>> getMovementsOfBankAccountByDocumentNumberAndAccountType(@PathVariable("documentNumber") String documentNumber, @PathVariable("accountNumber") String accountNumber) {
        return service.findMovementsByDocumentNumber(documentNumber, accountNumber)
                .map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
