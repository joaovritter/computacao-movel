package com.example.produtodeaprendizado25_03;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "equipamentos")
public class Equipamentos {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "numero_serie")
    private String numeroSerie;

    @ColumnInfo(name = "nome_equipamento")
    private String nomeEquipamento;

    @ColumnInfo(name = "departamento")
    private String departamento;

    public Equipamentos() {

    }

    public Equipamentos(@NonNull String numeroSerie, String nomeEquipamento, String departamento) {
        this.numeroSerie = numeroSerie;
        this.nomeEquipamento = nomeEquipamento;
        this.departamento = departamento;
    }

    @NonNull
    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(@NonNull String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNomeEquipamento() {
        return nomeEquipamento;
    }

    public void setNomeEquipamento(String nomeEquipamento) {
        this.nomeEquipamento = nomeEquipamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}