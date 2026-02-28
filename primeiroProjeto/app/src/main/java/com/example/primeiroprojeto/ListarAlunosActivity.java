package com.example.primeiroprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ListarAlunosActivity extends AppCompatActivity {
    private ListView listView;
    private AlunoDAO alunoDAO;
    private List<Aluno> alunos;

    @Override
    //bundle preserva os dados (chave valor) da tela quando precisa ser reiniciada pelo sistema
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_alunos);

        //vincular variaveis com os campos do layout
        listView = findViewById(R.id.lista_alunos);
        alunoDAO = new AlunoDAO(this);
        alunos = alunoDAO.obterTodos();

        //ArrayAdapter conceta fonte de dados (array alunos) a componentes da interface (listView)
        ArrayAdapter<Aluno> adaptor = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);

        listView.setAdapter(adaptor);
    }

    public void voltarMenu(View view) {
        // O Intent é o objeto que diz ao Android para mudar de tela
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
