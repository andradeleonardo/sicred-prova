package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.mocks.SecaoMock;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.v1.controller.request.SecaoRequest;
import br.com.sicred.prova.v1.controller.response.SecaoResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

public class SecaoMapperTest {

    @Spy
    private SecaoMapper secaoMapper = Mappers.getMapper(SecaoMapper.class);

    @Test
    public void shouldMapToResponse() {
        final Secao secao = SecaoMock.getEntity();
        final SecaoResponse resp = secaoMapper.mapToResponse(secao);
        assertEquals(secao.getId(), resp.getId());
        assertEquals(secao.getDataExpiracao(), resp.getDataExpiracao());
    }

    @Test
    public void shouldMapToEntityInsert_whenDuracaoEmMinutosIsEmpty() {
        final SecaoRequest req = SecaoMock.getRequest();
        req.setDuracaoEmMinutos(null);
        final Pauta pauta = new Pauta();
        pauta.setId(req.getIdPauta());
        final Secao secao = secaoMapper.mapToEntityInsert(req, pauta);
        assertNotNull(secao.getId());
        assertNotNull(secao.getCreatedAt());
        assertTrue(secao.getEnabled());
        assertNotNull(secao.getDataExpiracao());
        assertEquals(req.getIdPauta(), secao.getPauta().getId());
    }

}
