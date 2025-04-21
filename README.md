# 💸 PayTracker API

**PayTracker** é uma API REST desenvolvida em **Java 23** com **Spring Boot** para gerenciamento de finanças pessoais. Permite aos usuários registrarem receitas e despesas, fornecendo alertas automáticos por e-mail dois dias antes do vencimento das transações.

A aplicação utiliza autenticação baseada em tokens JWT, gerenciamento eficiente do banco de dados com Flyway e segurança robusta através do Spring Security.

![PayTracker Preview](https://github.com/user-attachments/assets/02dc4fa5-1dc8-4723-8fb8-3d761e8095bd)

---

## 🚀 Tecnologias e Ferramentas

### Backend
- Java 23
- Spring Boot
- Spring Web
- Spring Security com JWT
- Spring Data JPA
- Spring Scheduler (agendamento de tarefas)

### Envio de E-mails
- JavaMailSender (Spring Boot Starter Mail)

### Banco de Dados
- PostgreSQL (Docker)
- H2 (Banco em memória usado nos testes)
- Flyway (migração e controle de versões)

### Testes
- JUnit 5
- Mockito
- Spring Boot Test

### Documentação
- OpenAPI (SpringDoc)

### Ferramentas e Deploy
- Maven
- Docker

---

## 📁 Estrutura do Projeto

```
src
├── main
│   ├── java
│   │   └── com.eduardo.paytracker
│   │       ├── config                # Configurações globais
│   │       │   ├── scheduler         # Agendamento de tarefas com @EnableScheduling
│   │       │   └── security          # Filtros JWT, SecurityConfig, TokenService
│   │       ├── controller            # REST Controllers (Transaction, Authentication)
│   │       ├── dto                   # DTOs (request/response) usados na API
│   │       ├── exception             # Exceções personalizadas e GlobalExceptionHandler
│   │       ├── model                 # Entidades JPA (Transaction, User)
│   │       │   └── enums             # Enums (Enumerations)
│   │       ├── repository            # Interfaces de persistência com Spring Data JPA
│   │       ├── scheduler             # Lógica de tarefas agendadas (ex: envio de e-mails)
│   │       ├── service               # Regras de negócio e serviços auxiliares
│   │       ├── utils                 # Utilitários diversos (ex: ErrorResponseUtil)
│   │       └── PaytrackerApplication.java  # Classe principal com @SpringBootApplication
│
│   └── resources
│       ├── db.migration              # Scripts SQL de migração com Flyway
│       ├── static                    # Arquivos estáticos (opcional)
│       ├── templates                 # Templates HTML (ex: e-mail de lembrete)
│       └── application.properties    # Configurações padrão da aplicação
│
├── test
│   ├── java
│   │   └── com.eduardo.paytracker
│   │       ├── controller            # Testes unitários de controllers (MockMvc + WebMvcTest)
│   │       ├── repository            # Testes de repositórios (opcional, com @DataJpaTest)
│   │       ├── service               # Testes de lógica de negócio (Mockito + JUnit)
│   │       └── PaytrackerApplicationTests.java  # Teste de carga de contexto (SpringBootTest)
│
│   └── resources
│       └── application-test.properties  # Configurações específicas para execução de testes

```

---

## 📌 Endpoints da API

### Autenticação
- `POST /auth/login` - Autenticação de usuários (gera token JWT)
- `POST /auth/register` - Cadastro de novos usuários

### Transações Financeiras
- `POST /transaction` - Cadastro de transações
- `GET /transaction` - Listagem das transações do usuário
- `GET /transaction/{id}` - Detalhamento de uma transação
- `PATCH /transaction/{id}` - Atualização parcial de transação
- `PUT /transaction/{id}` - Atualização completa de transação
- `DELETE /transaction/{id}` - Exclusão de transação

---

![image](https://github.com/user-attachments/assets/e9a67f60-9cf4-4236-a954-3895bde16086)

---

## ⚙️ Como Executar o Projeto

### 🔧 Pré-requisitos
- Java 23
- Docker
- Maven

### Configuração do Banco PostgreSQL

Execute o banco usando Docker:
```bash
docker run --name paytracker-postgres -e POSTGRES_PASSWORD=root -e POSTGRES_DB=paytracker -p 5432:5432 -d postgres:latest
```

Configure as propriedades em `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/paytracker
spring.datasource.username=postgres
spring.datasource.password=root
```

### Execução Local
1. Clone o repositório:
```sh
git clone https://github.com/eduardo-toste/paytracker.git
```

2. Navegue até a pasta do projeto:
```sh
cd paytracker
```

3. Execute a aplicação com Maven:
```sh
mvn spring-boot:run
```

4. Acesse a documentação OpenAPI:
```url
http://localhost:8080/swagger-ui.html
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:
- Faça um **fork** do repositório
- Crie uma nova **branch** para sua funcionalidade (`git checkout -b nova-feature`)
- Realize as alterações e envie um **pull request**

---
