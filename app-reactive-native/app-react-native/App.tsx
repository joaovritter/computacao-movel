import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  Button,
  ScrollView,
  StyleSheet,
  Alert
} from 'react-native';

import { Provider as PaperProvider, RadioButton, Checkbox } from 'react-native-paper';


interface Contato {
  id: number;
  nome: string;
  cpf: string;
  telefone: string;
  endereco: string;
  sexo?: 'M' | 'F';
  receberEmail?: boolean;
}

export default function App(): React.ReactElement {

  const [nome, setNome] = useState<string>('');
  const [cpf, setCpf] = useState<string>('');
  const [telefone, setTelefone] = useState<string>('');
  const [endereco, setEndereco] = useState<string>('');
  const [contatos, setContatos] = useState<Contato[]>([]);
  const [proximoId, setProximoId] = useState<number>(1);
  const [sexo, setSexo] = useState<'M' | 'F' | ''>('');
  const [aceitarTermos, setAceitarTermos] = useState<boolean>(false);
  const [receberEmail, setReceberEmail] = useState<boolean>(false);


  // 4. Função acionada pelo botão
  function adicionarContato(): void {
    if (nome.trim() === '') return;      // 4.1. Se vazio, não faz nada
    if (!aceitarTermos) {
      Alert.alert('Aviso', 'Você precisa aceitar os termos para continuar.');
      return;
    }
    const novo: Contato = {
      id: proximoId,
      nome,
      cpf,
      telefone,
      endereco,
      sexo: sexo || undefined,
      receberEmail
    };
    setContatos(contatos.concat(novo));
    setProximoId(proximoId + 1);
    setNome(''); setCpf(''); setTelefone(''); setEndereco('');
    setSexo(''); setAceitarTermos(false); setReceberEmail(false);
  }

  return (
    <PaperProvider>
      <View style={styles.container}>
        <Text style={styles.titulo}>Lista de Contatos</Text>

        <TextInput
          style={styles.input}
          placeholder="Digite um nome"
          value={nome}
          onChangeText={(texto) => setNome(texto)}
        />
        <TextInput
          style={styles.input}
          placeholder="Digite o CPF"
          value={cpf}
          onChangeText={(texto) => setCpf(texto)}
        />
        <TextInput
          style={styles.input}
          placeholder="Digite o Telefone"
          value={telefone}
          onChangeText={(texto) => setTelefone(texto)}
        />
        <TextInput
          style={styles.input}
          placeholder="Digite o Endereço"
          value={endereco}
          onChangeText={(texto) => setEndereco(texto)}
        />

        <Text style={{ marginTop: 8 }}>Sexo:</Text>
        <RadioButton.Group onValueChange={(value) => setSexo(value as 'M' | 'F')} value={sexo}>
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <RadioButton value="M" />
            <Text style={{ marginRight: 16 }}>Masculino</Text>
            <RadioButton value="F" />
            <Text>Feminino</Text>
          </View>
        </RadioButton.Group>

        <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 8 }}>
          <Checkbox status={aceitarTermos ? 'checked' : 'unchecked'} onPress={() => setAceitarTermos(!aceitarTermos)} />
          <Text>Aceitar os Termos (obrigatório)</Text>
        </View>

        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          <Checkbox status={receberEmail ? 'checked' : 'unchecked'} onPress={() => setReceberEmail(!receberEmail)} />
          <Text>Quero receber informações por e‑mail (opcional)</Text>
        </View>

        <Button title="Adicionar" onPress={adicionarContato} />

        <ScrollView style={styles.lista}>
          {contatos.map((contato) => (
            <View key={contato.id} style={{ marginBottom: 8 }}>
              <Text style={styles.item}>{contato.id} - {contato.nome}</Text>
              <Text>CPF: {contato.cpf} | Tel: {contato.telefone}</Text>
              <Text>Endereço: {contato.endereco}</Text>
              <Text>Sexo: {contato.sexo ?? '-'} | Receber e‑mail: {contato.receberEmail ? 'Sim' : 'Não'}</Text>
            </View>
          ))}
        </ScrollView>
      </View>
    </PaperProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,          // ocupa tela inteira
    padding: 20,      // espaçamento interno
    marginTop: 40     // afasta da status bar
  },
  titulo: {
    fontSize: 24,     // tamanho do título
    marginBottom: 10  // espaço abaixo
  },
  input: {
    borderWidth: 1,   // borda fina
    borderColor: '#ccc',
    padding: 10,
    marginBottom: 10
  },
  lista: {
    marginTop: 10     // espaço antes da lista
  },
  item: {
    fontSize: 18,     // tamanho do texto dos itens
    marginVertical: 5 // espaço entre itens
  }
});