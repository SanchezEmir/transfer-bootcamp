package com.nttdata.transfer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.transfer.entity.Transfer;

public interface ITransferRepository extends ReactiveMongoRepository<Transfer, String> {
  
  

}
