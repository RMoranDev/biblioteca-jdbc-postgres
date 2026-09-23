# biblioteca-jdbc-postgres

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![JDBC](https://img.shields.io/badge/JDBC-Data%20Access-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://docs.oracle.com/javase/tutorial/jdbc/)
[![JUnit 5](https://img.shields.io/badge/JUnit-Testing-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

> Sistema de gerenciamento de biblioteca desenvolvido em **Java**, utilizando **JDBC puro** para acesso direto ao **PostgreSQL**, com foco em persistência de dados, organização do código, validação e fundamentos de acesso a dados sem o uso de ORM.

---

## Sobre o Projeto

O **biblioteca-jdbc-postgres** é um projeto desenvolvido para praticar e consolidar conceitos fundamentais de desenvolvimento backend em Java, especialmente a comunicação direta entre uma aplicação Java e um banco de dados relacional.

Diferentemente de aplicações que utilizam frameworks de persistência como JPA/Hibernate, este projeto utiliza **JDBC (Java Database Connectivity)** diretamente. Dessa forma, a aplicação trabalha explicitamente com conceitos como:

- `Connection`
- `PreparedStatement`
- `Statement`
- `ResultSet`
- `SQLException`
- Transações JDBC
- Mapeamento entre registros do banco e objetos Java
- Separação entre regras de negócio e acesso a dados

O projeto utiliza **PostgreSQL** como banco de dados relacional e **Maven** para gerenciamento de dependências e build.

### Objetivos de aprendizado

Este projeto foi construído com foco em:

- Consolidar Java intermediário;
- Entender profundamente o funcionamento do JDBC;
- Praticar acesso e persistência de dados relacionais;
- Trabalhar com SQL e PostgreSQL;
- Aplicar separação de responsabilidades;
- Praticar tratamento de exceções;
- Organizar um projeto Java utilizando Maven;
- Criar uma base sólida para projetos posteriores com Spring Boot, JPA e APIs REST.

---

## Funcionalidades

### Gerenciamento da biblioteca

- [x] Persistência de dados utilizando PostgreSQL;
- [x] Conexão com o banco utilizando JDBC;
- [x] Execução de comandos SQL através de `PreparedStatement`;
- [x] Consulta de dados utilizando `ResultSet`;
- [x] Separação entre objetos Java e camada de persistência;
- [x] Validação de dados de entrada;
- [x] Tratamento de exceções relacionadas ao acesso ao banco;
- [x] Configuração das credenciais do banco fora do código-fonte.

### Operações de persistência

O projeto utiliza JDBC para realizar operações de banco de dados, incluindo os fundamentos de:

```text
CREATE / INSERT
READ   / SELECT
UPDATE
DELETE
```

A implementação das operações é realizada diretamente através da API JDBC e comandos SQL.

---

## Tecnologias Utilizadas

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Banco de dados | PostgreSQL |
| Acesso a dados | JDBC |
| Driver | PostgreSQL JDBC Driver 42.7.13 |
| Build | Apache Maven |
| Testes | JUnit 6 |
| Controle de versão | Git / GitHub |
| Licença | MIT |

---

## Arquitetura

O projeto segue uma organização baseada na separação de responsabilidades entre o domínio da aplicação, persistência e configuração de acesso ao banco.

Conceitualmente, o fluxo de persistência segue:

```text
Aplicação
   │
   ▼
Camada de domínio / regras
   │
   ▼
DAO / Persistência
   │
   ▼
JDBC
   │
   ▼
PostgreSQL
```

Essa abordagem permite compreender de forma explícita o fluxo que normalmente fica abstraído por frameworks como Spring Data JPA.

---

## Estrutura do Projeto

A estrutura principal do projeto é organizada da seguinte forma:

```text
biblioteca-jdbc-postgres/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   │
│   │   └── resources/
│   │       └── ...
│   │
│   └── test/
│       └── ...
│
├── pom.xml
├── .gitignore
├── LICENSE
└── README.md
```

### Principais responsabilidades

```text
src/main/java/
│
├── model/
│   └── Objetos que representam o domínio da biblioteca
│
├── dao/
│   └── Acesso e persistência dos dados
│
├── config/
│   └── Configuração da conexão com o PostgreSQL
│
└── util/
    └── Utilitários e validações
```

> Os nomes e responsabilidades acima representam a organização conceitual da aplicação. A implementação pode evoluir conforme novas funcionalidades forem adicionadas.

---

## Como funciona o acesso ao banco

O acesso ao PostgreSQL é realizado através do JDBC.

O fluxo básico é:

```text
Java
  │
  │ DriverManager
  ▼
Connection
  │
  │ PreparedStatement
  ▼
PostgreSQL
  │
  │ ResultSet
  ▼
Objetos Java
```

Uma operação de leitura, por exemplo, segue conceitualmente:

```java
Connection
    ↓
PreparedStatement
    ↓
executeQuery()
    ↓
ResultSet
    ↓
Mapeamento para objeto Java
```

Para operações de escrita:

```java
Connection
    ↓
PreparedStatement
    ↓
executeUpdate()
    ↓
Linhas afetadas
```

---

## Pré-requisitos

Antes de executar o projeto, instale:

- **JDK 21**
- **PostgreSQL**
- **Apache Maven**
- **Git**

Verifique as instalações:

```bash
java -version
```

```bash
mvn -version
```

```bash
psql --version
```

---

## Como Executar

### 1. Clonar o repositório

```bash
git clone https://github.com/RMoranDev/biblioteca-jdbc-postgres.git
```

Entre no diretório:

```bash
cd biblioteca-jdbc-postgres
```

### 2. Criar o banco de dados

No PostgreSQL, crie uma base para o projeto:

```sql
CREATE DATABASE biblioteca;
```

O script de criação das tabelas e estruturas do banco está disponível em:

```text
src/main/resources/schema.sql
```

Após criar o banco, conecte-se a ele:

```bash
psql -U postgres -d biblioteca
```

### 3. Configurar a conexão

As credenciais do banco devem ficar em um arquivo de configuração local e **não devem ser versionadas**.

Exemplo:

```properties
db.url=jdbc:postgresql://localhost:5432/biblioteca
db.user=postgres
db.password=sua_senha
```

### 4. Criar as tabelas

Execute o script SQL correspondente ao banco da aplicação.

Exemplo:

```bash
psql -U postgres -d biblioteca -f schema.sql
```

Caso esteja utilizando uma ferramenta gráfica, como pgAdmin, o mesmo script pode ser executado diretamente pelo editor SQL.

---

### 5. Compilar o projeto

Utilize o Maven:

```bash
mvn clean compile
```

Para gerar o pacote:

```bash
mvn clean package
```

---

### 6. Executar a aplicação

A aplicação pode ser executada pela classe principal configurada no projeto através da sua IDE.

Exemplo utilizando IntelliJ IDEA:

```text
Run
└── Classe principal da aplicação
```

Também é possível configurar o Maven para executar a classe principal diretamente pelo terminal.

---

## Configuração do PostgreSQL

A URL JDBC utilizada pela aplicação segue o padrão:

```text
jdbc:postgresql://HOST:PORT/DATABASE
```

Exemplo local:

```text
jdbc:postgresql://localhost:5432/biblioteca
```

Onde:

| Parâmetro | Exemplo |
|---|---|
| Host | `localhost` |
| Porta | `5432` |
| Database | `biblioteca` |
| Usuário | `postgres` |
| Driver | `org.postgresql.Driver` |

---

## Scripts SQL

O modelo de dados de uma aplicação de biblioteca normalmente envolve entidades relacionadas, como livros, usuários, exemplares e empréstimos.

Um exemplo simplificado de estrutura é:

```sql
CREATE TABLE livro (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    ano_publicacao INTEGER
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL
);

CREATE TABLE exemplar (
    id BIGSERIAL PRIMARY KEY,
    livro_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,

    CONSTRAINT fk_exemplar_livro
        FOREIGN KEY (livro_id)
        REFERENCES livro(id)
);

CREATE TABLE emprestimo (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    exemplar_id BIGINT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE,

    CONSTRAINT fk_emprestimo_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id),

    CONSTRAINT fk_emprestimo_exemplar
        FOREIGN KEY (exemplar_id)
        REFERENCES exemplar(id)
);
```

### Relacionamentos

```text
USUARIO
   │
   │ 1:N
   ▼
EMPRESTIMO
   │
   │ N:1
   ▼
EXEMPLAR
   │
   │ N:1
   ▼
LIVRO
```

> O SQL acima é um modelo de referência para representar o domínio de uma biblioteca. O schema efetivamente utilizado pelo projeto deve ser considerado a fonte de verdade da aplicação.

---

## JDBC na prática

Um dos objetivos deste projeto é trabalhar diretamente com a API JDBC e compreender o papel de cada componente.

### Connection

Responsável por representar a conexão entre a aplicação e o banco de dados.

```java
Connection connection = DriverManager.getConnection(
    url,
    user,
    password
);
```

### PreparedStatement

Permite enviar comandos SQL parametrizados:

```java
String sql = """
    SELECT *
    FROM livro
    WHERE id = ?
    """;

PreparedStatement ps = connection.prepareStatement(sql);

ps.setLong(1, id);
```

### ResultSet

Representa os resultados retornados por uma consulta:

```java
ResultSet rs = ps.executeQuery();

while (rs.next()) {
    // leitura dos dados
}
```

Essa abordagem ajuda a compreender os mecanismos de persistência que frameworks de maior nível posteriormente abstraem.

---

## Boas práticas aplicadas

O projeto também busca aplicar práticas importantes de desenvolvimento Java:

- Uso de `PreparedStatement` para consultas parametrizadas;
- Separação da configuração do banco do código-fonte;
- Tratamento de `SQLException`;
- Uso de `try-with-resources`;
- Organização das responsabilidades em classes distintas;
- Validação de dados;
- Utilização de Maven para gerenciamento de dependências;
- Controle de versão através do Git;
- Proteção de credenciais através do `.gitignore`.

---

## Testes

O projeto possui **JUnit** como dependência de testes.

Para executar os testes:

```bash
mvn test
```

Para executar o processo completo de build:

```bash
mvn clean test
```

---

## Segurança

Nunca faça commit de arquivos contendo:

```text
DB_PASSWORD
API_KEY
TOKEN
PRIVATE_KEY
DATABASE_PASSWORD
```

Em ambientes de produção, prefira utilizar variáveis de ambiente ou um sistema dedicado de gerenciamento de segredos.

---

## Melhorias Futuras

Este projeto pode evoluir posteriormente para uma arquitetura backend mais próxima de aplicações utilizadas em ambientes profissionais.

Possíveis evoluções:

- [ ] API REST com Spring Boot;
- [ ] Spring Data JPA / Hibernate;
- [ ] DTOs;
- [ ] Bean Validation;
- [ ] Tratamento global de exceções;
- [ ] Spring Security;
- [ ] Autenticação com JWT;
- [ ] Testes unitários mais abrangentes;
- [ ] Testes de integração;
- [ ] Docker;
- [ ] Docker Compose;
- [ ] PostgreSQL em container;
- [ ] Documentação da API com OpenAPI/Swagger;
- [ ] CI/CD com GitHub Actions.

---

## Contribuição

Contribuições são bem-vindas.

Para contribuir:

```bash
git fork
```

Crie uma branch:

```bash
git checkout -b feature/minha-feature
```

Faça suas alterações:

```bash
git add .
git commit -m "feat: adiciona nova funcionalidade"
```

Envie para o seu fork:

```bash
git push origin feature/minha-feature
```

Depois, abra um **Pull Request** no repositório.

Para alterações maiores, recomenda-se abrir uma Issue antes para discutir a proposta.

---

## Licença

Este projeto está licenciado sob a **MIT License**.

Consulte o arquivo [LICENSE](LICENSE) para obter o texto completo da licença.

---

## Autor

**RMoranDev**

GitHub: [@RMoranDev](https://github.com/RMoranDev)

---

## Projeto

Repositório:

[github.com/RMoranDev/biblioteca-jdbc-postgres](https://github.com/RMoranDev/biblioteca-jdbc-postgres)

> Projeto desenvolvido como parte do processo de aprendizado e aprofundamento em **Java, JDBC, SQL, PostgreSQL e persistência de dados**.
