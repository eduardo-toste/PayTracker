# 💸 PayTracker API

**PayTracker** é uma API REST desenvolvida em **Java 23** com **Spring Boot** para gerenciamento de finanças pessoais. Permite aos usuários registrarem receitas e despesas, fornecendo alertas automáticos por e-mail dois dias antes do vencimento das transações.

A aplicação utiliza autenticação baseada em tokens JWT, gerenciamento eficiente do banco de dados com Flyway e segurança robusta através do Spring Security.

![PayTracker Preview](https://github.com/user-attachments/assets/02dc4fa5-1dc8-4723-8fb8-3d761e8095bd)



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
com.eduardo.paytracker
├── config
│   ├── scheduler     # Configuração de agendamento
│   └── security      # Segurança e filtros JWT
├── controller        # Controladores REST
├── dto               # Data Transfer Objects (DTOs)
├── exception         # Exceções personalizadas
├── model             # Entidades JPA e modelos de domínio
│   └── enums         # Enumerações
├── repository        # Repositórios Spring Data JPA
├── scheduler         # Tarefas agendadas
├── services          # Lógica de negócio e envio de e-mails
├── utils             # Métodos utilitários
└── resources         # Scripts SQL para migração (Flyway)
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
