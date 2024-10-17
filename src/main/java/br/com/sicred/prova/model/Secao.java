package br.com.sicred.prova.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "secao")
public class Secao extends AbstractBaseEntity {

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime dataExpiracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", referencedColumnName = "id", nullable = false)
    private Pauta pauta;

    public Secao(UUID id) {
        setId(id);
    }

}
