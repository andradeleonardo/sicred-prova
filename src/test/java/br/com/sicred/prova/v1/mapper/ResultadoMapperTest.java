package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.enums.ValorVotoEnum;
import br.com.sicred.prova.v1.controller.response.ResultadoResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultadoMapperTest {

    @Spy
    private ResultadoMapper resultadoMapper = Mappers.getMapper(ResultadoMapper.class);

    @Test
    public void shouldMontarResultadoFinal_ambasOpcoesVotadas() {
        final ResultadoResponse sim = new ResultadoResponse(ValorVotoEnum.SIM, 2);
        final ResultadoResponse nao = new ResultadoResponse(ValorVotoEnum.NAO, 8);

        List<ResultadoResponse> resp = resultadoMapper.montaResultadoFinal(List.of(sim, nao));

        assertEquals(2, resp.size());
        assertEquals(20, resp.get(0).getPercentual());
        assertEquals(80, resp.get(1).getPercentual());
    }

    @Test
    public void shouldMontarResultadoFinal_apenasSimVotado() {
        final ResultadoResponse sim = new ResultadoResponse(ValorVotoEnum.SIM, 2);
        List<ResultadoResponse> resp = resultadoMapper.montaResultadoFinal(List.of(sim));
        assertEquals(100, resp.get(0).getPercentual());
        assertEquals(0, resp.get(1).getPercentual());
    }

    @Test
    public void shouldMontarResultadoFinal_apenasNaoVotado() {
        final ResultadoResponse nao = new ResultadoResponse(ValorVotoEnum.NAO, 2);
        List<ResultadoResponse> resp = resultadoMapper.montaResultadoFinal(List.of(nao));
        assertEquals(0, resp.get(0).getPercentual());
        assertEquals(100, resp.get(1).getPercentual());
    }

}
