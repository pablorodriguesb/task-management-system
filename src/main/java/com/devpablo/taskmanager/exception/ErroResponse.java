package com.devpablo.taskmanager.exception;

import org.springframework.cglib.core.Local;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;
import java.util.Map;

public class ErroResponse {
    private String mensagem;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> erros;

    public ErroResponse(String mensagem, int status, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = timestamp;
    }

    public ErroResponse(String mensagem, int status, LocalDateTime timestamp,
                         Map<String, String> erros) {
        this(mensagem, status, timestamp);
        this.erros = erros;
    }

    // getter e setter
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimeStamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Map<String, String> getErros() { return erros; }
    public void setErros(Map<String, String> erros) {this.erros = erros; }
}


