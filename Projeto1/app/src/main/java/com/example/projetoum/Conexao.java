package com.example.projetoum;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Conexao extends SQLiteOpenHelper{

    private static final String name = "banco.db";
    private static final int version = 2;

    private static final String TABELA_ALUNO = "aluno";
    private static final String SQL_CREATE_ALUNO = "create table " + TABELA_ALUNO + "(" +
            "id integer primary key autoincrement, " +
            "nome varchar(50), " +
            "cpf varchar(50), " +
            "telefone varchar(50), " +
            "endereco varchar(150), " +
            "curso varchar(100), " +
            "fotoBytes BLOB)";


    public Conexao(Context context) {
        super(context, name, null, version);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ALUNO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetarTabelaAluno(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetarTabelaAluno(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!schemaAlunoValido(db)) {
            resetarTabelaAluno(db);
        }
    }

    private void resetarTabelaAluno(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ALUNO);
        onCreate(db);
    }

    private boolean schemaAlunoValido(SQLiteDatabase db) {
        Set<String> colunasNecessarias = new HashSet<>(Arrays.asList(
                "id", "nome", "cpf", "telefone", "endereco", "curso", "fotoBytes"
        ));

        Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABELA_ALUNO + ")", null);
        try {
            int idxName = cursor.getColumnIndex("name");
            if (idxName == -1) {
                return false;
            }
            while (cursor.moveToNext()) {
                colunasNecessarias.remove(cursor.getString(idxName));
            }
            return colunasNecessarias.isEmpty();
        } finally {
            cursor.close();
        }
    }
}
