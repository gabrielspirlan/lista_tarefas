# 📋 Lista de Tarefas — API REST

**Autor:** Gabriel Resende Spirlandelli  
**Disciplina:** Laboratório de Desenvolvimento Multiplataforma

---

## 📖 Descrição

API REST desenvolvida em **Java 17** com **Spring Boot 4** para gerenciamento de tarefas. A aplicação permite criar, listar, atualizar e remover tarefas, com suporte a paginação, status controlado por enum e documentação automática via Swagger/OpenAPI.

O projeto segue uma arquitetura em camadas bem definida (Controller → Service → Repository → Domain), garantindo separação de responsabilidades e facilidade de manutenção e testes.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework principal da aplicação |
| Spring Data JPA | — | Abstração de acesso a dados |
| Spring Validation | — | Validação de dados de entrada |
| Hibernate | 7.x | ORM / mapeamento objeto-relacional |
| PostgreSQL | 16 | Banco de dados principal |
| H2 Database | — | Banco de dados em memória (testes) |
| Lombok | — | Redução de código boilerplate |
| MapStruct | 1.6.3 | Mapeamento entre entidades e DTOs |
| Springdoc OpenAPI | 2.8.8 | Documentação Swagger automática |
| Docker / Docker Compose | — | Containerização do banco de dados |
| JUnit 5 | — | Framework de testes |
| Mockito | — | Mocks para testes unitários |

---

## 🐳 Docker Compose — Banco de Dados

O banco de dados PostgreSQL é executado via Docker Compose, sem necessidade de instalação local.

### Arquivo `docker-compose.yml`

```yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: postgres_tarefas
    restart: unless-stopped
    ports:
      - "5435:5432"
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: tarefas
    command: >
      postgres
      -c listen_addresses='*'
    volumes:
      - ./database:/var/lib/postgresql/data
      - ./init-hba.sh:/docker-entrypoint-initdb.d/init-hba.sh:ro
```

### Comandos

```bash
# Subir o banco de dados
docker compose up -d

# Parar o banco de dados
docker compose down
```

O banco ficará disponível em `localhost:5435` com as credenciais `postgres / postgres` e banco `tarefas`.

---

## 🗄️ Script do Banco de Dados

O arquivo `criar-banco.sql` (na raiz do projeto) contém o script de criação da estrutura do banco para o ambiente de produção/desenvolvimento:

```sql
CREATE DATABASE tarefas;

CREATE TABLE IF NOT EXISTS tarefas (
    id               SERIAL PRIMARY KEY,
    descricao        VARCHAR(255) NOT NULL,
    tarefa_status    VARCHAR(3)   NOT NULL,
    observacoes      TEXT,
    data_criacao     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_status CHECK (tarefa_status IN ('PE', 'AND', 'CON'))
);
```

A constraint `chk_status` garante que apenas os códigos válidos do enum de status sejam armazenados: `PE` (Pendente), `AND` (Em Andamento) e `CON` (Concluída).

---

## 🏗️ Estrutura do Projeto

```
src/main/java/com/api/lista_tarefas/
├── ListaTarefasApplication.java       # Ponto de entrada da aplicação
├── application/
│   └── services/
│       ├── TarefaService.java         # Interface do serviço
│       └── TarefaServiceImpl.java     # Implementação dos serviços
├── domain/
│   ├── entities/
│   │   └── Tarefa.java                # Entidade JPA principal
│   ├── enums/
│   │   ├── TarefaStatusEnum.java      # Enum de status da tarefa
│   │   └── TarefaStatusConverter.java # Converter JPA para o enum
│   ├── dtos/
│   │   ├── request/TarefaRequestDTO   # DTO de entrada
│   │   ├── response/TarefaResponseDTO # DTO de saída
│   │   └── mappers/                   # Mappers MapStruct
│   └── exceptions/
│       └── NotFoundException.java     # Exceção de recurso não encontrado
├── infraestructure/
│   └── repositories/
│       └── TarefaRepository.java      # Interface Spring Data JPA
└── view/
    ├── controllers/
    │   └── TarefaController.java      # Controller REST
    └── handlers/                      # Handlers de exceção global
```

---

## 🗂️ Classes de Modelo

### `Tarefa.java` — Entidade Principal

Entidade JPA que representa uma tarefa na base de dados. Mapeada para a tabela `tarefas`.

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Integer` | Chave primária, gerada automaticamente |
| `descricao` | `String` | Descrição da tarefa (obrigatório) |
| `status` | `TarefaStatusEnum` | Status atual da tarefa |
| `observacoes` | `String` | Observações adicionais (opcional) |
| `dataCriacao` | `LocalDateTime` | Preenchida automaticamente no `@PrePersist` |
| `dataAtualizacao` | `LocalDateTime` | Atualizada automaticamente no `@PreUpdate` |

Os métodos `@PrePersist` e `@PreUpdate` garantem que os timestamps são gerenciados automaticamente pela aplicação, sem depender de defaults do banco.

### `TarefaStatusEnum.java` — Enum de Status

```java
PENDENTE     ("PE",  "Pendente")
EM_ANDAMENTO ("AND", "Em andamento")
CONCLUIDA    ("CON", "Concluída")
```

Cada constante carrega um código curto (`PE`, `AND`, `CON`) que é o valor persistido no banco de dados.

### `TarefaStatusConverter.java` — Converter JPA

Implementa `AttributeConverter<TarefaStatusEnum, String>` para que o Hibernate salve o **código curto** do enum (`PE`, `AND`, `CON`) na coluna, em vez do nome do enum. A conversão é aplicada automaticamente (`autoApply = true`).

---

## 🔌 Conexão com o Banco

### Produção / Desenvolvimento (`application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5435/tarefas
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

O DDL é gerenciado manualmente via script `criar-banco.sql`. O Hibernate não altera a estrutura do banco (`ddl-auto=none`).

### Testes (`application-test.properties`)

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;NON_KEYWORDS=STATUS
spring.datasource.driverClassName=org.h2.Driver

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
```

Nos testes, é utilizado o banco **H2 em memória**. O schema é criado pelo arquivo `src/test/resources/schema.sql`, sem constraints `CHECK` — necessário para compatibilidade com o H2 2.2.x e o Hibernate 7.

---

## ⚙️ Services Implementados

A interface `TarefaService` define os seguintes métodos, todos implementados em `TarefaServiceImpl`:

### `findAll(Integer pageNumber, Integer pageSize)`
Lista todas as tarefas com **paginação**. Os parâmetros `pageNumber` e `pageSize` são opcionais; quando não informados, assumem os valores padrão `0` e `10`, respectivamente. Retorna uma `Page<TarefaResponseDTO>`.

### `findById(Integer id)`
Busca uma tarefa pelo seu `id`. Lança `NotFoundException` (HTTP 404) caso a tarefa não exista no banco.

### `create(TarefaRequestDTO tarefaDTO)`
Cria uma nova tarefa a partir dos dados do DTO de requisição. Utiliza o `TarefaRequestMapper` para converter o DTO em entidade antes de persistir. Retorna o `TarefaResponseDTO` com os dados salvos.

### `update(Integer id, TarefaRequestDTO tarefaDTO)`
Atualiza parcialmente uma tarefa existente. Apenas os campos `descricao`, `status` e `observacoes` são atualizados, e somente quando o novo valor é diferente do atual (atualização seletiva). O campo `dataAtualizacao` é renovado automaticamente pelo `@PreUpdate`.

### `delete(Integer id)`
Remove uma tarefa pelo seu `id`. Lança `NotFoundException` caso o ID não exista, garantindo que nunca seja tentada uma exclusão de um recurso inexistente.

---

## ✅ Testes de Aplicação

O projeto conta com **12 testes automatizados** divididos em duas classes:

### `TarefaRepositoryTest` — Testes de Integração (Repository)

Utiliza `@DataJpaTest` com banco H2 em memória e `TestEntityManager` para testar o comportamento da camada de persistência diretamente.

| Teste | O que faz |
|---|---|
| `devePersistirUmaTarefaNaBaseDeDados` | Verifica que ao salvar uma tarefa, ela recebe um `id` não nulo, confirmando que a persistência ocorreu com sucesso. |
| `deveCriarDataDeCriacaoEAtualizacaoAutomaticamente` | Confirma que os campos `dataCriacao` e `dataAtualizacao` são preenchidos automaticamente pelo `@PrePersist` ao salvar. |
| `deveAtualizarDataDeAtualizacao` | Garante que o campo `dataAtualizacao` é atualizado para um valor mais recente após uma operação de update, enquanto `dataCriacao` permanece inalterada. |
| `devePersistirASituacaoDaTarefa` | Verifica que o status da tarefa é salvo e recuperado corretamente no formato esperado (`"PE"` / `TarefaStatusEnum.PENDENTE`). |
| `deveRetornarVerdadeiroQuandoATarefaExistir` | Testa que `existsById` retorna `true` para um ID de tarefa já persistida. |
| `deveRetornarFalsoQuandoATarefaNaoExistir` | Testa que `existsById` retorna `false` para um ID que não existe no banco. |

### `TarefaServiceTest` — Testes Unitários (Service)

Utiliza `@ExtendWith(MockitoExtension.class)` com mocks do repositório e dos mappers para testar a lógica de negócio em isolamento, sem acesso ao banco.

| Teste | O que faz |
|---|---|
| `deveLancarErroNotFoundAoBuscarPorIdInexistente` | Verifica que ao buscar um ID inexistente, o service lança `NotFoundException`. |
| `deveRetornarDTOAoBuscarPorID` | Confirma que `findById` retorna o `TarefaResponseDTO` correto quando a tarefa existe. |
| `deveCriarTarefaComSucesso` | Testa o fluxo completo de criação: conversão do DTO para entidade, chamada ao repositório e retorno do response DTO. |
| `deveAtualizarApenasOsCamposAlterados` | Garante que o update é seletivo — apenas os campos informados com valor diferente são modificados; os demais permanecem intactos. |
| `deveListarTarefasComPaginacao` | Verifica que `findAll` retorna uma página com os registros corretos e que o repositório é chamado com os parâmetros de paginação. |

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Docker e Docker Compose

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd lista_tarefas

# 2. Suba o banco de dados
docker compose up -d

# 3. Execute o script de criação do banco
# (conecte ao PostgreSQL em localhost:5435 e execute criar-banco.sql)

# 4. Execute a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.  
A documentação Swagger estará em `http://localhost:8080/swagger-ui.html`.

### Executar os Testes

```bash
./mvnw test
```

---

## 🌐 Endpoints da API

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/tarefas` | Lista todas as tarefas (paginado) |
| `GET` | `/tarefas/{id}` | Busca uma tarefa por ID |
| `POST` | `/tarefas` | Cria uma nova tarefa |
| `PATCH` | `/tarefas/{id}` | Atualiza parcialmente uma tarefa |
| `DELETE` | `/tarefas/{id}` | Remove uma tarefa |

### Parâmetros de Query (GET /tarefas)

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `pageNumber` | `Integer` | `0` | Número da página |
| `pageSize` | `Integer` | `10` | Quantidade de itens por página |
