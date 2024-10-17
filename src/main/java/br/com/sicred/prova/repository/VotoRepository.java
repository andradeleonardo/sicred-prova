package br.com.sicred.prova.repository;

import br.com.sicred.prova.model.Voto;
import br.com.sicred.prova.v1.controller.response.ResultadoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotoRepository extends JpaRepository<Voto, UUID> {

    @Query("select e from Voto e where e.secao.pauta.id = :pautaId and e.enabled = true order by e.createdAt")
    List<Voto> findByPautaId(@Param("pautaId") UUID pautaId);

    @Query("select count(e) from Voto e where e.secao.pauta.id = :pautaId and e.associado.cpf = :cpf and e.enabled = true")
    Long countByPautaIdAndCpf(@Param("pautaId") UUID pautaId, @Param("cpf") String cpf);

    @Query("select new br.com.sicred.prova.v1.controller.response.ResultadoResponse(e.valor, count(*)) e " +
            "from Voto e " +
            "where e.secao.pauta.id = :pautaId " +
            "and e.secao.enabled = true " +
            "and e.enabled = true " +
            "group by e.valor")
    List<ResultadoResponse> findResultadoByPautaId(@Param("pautaId") UUID pautaId);

}

