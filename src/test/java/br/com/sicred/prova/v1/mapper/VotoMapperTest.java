package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.mocks.AssociadoMock;
import br.com.sicred.prova.mocks.SecaoMock;
import br.com.sicred.prova.mocks.VotoMock;
import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.model.Voto;
import br.com.sicred.prova.v1.controller.request.VotoRequest;
import br.com.sicred.prova.v1.controller.response.VotoResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

public class VotoMapperTest {

    @Spy
    private VotoMapper votoMapper = Mappers.getMapper(VotoMapper.class);

    @Test
    public void shouldMapToResponse() {
        final Voto voto = VotoMock.getEntity();
        final VotoResponse resp = votoMapper.mapToResponse(voto);
        assertEquals(voto.getId(), resp.getId());
        assertEquals(voto.getValor(), resp.getValor());
        assertEquals(voto.getAssociado().getCpf(), resp.getCpf());
        assertEquals(voto.getSecao().getId(), resp.getIdSecao());
    }

    @Test
    public void shouldMapToEntityInsert() {
        final VotoRequest req = VotoMock.getRequest();
        final Secao secao = SecaoMock.getEntity();
        final Associado associado = AssociadoMock.getEntity();
        final Voto voto = votoMapper.mapToEntityInsert(req, secao, associado);
        assertNotNull(voto.getId());
        assertNotNull(voto.getCreatedAt());
        assertTrue(voto.getEnabled());
        assertEquals(secao.getId(), voto.getSecao().getId());
        assertEquals(associado.getId(), voto.getAssociado().getId());
        assertEquals(req.getValor(), voto.getValor());
    }

}
