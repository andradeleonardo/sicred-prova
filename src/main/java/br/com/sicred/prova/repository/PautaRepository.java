package br.com.sicred.prova.repository;

import br.com.sicred.prova.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, UUID> {

    @Query("select e from Pauta e where e.id = :id and e.enabled = true")
    Optional<Pauta> findById(@Param("id") @NonNull UUID id);

    @Query("select e from Pauta e where e.enabled = true")
    List<Pauta> findAll();

}

