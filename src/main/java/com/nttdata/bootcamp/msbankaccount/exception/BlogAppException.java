package com.nttdata.bootcamp.msbankaccount.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class BlogAppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus state;
    private String message;

    public BlogAppException(HttpStatus state, String message) {
        super();
        this.state = state;
        this.message = message;
    }
}
