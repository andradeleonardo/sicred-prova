package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.mocks.PautaMock;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.v1.controller.request.PautaRequest;
import br.com.sicred.prova.v1.controller.response.PautaResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

public class PautaMapperTest {

    @Spy
    private PautaMapper pautaMapper = Mappers.getMapper(PautaMapper.class);

    @Test
    public void shouldMapToResponse() {
        final Pauta pauta = PautaMock.getEntity();
        final PautaResponse resp = pautaMapper.mapToResponse(pauta);
        assertEquals(pauta.getId(), resp.getId());
        assertEquals(pauta.getNome(), resp.getNome());
    }

    @Test
    public void shouldMapToEntityInsert() {
        final PautaRequest req = PautaMock.getRequest();
        final Pauta pauta = pautaMapper.mapToEntityInsert(req);
        assertNotNull(pauta.getId());
        assertNotNull(pauta.getCreatedAt());
        assertTrue(pauta.getEnabled());
        assertEquals(req.getNome(), pauta.getNome());
    }

}
