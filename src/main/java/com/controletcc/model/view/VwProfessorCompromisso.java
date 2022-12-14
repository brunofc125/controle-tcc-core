package com.controletcc.model.view;

import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Immutable
@Table(name = "vw_professor_compromissos")
public class VwProfessorCompromisso {
    @Id
    @Column(name = "identifier")
    private String identifier;

    @Column(name = "id")
    private Long id;

    @Column(name = "tipo_compromisso")
    @Enumerated(EnumType.STRING)
    private TipoCompromisso tipoCompromisso;

    @Column(name = "id_projeto_tcc")
    private Long idProjetoTcc;

    @Column(name = "id_agenda_apresentacao")
    private Long idAgendaApresentacao;

    @Column(name = "tipo_tcc")
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "alunos_nome")
    private String alunosNome;

    @Column(name = "id_professor")
    private Long idProfessor;

    @Column(name = "id_professor_supervisor")
    private Long idProfessorSupervisor;

    @Column(name = "id_professor_orientador")
    private Long idProfessorOrientador;

    @Column(name = "id_professor_banca")
    private Long idProfessorBanca;

    @Column(name = "data_inicial")
    private LocalDateTime dataInicial;

    @Column(name = "data_final")
    private LocalDateTime dataFinal;

    public record Compromisso(TipoCompromisso tipoCompromisso, TipoTcc tipoTcc, Long id, Long idProfessor,
                              Long idProfessorSupervisor, Long idProfessorOrientador, LocalDateTime dataInicial,
                              LocalDateTime dataFinal) {
    }


}
