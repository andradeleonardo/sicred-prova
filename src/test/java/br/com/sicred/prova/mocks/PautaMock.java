package br.com.sicred.prova.mocks;

import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.utils.ResourceUtils;
import br.com.sicred.prova.v1.controller.request.PautaRequest;

public class PautaMock {

    private PautaMock() {
    }

    public static PautaRequest getRequest() {
        return (PautaRequest) ResourceUtils.getObject("json/request/pauta.json", PautaRequest.class);
    }

    public static Pauta getEntity() {
        return (Pauta) ResourceUtils.getObject("json/model/pauta.json", Pauta.class);
    }

}
