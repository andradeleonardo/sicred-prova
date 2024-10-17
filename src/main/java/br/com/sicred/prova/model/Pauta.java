package br.com.sicred.prova.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "pauta")
public class Pauta extends AbstractBaseEntity {

    @Column(name = "name", length = 150, nullable = false)
    private String nome;

    public Pauta(UUID id) {
        setId(id);
    }

}
