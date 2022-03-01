package com.nttdata.transfer.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document(collection = "Transfer")
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

  @Id
  private String id;

  @NotBlank
  @NotNull
  private String originAccount;

  @NotBlank
  @NotNull
  private String destinationAccount;

  @NotNull
  private Double amountTransference;

  @NotNull
  private String codeTransference;

  private String descriptionTransference;

  private LocalDateTime dateTransference;

}
