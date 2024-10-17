package br.com.sicred.prova.v1.controller.request;

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
public class AssociadoRequest {

    @NotBlank(message = "O CPF deve ser informado")
    private String cpf;

    @JsonIgnore
    private UUID idPauta;

}
