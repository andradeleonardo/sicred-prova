package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response422Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.common.exception.ResponseException;
import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.model.Voto;
import br.com.sicred.prova.repository.AssociadoRepository;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.repository.SecaoRepository;
import br.com.sicred.prova.repository.VotoRepository;
import br.com.sicred.prova.utils.AppUtils;
import br.com.sicred.prova.v1.controller.request.VotoRequest;
import br.com.sicred.prova.v1.controller.response.VotoResponse;
import br.com.sicred.prova.v1.mapper.VotoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotoService {

    private final PautaRepository pautaRepository;
    private final SecaoRepository secaoRepository;
    private final AssociadoRepository associadoRepository;
    private final VotoRepository votoRepository;
    private final VotoMapper votoMapper;
    private final AppResourceBundle resourceBundle;

    public List<VotoResponse> findByPautaId(final UUID pautaId) {
        try {
            return votoRepository.findByPautaId(pautaId).stream().map(votoMapper::mapToResponse).toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

    public VotoResponse create(final VotoRequest req) {
        try {
            if (pautaRepository.findById(req.getIdPauta()).isEmpty()) {
                throw new Response404Exception(resourceBundle.getMessage("pauta.nao.encontrada"));
            }
            Associado associado = associadoRepository.findByPautaIdAndCpf(req.getIdPauta(), req.getCpf()).orElseThrow(() ->
                    new Response404Exception(resourceBundle.getMessage("associado.nao.encontrado")));
            if (votoRepository.countByPautaIdAndCpf(req.getIdPauta(), req.getCpf()) > 0L) {
                throw new Response422Exception(resourceBundle.getMessage("pauta.associado.ja.votou"));
            }
            Secao secao = secaoRepository.findSecaoAtivaByPautaId(req.getIdPauta(), AppUtils.getCurrentDatetime()).orElseThrow(() ->
                    new Response404Exception(resourceBundle.getMessage("pauta.secao.aberta.nao.existente")));
            Voto entity = votoMapper.mapToEntityInsert(req, secao, associado);
            votoRepository.save(entity);
            return votoMapper.mapToResponse(entity);
        } catch (ResponseException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

}
