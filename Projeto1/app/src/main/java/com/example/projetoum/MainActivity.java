package com.example.projetoum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

private EditText nome;
private EditText cpf;
private EditText telefone;
private EditText cep;
private EditText endereco;
private EditText curso;
private AlunoDAO alunoDao;
private Aluno aluno = null;
private ImageView imageView;
private static final int CAMERA_PERMISSION_CODE = 100; //identificador para a permissão de câmera
private static final int REQUEST_IMAGE_CAPTURE = 200; //identificador para a captura de imagem
private String ultimoCepConsultado = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vinculando os componentes do XML com as variáveis Java (ID deve ser igual ao do XML)
        nome = findViewById(R.id.texto1);
        cpf = findViewById(R.id.texto2);
        telefone = findViewById(R.id.texto3);
        cep = findViewById(R.id.textoCep);
        endereco = findViewById(R.id.texto4);
        curso = findViewById(R.id.texto5);

        imageView = findViewById(R.id.imageView);


        // Dispara a busca quando 8 digitos de CEP forem preenchidos.
        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cepSomenteNumeros = s.toString().replaceAll("\\D", "");
                if (cepSomenteNumeros.length() == 8 && !cepSomenteNumeros.equals(ultimoCepConsultado)) {
                    consultarCep(cepSomenteNumeros);
                }
            }
        });


        // Instanciando o DAO. Passamos 'this' (a própria Activity) como Contexto.
        // O Contexto é necessário para o SQLite saber em que pasta do sistema salvar o arquivo.
       alunoDao = new AlunoDAO(this);

        //pegar dados que vem da intent do atualizar
        Intent it = getIntent(); //pega intenção
        if(it.hasExtra("aluno")){
            aluno = (Aluno) it.getSerializableExtra("aluno");
            nome.setText(aluno.getNome().toString());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());
            endereco.setText(aluno.getEndereco());
            curso.setText(aluno.getCurso());

            byte[] fotoBytes = aluno.getFotoBytes();
            if (fotoBytes != null && fotoBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                imageView.setImageBitmap(bitmap);
            }

        }
    }


    public void salvar(View view){


        String cpfDigitado = cpf.getText().toString();
        String telefoneDigitado = telefone.getText().toString();

        if(!alunoDao.validaCpf(cpfDigitado)){
            Toast.makeText(this, "CPF inválido!", Toast.LENGTH_LONG).show();
            cpf.requestFocus(); // Opcional: foca no campo de CPF para correção
            return; // Interrompe a execução do método salvar
        }

        if (!alunoDao.validaTelefone(telefoneDigitado)) {
            Toast.makeText(this, "Telefone inválido! Use o formato (XX) 9XXXX-XXXX.", Toast.LENGTH_LONG).show();
            telefone.requestFocus(); // Opcional: foca no campo de telefone para correção
            return; // Interrompe a execução do método salvar
        }

        if(aluno==null || aluno.getId()==null) { //CADASTRA O ALUNO
            aluno = new Aluno();
            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());
            aluno.setEndereco(endereco.getText().toString());
            aluno.setCurso(curso.getText().toString());
            long id = alunoDao.inserir(aluno); //inserir o aluno
            Toast.makeText(this,"Aluno Inserido com sucesso!! com id: ", Toast.LENGTH_SHORT).show();
        }
        else { //ATUALIZA
            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());
            aluno.setEndereco(endereco.getText().toString());
            aluno.setCurso(curso.getText().toString());
            alunoDao.atualizar(aluno); //inserir o aluno
            Toast.makeText(this,"Aluno Atualizado!! com id: ", Toast.LENGTH_SHORT).show();
        }



        // 1. Criamos um objeto de modelo (POJO)
        Aluno a = new Aluno();


        // 2. Coletamos os dados digitados nos EditText e convertemos para String
        a.setNome(nome.getText().toString());
        a.setCpf(cpf.getText().toString());
        a.setTelefone(telefone.getText().toString());
        a.setEndereco(endereco.getText().toString());
        a.setCurso(curso.getText().toString());
        // 3. Chamamos o método de persistência do DAO
        // O retorno 'long' indica o ID gerado pelo banco para este novo registro.

        long id = alunoDao.inserir(a);

        if (id == -1) {
            // O método Inserir retorna -1 quando o CPF já existe
            Toast.makeText(this, "Erro: CPF já cadastrado!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Aluno inserido com id: " + id, Toast.LENGTH_SHORT).show();
            // Opcional: Limpar os campos após o sucesso
        }

        //limpar formulario ao salvar


    }

    public void irLista(View view){
        Intent intent = new Intent(this, ListarAlunos.class);
        startActivity(intent);
    }

    public void buscarCep (View view){
        String cepDigitado = cep.getText().toString().replaceAll("\\D", "");
        if (cepDigitado.length() != 8) {
            Toast.makeText(this, "Informe um CEP com 8 digitos.", Toast.LENGTH_SHORT).show();
            cep.requestFocus();
            return;
        }
        consultarCep(cepDigitado);
    }

    private void consultarCep(String cepLimpo) {
        ultimoCepConsultado = cepLimpo;
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("https://viacep.com.br/ws/" + cepLimpo + "/json/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(7000);
                connection.setReadTimeout(7000);

                int statusCode = connection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> Toast.makeText(this, "Erro ao consultar CEP.", Toast.LENGTH_SHORT).show());
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    resposta.append(linha);
                }
                reader.close();

                JSONObject json = new JSONObject(resposta.toString());
                if (json.optBoolean("erro", false)) {
                    runOnUiThread(() -> {
                        endereco.setText("");
                        Toast.makeText(this, "CEP nao encontrado.", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                String logradouro = json.optString("logradouro", "");
                String bairro = json.optString("bairro", "");
                String localidade = json.optString("localidade", "");
                String uf = json.optString("uf", "");

                StringBuilder enderecoFormatado = new StringBuilder();
                if (!logradouro.isEmpty()) {
                    enderecoFormatado.append(logradouro);
                }
                if (!bairro.isEmpty()) {
                    if (enderecoFormatado.length() > 0) enderecoFormatado.append(" - ");
                    enderecoFormatado.append(bairro);
                }
                if (!localidade.isEmpty()) {
                    if (enderecoFormatado.length() > 0) enderecoFormatado.append(", ");
                    enderecoFormatado.append(localidade);
                }
                if (!uf.isEmpty()) {
                    if (enderecoFormatado.length() > 0) enderecoFormatado.append("/");
                    enderecoFormatado.append(uf);
                }

                runOnUiThread(() -> {
                    endereco.setText(enderecoFormatado.toString());
                    Toast.makeText(this, "Endereco preenchido automaticamente.", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Falha na consulta do CEP. Verifique sua conexao.", Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Verifica se a resposta que chegou é referente ao nosso pedido de câmera (código 100)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            // Verifica se o array de resultados não está vazio e se o usuário clicou em "Permitir"
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("CAMERA_DEBUG", "Usuário permitiu, abrindo câmera...");
                startCamera();
            } else {
                // Se o usuário negou, avisamos que ele não conseguirá tirar fotos.
                Toast.makeText(this, "A permissão é necessária para usar a câmera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        try {
            // Cria uma Intent (intenção) para capturar uma imagem.
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Inicia a atividade da câmera esperando um resultado (a foto).
            // O código REQUEST_IMAGE_CAPTURE (200) serve para identificarmos esta foto quando ela voltar.
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Log.e("CAMERA_DEBUG", "Erro ao abrir a câmera: " + e.getMessage());
            Toast.makeText(this, "Erro ao abrir a câmera no seu dispositivo.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 1. Verifica se o resultado que está voltando é o da nossa Câmera (código 200)
        // 2. Verifica se a foto foi tirada com sucesso (RESULT_OK)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Extrai a imagem compactada (thumbnail) que vem dentro da Intent 'data'
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Exibe a foto na tela para o usuário ver
            imageView.setImageBitmap(imageBitmap);
            // --- PREPARAÇÃO PARA O BANCO DE DADOS ---
            // Transforma o Bitmap em um Array de Bytes (byte[]), que é como o SQLite guarda imagens (BLOB)
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            // Se o objeto 'aluno' for nulo (novo cadastro), instanciamos ele aqui para guardar a foto
            if (aluno == null){
                aluno = new Aluno();
            }
            // Guarda os bytes da foto no objeto aluno (será usado no método salvar())
            aluno.setFotoBytes(byteArray);
        }
    }

    public void tirarFoto(View view) {
        // Verifica se a permissão de CAMERA já foi concedida anteriormente
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Se NÃO tem permissão, abre a janelinha do sistema pedindo a autorização.
            // O código CAMERA_PERMISSION_CODE (100) serve para identificarmos esta resposta depois.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            // Se JÁ tem permissão, chama o método para abrir a câmera de fato.
            startCamera();
        }
    }
}