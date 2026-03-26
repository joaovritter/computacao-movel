package com.example.produtodeaprendizado25_03;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface EquipamentosDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void inserir(Equipamentos equipamento);

    @Query("SELECT * FROM equipamentos ORDER BY nome_equipamento ASC")
    List<Equipamentos> listarTodos();
}
