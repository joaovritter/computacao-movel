package com.example.primeiroprojeto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AlunoDAO {
    private Conexao conexao;
    private SQLiteDatabase banco;

    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^\\(?([1-9]{2})\\)?\\s?9[0-9]{4}-?[0-9]{4}$");


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

    public boolean cpfExistente(String cpf) {
        // Consulta no banco de dados para verificar se o CPF já existe
        Cursor cursor = banco.query("aluno", new String[]{"id"}, "cpf = ?", new String[]{cpf}, null, null, null);
        boolean cpfExiste = cursor.getCount() > 0;
        cursor.close();
        return cpfExiste;
    }

    public boolean validaCpf(String CPF) {
        // Exibe a entrada recebida (usado para depuração)
        System.out.println("String de entrada do método: " + CPF);

        // Remove espaços e caracteres não numéricos (caso tenha sido digitado com . ou -)
        CPF = CPF.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem exatamente 11 dígitos
        if (CPF.length() != 11) {
            return false;
        }

        // Verifica se o CPF não é uma sequência repetida (como 00000000000, 11111111111, etc.)
        /*
        \\d	Representa qualquer número de 0 a 9
        (\\d)	Captura o primeiro dígito do CPF
        \\1	Repete o mesmo dígito capturado antes
        {10}	Exige que o mesmo dígito apareça mais 10 vezes, totalizando 11 dígitos iguais */
        if (CPF.matches("(\\d)\\1{10}")) {
            return false;
        }

        char dig10, dig11; // Variáveis para armazenar os dígitos verificadores
        int soma, num, peso, resto;

        try {
            // Cálculo do Primeiro Dígito Verificador (D1)
            soma = 0;
            peso = 10; // O primeiro peso começa em 10 e vai diminuindo até 2

            for (int i = 0; i < 9; i++) {
                num = CPF.charAt(i) - '0'; // Converte o caractere numérico para inteiro
                soma += (num * peso); // Multiplica pelo peso correspondente e soma
                peso--; // Diminui o peso
            }

            resto = soma % 11;
            dig10 = (resto < 2) ? '0' : (char) ((11 - resto) + '0'); // Se resto < 2, D1 = 0, senão D1 = 11 - resto

            // Cálculo do Segundo Dígito Verificador (D2)
            soma = 0;
            peso = 11; // Agora os pesos começam em 11 e vão até 2

            for (int i = 0; i < 10; i++) { // Inclui o primeiro dígito verificador já calculado
                num = CPF.charAt(i) - '0'; // Converte o caractere numérico para inteiro
                soma += (num * peso); // Multiplica pelo peso correspondente e soma
                peso--; // Diminui o peso
            }

            resto = soma % 11;
            dig11 = (resto < 2) ? '0' : (char) ((11 - resto) + '0'); // Se resto < 2, D2 = 0, senão D2 = 11 - resto

            // Comparação dos Dígitos Verificadores
            return (dig10 == (char) (CPF.charAt(9))) && (dig11 == (char) (CPF.charAt(10))); // Verifica se os dígitos calculados são iguais aos do CPF informado
            //se for verdadeiro retorna true

        } catch (Exception e) { // Captura qualquer erro inesperado
            return false;
        }
    }

    public boolean validaTelefone(String telefone) {
        if (telefone == null) return false;

        // Remove caracteres não numéricos, caso tenha espaços, parênteses ou traços
        telefone = telefone.replaceAll("[^0-9]", "");

        // O número deve ter 11 dígitos (2 do DDD + 9 do telefone)
        return telefone.length() == 11 && TELEFONE_PATTERN.matcher(telefone).matches();  //matches verifica se tem o padrão esperado
        //Retorna false se houver algo errado
        /*Padroes aceitos:
        (XX) 9XXXX-XXXX
        XX 9XXXXXXXX
        11987654321 */
    }
}
