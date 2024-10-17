package br.com.sicred.prova.integration.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.enums.UserStatusEnum;
import br.com.sicred.prova.integration.client.UserInfoClient;
import br.com.sicred.prova.integration.response.UserInfoResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoClient userInfoClient;

    private final AppResourceBundle resourceBundle;

    @Value("${external.userInfo.enabled:false}")
    private Boolean enabled;

    public UserInfoResponse getUserInfoByCpf(String cpf) {
        if (!enabled) {
            return new UserInfoResponse(UserStatusEnum.ABLE_TO_VOTE);
        }
        try {
            return userInfoClient.getUserInfoByCpf(cpf).getBody();
        } catch (FeignException.NotFound e) {
            log.error(e.getMessage(), e);
            throw new Response404Exception(resourceBundle.getMessage("cpf.nao.encontrado"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Response500Exception(resourceBundle.getMessage("busca.cpf.erro.inesperado"));
        }
    }

}
