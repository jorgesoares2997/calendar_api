package com.jorge.calendar_api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "escalas")
public class Escala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeVoluntario; // E-mail do voluntário

    @Column(nullable = false)
    private LocalDate dataEscala;

    @Column
    private String descricao;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeVoluntario() {
        return nomeVoluntario;
    }

    public void setNomeVoluntario(String nomeVoluntario) {
        this.nomeVoluntario = nomeVoluntario;
    }

    public LocalDate getDataEscala() {
        return dataEscala;
    }

    public void setDataEscala(LocalDate dataEscala) {
        this.dataEscala = dataEscala;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}