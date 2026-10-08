# Biblioteca JDBC PostgreSQL

Aplicação de console para gerenciamento de uma biblioteca. O projeto permite administrar livros, usuários, exemplares e empréstimos, usando Java e JDBC para persistir os dados em PostgreSQL.

## Tecnologias

- Java 21
- Maven
- PostgreSQL
- JDBC
- HikariCP
- JUnit 6

## Como funciona

A aplicação é iniciada pela classe `Application`, que configura os DAOs, serviços e menus de console.

As operações informadas no menu passam pela camada de serviço, onde ficam as validações e regras de negócio. Em seguida, os DAOs executam consultas SQL parametrizadas via JDBC e gravam ou consultam os dados no PostgreSQL. A conexão é gerenciada pelo HikariCP.

## Estrutura do projeto

```text
src/main/java/com/biblioteca
├── config       → configuração da conexão e pool de conexões
├── dao          → contratos de acesso aos dados
├── dao/impl     → implementações JDBC dos DAOs
├── exception    → exceções da aplicação
├── model        → entidades e enums do domínio
├── service      → regras de negócio e validações
├── ui           → menus e leitura do console
└── Application  → ponto de entrada da aplicação

src/main/resources
├── schema.sql             → criação das tabelas, índices e triggers
└── db-example.properties  → modelo da configuração local do banco
```

## Como executar

### Pré-requisitos

- JDK 21
- Maven
- PostgreSQL
- Uma IDE Java, como IntelliJ IDEA ou VS Code

### 1. Clone o projeto

```bash
git clone https://github.com/RMoranDev/biblioteca-jdbc-postgres.git
cd biblioteca-jdbc-postgres
```

### 2. Crie e prepare o banco

Crie um banco PostgreSQL chamado `biblioteca`:

```sql
CREATE DATABASE biblioteca;
```

Depois, execute o script [schema.sql](src/main/resources/schema.sql) nesse banco. Ele cria as tabelas de livros, usuários, exemplares e empréstimos, além de índices e triggers.

### 3. Configure a conexão local

Copie `src/main/resources/db-example.properties` para `src/main/resources/db.properties` e substitua os valores pelas credenciais locais:

```properties
db.url=jdbc:postgresql://localhost/biblioteca
db.user=seu_usuario
db.password=sua_senha
```

O arquivo `db.properties` é ignorado pelo Git e não deve ser versionado.

### 4. Compile e execute

Compile e execute os testes:

```bash
mvn clean test
```

Em seguida, execute a classe `com.biblioteca.Application` pela sua IDE. O menu principal será exibido no console.

> O `pom.xml` atual não possui uma configuração de plugin para executar a aplicação diretamente pelo Maven. Por isso, a execução pelo terminal com `mvn exec:java` não está disponível sem alterar a configuração do projeto.

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de livros
- Cadastro, consulta, atualização e exclusão de usuários
- Cadastro e gerenciamento de exemplares
- Registro, consulta, renovação, devolução e cancelamento de empréstimos
- Validação de CPF, ISBN, disponibilidade do exemplar e estados de empréstimo

## O que este projeto demonstra

- Programação orientada a objetos em Java
- Organização em camadas
- JDBC com `PreparedStatement` e mapeamento de resultados
- PostgreSQL e SQL relacional
- Pool de conexões com HikariCP
- Validações e tratamento de exceções de negócio e persistência
- Testes automatizados com JUnit

## Observações

- É necessário ter um PostgreSQL acessível com o banco e o esquema configurados antes de usar as operações do menu.
- As credenciais ficam exclusivamente em `db.properties`, que deve ser criado localmente a partir do arquivo de exemplo.
- Há problemas conhecidos na implementação JDBC atual: a busca por CPF usa a coluna incorreta, a consulta/listagem de empréstimos monta SQL inválido e o fluxo de atualização de empréstimos usa inserção. Essas operações precisam de correção no código antes de serem usadas em um ambiente real.
- O schema não aceita o status `CANCELADO`, embora o menu e o serviço disponibilizem o cancelamento de empréstimos.
