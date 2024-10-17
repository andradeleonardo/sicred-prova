package br.com.sicred.prova.integration.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.enums.UserStatusEnum;
import br.com.sicred.prova.integration.client.UserInfoClient;
import br.com.sicred.prova.integration.response.UserInfoResponse;
import br.com.sicred.prova.utils.FakeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class UserInfoServiceDisabledTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoClient userInfoClient;

    @Mock
    private AppResourceBundle resourceBundle;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(userInfoService, "enabled", false);
    }

    @Test
    public void shouldGetUserInfoByCpf_whenClientIsDisabled() {
        final UserInfoResponse resp = userInfoService.getUserInfoByCpf(FakeData.FAKE_CPF);
        assertNotNull(resp);
        assertEquals(UserStatusEnum.ABLE_TO_VOTE, resp.getStatus());
        verify(userInfoClient, times(0)).getUserInfoByCpf(anyString());
    }

}
