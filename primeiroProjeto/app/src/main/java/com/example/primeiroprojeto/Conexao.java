package com.example.primeiroprojeto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//OpenHelper = classe nativa do Android que gerencia a criação do banco e versão de tabelas
public class Conexao extends SQLiteOpenHelper {

    private static final String name = "banco.db";
    private static final int version = 1;

    public Conexao (Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL("create table aluno " +
                "(id integer primary key autoincrement, " +
                "nome varchar(50), " +
                "cpf varchar(50), " +
                "telefone varchar(50)," +
                "endereco varchar(100)," +
                "curso varchar (50))"
        );
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){

    }

}