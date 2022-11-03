package com.nttdata.bootcamp.msbankaccount.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DebitCardDto {

    @Id
    private String idDebitCard;
    private String cardNumber;
    private Boolean isMainAccount;
    private Integer order;

}
