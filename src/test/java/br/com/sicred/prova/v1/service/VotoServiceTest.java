package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response422Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.mocks.AssociadoMock;
import br.com.sicred.prova.mocks.PautaMock;
import br.com.sicred.prova.mocks.SecaoMock;
import br.com.sicred.prova.mocks.VotoMock;
import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.model.Voto;
import br.com.sicred.prova.repository.AssociadoRepository;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.repository.SecaoRepository;
import br.com.sicred.prova.repository.VotoRepository;
import br.com.sicred.prova.utils.FakeData;
import br.com.sicred.prova.v1.controller.request.VotoRequest;
import br.com.sicred.prova.v1.controller.response.VotoResponse;
import br.com.sicred.prova.v1.mapper.VotoMapper;
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
public class VotoServiceTest {

    @InjectMocks
    private VotoService votoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private SecaoRepository secaoRepository;

    @Mock
    private AppResourceBundle resourceBundle;

    @Spy
    private VotoMapper votoMapper = Mappers.getMapper(VotoMapper.class);

    @Test
    void shouldFindByPautaId() {
        final Voto voto = VotoMock.getEntity();
        when(votoRepository.findByPautaId(any(UUID.class))).thenReturn(List.of(voto));
        final List<VotoResponse> resp = votoService.findByPautaId(FakeData.FAKE_UUID);
        assertFalse(resp.isEmpty());
        assertEquals(1, resp.size());
    }

    @Test
    void shouldNotFindByPautaId_whenUnexpectedError_500Exception() {
        final Voto voto = VotoMock.getEntity();
        when(votoRepository.findByPautaId(any(UUID.class))).thenReturn(List.of(voto));
        when(votoMapper.mapToResponse(any(Voto.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> votoService.findByPautaId(FakeData.FAKE_UUID));
    }

    @Test
    void shouldCreate() {
        final VotoRequest req = VotoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final Associado associado = AssociadoMock.getEntity();
        final Secao secao = SecaoMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(associadoRepository.findByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(Optional.of(associado));
        when(votoRepository.countByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(0L);
        when(secaoRepository.findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class))).thenReturn(Optional.of(secao));
        votoService.create(req);
        verify(votoMapper, times(1)).mapToEntityInsert(any(VotoRequest.class), any(Secao.class), any(Associado.class));
        verify(votoRepository, times(1)).save(any(Voto.class));
        verify(votoMapper, times(1)).mapToResponse(any(Voto.class));
    }

    @Test
    void shouldNotCreate_whenPautaNotFound_404Exception() {
        final VotoRequest req = VotoMock.getRequest();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(Response404Exception.class, () -> votoService.create(req));
        verify(associadoRepository, times(0)).findByPautaIdAndCpf(any(UUID.class), anyString());
        verify(votoRepository, times(0)).countByPautaIdAndCpf(any(UUID.class), anyString());
        verify(secaoRepository, times(0)).findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class));
        verify(votoRepository, times(0)).save(any(Voto.class));
        verify(votoMapper, times(0)).mapToResponse(any(Voto.class));
    }

    @Test
    void shouldNotCreate_whenCpfIsNotRegistered_404Exception() {
        final VotoRequest req = VotoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(associadoRepository.findByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(Optional.empty());
        assertThrows(Response404Exception.class, () -> votoService.create(req));
        verify(votoRepository, times(0)).countByPautaIdAndCpf(any(UUID.class), anyString());
        verify(secaoRepository, times(0)).findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class));
        verify(votoMapper, times(0)).mapToEntityInsert(any(VotoRequest.class), any(Secao.class), any(Associado.class));
        verify(votoRepository, times(0)).save(any(Voto.class));
        verify(votoMapper, times(0)).mapToResponse(any(Voto.class));
    }

    @Test
    void shouldNotCreate_whenCpfHasAlreadyVoted_422Exception() {
        final VotoRequest req = VotoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final Associado associado = AssociadoMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(associadoRepository.findByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(Optional.of(associado));
        when(votoRepository.countByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(1L);
        assertThrows(Response422Exception.class, () -> votoService.create(req));
        verify(secaoRepository, times(0)).findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class));
        verify(votoMapper, times(0)).mapToEntityInsert(any(VotoRequest.class), any(Secao.class), any(Associado.class));
        verify(votoRepository, times(0)).save(any(Voto.class));
        verify(votoMapper, times(0)).mapToResponse(any(Voto.class));
    }

    @Test
    void shouldNotCreate_whenNoOpenSectionExists_404Exception() {
        final VotoRequest req = VotoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final Associado associado = AssociadoMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(associadoRepository.findByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(Optional.of(associado));
        when(votoRepository.countByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(0L);
        when(secaoRepository.findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class))).thenReturn(Optional.empty());
        assertThrows(Response404Exception.class, () -> votoService.create(req));
        verify(votoMapper, times(0)).mapToEntityInsert(any(VotoRequest.class), any(Secao.class), any(Associado.class));
        verify(votoRepository, times(0)).save(any(Voto.class));
        verify(votoMapper, times(0)).mapToResponse(any(Voto.class));
    }

    @Test
    void shouldNotCreate_whenUnexpectedError_500Exception() {
        final VotoRequest req = VotoMock.getRequest();
        final Pauta pauta = PautaMock.getEntity();
        final Associado associado = AssociadoMock.getEntity();
        final Secao secao = SecaoMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(associadoRepository.findByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(Optional.of(associado));
        when(votoRepository.countByPautaIdAndCpf(any(UUID.class), anyString())).thenReturn(0L);
        when(secaoRepository.findSecaoAtivaByPautaId(any(UUID.class), any(LocalDateTime.class))).thenReturn(Optional.of(secao));
        when(votoMapper.mapToEntityInsert(any(VotoRequest.class), any(Secao.class), any(Associado.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> votoService.create(req));
        verify(votoRepository, times(0)).save(any(Voto.class));
        verify(votoMapper, times(0)).mapToResponse(any(Voto.class));
    }

}
