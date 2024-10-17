package br.com.sicred.prova.mocks;

import br.com.sicred.prova.model.Voto;
import br.com.sicred.prova.utils.ResourceUtils;
import br.com.sicred.prova.v1.controller.request.VotoRequest;

public class VotoMock {

    private VotoMock() {
    }

    public static VotoRequest getRequest() {
        return (VotoRequest) ResourceUtils.getObject("json/request/voto.json", VotoRequest.class);
    }

    public static Voto getEntity() {
        return (Voto) ResourceUtils.getObject("json/model/voto.json", Voto.class);
    }

}
