package com.nttdata.bootcamp.msbankaccount.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Headline {
    private String names;
    private String surnames;
    private String documentType;
    private String documentNumber;
}
