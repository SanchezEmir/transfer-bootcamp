package com.nttdata.transfer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.transfer.entity.Transfer;
import com.nttdata.transfer.entity.dto.BankAccount;
import com.nttdata.transfer.entity.dto.CurrentAccount;
import com.nttdata.transfer.entity.dto.FixedTerm;
import com.nttdata.transfer.entity.dto.SavingAccount;
import com.nttdata.transfer.service.ITransferService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
@RequestMapping("/transfer")
public class TransferController {
  
  @Autowired
  ITransferService service;

  @GetMapping("list")
  public Flux<Transfer> findAll() {
    return service.findAll();
  }

  @GetMapping("/find/{id}")
  public Mono<Transfer> findById(@PathVariable String id) {
    return service.findById(id);
  }

  @GetMapping("/findAccount/{accountNumber}")
  public Mono<BankAccount> findAccountBank(@PathVariable String accountNumber) {
    System.out.println("Controller INICIO");
    return service.findBankAccount(accountNumber).filter(accountBank -> {
      System.out.println("Controller filter > " + accountBank.isPresent());
      return accountBank.isPresent();
    }).map(optionalAccountBank -> {
      BankAccount ba = optionalAccountBank.get();
      if (ba instanceof CurrentAccount) {
        System.out.println("Controller casteo1 > " + (CurrentAccount) ba);
        return (CurrentAccount) ba;
      }
      if (ba instanceof FixedTerm) {
        System.out.println("Controller casteo2 > " + (FixedTerm) ba);
        return (FixedTerm) ba;
      }
      if (ba instanceof SavingAccount) {
        System.out.println("Controller casteo3 > " + (SavingAccount) ba);
        return (SavingAccount) ba;
      }
      return null;
    });
  }

  public Mono<BankAccount> findAccountBankInfo(String accountNumber) {
    System.out.println("Controller INICIO");
    return service.findBankAccount(accountNumber).filter(accountBank -> {
      System.out.println("Controller filter > " + accountBank.isPresent());
      return accountBank.isPresent();
    }).map(optionalAccountBank -> {
      BankAccount ba = optionalAccountBank.get();
      if (ba instanceof CurrentAccount) {
        System.out.println("Controller casteo1 > " + (CurrentAccount) ba);
        return (CurrentAccount) ba;
      }
      if (ba instanceof FixedTerm) {
        System.out.println("Controller casteo2 > " + (FixedTerm) ba);
        return (FixedTerm) ba;
      }
      if (ba instanceof SavingAccount) {
        System.out.println("Controller casteo3 > " + (SavingAccount) ba);
        return (SavingAccount) ba;
      }
      return null;
    });
  }

  @PostMapping("/create")
  public Mono<ResponseEntity<Transfer>> create(
      @Valid @RequestBody Transfer transfer) {
    return findAccountBankInfo(transfer.getOriginAccount()).flatMap(origin -> {
      if (origin instanceof CurrentAccount) {
        ((CurrentAccount) origin)
            .setBalance(((CurrentAccount) origin).getBalance()
                - transfer.getAmountTransference());
      }
      if (origin instanceof FixedTerm) {
        ((FixedTerm) origin).setBalance(((FixedTerm) origin).getBalance()
            - transfer.getAmountTransference());
      }
      if (origin instanceof SavingAccount) {
        ((SavingAccount) origin)
            .setBalance(((SavingAccount) origin).getBalance()
                - transfer.getAmountTransference());
      }
      return service.updateBankAccountBalance(origin).flatMap(
          originUpdate -> findAccountBankInfo(transfer.getDestinationAccount())
              .flatMap(destiny -> {
                if (destiny instanceof CurrentAccount) {
                  ((CurrentAccount) destiny)
                      .setBalance(((CurrentAccount) destiny).getBalance()
                          + transfer.getAmountTransference());
                }
                if (destiny instanceof FixedTerm) {
                  ((FixedTerm) destiny)
                      .setBalance(((FixedTerm) destiny).getBalance()
                          + transfer.getAmountTransference());
                }
                if (destiny instanceof SavingAccount) {
                  ((SavingAccount) destiny)
                      .setBalance(((SavingAccount) destiny).getBalance()
                          + transfer.getAmountTransference());
                }
                return service.updateBankAccountBalance(destiny)
                    .flatMap(destinyUpdate -> service.create(transfer));

              }));
    }).map(ft -> new ResponseEntity<>(ft, HttpStatus.CREATED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

  }

  @PutMapping("/update")
  public Mono<ResponseEntity<Transfer>> update(@RequestBody Transfer transfer) {
    return null;
  }

  @DeleteMapping("/delete/{id}")
  public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
    return service.delete(id).filter(deleteFixedTerm -> deleteFixedTerm)
        .map(deleteFixedTerm -> new ResponseEntity<>("Customer Deleted",
            HttpStatus.ACCEPTED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}
