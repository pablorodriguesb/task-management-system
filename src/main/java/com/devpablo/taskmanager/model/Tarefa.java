package com.devpablo.taskmanager.model;

import com.devpablo.taskmanager.enums.CategoriaTarefa;
import com.devpablo.taskmanager.enums.PrioridadeTarefa;
import com.devpablo.taskmanager.enums.StatusTarefa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "responsavel")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    @Enumerated(EnumType.STRING)
    private PrioridadeTarefa prioridade;

    @Enumerated(EnumType.STRING)
    private CategoriaTarefa categoria;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "data_vencimento")
    private LocalDateTime dataVencimento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario responsavel;
}
