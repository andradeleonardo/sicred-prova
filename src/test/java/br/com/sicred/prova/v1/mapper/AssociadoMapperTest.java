package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.mocks.AssociadoMock;
import br.com.sicred.prova.mocks.PautaMock;
import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.v1.controller.request.AssociadoRequest;
import br.com.sicred.prova.v1.controller.request.PautaRequest;
import br.com.sicred.prova.v1.controller.response.AssociadoResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

public class AssociadoMapperTest {

    @Spy
    private AssociadoMapper associadoMapper = Mappers.getMapper(AssociadoMapper.class);

    @Test
    public void shouldMapToResponse() {
        final Associado associado = AssociadoMock.getEntity();
        final AssociadoResponse resp = associadoMapper.mapToResponse(associado);
        assertEquals(associado.getId(), resp.getId());
        assertEquals(associado.getCpf(), resp.getCpf());
    }

    @Test
    public void shouldMapToEntityInsert() {
        final AssociadoRequest req = AssociadoMock.getRequest();
        final Pauta pauta = new Pauta();
        pauta.setId(req.getIdPauta());
        final Associado associado = associadoMapper.mapToEntityInsert(req, pauta);
        assertNotNull(associado.getId());
        assertNotNull(associado.getCreatedAt());
        assertTrue(associado.getEnabled());
        assertEquals(req.getCpf(), associado.getCpf());
        assertEquals(req.getIdPauta(), associado.getPauta().getId());
    }

}
