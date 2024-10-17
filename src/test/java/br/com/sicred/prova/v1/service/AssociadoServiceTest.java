package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response422Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.enums.UserStatusEnum;
import br.com.sicred.prova.integration.response.UserInfoResponse;
import br.com.sicred.prova.integration.service.UserInfoService;
import br.com.sicred.prova.mocks.AssociadoMock;
import br.com.sicred.prova.mocks.PautaMock;
import br.com.sicred.prova.mocks.UserInfoMock;
import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.repository.AssociadoRepository;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.utils.FakeData;
import br.com.sicred.prova.v1.controller.request.AssociadoRequest;
import br.com.sicred.prova.v1.controller.response.AssociadoResponse;
import br.com.sicred.prova.v1.mapper.AssociadoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AssociadoServiceTest {

    @InjectMocks
    private AssociadoService associadoService;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private AppResourceBundle resourceBundle;

    @Spy
    private AssociadoMapper associadoMapper = Mappers.getMapper(AssociadoMapper.class);

    @Test
    void shouldFindByPautaId() {
        final Associado associado = AssociadoMock.getEntity();
        when(associadoRepository.findByPautaId(any(UUID.class))).thenReturn(List.of(associado));
        final List<AssociadoResponse> resp = associadoService.findByPautaId(FakeData.FAKE_UUID);
        assertFalse(resp.isEmpty());
        assertEquals(1, resp.size());
    }

    @Test
    void shouldNotFindByPautaId_whenUnexpectedError_500Exception() {
        final Associado associado = AssociadoMock.getEntity();
        when(associadoRepository.findByPautaId(any(UUID.class))).thenReturn(List.of(associado));
        when(associadoMapper.mapToResponse(any(Associado.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> associadoService.findByPautaId(FakeData.FAKE_UUID));
    }

    @Test
    void shouldCreate() {
        final AssociadoRequest req = AssociadoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final UserInfoResponse userInfoResponse = UserInfoMock.getResponse();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(userInfoService.getUserInfoByCpf(anyString())).thenReturn(userInfoResponse);
        when(associadoRepository.countByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(0L);
        associadoService.create(req);
        verify(associadoMapper, times(1)).mapToEntityInsert(any(AssociadoRequest.class), any(Pauta.class));
        verify(associadoRepository, times(1)).save(any(Associado.class));
        verify(associadoMapper, times(1)).mapToResponse(any(Associado.class));
    }

    @Test
    void shouldNotCreate_whenPautaNotFound_404Exception() {
        final AssociadoRequest req = AssociadoMock.getRequest();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(Response404Exception.class, () -> associadoService.create(req));
        verify(userInfoService, times(0)).getUserInfoByCpf(anyString());
        verify(associadoRepository, times(0)).countByPautaIdAndCpf(any(UUID.class), anyString());
        verify(associadoRepository, times(0)).save(any(Associado.class));
        verify(associadoMapper, times(0)).mapToResponse(any(Associado.class));
    }

    @Test
    void shouldNotCreate_whenUserIsUnable_422Exception() {
        final AssociadoRequest req = AssociadoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final UserInfoResponse userInfoResponse = UserInfoMock.getResponse();
        userInfoResponse.setStatus(UserStatusEnum.UNABLE_TO_VOTE);
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(userInfoService.getUserInfoByCpf(anyString())).thenReturn(userInfoResponse);
        assertThrows(Response422Exception.class, () -> associadoService.create(req));
        verify(associadoRepository, times(0)).countByPautaIdAndCpf(any(UUID.class), anyString());
        verify(associadoRepository, times(0)).save(any(Associado.class));
        verify(associadoMapper, times(0)).mapToResponse(any(Associado.class));
    }

    @Test
    void shouldNotCreate_whenUserAlreadyExists_422Exception() {
        final AssociadoRequest req = AssociadoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final UserInfoResponse userInfoResponse = UserInfoMock.getResponse();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(userInfoService.getUserInfoByCpf(anyString())).thenReturn(userInfoResponse);
        when(associadoRepository.countByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(1L);
        assertThrows(Response422Exception.class, () -> associadoService.create(req));
        verify(associadoRepository, times(0)).save(any(Associado.class));
        verify(associadoMapper, times(0)).mapToResponse(any(Associado.class));
    }

    @Test
    void shouldNotCreate_whenUnexpectedError_500Exception() {
        final AssociadoRequest req = AssociadoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final UserInfoResponse userInfoResponse = UserInfoMock.getResponse();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(userInfoService.getUserInfoByCpf(anyString())).thenReturn(userInfoResponse);
        when(associadoRepository.countByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(0L);
        when(associadoMapper.mapToEntityInsert(any(AssociadoRequest.class), any(Pauta.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> associadoService.create(req));
        verify(associadoRepository, times(0)).save(any(Associado.class));
        verify(associadoMapper, times(0)).mapToResponse(any(Associado.class));
    }

}
