package br.com.sicred.prova.integration.client;

import br.com.sicred.prova.integration.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "user-info-client", url = "${external.userInfo.url}")
public interface UserInfoClient {

    @GetMapping(value = "/users/{cpf}")
    ResponseEntity<UserInfoResponse> getUserInfoByCpf(@PathVariable("cpf") String cpf);

}
