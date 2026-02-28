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
        cpf = findViewById(R.id.editCpf);
        telefone = findViewById(R.id.editTelefone);
        endereco = findViewById(R.id.editEndereco);
        curso = findViewById(R.id.editCurso);

        dao = new AlunoDAO(this);
    }

    private boolean validarFormAluno (String nome, String cpf, String telefone, String endereco, String curso){
        //1 campos vazios
        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || endereco.isEmpty() || curso.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dao.cpfExistente(cpf)) {
            Toast.makeText(this, "CPF duplicado. Insira um CPF diferente.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!dao.validaCpf(cpf)){
            Toast.makeText(this,"Cpf invalido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!dao.validaTelefone(telefone)) {
            Toast.makeText(this, "Telefone inválido! Use o formato (XX) 9XXXX-XXXX", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }

    //método para botão salvar quando clicar
    public void salvar(View view) {
        // Captura os dados
        String nomeDig = nome.getText().toString().trim();
        String cpfDig = cpf.getText().toString().trim();
        String telDig = telefone.getText().toString().trim();
        String endDig = endereco.getText().toString().trim();
        String cursoDig = curso.getText().toString().trim();

        if (!validarFormAluno(nomeDig, cpfDig, telDig, endDig, cursoDig)) {
            return;
        }

        // Se passou da validação, executa o salvamento
        Aluno aluno = new Aluno();
        aluno.setNome(nomeDig);
        aluno.setCpf(cpfDig);
        aluno.setTelefone(telDig);
        aluno.setEndereco(endDig);
        aluno.setCurso(cursoDig);

        long id = dao.inserir(aluno);

        Toast.makeText(this, "Aluno " + aluno.getNome() + " inserido com ID: " + id, Toast.LENGTH_SHORT).show();
    }

    public void abrirLista(View view) {
        // O Intent é o objeto que diz ao Android para mudar de tela
        Intent intent = new Intent(this, ListarAlunosActivity.class);
        startActivity(intent);
    }

}