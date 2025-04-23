package com.example.ac2remedios;

import java.util.Date;

public class Medicamento {
    private int id;
    private String nome;
    private String descricao;
    private Date horario;
    private boolean tomado;

    public Medicamento(String nome, String descricao, Date horario, boolean tomado) {
        this.nome = nome;
        this.descricao = descricao;
        this.horario = horario;
        this.tomado = tomado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public boolean isTomado() {
        return tomado;
    }

    public void setTomado(boolean tomado) {
        this.tomado = tomado;
    }
}
