# 🎮 Biblioteca de Jogos API

REST API para gerenciamento de biblioteca pessoal de jogos. Cada usuário possui sua própria coleção isolada, com autenticação via JWT e controle total sobre seus jogos.

🔗 **[Swagger UI — API em produção](https://biblioteca-jogos-public-production.up.railway.app/swagger-ui/index.html)**

---

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **MapStruct**
- **Springdoc OpenAPI (Swagger)**
- **JUnit 5 + Mockito**
- **Railway** (deploy)

---

## ✅ Funcionalidades

- Cadastro e autenticação de usuários com JWT
- CRUD completo de jogos
- Cada usuário acessa apenas seus próprios jogos
- Busca por título, plataforma, gênero, status e nota
- Adição incremental de horas jogadas
- Atualização parcial de jogos
- Contagem de jogos por plataforma
- Validações de dados com mensagens de erro semânticas

---

## 🔐 Autenticação

A API utiliza autenticação **Bearer JWT**. Para acessar os endpoints protegidos:

1. Cadastre um usuário: `POST /usuario`
2. Faça login: `POST /usuario/login`
3. Copie o token retornado
4. Clique no botão **Authorize 🔒** no Swagger e cole o token

---

## 📋 Endpoints

### Usuário
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/usuario` | Cadastrar usuário | ❌ |
| POST | `/usuario/login` | Autenticar e obter token | ❌ |
| GET | `/usuario` | Listar todos os usuários | ✅ |
| GET | `/usuario/email` | Buscar usuário por email | ✅ |
| DELETE | `/usuario/{email}` | Deletar usuário | ✅ |

### Jogos
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/jogos` | Cadastrar jogo | ✅ |
| GET | `/jogos` | Listar todos os jogos | ✅ |
| GET | `/jogos/{id}` | Buscar jogo por ID | ✅ |
| GET | `/jogos/titulo` | Buscar por título | ✅ |
| GET | `/jogos/plataforma` | Buscar por plataforma | ✅ |
| GET | `/jogos/genero` | Buscar por gênero | ✅ |
| GET | `/jogos/status` | Buscar por status | ✅ |
| GET | `/jogos/nota-pessoal` | Buscar por nota | ✅ |
| GET | `/jogos/nota_pessoal_min` | Listar jogos acima de nota mínima | ✅ |
| GET | `/jogos/quantidade/total` | Contar total de jogos | ✅ |
| GET | `/jogos/quantidade/plataformas` | Contar jogos por plataforma | ✅ |
| PUT | `/jogos/{id}` | Atualizar jogo | ✅ |
| PATCH | `/jogos/{id}/adicionar-horas` | Adicionar horas jogadas | ✅ |
| PATCH | `/jogos/{id}/atualizar-status` | Atualizar status | ✅ |
| DELETE | `/jogos/{id}` | Deletar jogo | ✅ |

---

## 📝 Exemplos de Requisições

### Cadastrar usuário
```json
POST /usuario
{
  "nome": "Caigods",
  "email": "caigods@email.com",
  "senha": "senha123"
}
```

### Login
```json
POST /usuario/login
{
  "email": "caigods@email.com",
  "senha": "senha123"
}
```


Logo após o login copie o token retornado SEM o Bearer ex:
(Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpYXQiOjE3NzMzNzIyMzQsImV4cCI6MTc3MzM3NTgzNH0.jr0gN3ywUX4h0tu-0PDG8psW3CWruTDfXr9U17M9Oe0).


Clique no botão **Authorize 🔒** no Swagger(Superior direito na página) e cole o token.


Caso queira testar sem cadastro, use o login a seguir.

```json
POST /usuario/login
{
  "email": "admin@admin.com",
  "senha": "admin"
}
```

### Cadastrar jogo
```json
POST /jogos
{
  "titulo": "The Witcher 3",
  "plataformas": "PC",
  "genero": "RPG; Aventura; Medieval",
  "anoDeLancamento": 2015,
  "status": "ZERADO",
  "notaPessoal": 9.5,
  "horasJogadas": 120.0
}
```

> ⚠️ Os campos `plataformas` e `status` são obrigatórios e devem usar exatamente os valores listados abaixo.

---

## 🎮 Valores aceitos: Plataformas

```
PC

Consoles — 1ª e 2ª geração:
MAGNAVOX_ODYSSEY, ATARI_2600, COLECOVISION, INTELLIVISION

Consoles — 3ª geração:
NES, MASTER_SYSTEM, ATARI_7800

Consoles — 4ª geração:
SNES, MEGA_DRIVE, NEO_GEO, TURBOGRAFX_16

Consoles — 5ª geração:
PLAYSTATION_1, NINTENDO_64, SEGA_SATURN, IM_3D0

Consoles — 6ª geração:
PLAYSTATION_2, DREAMCAST, GAMECUBE, XBOX

Consoles — 7ª geração:
PLAYSTATION_3, XBOX_360, WII

Consoles — 8ª geração:
PLAYSTATION_4, XBOX_ONE, WII_U, NINTENDO_SWITCH

Consoles — 9ª geração:
PLAYSTATION_5, XBOX_SERIES_X, XBOX_SERIES_S

Portáteis:
GAME_BOY, GAME_GEAR, PSP, NINTENDO_DS, PS_VITA, NINTENDO_3DS
```

---

## 📊 Valores aceitos: Status

```
JOGANDO   → jogo em andamento
ZERADO    → jogo finalizado
DROPADO   → jogo abandonado
QUEUE     → na fila para jogar
```

### Atualizar jogo
É possivel fazer um PUT "parcial", caso não mande todos os valores do objeto, ele atualiza apenas o que foi enviado no JSON, sem perder o que existia antes.
EXEMPLO:
```json
PUT /jogos/{id}
{
  "genero": "RPG; Aventura; Medieval; Fantasia",
  "status": "JOGANDO"
}
```
Resultado após o PUT "parcial" anterior. 

```json
{
  "id": 1,
  "titulo": "The Witcher 3",
  "plataformas": "PC",
  "genero": "RPG; Aventura; Medieval; Fantasia",
  "anoDeLancamento": 2015,
  "status": "JOGANDO",
  "notaPessoal": 9.5,
  "horasJogadas": 120
}
```
---

## ▶️ Como rodar localmente

### Pré-requisitos
- Java 21
- Maven
- PostgreSQL

### Configuração

1. Clone o repositório:
```bash
git clone https://github.com/caigods/biblioteca-jogos-public.git
cd biblioteca-jogos-public
```

2. Crie o banco de dados no PostgreSQL:
```sql
CREATE DATABASE biblioteca_jogos;
```

3. Configure o `src/main/resources/application.properties`:
```properties
spring.application.name=biblioteca-jogos
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca_jogos
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=org.postgresql.Driver
jwt.secret=sua_chave_secreta_com_minimo_32_caracteres
```

4. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

5. Acesse o Swagger local:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Testes

```bash
./mvnw test
```

Testes unitários com JUnit 5 e Mockito cobrindo os principais fluxos do `JogoService`.

---

## 📦 Estrutura do Projeto

```
src/main/java/com/caigods/biblioteca_jogos/
├── business/           # Services (regras de negócio)
├── controller/         # Controllers REST
├── dto/                # Data Transfer Objects
├── exception/          # Exceções customizadas
├── infrasctuture/
│   ├── entity/         # Entidades JPA
│   ├── repository/     # Repositórios Spring Data
│   └── security/       # JWT, Security Config, Swagger
└── mapper/             # Mappers MapStruct
```

---


🛠️ Próximos Passos

Front-end com React: Criação de interface para consumo da API e gerenciamento visual da biblioteca.

Dockerização: Configuração de Dockerfile e docker-compose.yml para padronização do ambiente.

Capa dos Jogos: Integração com serviço de storage (AWS S3 ou Cloudinary) para upload de imagens.

Pipeline de CI/CD: Automação de testes e deploy via GitHub Actions.

Dashboard: Endpoints para estatísticas de progresso (ex: % de jogos zerados por plataforma).


## 👤 Autor

**Caigods** — Desenvolvedor em formação, focado em backend Java com Spring Boot.
