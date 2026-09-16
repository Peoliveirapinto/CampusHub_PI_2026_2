# PI S6 - CampusHub

## O projeto
### Introdução
Esse software é um projeto desenvolvido por alunos do Instituto Mauá de Tecnologia (IMT) para a disciplina de Projeto Integrador Interdisciplinar.
### Funcionalidades
Esse projeto tem como objetivo ser uma plataforma que oferece serviços úteis para alunos universitários. Inicialmente as features são as seguintes:
- Cadastro e login
- Criação de perfil com seus dados
- Sistema de pareamento que busca parear alunos que são considerados compatíveis entre si para que se conectem visando se tornarem colegas de grupo para trabalhos ou projetos da faculdade

## Tecnologias Utilizadas
- **Frontend**: [React](https://react.dev/)
- **Backend**: [Java](https://www.java.com) + [Spring](https://spring.io/)
- **Banco de Dados**: [PostgreSQL](https://www.postgresql.org/)

## Como utilizar o projeto
### Pré-requisitos
1. Ter o **Java JDK 21** instalado
2. Ter o **Git** instalado
3. Ter o acesso a um banco de dados **PostgreSQL**, podendo ele ser na nuvem ou local

### 1. Clonagem do repositório
- Inicialmente clone o repositório usando o seguinte comando:

```bash
git clone https://github.com/Peoliveirapinto/CampusHub_PI_2026_2.git
```

### 2. Frontend
- Ainda em desenvolvimento

### 3. Backend
1. Com um terminal já aberto na pasta do projeto navegue para a pasta do backend usando o seguinte comando:

```bash
cd backend
```

2. Execute um dos seguintes comandos (baseado no seu sistema operacional) para criar o arquivo ``` application-local.properties ``` baseado no ``` application-local.properties.example ``` e então troque os valores das variáveis de configuração no arquivo ``` application-local.properties ``` para os valores que serão usados no projeto

```bash
# No Windows
copy src/main/resources/application-local.properties.example src/main/resources/application-local.properties

# No macOS / Linux
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

3. Inicie a aplicação do backend executando o seguinte comando:
```bash
./gradlew bootRun
```

### 4. Testes
Para rodar os testes do backend entre na pasta do backend e execute o comando de testes
```bash
# Entra na pasta do backend
cd backend

# Executa os testes
./gradlew test
```