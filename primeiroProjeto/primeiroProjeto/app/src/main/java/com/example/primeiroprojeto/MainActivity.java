package com.example.primeiroprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.primeiroprojeto.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private EditText endereco;
    private EditText curso;
    private AlunoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vinculando os campos do layout com as variaveis do Java
        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editEndereco);
        telefone = findViewById(R.id.editCurso);
        endereco = findViewById(R.id.editEndereco);
        curso = findViewById(R.id.editCurso);

        dao = new AlunoDAO(this);
    }

    //método para botão salvar quando clicar
    public void salvar (View view){
        Aluno aluno = new Aluno();

        aluno.setNome(nome.getText().toString());
        aluno.setCpf(cpf.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setCurso(curso.getText().toString());

        long id = dao.inserir(aluno);

        //Toast é equivalente ao print ou sout
        Toast.makeText(this, "Aluno " + aluno.getNome() + " inserido com ID: " + id, Toast.LENGTH_SHORT).show();

    }

    public void abrirLista(View view) {
        // O Intent é o objeto que diz ao Android para mudar de tela
        Intent intent = new Intent(this, ListarAlunosActivity.class);
        startActivity(intent);
    }

}