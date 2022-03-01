package com.nttdata.transfer.service;

import java.util.Optional;

import com.nttdata.transfer.entity.Transfer;
import com.nttdata.transfer.entity.dto.BankAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransferService {

  Mono<Transfer> create(Transfer t);

  Flux<Transfer> findAll();

  Mono<Transfer> findById(String id);

  Mono<Transfer> update(Transfer t);

  Mono<Boolean> delete(String t);

  Mono<Optional<BankAccount>> findBankAccount(String cardNumber);

  Mono<BankAccount> updateBankAccountBalance(BankAccount numberAccount);

}
