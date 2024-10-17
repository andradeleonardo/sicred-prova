package br.com.sicred.prova.integration.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.enums.UserStatusEnum;
import br.com.sicred.prova.integration.client.UserInfoClient;
import br.com.sicred.prova.integration.response.UserInfoResponse;
import br.com.sicred.prova.mocks.UserInfoMock;
import br.com.sicred.prova.utils.FakeData;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserInfoServiceEnabledTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoClient userInfoClient;

    @Mock
    private AppResourceBundle resourceBundle;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(userInfoService, "enabled", true);
    }

    @Test
    public void shouldGetUserInfoByCpf() {
        final UserInfoResponse userInfoResponse = UserInfoMock.getResponse();
        when(userInfoClient.getUserInfoByCpf(anyString())).thenReturn(ResponseEntity.ok(userInfoResponse));
        final UserInfoResponse resp = userInfoService.getUserInfoByCpf(FakeData.FAKE_CPF);
        assertNotNull(resp);
        assertEquals(UserStatusEnum.ABLE_TO_VOTE, resp.getStatus());
    }

    @Test
    public void shouldNotGetUserInfoByCpf_404Exception() {
        final Request request = mock(Request.class);
        when(userInfoClient.getUserInfoByCpf(anyString())).thenThrow(
                new FeignException.NotFound("message", request, null, null));
        assertThrows(Response404Exception.class, () -> userInfoService.getUserInfoByCpf(FakeData.FAKE_CPF));
    }

    @Test
    public void shouldNotGetUserInfoByCpf_500Exception() {
        final Request request = mock(Request.class);
        when(userInfoClient.getUserInfoByCpf(anyString())).thenThrow(
                new FeignException.BadGateway("message", request, null, null));
        assertThrows(Response500Exception.class, () -> userInfoService.getUserInfoByCpf(FakeData.FAKE_CPF));
    }

}
