package com.jorge.calendar_api.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.jorge.calendar_api.model.Escala;
import com.jorge.calendar_api.repository.EscalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class EscalaService {

    @Autowired
    private EscalaRepository escalaRepository;

    @Autowired
    private EmailService emailService;

    private boolean firebaseInitialized = false;

    @PostConstruct
    public void initializeFirebase() {
        try {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-adminsdk.json");
            if (serviceAccount == null) {
                System.err.println(
                        "Arquivo firebase-adminsdk.json não encontrado. Push notifications não serão enviadas.");
                return;
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                firebaseInitialized = true;
                System.out.println("Firebase inicializado com sucesso.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
        }
    }

    public Escala criarEscala(Escala escala) {
        return escalaRepository.save(escala);
    }

    public List<Escala> listarEscalas() {
        return escalaRepository.findAll();
    }

    public List<Escala> listarEscalasPorData(LocalDate data) {
        return escalaRepository.findByDataEscala(data);
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void enviarLembretesDiarios() {
        LocalDate hoje = LocalDate.now();
        List<Escala> escalasDoDia = listarEscalasPorData(hoje);

        for (Escala escala : escalasDoDia) {
            String mensagem = formatarLembreteEscala(escala);
            try {
                emailService.sendReportEmail(
                        escala.getNomeVoluntario(),
                        "Lembrete: Sua Escala de Hoje",
                        mensagem);
                System.out.println("E-mail enviado para: " + escala.getNomeVoluntario());

                if (firebaseInitialized) {
                    Message pushMessage = Message.builder()
                            .putData("title", "Lembrete de Escala")
                            .putData("body", "Você tem uma escala hoje: " + escala.getDescricao())
                            .setToken("fcm-token-do-voluntario") // Substitua pelo token real
                            .build();
                    FirebaseMessaging.getInstance().send(pushMessage);
                    System.out.println("Push enviado para: " + escala.getNomeVoluntario());
                } else {
                    System.out.println("Firebase não inicializado. Push não enviado.");
                }
            } catch (Exception e) {
                System.err
                        .println("Erro ao enviar lembrete para " + escala.getNomeVoluntario() + ": " + e.getMessage());
            }
        }
    }

    private String formatarLembreteEscala(Escala escala) {
        return "<h2>Lembrete de Escala</h2>" +
                "<p>Olá, " + escala.getNomeVoluntario() + "!</p>" +
                "<p>Você tem uma escala hoje (" + escala.getDataEscala().toString() + ").</p>" +
                "<p><strong>Descrição:</strong> "
                + (escala.getDescricao() != null ? escala.getDescricao() : "Sem descrição") + "</p>" +
                "<p>Por favor, lembre-se de registrar seu relatório após completar a tarefa.</p>";
    }
}