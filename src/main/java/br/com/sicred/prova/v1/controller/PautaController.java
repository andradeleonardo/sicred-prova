package br.com.sicred.prova.v1.controller;

import br.com.sicred.prova.v1.controller.request.AssociadoRequest;
import br.com.sicred.prova.v1.controller.request.PautaRequest;
import br.com.sicred.prova.v1.controller.request.SecaoRequest;
import br.com.sicred.prova.v1.controller.request.VotoRequest;
import br.com.sicred.prova.v1.controller.response.*;
import br.com.sicred.prova.v1.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@Tag(name = "pautas", description = "Controlador relacionado a pautas")
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;
    private final AssociadoService associadoService;
    private final SecaoService secaoService;
    private final VotoService votoService;
    private final ResultadoService resultadoService;

    @Operation(description = "Lista todas as pautas disponíveis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pautas listadas com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<PautaResponse>> findAll() {
        return ResponseEntity.ok().body(pautaService.findAll());
    }

    @Operation(description = "Recupera detalhes de uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes da pauta recuperados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> findById(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id) {
        return ResponseEntity.ok().body(pautaService.findById(id));
    }

    @Operation(description = "Cria uma nova pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping
    @Validated
    public ResponseEntity<PautaResponse> create(@Valid @RequestBody PautaRequest req) {
        return ResponseEntity.ok().body(pautaService.create(req));
    }

    @Operation(description = "Lista todos os associados de uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associados listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/{id}/associados")
    public ResponseEntity<List<AssociadoResponse>> findAssociadosByPautaId(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id) {
        return ResponseEntity.ok().body(associadoService.findByPautaId(id));
    }

    @Operation(description = "Adiciona um associado a uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associado adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta nao encontrada"),
            @ApiResponse(responseCode = "422", description = "CPF nao habilitado para votacao"),
            @ApiResponse(responseCode = "422", description = "CPF ja associado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/{id}/associados")
    @Validated
    public ResponseEntity<AssociadoResponse> createAssociado(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id,
            @Valid @RequestBody AssociadoRequest req) {
        req.setIdPauta(id);
        return ResponseEntity.ok().body(associadoService.create(req));
    }

    @Operation(description = "Lista todas as seções de uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seções listadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/{id}/secoes")
    public ResponseEntity<List<SecaoResponse>> findSecoesByPautaId(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id) {
        return ResponseEntity.ok().body(secaoService.findByPautaId(id));
    }

    @Operation(description = "Cria uma nova seção a uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seção criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta nao encontrada"),
            @ApiResponse(responseCode = "422", description = "Ja existe uma seção aberta"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/{id}/secoes")
    @Validated
    public ResponseEntity<SecaoResponse> createSecao(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id,
            @Valid @RequestBody SecaoRequest req) {
        req.setIdPauta(id);
        return ResponseEntity.ok().body(secaoService.create(req));
    }

    @Operation(description = "Lista todos os votos de uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Votos listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/{id}/votos")
    public ResponseEntity<List<VotoResponse>> findVotosByPautaId(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id) {
        return ResponseEntity.ok().body(votoService.findByPautaId(id));
    }

    @Operation(description = "Registra um novo voto em seção aberta de uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto registrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta nao encontrada"),
            @ApiResponse(responseCode = "404", description = "CPF nao associado"),
            @ApiResponse(responseCode = "422", description = "Associado já votou"),
            @ApiResponse(responseCode = "404", description = "Nenhuma seção aberta encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/{id}/votos")
    @Validated
    public ResponseEntity<VotoResponse> createVoto(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id,
            @Valid @RequestBody VotoRequest req) {
        req.setIdPauta(id);
        return ResponseEntity.ok().body(votoService.create(req));
    }

    @Operation(description = "Apresenta o resultado da votacao de uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado da votacao apresentado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/{id}/resultado")
    public ResponseEntity<List<ResultadoResponse>> findResultadoByPautaId(
            @Parameter(description = "ID da pauta")
            @PathVariable UUID id) {
        return ResponseEntity.ok().body(resultadoService.findByPautaId(id));
    }

}
