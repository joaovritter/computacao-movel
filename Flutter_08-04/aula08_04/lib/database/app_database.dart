import 'package:drift/drift.dart';
import 'package:drift/native.dart';
import 'package:path_provider/path_provider.dart';
import 'package:path/path.dart' as p;
import 'dart:io';

//drift é um ORM para Flutter que facilita o trabalho com bancos de dados SQLite
part 'app_database.g.dart';

//Tabela clientes
class Clientes extends Table {
  IntColumn get id => integer().autoIncrement()(); //pk
  TextColumn get nome => text()();
  TextColumn get cpf => text()();
  TextColumn get telefone => text()();
}

//Classe de acesso ao banco de dados
@DriftDatabase(tables: [Clientes])
class AppDatabase extends _$AppDatabase {
  //construtor
  AppDatabase() : super(_abrirConexao());

  //Definir a versão do banco de dados
  @override
  int get schemaVersion => 1;

  //Future - Significa que a operação é assíncrona (vai acontecer no futuro).
  //inserir
  Future<int> inserirCliente(ClientesCompanion cliente) {
    return into(clientes).insert(cliente);
  }

  //listar todos
  Future<List<Cliente>> listarClientes() {
    return select(clientes).get();
  }

  //atualizar
  Future<bool> atualizarCliente(Cliente cliente) {
    return update(clientes).replace(cliente);
  }

  //excluir
  Future<int> excluirCliente(int id) {
    return (delete(clientes)..where((c) => c.id.equals(id))).go();
  }
}

// Função para abrir a conexão com o banco de dados usando LazyDatabase
//lazy é um padrão de design que adia a criação de um objeto até que ele seja realmente necessário,
//economizando recursos e melhorando o desempenho do aplicativo.
LazyDatabase _abrirConexao() {
  return LazyDatabase(() async {
    final dir = await getApplicationDocumentsDirectory(); // pasta local do app
    final path = p.join(dir.path, 'clientes.db'); // nome do arquivo do banco
    return NativeDatabase(File(path)); // retorna o banco SQLite nativo
  });
}
