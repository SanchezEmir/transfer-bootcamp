package com.nttdata.transfer.entity.dto;

import com.nttdata.transfer.entity.enums.ETypeCustomer;

import lombok.Data;

@Data
public class TypeCustomer {

  private String id;

  private ETypeCustomer value;

  private SubType subType;

}
