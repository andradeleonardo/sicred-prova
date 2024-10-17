package br.com.sicred.prova.v1.controller.request;

import br.com.sicred.prova.enums.ValorVotoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotoRequest {

    @NotBlank(message = "O cpf do associado deve ser informado")
    private String cpf;

    private ValorVotoEnum valor;

    @JsonIgnore
    private UUID idPauta;

}
