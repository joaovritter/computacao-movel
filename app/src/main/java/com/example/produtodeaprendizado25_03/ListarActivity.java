package com.example.produtodeaprendizado25_03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListarActivity extends AppCompatActivity {

    private ListView listView;
    private AppDatabase db;
    private List<Equipamentos> equipamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        listView = findViewById(R.id.listaEquipamentos);
        db = AppDatabase.getInstance(this);

        equipamentos = db.equipamentosDao().listarTodos();

        // Separacao de texto para nao ficar tudo junto
        List<String> itens = new ArrayList<>();
        for (Equipamentos e : equipamentos) {
            itens.add(e.getNumeroSerie() + " - " + e.getNomeEquipamento() + " - " + e.getDepartamento());
        }

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itens);
        listView.setAdapter(adaptador);
    }

    public void voltarMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}