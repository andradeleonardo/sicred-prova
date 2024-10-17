package br.com.sicred.prova.common.exception;

import org.springframework.http.HttpStatus;

public class Response422Exception extends ResponseException {

    public Response422Exception(String msg) {
        super(msg, HttpStatus.PRECONDITION_REQUIRED);
    }

}
