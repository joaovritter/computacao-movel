package com.example.projetoum;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class activity_listar_alunos extends AppCompatActivity {

    private ListView listView;
    private AlunoDAO dao;
    private List<Aluno> alunos;
    private ArrayAdapter<Aluno> adaptador;
    private List<Aluno> alunosFiltrados = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define qual arquivo XML será o layout desta tela
        setContentView(R.layout.activity_listar_alunos);

        // Vinculando os componentes do XML com as variáveis Java (ID deve ser igual ao
        // do XML)
        listView = findViewById(R.id.lista_alunos);
        dao = new AlunoDAO(this);
        alunos = dao.obterTodos();
        alunosFiltrados.addAll(alunos); // somente alunos que foram consultados

        // registrar o menu de contexto (excluir e atualizar) na listview
        registerForContextMenu(listView);

        adaptador = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunosFiltrados);
        listView.setAdapter(adaptador);

        EditText editTextBuscar = findViewById(R.id.editTextBuscar);
        Button btnBuscar = findViewById(R.id.btnBuscar);

        // funcao para buscar alunos na lista, se nao buscar nada mostra todos
        // vamos utilizar o botao btnBuscar e setOnClickListener para isso
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busca = editTextBuscar.getText().toString().toLowerCase().trim();

                // limpa a lista filtrada para adicionar os resultados da busca
                alunosFiltrados.clear();

                if (busca.isEmpty()) {
                    // se a busca estiver vazia, mostra todos os alunos
                    alunosFiltrados.addAll(alunos);
                } else {
                    // filtra os alunos que contêm o texto da busca no nome
                    for (Aluno aluno : alunos) {
                        if (aluno.getNome().toLowerCase().contains(busca)) {
                            alunosFiltrados.add(aluno);
                        }
                    }
                }
                // notifica o adaptador que os dados foram alterados para atualizar a ListView
                adaptador.notifyDataSetChanged();
            }
        });

    }

    public void irCadastro(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo); // Chama o método da superclasse (neste caso, o método
                                                      // onCreateContextMenu da classe pai).

        // inflar (converter um arquivo XML de menu em um objeto Menu)
        MenuInflater i = getMenuInflater();

        // inflate do MenuInflater é usado para inflar o menu de contexto.
        i.inflate(R.menu.menu_contexto, menu);
    }

    public void excluir(MenuItem item) {
        // Obtém informações do item selecionado no menu de contexto.
        // get de aluno no menu
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // Obtém o aluno que será excluído a partir da lista filtrada.
        final Aluno alunoExcluir = alunosFiltrados.get(menuInfo.position);

        // Exibe um alerta de confirmação antes de excluir o aluno
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção") // Título do alerta
                .setMessage("Realmente deseja excluir o aluno?")
                .setNegativeButton("NÃO", null) // Caso o usuário clique em "NÃO", fecha o alerta sem fazer nada.
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove o aluno da lista filtrada
                        alunosFiltrados.remove(alunoExcluir);
                        // Remove o aluno da lista principal
                        alunos.remove(alunoExcluir);
                        // Exclui o aluno do banco de dados
                        dao.excluir(alunoExcluir);
                        // Atualiza a ListView para refletir a exclusão
                        listView.invalidateViews();
                    }
                }).create(); // Cria a caixa de diálogo
        dialog.show(); // Exibe o alerta na tela
    }

    public void atualizar(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Aluno alunoAtualizar = alunosFiltrados.get(menuInfo.position);

        // Abre a tela de cadastro (MainActivity). Usa mesma tela para editar
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("aluno", alunoAtualizar);
        // inicia a activity carregada com os dados do aluno a editar
        startActivity(it);
    }

    // Recarregar a lista de alunos a ser exibida após as modificações do método
    // 'atualizar'
    @Override
    protected void onResume() {
        super.onResume();
        alunos = dao.obterTodos();

        // limpa lista filtrada e adiciona novos alunos
        alunosFiltrados.clear();
        alunosFiltrados.addAll(alunos);

        if (adaptador != null) {
            adaptador.notifyDataSetChanged();
        }
    }
}