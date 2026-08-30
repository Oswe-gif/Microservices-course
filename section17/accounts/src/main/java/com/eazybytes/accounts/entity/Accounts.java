package com.eazybytes.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Accounts extends BaseEntity{
    @Id
    private Long accountNumber;
    private Long customerId;
    private String accountType;
    private String branchAddress;
    private Boolean communicationSw;
}
