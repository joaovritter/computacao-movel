package com.example.produtodeaprendizado25_03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText txtNumeroSerie;
    private EditText txtNome;
    private EditText txtDepartamento;
    private Button btnCadastrar;
    private Button btnListar;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNumeroSerie = findViewById(R.id.txtNumeroSerie);
        txtNome = findViewById(R.id.txtNome);
        txtDepartamento = findViewById(R.id.txtDepartamento);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnListar = findViewById(R.id.btnListar);

        db = AppDatabase.getInstance(this);


    }

    public void cadastrar(View view) {
        String numeroSerie = txtNumeroSerie.getText().toString().trim();
        String nome = txtNome.getText().toString().trim();
        String departamento = txtDepartamento.getText().toString().trim();

        if (numeroSerie.isEmpty() || nome.isEmpty() || departamento.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Equipamentos equipamento = new Equipamentos(numeroSerie, nome, departamento);
            db.equipamentosDao().inserir(equipamento);

            Toast.makeText(this, "Equipamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

            txtNumeroSerie.setText("");
            txtNome.setText("");
            txtDepartamento.setText("");
            txtNumeroSerie.requestFocus();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao cadastrar. Numero de serie pode ja existir.", Toast.LENGTH_LONG).show();
        }
    }

    public void abrirLista(View view) {
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }
}