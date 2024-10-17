package br.com.sicred.prova.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppResourceBundle {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    public String getMessage(String key, String... params) {
        return messageSource.getMessage(key, params, Locale.getDefault());
    }

}
