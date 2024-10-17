package br.com.sicred.prova.common.exception;

import org.springframework.http.HttpStatus;

public class Response404Exception extends ResponseException {

    public Response404Exception(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }

}
