package br.com.sicred.prova.v1.controller.response;

import br.com.sicred.prova.enums.ValorVotoEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultadoResponse {

    private ValorVotoEnum valor;

    private long quantidade;

    private double percentual;

    public ResultadoResponse(ValorVotoEnum valor, long quantidade) {
        this.valor = valor;
        this.quantidade = quantidade;
    }

}
