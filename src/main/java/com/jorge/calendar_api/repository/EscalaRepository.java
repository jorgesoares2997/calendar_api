package com.jorge.calendar_api.repository;

import com.jorge.calendar_api.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EscalaRepository extends JpaRepository<Escala, Long> {
    List<Escala> findByDataEscala(LocalDate dataEscala);
}