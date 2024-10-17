package br.com.sicred.prova.repository;

import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, UUID> {

    @Query("select e from Associado e where e.pauta.id = :pautaId and e.enabled = true order by e.cpf")
    List<Associado> findByPautaId(@Param("pautaId") UUID pautaId);

    @Query("select count(e) from Associado e where e.pauta.id = :pautaId and e.enabled = true and e.cpf = :cpf")
    Long countByPautaIdAndCpf(@Param("pautaId") UUID pautaId, @Param("cpf") String cpf);

    @Query("select e from Associado e where e.pauta.id = :pautaId and e.enabled = true and e.cpf = :cpf")
    Optional<Associado> findByPautaIdAndCpf(@Param("pautaId") UUID pautaId, @Param("cpf") String cpf);

}

