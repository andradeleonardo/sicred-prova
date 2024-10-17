package br.com.sicred.prova.mocks;

import br.com.sicred.prova.integration.response.UserInfoResponse;
import br.com.sicred.prova.utils.ResourceUtils;

public class UserInfoMock {

    private UserInfoMock() {
    }

    public static UserInfoResponse getResponse() {
        return (UserInfoResponse) ResourceUtils.getObject("json/response/userInfo.json", UserInfoResponse.class);
    }

}
