package br.com.sicred.prova.mocks;

import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.utils.ResourceUtils;
import br.com.sicred.prova.v1.controller.request.SecaoRequest;

public class SecaoMock {

    private SecaoMock() {
    }

    public static SecaoRequest getRequest() {
        return (SecaoRequest) ResourceUtils.getObject("json/request/secao.json", SecaoRequest.class);
    }

    public static Secao getEntity() {
        return (Secao) ResourceUtils.getObject("json/model/secao.json", Secao.class);
    }

}
