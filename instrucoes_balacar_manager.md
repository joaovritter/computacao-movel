# BalaCar Manager — Guia Completo de Desenvolvimento

> Versão ajustada para usar **classes, tipos, interfaces, funções, variáveis e arquivos do projeto em português**.  
> Bibliotecas externas continuam com seus nomes originais, como `firebase`, `React Navigation`, `Expo Notifications` e `Google Sign-In`.

---

## 1. Visão geral do projeto

O **BalaCar Manager** é um aplicativo mobile desenvolvido em **React Native com Expo e TypeScript** para gerenciamento de manutenções automotivas.

O objetivo do sistema é permitir que o usuário faça login com sua conta Google, cadastre veículos, registre manutenções e acompanhe se cada manutenção está **em dia**, **próxima do vencimento** ou **atrasada**.

O sistema também deve permitir o envio de notificações locais para lembrar o usuário sobre manutenções futuras.

---

## 2. Requisitos obrigatórios atendidos

| Requisito | Como será atendido |
|---|---|
| Interface gráfica funcional | Telas em React Native com formulários, cards e botões |
| Navegação entre telas | React Navigation |
| Integração com duas APIs externas | Google Sign-In e Expo Notifications |
| Persistência de dados | Firebase Cloud Firestore |
| Tratamento básico de erros | Validação de campos e mensagens amigáveis |
| Código organizado e comentado | Separação por componentes, telas, serviços, utilitários e tipos |
| Aplicação funcionando na apresentação | Execução com `npm install` e `npx expo start` |

---

## 3. Tecnologias utilizadas

- React Native
- Expo
- TypeScript
- Firebase
- Firebase Authentication
- Firebase Cloud Firestore
- Google Sign-In
- Expo Notifications
- React Navigation
- AsyncStorage, se necessário

---

## 4. APIs externas utilizadas

### 4.1 Google Sign-In

Responsável pela autenticação do usuário com conta Google.

Funções principais:

- Login com Google;
- Obtenção de nome, e-mail e foto do usuário;
- Autenticação no Firebase;
- Identificação do usuário logado;
- Logout.

### 4.2 Expo Notifications

Responsável pelo envio e agendamento de notificações locais.

Funções principais:

- Solicitar permissão de notificação;
- Enviar notificação de teste;
- Agendar lembrete para manutenção futura;
- Alertar o usuário sobre manutenções próximas.

---

## 5. Padrão de nomes em português

Neste projeto, os nomes criados por nós devem ficar em português.

Exemplos:

| Inglês | Português |
|---|---|
| `Vehicle` | `Veiculo` |
| `Maintenance` | `Manutencao` |
| `User` | `Usuario` |
| `currentKm` | `quilometragemAtual` |
| `nextDate` | `proximaData` |
| `nextKm` | `proximaQuilometragem` |
| `status` | `situacao` |
| `createdAt` | `criadoEm` |
| `updatedAt` | `atualizadoEm` |
| `DashboardScreen` | `TelaInicial` |
| `VehicleFormScreen` | `TelaFormularioVeiculo` |
| `MaintenanceFormScreen` | `TelaFormularioManutencao` |
| `authService` | `servicoAutenticacao` |
| `vehicleService` | `servicoVeiculo` |
| `maintenanceService` | `servicoManutencao` |
| `notificationService` | `servicoNotificacao` |

Observação: nomes de bibliotecas e imports externos não devem ser traduzidos. Por exemplo:

```ts
import { initializeApp } from "firebase/app";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import * as Notifications from "expo-notifications";
```

---

## 6. Persistência de dados

A persistência será feita em nuvem usando **Firebase Cloud Firestore**.

Estrutura sugerida em português:

```txt
usuarios/{idUsuario}
  nome
  email
  urlFoto
  criadoEm

usuarios/{idUsuario}/veiculos/{idVeiculo}
  marca
  modelo
  ano
  placa
  quilometragemAtual
  criadoEm
  atualizadoEm

usuarios/{idUsuario}/veiculos/{idVeiculo}/manutencoes/{idManutencao}
  tipo
  descricao
  dataRealizada
  quilometragemRealizada
  proximaData
  proximaQuilometragem
  custo
  observacoes
  situacao
  idNotificacao
  criadoEm
  atualizadoEm
```

Cada usuário deve acessar apenas seus próprios dados.

---

## 7. Funcionalidades do sistema

### 7.1 Autenticação

- Login com conta Google;
- Logout;
- Salvamento dos dados básicos do usuário no Firestore;
- Proteção das telas internas para usuários autenticados.

### 7.2 Veículos

O usuário poderá:

- Cadastrar veículos;
- Listar veículos cadastrados;
- Visualizar detalhes de um veículo;
- Atualizar quilometragem atual;
- Excluir veículo, se desejado.

Campos do veículo:

```txt
marca
modelo
ano
placa
quilometragemAtual
criadoEm
atualizadoEm
```

### 7.3 Manutenções

O usuário poderá:

- Cadastrar manutenções;
- Listar manutenções de um veículo;
- Visualizar situação da manutenção;
- Agendar lembrete;
- Excluir manutenção, se desejado.

Campos da manutenção:

```txt
tipo
descricao
dataRealizada
quilometragemRealizada
proximaData
proximaQuilometragem
custo
observacoes
situacao
idNotificacao
criadoEm
atualizadoEm
```

---

## 8. Regras de situação da manutenção

A situação da manutenção será calculada com base na data prevista e na quilometragem prevista.

Tipos possíveis:

```ts
export type SituacaoManutencao = "em_dia" | "proxima" | "atrasada";
```

Regras:

```txt
Se proximaData já passou ou quilometragemAtual >= proximaQuilometragem:
  situacao = "atrasada"

Se faltam 15 dias ou menos para proximaData:
  situacao = "proxima"

Se faltam 500 km ou menos para proximaQuilometragem:
  situacao = "proxima"

Caso contrário:
  situacao = "em_dia"
```

Observações:

- Se `proximaData` estiver vazia, considerar apenas `proximaQuilometragem`;
- Se `proximaQuilometragem` estiver vazia, considerar apenas `proximaData`;
- Se ambas estiverem vazias, retornar `em_dia`.

---

## 9. Estrutura de pastas

```txt
balacar-manager/
  App.tsx
  app.json
  package.json
  tsconfig.json
  .env
  .env.example
  README.md

  src/
    componentes/
      Botao.tsx
      CampoTexto.tsx
      EtiquetaSituacao.tsx
      CardVeiculo.tsx
      CardManutencao.tsx

    telas/
      TelaLogin.tsx
      TelaInicial.tsx
      TelaFormularioVeiculo.tsx
      TelaDetalhesVeiculo.tsx
      TelaFormularioManutencao.tsx
      TelaPerfil.tsx

    servicos/
      firebase.ts
      servicoAutenticacao.ts
      servicoVeiculo.ts
      servicoManutencao.ts
      servicoNotificacao.ts

    navegacao/
      NavegadorRaiz.tsx
      NavegadorAutenticacao.tsx
      NavegadorApp.tsx

    utilitarios/
      calcularSituacaoManutencao.ts
      formatarData.ts
      formatarMoeda.ts

    tipos/
      Usuario.ts
      Veiculo.ts
      Manutencao.ts
```

---

## 10. Criação do projeto

Criar o projeto com Expo:

```bash
npx create-expo-app balacar-manager --template
```

Escolha:

```txt
Blank (TypeScript)
```

Depois, se aparecer a escolha de SDK:

```txt
For planning with Expo Go (SDK 54)
```

Entrar na pasta:

```bash
cd balacar-manager
```

Instalar dependências principais:

```bash
npm install firebase
npm install @react-navigation/native
npx expo install react-native-screens react-native-safe-area-context
npm install @react-navigation/native-stack --legacy-peer-deps
npx expo install expo-notifications
npx expo install expo-auth-session expo-web-browser
npx expo install expo-constants
npx expo install @react-native-async-storage/async-storage
```

Rodar o app:

```bash
npx expo start
```

Caso tenha problema de cache:

```bash
npx expo start --clear
```

---

## 11. Variáveis de ambiente

Criar um arquivo `.env.example`:

```env
EXPO_PUBLIC_FIREBASE_API_KEY=
EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN=
EXPO_PUBLIC_FIREBASE_PROJECT_ID=
EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET=
EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=
EXPO_PUBLIC_FIREBASE_APP_ID=

EXPO_PUBLIC_GOOGLE_WEB_CLIENT_ID=
EXPO_PUBLIC_GOOGLE_ANDROID_CLIENT_ID=
EXPO_PUBLIC_GOOGLE_IOS_CLIENT_ID=
```

Criar também um arquivo `.env` com os valores reais:

```env
EXPO_PUBLIC_FIREBASE_API_KEY=SUA_API_KEY
EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN=SEU_AUTH_DOMAIN
EXPO_PUBLIC_FIREBASE_PROJECT_ID=SEU_PROJECT_ID
EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET=SEU_STORAGE_BUCKET
EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=SEU_MESSAGING_SENDER_ID
EXPO_PUBLIC_FIREBASE_APP_ID=SEU_APP_ID

EXPO_PUBLIC_GOOGLE_WEB_CLIENT_ID=SEU_WEB_CLIENT_ID
EXPO_PUBLIC_GOOGLE_ANDROID_CLIENT_ID=SEU_ANDROID_CLIENT_ID
EXPO_PUBLIC_GOOGLE_IOS_CLIENT_ID=SEU_IOS_CLIENT_ID
```

Nunca deixar as chaves diretamente no código.

---

## 12. Configuração do Firebase

Arquivo: `src/servicos/firebase.ts`

```ts
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

const configuracaoFirebase = {
  apiKey: process.env.EXPO_PUBLIC_FIREBASE_API_KEY,
  authDomain: process.env.EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN,
  projectId: process.env.EXPO_PUBLIC_FIREBASE_PROJECT_ID,
  storageBucket: process.env.EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.EXPO_PUBLIC_FIREBASE_APP_ID,
};

// Inicializa o Firebase no app.
const app = initializeApp(configuracaoFirebase);

// Exporta autenticação e banco de dados para uso nos serviços.
export const autenticacao = getAuth(app);
export const bancoDados = getFirestore(app);
```

---

## 13. Tipos do sistema

### `src/tipos/Usuario.ts`

```ts
export interface Usuario {
  id: string;
  nome: string;
  email: string;
  urlFoto?: string;
  criadoEm?: Date;
}
```

### `src/tipos/Veiculo.ts`

```ts
export interface Veiculo {
  id?: string;
  marca: string;
  modelo: string;
  ano: string;
  placa: string;
  quilometragemAtual: number;
  criadoEm?: Date;
  atualizadoEm?: Date;
}
```

### `src/tipos/Manutencao.ts`

```ts
export type SituacaoManutencao = "em_dia" | "proxima" | "atrasada";

export interface Manutencao {
  id?: string;
  tipo: string;
  descricao?: string;
  dataRealizada: string;
  quilometragemRealizada: number;
  proximaData?: string;
  proximaQuilometragem?: number;
  custo?: number;
  observacoes?: string;
  situacao: SituacaoManutencao;
  idNotificacao?: string | null;
  criadoEm?: Date;
  atualizadoEm?: Date;
}
```

---

## 14. Utilitário de cálculo de situação

Arquivo: `src/utilitarios/calcularSituacaoManutencao.ts`

```ts
import { SituacaoManutencao } from "../tipos/Manutencao";

interface Parametros {
  quilometragemAtual: number;
  proximaData?: string;
  proximaQuilometragem?: number;
}

export function calcularSituacaoManutencao({
  quilometragemAtual,
  proximaData,
  proximaQuilometragem,
}: Parametros): SituacaoManutencao {
  const hoje = new Date();
  hoje.setHours(0, 0, 0, 0);

  let estaAtrasadaPorData = false;
  let estaProximaPorData = false;

  if (proximaData) {
    const dataAlvo = new Date(proximaData);
    dataAlvo.setHours(0, 0, 0, 0);

    const diferencaTempo = dataAlvo.getTime() - hoje.getTime();
    const diferencaDias = Math.ceil(diferencaTempo / (1000 * 60 * 60 * 24));

    estaAtrasadaPorData = diferencaDias < 0;
    estaProximaPorData = diferencaDias >= 0 && diferencaDias <= 15;
  }

  const estaAtrasadaPorKm =
    typeof proximaQuilometragem === "number" &&
    quilometragemAtual >= proximaQuilometragem;

  const estaProximaPorKm =
    typeof proximaQuilometragem === "number" &&
    proximaQuilometragem - quilometragemAtual > 0 &&
    proximaQuilometragem - quilometragemAtual <= 500;

  if (estaAtrasadaPorData || estaAtrasadaPorKm) {
    return "atrasada";
  }

  if (estaProximaPorData || estaProximaPorKm) {
    return "proxima";
  }

  return "em_dia";
}
```

---

## 15. Utilitários de formatação

### `src/utilitarios/formatarData.ts`

```ts
export function formatarData(data?: string): string {
  if (!data) return "Não informado";

  const dataConvertida = new Date(data);

  if (Number.isNaN(dataConvertida.getTime())) {
    return "Data inválida";
  }

  return dataConvertida.toLocaleDateString("pt-BR");
}
```

### `src/utilitarios/formatarMoeda.ts`

```ts
export function formatarMoeda(valor?: number): string {
  if (typeof valor !== "number") return "R$ 0,00";

  return valor.toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}
```

---

## 16. Serviço de autenticação

Arquivo: `src/servicos/servicoAutenticacao.ts`

```ts
import * as WebBrowser from "expo-web-browser";
import * as Google from "expo-auth-session/providers/google";
import { GoogleAuthProvider, signInWithCredential, signOut } from "firebase/auth";
import { doc, setDoc, serverTimestamp } from "firebase/firestore";
import { autenticacao, bancoDados } from "./firebase";

WebBrowser.maybeCompleteAuthSession();

export function usarRequisicaoAutenticacaoGoogle() {
  return Google.useAuthRequest({
    webClientId: process.env.EXPO_PUBLIC_GOOGLE_WEB_CLIENT_ID,
    androidClientId: process.env.EXPO_PUBLIC_GOOGLE_ANDROID_CLIENT_ID,
    iosClientId: process.env.EXPO_PUBLIC_GOOGLE_IOS_CLIENT_ID,
  });
}

export async function entrarComTokenGoogle(idToken: string) {
  try {
    const credencial = GoogleAuthProvider.credential(idToken);
    const resultado = await signInWithCredential(autenticacao, credencial);

    const usuario = resultado.user;

    await setDoc(
      doc(bancoDados, "usuarios", usuario.uid),
      {
        nome: usuario.displayName,
        email: usuario.email,
        urlFoto: usuario.photoURL,
        criadoEm: serverTimestamp(),
      },
      { merge: true }
    );

    return usuario;
  } catch (erro) {
    console.error("Erro ao fazer login com Google:", erro);
    throw new Error("Não foi possível fazer login com Google.");
  }
}

export async function sairDaConta() {
  try {
    await signOut(autenticacao);
  } catch (erro) {
    console.error("Erro ao sair:", erro);
    throw new Error("Não foi possível sair da conta.");
  }
}
```

Observação: dependendo da versão do Expo e do Firebase, pode ser necessário ajustar o fluxo de `idToken` ou usar `response.authentication?.idToken` na tela de login.

---

## 17. Serviço de veículos

Arquivo: `src/servicos/servicoVeiculo.ts`

```ts
import {
  addDoc,
  collection,
  deleteDoc,
  doc,
  getDocs,
  orderBy,
  query,
  serverTimestamp,
  updateDoc,
} from "firebase/firestore";
import { bancoDados } from "./firebase";
import { Veiculo } from "../tipos/Veiculo";

function colecaoVeiculos(idUsuario: string) {
  return collection(bancoDados, "usuarios", idUsuario, "veiculos");
}

export async function criarVeiculo(idUsuario: string, veiculo: Veiculo) {
  try {
    await addDoc(colecaoVeiculos(idUsuario), {
      ...veiculo,
      criadoEm: serverTimestamp(),
      atualizadoEm: serverTimestamp(),
    });
  } catch (erro) {
    console.error("Erro ao salvar veículo:", erro);
    throw new Error("Não foi possível salvar o veículo.");
  }
}

export async function buscarVeiculos(idUsuario: string): Promise<Veiculo[]> {
  try {
    const consulta = query(colecaoVeiculos(idUsuario), orderBy("criadoEm", "desc"));
    const resultado = await getDocs(consulta);

    return resultado.docs.map((documento) => ({
      id: documento.id,
      ...documento.data(),
    })) as Veiculo[];
  } catch (erro) {
    console.error("Erro ao buscar veículos:", erro);
    throw new Error("Não foi possível buscar os veículos.");
  }
}

export async function atualizarVeiculo(
  idUsuario: string,
  idVeiculo: string,
  dados: Partial<Veiculo>
) {
  try {
    await updateDoc(doc(bancoDados, "usuarios", idUsuario, "veiculos", idVeiculo), {
      ...dados,
      atualizadoEm: serverTimestamp(),
    });
  } catch (erro) {
    console.error("Erro ao atualizar veículo:", erro);
    throw new Error("Não foi possível atualizar o veículo.");
  }
}

export async function atualizarQuilometragemVeiculo(
  idUsuario: string,
  idVeiculo: string,
  quilometragemAtual: number
) {
  return atualizarVeiculo(idUsuario, idVeiculo, { quilometragemAtual });
}

export async function excluirVeiculo(idUsuario: string, idVeiculo: string) {
  try {
    await deleteDoc(doc(bancoDados, "usuarios", idUsuario, "veiculos", idVeiculo));
  } catch (erro) {
    console.error("Erro ao excluir veículo:", erro);
    throw new Error("Não foi possível excluir o veículo.");
  }
}
```

---

## 18. Serviço de manutenções

Arquivo: `src/servicos/servicoManutencao.ts`

```ts
import {
  addDoc,
  collection,
  deleteDoc,
  doc,
  getDocs,
  orderBy,
  query,
  serverTimestamp,
  updateDoc,
} from "firebase/firestore";
import { bancoDados } from "./firebase";
import { Manutencao } from "../tipos/Manutencao";
import { calcularSituacaoManutencao } from "../utilitarios/calcularSituacaoManutencao";

function colecaoManutencoes(idUsuario: string, idVeiculo: string) {
  return collection(
    bancoDados,
    "usuarios",
    idUsuario,
    "veiculos",
    idVeiculo,
    "manutencoes"
  );
}

export async function criarManutencao(
  idUsuario: string,
  idVeiculo: string,
  quilometragemAtual: number,
  manutencao: Omit<Manutencao, "situacao">
) {
  try {
    const situacao = calcularSituacaoManutencao({
      quilometragemAtual,
      proximaData: manutencao.proximaData,
      proximaQuilometragem: manutencao.proximaQuilometragem,
    });

    await addDoc(colecaoManutencoes(idUsuario, idVeiculo), {
      ...manutencao,
      situacao,
      criadoEm: serverTimestamp(),
      atualizadoEm: serverTimestamp(),
    });
  } catch (erro) {
    console.error("Erro ao salvar manutenção:", erro);
    throw new Error("Não foi possível salvar a manutenção.");
  }
}

export async function buscarManutencoes(
  idUsuario: string,
  idVeiculo: string
): Promise<Manutencao[]> {
  try {
    const consulta = query(
      colecaoManutencoes(idUsuario, idVeiculo),
      orderBy("criadoEm", "desc")
    );

    const resultado = await getDocs(consulta);

    return resultado.docs.map((documento) => ({
      id: documento.id,
      ...documento.data(),
    })) as Manutencao[];
  } catch (erro) {
    console.error("Erro ao buscar manutenções:", erro);
    throw new Error("Não foi possível buscar as manutenções.");
  }
}

export async function atualizarManutencao(
  idUsuario: string,
  idVeiculo: string,
  idManutencao: string,
  dados: Partial<Manutencao>
) {
  try {
    await updateDoc(
      doc(
        bancoDados,
        "usuarios",
        idUsuario,
        "veiculos",
        idVeiculo,
        "manutencoes",
        idManutencao
      ),
      {
        ...dados,
        atualizadoEm: serverTimestamp(),
      }
    );
  } catch (erro) {
    console.error("Erro ao atualizar manutenção:", erro);
    throw new Error("Não foi possível atualizar a manutenção.");
  }
}

export async function excluirManutencao(
  idUsuario: string,
  idVeiculo: string,
  idManutencao: string
) {
  try {
    await deleteDoc(
      doc(
        bancoDados,
        "usuarios",
        idUsuario,
        "veiculos",
        idVeiculo,
        "manutencoes",
        idManutencao
      )
    );
  } catch (erro) {
    console.error("Erro ao excluir manutenção:", erro);
    throw new Error("Não foi possível excluir a manutenção.");
  }
}

export function obterResumoManutencoes(manutencoes: Manutencao[]) {
  return manutencoes.reduce(
    (resumo, manutencao) => {
      resumo[manutencao.situacao] += 1;
      return resumo;
    },
    {
      em_dia: 0,
      proxima: 0,
      atrasada: 0,
    }
  );
}
```

---

## 19. Serviço de notificações

Arquivo: `src/servicos/servicoNotificacao.ts`

```ts
import * as Notifications from "expo-notifications";

Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: true,
    shouldSetBadge: false,
  }),
});

export async function solicitarPermissaoNotificacao(): Promise<boolean> {
  const { status } = await Notifications.getPermissionsAsync();

  if (status === "granted") {
    return true;
  }

  const solicitacao = await Notifications.requestPermissionsAsync();
  return solicitacao.status === "granted";
}

export async function enviarNotificacaoTeste() {
  const possuiPermissao = await solicitarPermissaoNotificacao();

  if (!possuiPermissao) {
    throw new Error("Permissão de notificação negada.");
  }

  await Notifications.scheduleNotificationAsync({
    content: {
      title: "BalaCar Manager",
      body: "Notificação de teste funcionando.",
    },
    trigger: null,
  });
}

export async function agendarNotificacaoManutencao({
  titulo,
  mensagem,
  data,
}: {
  titulo: string;
  mensagem: string;
  data: Date;
}) {
  const possuiPermissao = await solicitarPermissaoNotificacao();

  if (!possuiPermissao) {
    throw new Error("Permissão de notificação negada.");
  }

  const idNotificacao = await Notifications.scheduleNotificationAsync({
    content: {
      title: titulo,
      body: mensagem,
    },
    trigger: data,
  });

  return idNotificacao;
}

export async function cancelarNotificacao(idNotificacao: string) {
  await Notifications.cancelScheduledNotificationAsync(idNotificacao);
}
```

---

## 20. Componentes reutilizáveis

### `src/componentes/Botao.tsx`

```tsx
import React from "react";
import { TouchableOpacity, Text, StyleSheet, ActivityIndicator } from "react-native";

interface Propriedades {
  titulo: string;
  aoPressionar: () => void;
  carregando?: boolean;
}

export function Botao({ titulo, aoPressionar, carregando = false }: Propriedades) {
  return (
    <TouchableOpacity style={estilos.botao} onPress={aoPressionar} disabled={carregando}>
      {carregando ? (
        <ActivityIndicator color="#fff" />
      ) : (
        <Text style={estilos.texto}>{titulo}</Text>
      )}
    </TouchableOpacity>
  );
}

const estilos = StyleSheet.create({
  botao: {
    backgroundColor: "#0F172A",
    padding: 14,
    borderRadius: 10,
    alignItems: "center",
    marginVertical: 8,
  },
  texto: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 16,
  },
});
```

### `src/componentes/CampoTexto.tsx`

```tsx
import React from "react";
import { TextInput, StyleSheet, TextInputProps } from "react-native";

export function CampoTexto(propriedades: TextInputProps) {
  return (
    <TextInput
      style={estilos.campo}
      placeholderTextColor="#777"
      {...propriedades}
    />
  );
}

const estilos = StyleSheet.create({
  campo: {
    backgroundColor: "#fff",
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 10,
    padding: 12,
    marginVertical: 6,
  },
});
```

### `src/componentes/EtiquetaSituacao.tsx`

```tsx
import React from "react";
import { Text, StyleSheet } from "react-native";
import { SituacaoManutencao } from "../tipos/Manutencao";

interface Propriedades {
  situacao: SituacaoManutencao;
}

const mapaSituacao = {
  em_dia: { rotulo: "Em dia", cor: "#16A34A" },
  proxima: { rotulo: "Próxima", cor: "#F97316" },
  atrasada: { rotulo: "Atrasada", cor: "#DC2626" },
};

export function EtiquetaSituacao({ situacao }: Propriedades) {
  const situacaoAtual = mapaSituacao[situacao];

  return (
    <Text style={[estilos.etiqueta, { backgroundColor: situacaoAtual.cor }]}>
      {situacaoAtual.rotulo}
    </Text>
  );
}

const estilos = StyleSheet.create({
  etiqueta: {
    color: "#fff",
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 20,
    alignSelf: "flex-start",
    fontWeight: "bold",
  },
});
```

### `src/componentes/CardVeiculo.tsx`

```tsx
import React from "react";
import { TouchableOpacity, Text, StyleSheet } from "react-native";
import { Veiculo } from "../tipos/Veiculo";

interface Propriedades {
  veiculo: Veiculo;
  aoPressionar: () => void;
}

export function CardVeiculo({ veiculo, aoPressionar }: Propriedades) {
  return (
    <TouchableOpacity style={estilos.card} onPress={aoPressionar}>
      <Text style={estilos.titulo}>
        {veiculo.marca} {veiculo.modelo}
      </Text>
      <Text>Ano: {veiculo.ano}</Text>
      <Text>Placa: {veiculo.placa}</Text>
      <Text>Km atual: {veiculo.quilometragemAtual}</Text>
    </TouchableOpacity>
  );
}

const estilos = StyleSheet.create({
  card: {
    backgroundColor: "#fff",
    padding: 14,
    borderRadius: 12,
    marginVertical: 8,
  },
  titulo: {
    fontSize: 18,
    fontWeight: "bold",
    marginBottom: 4,
  },
});
```

### `src/componentes/CardManutencao.tsx`

```tsx
import React from "react";
import { View, Text, StyleSheet } from "react-native";
import { Manutencao } from "../tipos/Manutencao";
import { EtiquetaSituacao } from "./EtiquetaSituacao";
import { formatarData } from "../utilitarios/formatarData";
import { formatarMoeda } from "../utilitarios/formatarMoeda";

interface Propriedades {
  manutencao: Manutencao;
}

export function CardManutencao({ manutencao }: Propriedades) {
  return (
    <View style={estilos.card}>
      <Text style={estilos.titulo}>{manutencao.tipo}</Text>
      <Text>{manutencao.descricao}</Text>
      <Text>Data realizada: {formatarData(manutencao.dataRealizada)}</Text>
      <Text>Próxima data: {formatarData(manutencao.proximaData)}</Text>
      <Text>
        Próxima quilometragem:{" "}
        {manutencao.proximaQuilometragem ?? "Não informado"}
      </Text>
      <Text>Custo: {formatarMoeda(manutencao.custo)}</Text>
      <EtiquetaSituacao situacao={manutencao.situacao} />
    </View>
  );
}

const estilos = StyleSheet.create({
  card: {
    backgroundColor: "#fff",
    padding: 14,
    borderRadius: 12,
    marginVertical: 8,
  },
  titulo: {
    fontSize: 16,
    fontWeight: "bold",
    marginBottom: 4,
  },
});
```

---

## 21. Navegação

### `src/navegacao/NavegadorAutenticacao.tsx`

```tsx
import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { TelaLogin } from "../telas/TelaLogin";

const Pilha = createNativeStackNavigator();

export function NavegadorAutenticacao() {
  return (
    <Pilha.Navigator>
      <Pilha.Screen
        name="Login"
        component={TelaLogin}
        options={{ headerShown: false }}
      />
    </Pilha.Navigator>
  );
}
```

### `src/navegacao/NavegadorApp.tsx`

```tsx
import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { TelaInicial } from "../telas/TelaInicial";
import { TelaFormularioVeiculo } from "../telas/TelaFormularioVeiculo";
import { TelaDetalhesVeiculo } from "../telas/TelaDetalhesVeiculo";
import { TelaFormularioManutencao } from "../telas/TelaFormularioManutencao";
import { TelaPerfil } from "../telas/TelaPerfil";

const Pilha = createNativeStackNavigator();

export function NavegadorApp() {
  return (
    <Pilha.Navigator>
      <Pilha.Screen
        name="TelaInicial"
        component={TelaInicial}
        options={{ title: "BalaCar Manager" }}
      />
      <Pilha.Screen
        name="TelaFormularioVeiculo"
        component={TelaFormularioVeiculo}
        options={{ title: "Cadastrar veículo" }}
      />
      <Pilha.Screen
        name="TelaDetalhesVeiculo"
        component={TelaDetalhesVeiculo}
        options={{ title: "Detalhes do veículo" }}
      />
      <Pilha.Screen
        name="TelaFormularioManutencao"
        component={TelaFormularioManutencao}
        options={{ title: "Cadastrar manutenção" }}
      />
      <Pilha.Screen
        name="TelaPerfil"
        component={TelaPerfil}
        options={{ title: "Perfil" }}
      />
    </Pilha.Navigator>
  );
}
```

### `src/navegacao/NavegadorRaiz.tsx`

```tsx
import React, { useEffect, useState } from "react";
import { NavigationContainer } from "@react-navigation/native";
import { onAuthStateChanged, User } from "firebase/auth";
import { autenticacao } from "../servicos/firebase";
import { NavegadorAutenticacao } from "./NavegadorAutenticacao";
import { NavegadorApp } from "./NavegadorApp";

export function NavegadorRaiz() {
  const [usuario, definirUsuario] = useState<User | null>(null);
  const [carregando, definirCarregando] = useState(true);

  useEffect(() => {
    const cancelarInscricao = onAuthStateChanged(autenticacao, (usuarioAtual) => {
      definirUsuario(usuarioAtual);
      definirCarregando(false);
    });

    return cancelarInscricao;
  }, []);

  if (carregando) {
    return null;
  }

  return (
    <NavigationContainer>
      {usuario ? <NavegadorApp /> : <NavegadorAutenticacao />}
    </NavigationContainer>
  );
}
```

### `App.tsx`

```tsx
import React from "react";
import { NavegadorRaiz } from "./src/navegacao/NavegadorRaiz";

export default function App() {
  return <NavegadorRaiz />;
}
```

---

## 22. Telas do sistema

As telas podem ser implementadas inicialmente de forma simples, priorizando funcionamento.

### `TelaLogin.tsx`

Responsabilidades:

- Exibir nome do app;
- Exibir botão de login com Google;
- Chamar fluxo de autenticação;
- Exibir erro se falhar.

Fluxo esperado:

```tsx
// 1. Criar requisição com usarRequisicaoAutenticacaoGoogle
// 2. Ao clicar no botão, chamar promptAsync()
// 3. Se resposta for success, capturar idToken
// 4. Chamar entrarComTokenGoogle(idToken)
```

### `TelaInicial.tsx`

Responsabilidades:

- Buscar veículos do usuário;
- Exibir resumo;
- Listar veículos;
- Navegar para cadastro de veículo;
- Navegar para detalhes do veículo;
- Navegar para perfil.

### `TelaFormularioVeiculo.tsx`

Responsabilidades:

- Formulário de cadastro de veículo;
- Validação dos campos;
- Salvar no Firestore usando `criarVeiculo`.

### `TelaDetalhesVeiculo.tsx`

Responsabilidades:

- Exibir dados do veículo;
- Buscar manutenções;
- Listar manutenções;
- Navegar para cadastro de manutenção;
- Atualizar quilometragem.

### `TelaFormularioManutencao.tsx`

Responsabilidades:

- Formulário de manutenção;
- Validação de campos;
- Calcular situação;
- Agendar notificação, se ativada;
- Salvar manutenção no Firestore.

### `TelaPerfil.tsx`

Responsabilidades:

- Exibir dados do usuário;
- Botão para testar notificação;
- Botão de logout.

---

## 23. Exemplo de validação de formulário

```ts
function validarFormularioVeiculo() {
  if (!marca.trim()) return "Informe a marca do veículo.";
  if (!modelo.trim()) return "Informe o modelo do veículo.";
  if (!ano.trim()) return "Informe o ano do veículo.";
  if (!placa.trim()) return "Informe a placa do veículo.";

  if (!quilometragemAtual.trim() || Number.isNaN(Number(quilometragemAtual))) {
    return "Informe uma quilometragem válida.";
  }

  return null;
}
```

---

## 24. Tratamento de erros

Usar mensagens amigáveis com `Alert.alert`.

Exemplo:

```ts
try {
  await criarVeiculo(usuario.uid, veiculo);
  navigation.goBack();
} catch (erro) {
  Alert.alert(
    "Erro",
    "Não foi possível salvar o veículo. Verifique os dados e tente novamente."
  );
}
```

Mensagens sugeridas:

```txt
Erro ao fazer login.
Não foi possível salvar o veículo.
Não foi possível buscar os veículos.
Não foi possível salvar a manutenção.
Não foi possível buscar as manutenções.
Preencha todos os campos obrigatórios.
Permissão de notificação negada.
Não foi possível sair da conta.
```

---

## 25. Visual sugerido

Cores:

```txt
Primária: #0F172A
Fundo: #F1F5F9
Cards: #FFFFFF
Texto principal: #111827
Texto secundário: #64748B
Verde: #16A34A
Laranja: #F97316
Vermelho: #DC2626
```

Estilo geral:

- Tela com fundo cinza claro;
- Cards brancos com bordas arredondadas;
- Botões azul escuro;
- Situação com etiquetas coloridas;
- Campos brancos com borda clara.

---

## 26. Configuração no Firebase

Passos:

1. Acessar o console do Firebase;
2. Criar um novo projeto;
3. Adicionar um app web;
4. Copiar as credenciais do Firebase;
5. Preencher o arquivo `.env`;
6. Ativar o Firebase Authentication;
7. Ativar provedor Google;
8. Criar banco Firestore;
9. Iniciar em modo de teste durante desenvolvimento;
10. Ajustar regras de segurança antes da entrega final.

---

## 27. Configuração no Google Cloud/Firebase para Google Sign-In

Passos gerais:

1. No Firebase Authentication, ativar o provedor Google;
2. No Google Cloud Console, verificar credenciais OAuth;
3. Criar Client ID Web;
4. Criar Client ID Android, se for testar em Android;
5. Criar Client ID iOS, se for testar em iPhone;
6. Copiar os IDs para o `.env`.

---

## 28. Regras simples do Firestore para desenvolvimento

Durante desenvolvimento, pode usar modo de teste temporariamente.

Para uma versão mais segura, usar algo como:

```txt
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{idUsuario}/{documento=**} {
      allow read, write: if request.auth != null && request.auth.uid == idUsuario;
    }
  }
}
```

---

## 29. Cuidados com a apresentação

Antes da apresentação final:

- Rodar `npm install`;
- Conferir se o `.env` está preenchido;
- Conferir se o Firebase Authentication está ativo;
- Conferir se o Firestore foi criado;
- Testar login com Google;
- Cadastrar pelo menos um veículo;
- Cadastrar pelo menos três manutenções: uma em dia, uma próxima e uma atrasada;
- Testar notificação;
- Testar navegação entre todas as telas;
- Testar em celular real pelo Expo Go, se possível.

---

## 30. Dados de teste sugeridos

### Veículo

```txt
Marca: Chevrolet
Modelo: Onix
Ano: 2020
Placa: ABC1D23
Quilometragem atual: 52000
```

### Manutenção em dia

```txt
Tipo: Troca de óleo
Descrição: Óleo 5W30 e filtro
Data realizada: 2026-05-01
Km realizada: 50000
Próxima data: 2026-11-01
Próxima quilometragem: 60000
Custo: 280
```

### Manutenção próxima

```txt
Tipo: Alinhamento
Descrição: Alinhamento preventivo
Data realizada: 2026-05-10
Km realizada: 51500
Próxima data: 2026-06-10
Próxima quilometragem: 52500
Custo: 120
```

### Manutenção atrasada

```txt
Tipo: Freios
Descrição: Revisão do sistema de freio
Data realizada: 2025-12-01
Km realizada: 45000
Próxima data: 2026-04-01
Próxima quilometragem: 51000
Custo: 350
```

---

## 31. README sugerido

O `README.md` deve conter:

```txt
# BalaCar Manager

## Descrição
Aplicativo mobile para gerenciamento de manutenções automotivas.

## Objetivo
Permitir que usuários cadastrem veículos, registrem manutenções e recebam lembretes.

## Requisitos atendidos
- Interface gráfica funcional
- Navegação entre telas
- Duas APIs externas
- Persistência em nuvem
- Tratamento de erros
- Código organizado

## Tecnologias
- React Native
- Expo
- TypeScript
- Firebase
- Google Sign-In
- Expo Notifications

## APIs externas
1. Google Sign-In: autenticação de usuários.
2. Expo Notifications: envio e agendamento de notificações.

## Persistência
Firebase Cloud Firestore.

## Instalação
npm install

## Execução
npx expo start

## Variáveis de ambiente
Explicar o arquivo .env.example.

## Estrutura do Firestore
Explicar usuarios, veiculos e manutencoes.

## Funcionalidades
Listar funcionalidades implementadas.
```

---

## 32. Critérios de conclusão

O projeto estará completo quando:

- O app abrir corretamente;
- O login com Google funcionar;
- O usuário conseguir sair da conta;
- O usuário conseguir cadastrar veículo;
- O usuário conseguir listar veículos;
- O usuário conseguir acessar detalhes do veículo;
- O usuário conseguir cadastrar manutenção;
- A situação da manutenção for calculada automaticamente;
- Os dados forem salvos no Firestore;
- A notificação de teste funcionar;
- A navegação entre telas funcionar;
- O tratamento básico de erros estiver implementado;
- O README estiver preenchido;
- O `.env.example` estiver no projeto.

---

## 33. Funcionalidades que não devem ser implementadas

Não implementar:

- API de produtos;
- Mercado Livre;
- Links de afiliado;
- n8n;
- Backend Node.js;
- Docker;
- Scraping;
- Recomendação de produtos;
- Integração com lojas;
- Cotação de peças.

O foco deve permanecer no gerenciamento de manutenção automotiva.

---

## 34. Resumo final do sistema

O **BalaCar Manager** é um app mobile de controle de manutenção automotiva. Ele usa **Google Sign-In** para autenticação, **Expo Notifications** para lembretes e **Firebase Firestore** para persistência em nuvem.

O sistema permite cadastrar veículos, registrar manutenções, calcular a situação de cada manutenção e alertar o usuário sobre revisões próximas ou atrasadas.

O projeto é simples, direto, sem backend próprio e adequado para uma apresentação acadêmica de Computação Móvel.
