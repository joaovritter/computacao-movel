//package com.example.projetoum;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import java.util.List;
//
//@Dao
//public interface AlunoDaoRoom {
//
//    // Inserir um aluno no banco de dados
//    @Insert
//    long inserir(Aluno aluno);
//
//    // Atualizar os dados de um aluno
//    @Update
//    void atualizar(Aluno aluno);
//
//    // Obter todos os alunos do banco de dados
//    @Query("SELECT * FROM aluno")
//    List<Aluno> obterTodos();
//
//    // Excluir um aluno do banco de dados
//    @Delete
//    void excluir(Aluno aluno);
//
//    // Verificar se o CPF já existe no banco de dados
//    @Query("SELECT COUNT(*) FROM aluno WHERE cpf = :cpf")
//    int cpfExistente(String cpf);
//
//
//}
