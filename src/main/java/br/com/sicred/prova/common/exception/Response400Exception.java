package br.com.sicred.prova.common.exception;

import org.springframework.http.HttpStatus;

public class Response400Exception extends ResponseException {

    public Response400Exception(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }

}
