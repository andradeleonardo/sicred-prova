package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response422Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.common.exception.ResponseException;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.repository.SecaoRepository;
import br.com.sicred.prova.utils.AppUtils;
import br.com.sicred.prova.v1.controller.request.SecaoRequest;
import br.com.sicred.prova.v1.controller.response.SecaoResponse;
import br.com.sicred.prova.v1.mapper.SecaoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecaoService {

    private final PautaRepository pautaRepository;
    private final SecaoRepository secaoRepository;
    private final SecaoMapper secaoMapper;
    private final AppResourceBundle resourceBundle;

    public List<SecaoResponse> findByPautaId(final UUID pautaId) {
        try {
            return secaoRepository.findByPautaId(pautaId).stream().map(secaoMapper::mapToResponse).toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

    public SecaoResponse create(final SecaoRequest req) {
        try {
            Pauta pauta = pautaRepository.findById(req.getIdPauta()).orElseThrow(() ->
                    new Response404Exception(resourceBundle.getMessage("pauta.nao.encontrada")));
            if (secaoRepository.findSecaoAtivaByPautaId(req.getIdPauta(), AppUtils.getCurrentDatetime()).isPresent()) {
                throw new Response422Exception(resourceBundle.getMessage("pauta.secao.aberta.existente"));
            }
            Secao entity = secaoMapper.mapToEntityInsert(req, pauta);
            secaoRepository.save(entity);
            return secaoMapper.mapToResponse(entity);
        } catch (ResponseException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

}
