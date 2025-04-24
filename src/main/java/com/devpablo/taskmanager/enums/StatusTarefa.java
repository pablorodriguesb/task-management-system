package com.devpablo.taskmanager.enums;

public enum StatusTarefa {
    PENDENTE, // tarefa criada mas nao iniciada
    EM_ANDAMENTO, // tarefa sendo trabalhada
    CONCLUIDA, // tarefa finalizada
    CANCELADA; // tarefa cancelada


    public boolean isAtiva() {
        return this == PENDENTE || this == EM_ANDAMENTO;
    }
}

