package com.nttdata.transfer.entity.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedTerm implements BankAccount {
  
  private String id;
  
  private Customer customer;
  
  private String cardNumber;
  
  private Integer limitTransactions;
  
  private Integer freeTransactions;
  
  private Double commissionTransactions;
  
  private Double balance;
  
  private Integer limitDeposits;
  
  private Integer limitDraft;
  
  private LocalDate allowDateTransaction;
  
  private LocalDateTime createAt;
  
  private List<Managers> owners;
  
  private List<Managers> signatories;
  
}
