package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.common.exception.ResponseException;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.v1.controller.request.PautaRequest;
import br.com.sicred.prova.v1.controller.response.PautaResponse;
import br.com.sicred.prova.v1.mapper.PautaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;
    private final AppResourceBundle resourceBundle;

    public List<PautaResponse> findAll() {
        return pautaRepository.findAll().stream().map(pautaMapper::mapToResponse).toList();
    }

    public PautaResponse findById(final UUID id) {
        try {
            return pautaRepository.findById(id).map(pautaMapper::mapToResponse).orElseThrow(() ->
                    new Response404Exception(resourceBundle.getMessage("registro.nao.encontrado")));
        } catch (ResponseException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

    public PautaResponse create(final PautaRequest req) {
        try {
            Pauta entity = pautaMapper.mapToEntityInsert(req);
            pautaRepository.save(entity);
            return pautaMapper.mapToResponse(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

}
