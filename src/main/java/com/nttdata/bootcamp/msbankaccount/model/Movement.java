package com.nttdata.bootcamp.msbankaccount.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movement {
    private String accountNumber;
    private Integer numberMovement;
    private String movementType;
    private Double amount;
    private Double balance;
    private String currency;
    private Date movementDate;

}
