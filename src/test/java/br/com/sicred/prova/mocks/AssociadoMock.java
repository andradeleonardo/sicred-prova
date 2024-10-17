package br.com.sicred.prova.mocks;

import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.utils.ResourceUtils;
import br.com.sicred.prova.v1.controller.request.AssociadoRequest;

public class AssociadoMock {

    private AssociadoMock() {
    }

    public static AssociadoRequest getRequest() {
        return (AssociadoRequest) ResourceUtils.getObject("json/request/associado.json", AssociadoRequest.class);
    }

    public static Associado getEntity() {
        return (Associado) ResourceUtils.getObject("json/model/associado.json", Associado.class);
    }

}
