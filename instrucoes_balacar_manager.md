# BalaCar Manager — Guia Completo de Desenvolvimento

## 1. Visão geral do projeto

O **BalaCar Manager** é um aplicativo mobile desenvolvido em **React Native com Expo e TypeScript** para gerenciamento de manutenções automotivas.

O objetivo do sistema é permitir que o usuário faça login com sua conta Google, cadastre veículos, registre manutenções e acompanhe se cada manutenção está em dia, próxima do vencimento ou atrasada.

O sistema também deve permitir o envio de notificações locais para lembrar o usuário sobre manutenções futuras.

## 2. Requisitos obrigatórios atendidos

| Requisito | Como será atendido |
|---|---|
| Interface gráfica funcional | Telas em React Native com formulários, cards e botões |
| Navegação entre telas | React Navigation |
| Integração com duas APIs externas | Google Sign-In e Expo Notifications |
| Persistência de dados | Firebase Cloud Firestore |
| Tratamento básico de erros | Validação de campos e mensagens amigáveis |
| Código organizado e comentado | Separação por components, screens, services, utils e types |
| Aplicação funcionando na apresentação | Execução com `npm install` e `npx expo start` |

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

## 5. Persistência de dados

A persistência será feita em nuvem usando **Firebase Cloud Firestore**.

Estrutura sugerida:

```txt
users/{userId}
  name
  email
  photoURL
  createdAt

users/{userId}/vehicles/{vehicleId}
  brand
  model
  year
  plate
  currentKm
  createdAt
  updatedAt

users/{userId}/vehicles/{vehicleId}/maintenances/{maintenanceId}
  type
  description
  performedDate
  performedKm
  nextDate
  nextKm
  cost
  notes
  status
  notificationId
  createdAt
  updatedAt
```

Cada usuário deve acessar apenas seus próprios dados.

## 6. Funcionalidades do sistema

### 6.1 Autenticação

- Login com conta Google;
- Logout;
- Salvamento dos dados básicos do usuário no Firestore;
- Proteção das telas internas para usuários autenticados.

### 6.2 Veículos

O usuário poderá:

- Cadastrar veículos;
- Listar veículos cadastrados;
- Visualizar detalhes de um veículo;
- Atualizar quilometragem atual;
- Excluir veículo, se desejado.

Campos do veículo:

```txt
brand
model
year
plate
currentKm
createdAt
updatedAt
```

### 6.3 Manutenções

O usuário poderá:

- Cadastrar manutenções;
- Listar manutenções de um veículo;
- Visualizar status da manutenção;
- Agendar lembrete;
- Excluir manutenção, se desejado.

Campos da manutenção:

```txt
type
description
performedDate
performedKm
nextDate
nextKm
cost
notes
status
notificationId
createdAt
updatedAt
```

## 7. Regras de status da manutenção

O status da manutenção será calculado com base na data prevista e na quilometragem prevista.

Tipos possíveis:

```ts
export type MaintenanceStatus = "em_dia" | "proxima" | "atrasada";
```

Regras:

```txt
Se nextDate já passou ou currentKm >= nextKm:
  status = "atrasada"

Se faltam 15 dias ou menos para nextDate:
  status = "proxima"

Se faltam 500 km ou menos para nextKm:
  status = "proxima"

Caso contrário:
  status = "em_dia"
```

Observações:

- Se `nextDate` estiver vazio, considerar apenas `nextKm`;
- Se `nextKm` estiver vazio, considerar apenas `nextDate`;
- Se ambos estiverem vazios, retornar `em_dia`.

## 8. Estrutura de pastas

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
    components/
      Button.tsx
      Input.tsx
      StatusBadge.tsx
      VehicleCard.tsx
      MaintenanceCard.tsx

    screens/
      LoginScreen.tsx
      DashboardScreen.tsx
      VehicleFormScreen.tsx
      VehicleDetailsScreen.tsx
      MaintenanceFormScreen.tsx
      ProfileScreen.tsx

    services/
      firebase.ts
      authService.ts
      vehicleService.ts
      maintenanceService.ts
      notificationService.ts

    navigation/
      RootNavigator.tsx
      AuthNavigator.tsx
      AppNavigator.tsx

    utils/
      calculateMaintenanceStatus.ts
      formatDate.ts
      formatCurrency.ts

    types/
      User.ts
      Vehicle.ts
      Maintenance.ts
```

## 9. Criação do projeto

Criar o projeto com Expo:

```bash
npx create-expo-app balacar-manager --template
```

Entrar na pasta:

```bash
cd balacar-manager
```

Instalar dependências principais:

```bash
npm install firebase
npm install @react-navigation/native
npm install @react-navigation/native-stack
npx expo install react-native-screens react-native-safe-area-context
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

## 10. Variáveis de ambiente

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

## 11. Configuração do Firebase

Arquivo: `src/services/firebase.ts`

```ts
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: process.env.EXPO_PUBLIC_FIREBASE_API_KEY,
  authDomain: process.env.EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN,
  projectId: process.env.EXPO_PUBLIC_FIREBASE_PROJECT_ID,
  storageBucket: process.env.EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.EXPO_PUBLIC_FIREBASE_APP_ID,
};

// Inicializa o Firebase no app.
const app = initializeApp(firebaseConfig);

// Exporta autenticação e banco de dados para uso nos serviços.
export const auth = getAuth(app);
export const db = getFirestore(app);
```

## 12. Tipos do sistema

### `src/types/User.ts`

```ts
export interface AppUser {
  id: string;
  name: string;
  email: string;
  photoURL?: string;
  createdAt?: Date;
}
```

### `src/types/Vehicle.ts`

```ts
export interface Vehicle {
  id?: string;
  brand: string;
  model: string;
  year: string;
  plate: string;
  currentKm: number;
  createdAt?: Date;
  updatedAt?: Date;
}
```

### `src/types/Maintenance.ts`

```ts
export type MaintenanceStatus = "em_dia" | "proxima" | "atrasada";

export interface Maintenance {
  id?: string;
  type: string;
  description?: string;
  performedDate: string;
  performedKm: number;
  nextDate?: string;
  nextKm?: number;
  cost?: number;
  notes?: string;
  status: MaintenanceStatus;
  notificationId?: string | null;
  createdAt?: Date;
  updatedAt?: Date;
}
```

## 13. Utilitário de cálculo de status

Arquivo: `src/utils/calculateMaintenanceStatus.ts`

```ts
import { MaintenanceStatus } from "../types/Maintenance";

interface Params {
  currentKm: number;
  nextDate?: string;
  nextKm?: number;
}

export function calculateMaintenanceStatus({
  currentKm,
  nextDate,
  nextKm,
}: Params): MaintenanceStatus {
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  let isOverdueByDate = false;
  let isSoonByDate = false;

  if (nextDate) {
    const targetDate = new Date(nextDate);
    targetDate.setHours(0, 0, 0, 0);

    const diffTime = targetDate.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    isOverdueByDate = diffDays < 0;
    isSoonByDate = diffDays >= 0 && diffDays <= 15;
  }

  const isOverdueByKm = typeof nextKm === "number" && currentKm >= nextKm;
  const isSoonByKm =
    typeof nextKm === "number" && nextKm - currentKm > 0 && nextKm - currentKm <= 500;

  if (isOverdueByDate || isOverdueByKm) {
    return "atrasada";
  }

  if (isSoonByDate || isSoonByKm) {
    return "proxima";
  }

  return "em_dia";
}
```

## 14. Utilitários de formatação

### `src/utils/formatDate.ts`

```ts
export function formatDate(date?: string): string {
  if (!date) return "Não informado";

  const parsedDate = new Date(date);

  if (Number.isNaN(parsedDate.getTime())) {
    return "Data inválida";
  }

  return parsedDate.toLocaleDateString("pt-BR");
}
```

### `src/utils/formatCurrency.ts`

```ts
export function formatCurrency(value?: number): string {
  if (typeof value !== "number") return "R$ 0,00";

  return value.toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}
```

## 15. Serviço de autenticação

Arquivo: `src/services/authService.ts`

```ts
import * as WebBrowser from "expo-web-browser";
import * as Google from "expo-auth-session/providers/google";
import { GoogleAuthProvider, signInWithCredential, signOut } from "firebase/auth";
import { doc, setDoc, serverTimestamp } from "firebase/firestore";
import { auth, db } from "./firebase";

WebBrowser.maybeCompleteAuthSession();

export function useGoogleAuthRequest() {
  return Google.useAuthRequest({
    webClientId: process.env.EXPO_PUBLIC_GOOGLE_WEB_CLIENT_ID,
    androidClientId: process.env.EXPO_PUBLIC_GOOGLE_ANDROID_CLIENT_ID,
    iosClientId: process.env.EXPO_PUBLIC_GOOGLE_IOS_CLIENT_ID,
  });
}

export async function signInWithGoogleToken(idToken: string) {
  try {
    const credential = GoogleAuthProvider.credential(idToken);
    const result = await signInWithCredential(auth, credential);

    const user = result.user;

    await setDoc(
      doc(db, "users", user.uid),
      {
        name: user.displayName,
        email: user.email,
        photoURL: user.photoURL,
        createdAt: serverTimestamp(),
      },
      { merge: true }
    );

    return user;
  } catch (error) {
    console.error("Erro ao fazer login com Google:", error);
    throw new Error("Não foi possível fazer login com Google.");
  }
}

export async function logout() {
  try {
    await signOut(auth);
  } catch (error) {
    console.error("Erro ao sair:", error);
    throw new Error("Não foi possível sair da conta.");
  }
}
```

Observação: dependendo da versão do Expo e do Firebase, pode ser necessário ajustar o fluxo de `idToken` ou usar `response.authentication?.idToken` na tela de login.

## 16. Serviço de veículos

Arquivo: `src/services/vehicleService.ts`

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
import { db } from "./firebase";
import { Vehicle } from "../types/Vehicle";

function vehiclesCollection(userId: string) {
  return collection(db, "users", userId, "vehicles");
}

export async function createVehicle(userId: string, vehicle: Vehicle) {
  try {
    await addDoc(vehiclesCollection(userId), {
      ...vehicle,
      createdAt: serverTimestamp(),
      updatedAt: serverTimestamp(),
    });
  } catch (error) {
    console.error("Erro ao salvar veículo:", error);
    throw new Error("Não foi possível salvar o veículo.");
  }
}

export async function getVehicles(userId: string): Promise<Vehicle[]> {
  try {
    const q = query(vehiclesCollection(userId), orderBy("createdAt", "desc"));
    const snapshot = await getDocs(q);

    return snapshot.docs.map((docItem) => ({
      id: docItem.id,
      ...docItem.data(),
    })) as Vehicle[];
  } catch (error) {
    console.error("Erro ao buscar veículos:", error);
    throw new Error("Não foi possível buscar os veículos.");
  }
}

export async function updateVehicle(userId: string, vehicleId: string, data: Partial<Vehicle>) {
  try {
    await updateDoc(doc(db, "users", userId, "vehicles", vehicleId), {
      ...data,
      updatedAt: serverTimestamp(),
    });
  } catch (error) {
    console.error("Erro ao atualizar veículo:", error);
    throw new Error("Não foi possível atualizar o veículo.");
  }
}

export async function updateVehicleKm(userId: string, vehicleId: string, currentKm: number) {
  return updateVehicle(userId, vehicleId, { currentKm });
}

export async function deleteVehicle(userId: string, vehicleId: string) {
  try {
    await deleteDoc(doc(db, "users", userId, "vehicles", vehicleId));
  } catch (error) {
    console.error("Erro ao excluir veículo:", error);
    throw new Error("Não foi possível excluir o veículo.");
  }
}
```

## 17. Serviço de manutenções

Arquivo: `src/services/maintenanceService.ts`

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
import { db } from "./firebase";
import { Maintenance } from "../types/Maintenance";
import { calculateMaintenanceStatus } from "../utils/calculateMaintenanceStatus";

function maintenancesCollection(userId: string, vehicleId: string) {
  return collection(db, "users", userId, "vehicles", vehicleId, "maintenances");
}

export async function createMaintenance(
  userId: string,
  vehicleId: string,
  currentKm: number,
  maintenance: Omit<Maintenance, "status">
) {
  try {
    const status = calculateMaintenanceStatus({
      currentKm,
      nextDate: maintenance.nextDate,
      nextKm: maintenance.nextKm,
    });

    await addDoc(maintenancesCollection(userId, vehicleId), {
      ...maintenance,
      status,
      createdAt: serverTimestamp(),
      updatedAt: serverTimestamp(),
    });
  } catch (error) {
    console.error("Erro ao salvar manutenção:", error);
    throw new Error("Não foi possível salvar a manutenção.");
  }
}

export async function getMaintenances(userId: string, vehicleId: string): Promise<Maintenance[]> {
  try {
    const q = query(maintenancesCollection(userId, vehicleId), orderBy("createdAt", "desc"));
    const snapshot = await getDocs(q);

    return snapshot.docs.map((docItem) => ({
      id: docItem.id,
      ...docItem.data(),
    })) as Maintenance[];
  } catch (error) {
    console.error("Erro ao buscar manutenções:", error);
    throw new Error("Não foi possível buscar as manutenções.");
  }
}

export async function updateMaintenance(
  userId: string,
  vehicleId: string,
  maintenanceId: string,
  data: Partial<Maintenance>
) {
  try {
    await updateDoc(doc(db, "users", userId, "vehicles", vehicleId, "maintenances", maintenanceId), {
      ...data,
      updatedAt: serverTimestamp(),
    });
  } catch (error) {
    console.error("Erro ao atualizar manutenção:", error);
    throw new Error("Não foi possível atualizar a manutenção.");
  }
}

export async function deleteMaintenance(userId: string, vehicleId: string, maintenanceId: string) {
  try {
    await deleteDoc(doc(db, "users", userId, "vehicles", vehicleId, "maintenances", maintenanceId));
  } catch (error) {
    console.error("Erro ao excluir manutenção:", error);
    throw new Error("Não foi possível excluir a manutenção.");
  }
}

export function getMaintenanceSummary(maintenances: Maintenance[]) {
  return maintenances.reduce(
    (summary, maintenance) => {
      summary[maintenance.status] += 1;
      return summary;
    },
    {
      em_dia: 0,
      proxima: 0,
      atrasada: 0,
    }
  );
}
```

## 18. Serviço de notificações

Arquivo: `src/services/notificationService.ts`

```ts
import * as Notifications from "expo-notifications";

Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: true,
    shouldSetBadge: false,
  }),
});

export async function requestNotificationPermission(): Promise<boolean> {
  const { status } = await Notifications.getPermissionsAsync();

  if (status === "granted") {
    return true;
  }

  const request = await Notifications.requestPermissionsAsync();
  return request.status === "granted";
}

export async function sendTestNotification() {
  const hasPermission = await requestNotificationPermission();

  if (!hasPermission) {
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

export async function scheduleMaintenanceNotification({
  title,
  body,
  date,
}: {
  title: string;
  body: string;
  date: Date;
}) {
  const hasPermission = await requestNotificationPermission();

  if (!hasPermission) {
    throw new Error("Permissão de notificação negada.");
  }

  const notificationId = await Notifications.scheduleNotificationAsync({
    content: {
      title,
      body,
    },
    trigger: date,
  });

  return notificationId;
}

export async function cancelNotification(notificationId: string) {
  await Notifications.cancelScheduledNotificationAsync(notificationId);
}
```

## 19. Componentes reutilizáveis

### `src/components/Button.tsx`

```tsx
import React from "react";
import { TouchableOpacity, Text, StyleSheet, ActivityIndicator } from "react-native";

interface Props {
  title: string;
  onPress: () => void;
  loading?: boolean;
}

export function Button({ title, onPress, loading = false }: Props) {
  return (
    <TouchableOpacity style={styles.button} onPress={onPress} disabled={loading}>
      {loading ? <ActivityIndicator color="#fff" /> : <Text style={styles.text}>{title}</Text>}
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  button: {
    backgroundColor: "#0F172A",
    padding: 14,
    borderRadius: 10,
    alignItems: "center",
    marginVertical: 8,
  },
  text: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 16,
  },
});
```

### `src/components/Input.tsx`

```tsx
import React from "react";
import { TextInput, StyleSheet, TextInputProps } from "react-native";

export function Input(props: TextInputProps) {
  return <TextInput style={styles.input} placeholderTextColor="#777" {...props} />;
}

const styles = StyleSheet.create({
  input: {
    backgroundColor: "#fff",
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 10,
    padding: 12,
    marginVertical: 6,
  },
});
```

### `src/components/StatusBadge.tsx`

```tsx
import React from "react";
import { Text, StyleSheet } from "react-native";
import { MaintenanceStatus } from "../types/Maintenance";

interface Props {
  status: MaintenanceStatus;
}

const statusMap = {
  em_dia: { label: "Em dia", color: "#16A34A" },
  proxima: { label: "Próxima", color: "#F97316" },
  atrasada: { label: "Atrasada", color: "#DC2626" },
};

export function StatusBadge({ status }: Props) {
  const current = statusMap[status];

  return <Text style={[styles.badge, { backgroundColor: current.color }]}>{current.label}</Text>;
}

const styles = StyleSheet.create({
  badge: {
    color: "#fff",
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 20,
    alignSelf: "flex-start",
    fontWeight: "bold",
  },
});
```

### `src/components/VehicleCard.tsx`

```tsx
import React from "react";
import { TouchableOpacity, Text, StyleSheet } from "react-native";
import { Vehicle } from "../types/Vehicle";

interface Props {
  vehicle: Vehicle;
  onPress: () => void;
}

export function VehicleCard({ vehicle, onPress }: Props) {
  return (
    <TouchableOpacity style={styles.card} onPress={onPress}>
      <Text style={styles.title}>{vehicle.brand} {vehicle.model}</Text>
      <Text>Ano: {vehicle.year}</Text>
      <Text>Placa: {vehicle.plate}</Text>
      <Text>Km atual: {vehicle.currentKm}</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: "#fff",
    padding: 14,
    borderRadius: 12,
    marginVertical: 8,
  },
  title: {
    fontSize: 18,
    fontWeight: "bold",
    marginBottom: 4,
  },
});
```

### `src/components/MaintenanceCard.tsx`

```tsx
import React from "react";
import { View, Text, StyleSheet } from "react-native";
import { Maintenance } from "../types/Maintenance";
import { StatusBadge } from "./StatusBadge";
import { formatDate } from "../utils/formatDate";
import { formatCurrency } from "../utils/formatCurrency";

interface Props {
  maintenance: Maintenance;
}

export function MaintenanceCard({ maintenance }: Props) {
  return (
    <View style={styles.card}>
      <Text style={styles.title}>{maintenance.type}</Text>
      <Text>{maintenance.description}</Text>
      <Text>Data realizada: {formatDate(maintenance.performedDate)}</Text>
      <Text>Próxima data: {formatDate(maintenance.nextDate)}</Text>
      <Text>Próximo km: {maintenance.nextKm ?? "Não informado"}</Text>
      <Text>Custo: {formatCurrency(maintenance.cost)}</Text>
      <StatusBadge status={maintenance.status} />
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: "#fff",
    padding: 14,
    borderRadius: 12,
    marginVertical: 8,
  },
  title: {
    fontSize: 16,
    fontWeight: "bold",
    marginBottom: 4,
  },
});
```

## 20. Navegação

### `src/navigation/AuthNavigator.tsx`

```tsx
import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { LoginScreen } from "../screens/LoginScreen";

const Stack = createNativeStackNavigator();

export function AuthNavigator() {
  return (
    <Stack.Navigator>
      <Stack.Screen name="Login" component={LoginScreen} options={{ headerShown: false }} />
    </Stack.Navigator>
  );
}
```

### `src/navigation/AppNavigator.tsx`

```tsx
import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { DashboardScreen } from "../screens/DashboardScreen";
import { VehicleFormScreen } from "../screens/VehicleFormScreen";
import { VehicleDetailsScreen } from "../screens/VehicleDetailsScreen";
import { MaintenanceFormScreen } from "../screens/MaintenanceFormScreen";
import { ProfileScreen } from "../screens/ProfileScreen";

const Stack = createNativeStackNavigator();

export function AppNavigator() {
  return (
    <Stack.Navigator>
      <Stack.Screen name="Dashboard" component={DashboardScreen} options={{ title: "BalaCar Manager" }} />
      <Stack.Screen name="VehicleForm" component={VehicleFormScreen} options={{ title: "Cadastrar veículo" }} />
      <Stack.Screen name="VehicleDetails" component={VehicleDetailsScreen} options={{ title: "Detalhes do veículo" }} />
      <Stack.Screen name="MaintenanceForm" component={MaintenanceFormScreen} options={{ title: "Cadastrar manutenção" }} />
      <Stack.Screen name="Profile" component={ProfileScreen} options={{ title: "Perfil" }} />
    </Stack.Navigator>
  );
}
```

### `src/navigation/RootNavigator.tsx`

```tsx
import React, { useEffect, useState } from "react";
import { NavigationContainer } from "@react-navigation/native";
import { onAuthStateChanged, User } from "firebase/auth";
import { auth } from "../services/firebase";
import { AuthNavigator } from "./AuthNavigator";
import { AppNavigator } from "./AppNavigator";

export function RootNavigator() {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
      setUser(currentUser);
      setLoading(false);
    });

    return unsubscribe;
  }, []);

  if (loading) {
    return null;
  }

  return (
    <NavigationContainer>
      {user ? <AppNavigator /> : <AuthNavigator />}
    </NavigationContainer>
  );
}
```

### `App.tsx`

```tsx
import React from "react";
import { RootNavigator } from "./src/navigation/RootNavigator";

export default function App() {
  return <RootNavigator />;
}
```

## 21. Telas do sistema

As telas podem ser implementadas inicialmente de forma simples, priorizando funcionamento.

### `LoginScreen.tsx`

Responsabilidades:

- Exibir nome do app;
- Exibir botão de login com Google;
- Chamar fluxo de autenticação;
- Exibir erro se falhar.

Fluxo esperado:

```tsx
// 1. Criar request com useGoogleAuthRequest
// 2. Ao clicar no botão, chamar promptAsync()
// 3. Se response for success, capturar idToken
// 4. Chamar signInWithGoogleToken(idToken)
```

### `DashboardScreen.tsx`

Responsabilidades:

- Buscar veículos do usuário;
- Exibir resumo;
- Listar veículos;
- Navegar para cadastro de veículo;
- Navegar para detalhes do veículo;
- Navegar para perfil.

### `VehicleFormScreen.tsx`

Responsabilidades:

- Formulário de cadastro de veículo;
- Validação dos campos;
- Salvar no Firestore usando `createVehicle`.

### `VehicleDetailsScreen.tsx`

Responsabilidades:

- Exibir dados do veículo;
- Buscar manutenções;
- Listar manutenções;
- Navegar para cadastro de manutenção;
- Atualizar quilometragem.

### `MaintenanceFormScreen.tsx`

Responsabilidades:

- Formulário de manutenção;
- Validação de campos;
- Calcular status;
- Agendar notificação, se ativada;
- Salvar manutenção no Firestore.

### `ProfileScreen.tsx`

Responsabilidades:

- Exibir dados do usuário;
- Botão para testar notificação;
- Botão de logout.

## 22. Exemplo de validação de formulário

```ts
function validateVehicleForm() {
  if (!brand.trim()) return "Informe a marca do veículo.";
  if (!model.trim()) return "Informe o modelo do veículo.";
  if (!year.trim()) return "Informe o ano do veículo.";
  if (!plate.trim()) return "Informe a placa do veículo.";
  if (!currentKm.trim() || Number.isNaN(Number(currentKm))) {
    return "Informe uma quilometragem válida.";
  }

  return null;
}
```

## 23. Tratamento de erros

Usar mensagens amigáveis com `Alert.alert`.

Exemplo:

```ts
try {
  await createVehicle(user.uid, vehicle);
  navigation.goBack();
} catch (error) {
  Alert.alert("Erro", "Não foi possível salvar o veículo. Verifique os dados e tente novamente.");
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

## 24. Visual sugerido

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
- Status com badges coloridas;
- Inputs brancos com borda clara.

## 25. Configuração no Firebase

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

## 26. Configuração no Google Cloud/Firebase para Google Sign-In

Passos gerais:

1. No Firebase Authentication, ativar o provedor Google;
2. No Google Cloud Console, verificar credenciais OAuth;
3. Criar Client ID Web;
4. Criar Client ID Android, se for testar em Android;
5. Criar Client ID iOS, se for testar em iPhone;
6. Copiar os IDs para o `.env`.

## 27. Regras simples do Firestore para desenvolvimento

Durante desenvolvimento, pode usar modo de teste temporariamente.

Para uma versão mais segura, usar algo como:

```txt
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## 28. Cuidados com a apresentação

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

## 29. Dados de teste sugeridos

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
Próxima km: 60000
Custo: 280
```

### Manutenção próxima

```txt
Tipo: Alinhamento
Descrição: Alinhamento preventivo
Data realizada: 2026-05-10
Km realizada: 51500
Próxima data: 2026-06-10
Próxima km: 52500
Custo: 120
```

### Manutenção atrasada

```txt
Tipo: Freios
Descrição: Revisão do sistema de freio
Data realizada: 2025-12-01
Km realizada: 45000
Próxima data: 2026-04-01
Próxima km: 51000
Custo: 350
```

## 30. README sugerido

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
Explicar users, vehicles e maintenances.

## Funcionalidades
Listar funcionalidades implementadas.
```

## 31. Critérios de conclusão

O projeto estará completo quando:

- O app abrir corretamente;
- O login com Google funcionar;
- O usuário conseguir sair da conta;
- O usuário conseguir cadastrar veículo;
- O usuário conseguir listar veículos;
- O usuário conseguir acessar detalhes do veículo;
- O usuário conseguir cadastrar manutenção;
- O status da manutenção for calculado automaticamente;
- Os dados forem salvos no Firestore;
- A notificação de teste funcionar;
- A navegação entre telas funcionar;
- O tratamento básico de erros estiver implementado;
- O README estiver preenchido;
- O `.env.example` estiver no projeto.

## 32. Funcionalidades que não devem ser implementadas

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

## 33. Resumo final do sistema

O **BalaCar Manager** é um app mobile de controle de manutenção automotiva. Ele usa **Google Sign-In** para autenticação, **Expo Notifications** para lembretes e **Firebase Firestore** para persistência em nuvem.

O sistema permite cadastrar veículos, registrar manutenções, calcular o status de cada manutenção e alertar o usuário sobre revisões próximas ou atrasadas.

O projeto é simples, direto, sem backend próprio e adequado para uma apresentação acadêmica de Computação Móvel.
