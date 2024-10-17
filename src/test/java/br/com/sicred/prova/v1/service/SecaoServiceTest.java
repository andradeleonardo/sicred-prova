package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response422Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.mocks.PautaMock;
import br.com.sicred.prova.mocks.SecaoMock;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.repository.SecaoRepository;
import br.com.sicred.prova.utils.FakeData;
import br.com.sicred.prova.v1.controller.request.SecaoRequest;
import br.com.sicred.prova.v1.controller.response.SecaoResponse;
import br.com.sicred.prova.v1.mapper.SecaoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SecaoServiceTest {

    @InjectMocks
    private SecaoService secaoService;

    @Mock
    private SecaoRepository secaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AppResourceBundle resourceBundle;

    @Spy
    private SecaoMapper secaoMapper = Mappers.getMapper(SecaoMapper.class);

    @Test
    void shouldFindByPautaId() {
        final Secao secao = SecaoMock.getEntity();
        when(secaoRepository.findByPautaId(any(UUID.class))).thenReturn(List.of(secao));
        final List<SecaoResponse> resp = secaoService.findByPautaId(FakeData.FAKE_UUID);
        assertFalse(resp.isEmpty());
        assertEquals(1, resp.size());
    }

    @Test
    void shouldNotFindByPautaId_whenUnexpectedError_500Exception() {
        final Secao secao = SecaoMock.getEntity();
        when(secaoRepository.findByPautaId(any(UUID.class))).thenReturn(List.of(secao));
        when(secaoMapper.mapToResponse(any(Secao.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> secaoService.findByPautaId(FakeData.FAKE_UUID));
    }

    @Test
    void shouldCreate() {
        final SecaoRequest req = SecaoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(secaoRepository.findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class))).thenReturn(Optional.empty());
        secaoService.create(req);
        verify(secaoMapper, times(1)).mapToEntityInsert(any(SecaoRequest.class), any(Pauta.class));
        verify(secaoRepository, times(1)).save(any(Secao.class));
        verify(secaoMapper, times(1)).mapToResponse(any(Secao.class));
    }

    @Test
    void shouldNotCreate_whenPautaNotFound_404Exception() {
        final SecaoRequest req = SecaoMock.getRequest();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(Response404Exception.class, () -> secaoService.create(req));
        verify(secaoRepository, times(0)).findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class));
        verify(secaoRepository, times(0)).save(any(Secao.class));
        verify(secaoMapper, times(0)).mapToResponse(any(Secao.class));
    }

    @Test
    void shouldNotCreate_whenAnotherOpenSectionExists_422Exception() {
        final SecaoRequest req = SecaoMock.getRequest();
        final Secao openSection = SecaoMock.getEntity();
        final Pauta pauta = PautaMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(secaoRepository.findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class))).thenReturn(Optional.of(openSection));
        assertThrows(Response422Exception.class, () -> secaoService.create(req));
        verify(secaoRepository, times(0)).save(any(Secao.class));
        verify(secaoMapper, times(0)).mapToResponse(any(Secao.class));
    }

    @Test
    void shouldNotCreate_whenUnexpectedError_500Exception() {
        final SecaoRequest req = SecaoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(secaoRepository.findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class))).thenReturn(Optional.empty());
        when(secaoMapper.mapToEntityInsert(any(SecaoRequest.class), any(Pauta.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> secaoService.create(req));
        verify(secaoRepository, times(0)).save(any(Secao.class));
        verify(secaoMapper, times(0)).mapToResponse(any(Secao.class));
    }

}
