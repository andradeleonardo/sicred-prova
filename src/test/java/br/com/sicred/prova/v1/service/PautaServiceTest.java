package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response404Exception;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.mocks.PautaMock;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.repository.PautaRepository;
import br.com.sicred.prova.utils.FakeData;
import br.com.sicred.prova.v1.controller.request.PautaRequest;
import br.com.sicred.prova.v1.controller.response.PautaResponse;
import br.com.sicred.prova.v1.mapper.PautaMapper;
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
public class PautaServiceTest {

    @InjectMocks
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AppResourceBundle resourceBundle;

    @Spy
    private PautaMapper pautaMapper = Mappers.getMapper(PautaMapper.class);

    @Test
    void shouldFindAll() {
        final Pauta pauta = PautaMock.getEntity();
        when(pautaRepository.findAll()).thenReturn(List.of(pauta));
        final List<PautaResponse> resp = pautaService.findAll();
        assertFalse(resp.isEmpty());
        assertEquals(1, resp.size());
    }

    @Test
    void shouldFindById() {
        final Pauta pauta = PautaMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        final PautaResponse resp = pautaService.findById(FakeData.FAKE_UUID);
        assertNotNull(resp);
    }

    @Test
    void shouldNotFindById_whenNotFound_404Exception() {
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(Response404Exception.class, () -> pautaService.findById(FakeData.FAKE_UUID));
    }

    @Test
    void shouldNotFindById_whenUnexpectedError_500Exception() {
        final Pauta pauta = PautaMock.getEntity();
        when(pautaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pauta));
        when(pautaMapper.mapToResponse(any(Pauta.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> pautaService.findById(FakeData.FAKE_UUID));
    }

    @Test
    void shouldCreate() {
        final PautaRequest req = PautaMock.getRequest();
        pautaService.create(req);
        verify(pautaMapper, times(1)).mapToEntityInsert(any(PautaRequest.class));
        verify(pautaRepository, times(1)).save(any(Pauta.class));
        verify(pautaMapper, times(1)).mapToResponse(any(Pauta.class));
    }

    @Test
    void shouldNotCreate_whenUnexpectedError_500Exception() {
        final PautaRequest req = PautaMock.getRequest();
        when(pautaMapper.mapToEntityInsert(any(PautaRequest.class))).thenThrow(new NullPointerException());
        assertThrows(Response500Exception.class, () -> pautaService.create(req));
    }

}
