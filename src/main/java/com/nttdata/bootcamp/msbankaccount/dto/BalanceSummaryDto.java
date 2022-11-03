package com.nttdata.bootcamp.msbankaccount.dto;

import com.nttdata.bootcamp.msbankaccount.model.Movement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
@Builder
public class BalanceSummaryDto {

    private String documentNumber;
    private List<InfoBankAccount> objBankAccountInfo;
    private List<Movement> movements;

}
