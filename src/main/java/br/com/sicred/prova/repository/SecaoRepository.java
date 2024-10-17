package br.com.sicred.prova.repository;

import br.com.sicred.prova.model.Secao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecaoRepository extends JpaRepository<Secao, UUID> {

    @Query("select e from Secao e where e.pauta.id = :pautaId and e.enabled = true order by e.createdAt")
    List<Secao> findByPautaId(@Param("pautaId") UUID pautaId);

    @Query("select e from Secao e where e.pauta.id = :pautaId and e.enabled = true and dataExpiracao >= :currentDateTime")
    Optional<Secao> findSecaoAtivaByPautaId(@Param("pautaId") UUID pautaId,
                                            @Param("currentDateTime") LocalDateTime currentDateTime);

}

