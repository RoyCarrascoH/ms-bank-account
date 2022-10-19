package com.nttdata.bootcamp.msbankaccount.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Credit {

    @Id
    private String idCredit;
    private Client client;
    private Integer creditNumber;
    private String creditType;
    private Double creditLineAmount;
    private String currency;
    private Boolean status;
    private Double balance;

}
