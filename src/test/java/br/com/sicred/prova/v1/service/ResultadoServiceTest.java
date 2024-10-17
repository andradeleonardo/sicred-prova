package br.com.sicred.prova.v1.service;

import br.com.sicred.prova.common.AppResourceBundle;
import br.com.sicred.prova.common.exception.Response500Exception;
import br.com.sicred.prova.enums.ValorVotoEnum;
import br.com.sicred.prova.repository.VotoRepository;
import br.com.sicred.prova.utils.FakeData;
import br.com.sicred.prova.v1.controller.response.ResultadoResponse;
import br.com.sicred.prova.v1.mapper.ResultadoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ResultadoServiceTest {

    @InjectMocks
    private ResultadoService resultadoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AppResourceBundle resourceBundle;

    @Spy
    private ResultadoMapper resultadoMapper = Mappers.getMapper(ResultadoMapper.class);

    @Test
    void shouldFindByPautaId() {
        final ResultadoResponse resultadoSim = new ResultadoResponse(ValorVotoEnum.SIM, 2);
        final ResultadoResponse resultadoNao = new ResultadoResponse(ValorVotoEnum.NAO, 8);
        when(votoRepository.findResultadoByPautaId(any(UUID.class))).thenReturn(List.of(resultadoSim, resultadoNao));
        final List<ResultadoResponse> resp = resultadoService.findByPautaId(FakeData.FAKE_UUID);
        assertFalse(resp.isEmpty());
        assertEquals(2, resp.size());
        assertEquals(20, resp.get(0).getPercentual());
        assertEquals(80, resp.get(1).getPercentual());
    }

    @Test
    void shouldFindByPautaId_nenhumVoto() {
        when(votoRepository.findResultadoByPautaId(any(UUID.class))).thenReturn(List.of());
        final List<ResultadoResponse> resp = resultadoService.findByPautaId(FakeData.FAKE_UUID);
        assertFalse(resp.isEmpty());
        assertEquals(2, resp.size());
        assertEquals(0, resp.get(0).getPercentual());
        assertEquals(0, resp.get(1).getPercentual());
    }

    @Test
    void shouldNotFindByPautaId_whenUnexpectedError_500Exception() {
        when(votoRepository.findResultadoByPautaId(any(UUID.class))).thenReturn(null);
        assertThrows(Response500Exception.class, () -> resultadoService.findByPautaId(FakeData.FAKE_UUID));
    }

}
