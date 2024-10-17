package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.repository.VotoRepository;
import br.com.sicred.prova.v1.controller.response.ResultadoResponse;
import br.com.sicred.prova.v1.mapper.ResultadoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultadoService {

    private final VotoRepository votoRepository;
    private final AppResourceBundle resourceBundle;
    private final ResultadoMapper resultadoMapper;

    public List<ResultadoResponse> findByPautaId(final UUID pautaId) {
        try {
            List<ResultadoResponse> items = votoRepository.findResultadoByPautaId(pautaId);
            return resultadoMapper.montaResultadoFinal(items);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

}
