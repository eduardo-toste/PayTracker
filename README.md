# 💸 PayTracker API

PayTracker é uma API para controle financeiro pessoal desenvolvida em **Java 23** com **Spring Boot**. A aplicação permite que usuários cadastrem suas receitas e despesas, com envio automático de **alertas por e-mail dois dias antes do vencimento** de cada transação. Possui autenticação via **JWT**, versionamento de banco com **Flyway**, envio de e-mails com **JavaMailSender** e segurança com **Spring Security**.

O projeto segue boas práticas de arquitetura em camadas, tratamento global de exceções e validação robusta de dados. A estrutura é modular, visando a escalabilidade e manutenção contínua.

![image](https://github.com/user-attachments/assets/02dc4fa5-1dc8-4723-8fb8-3d761e8095bd)

---

## 🚀 Tecnologias Utilizadas

### Backend
- Java 23
- Spring Boot
- Spring Web
- Spring Security (JWT)
- Spring Data JPA
- Spring Scheduler (Agendamento de tarefas)

### Envio de E-mails
- JavaMailSender (Spring Boot Starter Mail)

### Banco de Dados
- PostgreSQL (via Docker)
- Flyway (Migração e versionamento)

### Testes
- JUnit 5 (Testes unitários)
- Mockito (Mock de dependências)
- Spring Boot Test

### Documentação
- OpenAPI (SpringDoc)

### Ferramentas e Build
- Maven
- Docker

---

## 📂 Estrutura do Projeto

```
com.eduardo.paytracker
│── config
│   ├── mail (Configuração de envio de e-mails)
│── controllers (Controladores da API)
│── dtos (Objetos de Transferência de Dados)
│── exceptions (Exceções personalizadas)
│── models (Modelos e Entidades do banco de dados)
│   ├── enums (Enums)
│── repositories (Interfaces de repositórios JPA)
├── security (Configuração de segurança e filtros)
│── schedulers (Agendadores com @Scheduled)
│── services (Regras de negócio e envio de e-mails)
│── utils (Funções auxiliares)
│── resources (Scripts SQL para migração do banco)
```

---

## 📌 Endpoints Disponíveis

### Autenticação
- `POST /auth/login` → Autentica usuário e gera token JWT
- `POST /auth/register` → Cadastra novo usuário

### Transações Financeiras
- `POST /transaction` → Cadastra nova transação
- `GET /transaction` → Lista todas as transações do usuário
- `GET /transaction/{id}` → Detalha uma transação
- `PATCH /transaction/{id}` → Atualiza dados parciais de uma transação
- `PUT /transaction/{id}` → Atualiza dados de uma transação
- `DELETE /transaction/{id}` → Remove uma transação

---

![image](https://github.com/user-attachments/assets/26d48313-2f77-4753-b316-250657575168)

---

## ⚙️ Configuração e Execução

### Pré-requisitos
Certifique-se de ter as seguintes dependências instaladas:
- Java 23
- Docker
- Maven

### Configuração do Banco de Dados
A aplicação utiliza um banco de dados PostgreSQL via Docker. Para configurá-lo, execute o seguinte comando:

```bash
docker run --name stockify-postgres -e POSTGRES_PASSWORD=root -e POSTGRES_DB=stockify -p 5432:5432 -d postgres:latest
```
Edite o arquivo `application.properties` com as credenciais do banco:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/stockify
spring.datasource.username=postgres
spring.datasource.password=123456
```

### Executando a Aplicação
1. Clone o repositório:
   ```sh
   git clone https://github.com/eduardo-toste/paytracker.git
   ```
2. Navegue até a pasta do projeto:
   ```sh
   cd paytracker
   ```
3. Execute o projeto com Maven:
   ```sh
   mvn spring-boot:run
   ```
4. Acesse a documentação da API via OpenAPI:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## 🤝 Contribuição
Se deseja contribuir com melhorias, faça um fork do repositório, crie uma branch e envie um pull request.
