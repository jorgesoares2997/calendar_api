package com.jorge.calendar_api.controller;

import com.jorge.calendar_api.model.Escala;
import com.jorge.calendar_api.service.EscalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/escalas")
public class EscalaController {

    @Autowired
    private EscalaService escalaService;

    @PostMapping
    public ResponseEntity<Escala> criarEscala(@RequestBody Escala escala) {
        Escala novaEscala = escalaService.criarEscala(escala);
        return ResponseEntity.ok(novaEscala);
    }

    @GetMapping
    public ResponseEntity<List<Escala>> listarEscalas() {
        return ResponseEntity.ok(escalaService.listarEscalas());
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<List<Escala>> listarEscalasPorData(@PathVariable String data) {
        LocalDate dataEscala = LocalDate.parse(data);
        return ResponseEntity.ok(escalaService.listarEscalasPorData(dataEscala));
    }
}