package br.com.sicred.prova.common.exception;

import org.springframework.http.HttpStatus;

public class Response500Exception extends ResponseException {

    public Response500Exception(String msg) {
        super(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
