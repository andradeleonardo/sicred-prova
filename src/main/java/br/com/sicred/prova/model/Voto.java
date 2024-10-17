package br.com.sicred.prova.model;

import br.com.sicred.prova.enums.ValorVotoEnum;
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
@Table(name = "voto")
public class Voto extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_value", length = 10, nullable = false)
    private ValorVotoEnum valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id", referencedColumnName = "id", nullable = false)
    private Associado associado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secao_id", referencedColumnName = "id", nullable = false)
    private Secao secao;

    public Voto(UUID id) {
        setId(id);
    }

}
