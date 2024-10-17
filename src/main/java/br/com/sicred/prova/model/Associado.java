package br.com.sicred.prova.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "associado")
public class Associado extends AbstractBaseEntity {

    @Column(name = "cpf", length = 14, nullable = false)
    private String cpf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", referencedColumnName = "id", nullable = false)
    private Pauta pauta;

    public Associado(UUID id) {
        setId(id);
    }

}
