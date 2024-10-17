package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response422Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.common.exception.ResponseException;
import br.com.sicred.prova.enums.UserStatusEnum;
import br.com.sicred.prova.integration.response.UserInfoResponse;
import br.com.sicred.prova.integration.service.UserInfoService;
import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.repository.AssociadoRepository;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.v1.controller.request.AssociadoRequest;
import br.com.sicred.prova.v1.controller.response.AssociadoResponse;
import br.com.sicred.prova.v1.mapper.AssociadoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final UserInfoService userInfoService;
    private final PautaRepository pautaRepository;
    private final AssociadoRepository associadoRepository;
    private final AssociadoMapper associadoMapper;
    private final AppResourceBundle resourceBundle;

    public List<AssociadoResponse> findByPautaId(final UUID pautaId) {
        try {
            return associadoRepository.findByPautaId(pautaId).stream().map(associadoMapper::mapToResponse).toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

    public AssociadoResponse create(final AssociadoRequest req) {
        try {
            Pauta pauta = pautaRepository.findById(req.getIdPauta()).orElseThrow(() ->
                    new Response404Exception(resourceBundle.getMessage("pauta.nao.encontrada")));
            UserInfoResponse userInfoSaida = userInfoService.getUserInfoByCpf(req.getCpf());
            if (UserStatusEnum.UNABLE_TO_VOTE.equals(userInfoSaida.getStatus())) {
                throw new Response422Exception(resourceBundle.getMessage("associado.nao.habilitado"));
            }
            if (associadoRepository.countByPautaIdAndCpf(req.getIdPauta(), req.getCpf()) > 0L) {
                throw new Response422Exception(resourceBundle.getMessage("cpf.ja.associado"));
            }
            Associado entity = associadoMapper.mapToEntityInsert(req, pauta);
            associadoRepository.save(entity);
            return associadoMapper.mapToResponse(entity);
        } catch (ResponseException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("erro.inesperado"));
        }
    }

}
