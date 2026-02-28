package com.example.primeiroprojeto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private Conexao conexao;

    private SQLiteDatabase banco;

    public AlunoDAO (Context context){
        conexao = new Conexao(context); //cria uma conexao
        banco = conexao.getWritableDatabase(); //iniciar um banco para escrita
    }

    public long inserir (Aluno aluno){ //long pois retorna o id do aluno
        ContentValues values = new ContentValues(); //valores que irei inserir (pacote de dados de chave valor)
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("curso", aluno.getCurso());

        return banco.insert("aluno", null, values); //table aluno, nao tera colunas vazias, valores values
    }

    public List<Aluno> obterTodos() {
        List<Aluno> alunos = new ArrayList<>();
        //cursor aponta para as linhas retornadas
        Cursor cursor = banco.query("aluno", new String[]{"id", "nome", "cpf", "telefone", "endereco", "curso"},
                null, null, null, null, null);
        while (cursor.moveToNext()){
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(0));
            aluno.setNome(cursor.getString(1));
            aluno.setCpf(cursor.getString(2));
            aluno.setTelefone(cursor.getString(3));

            alunos.add(aluno);
        }
        return  alunos;
    }
}
