package br.com.sicred.prova.v1.controller.response;

import br.com.sicred.prova.enums.ValorVotoEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotoResponse {

    private UUID id;

    private String cpf;

    private ValorVotoEnum valor;

    private UUID idSecao;

}
