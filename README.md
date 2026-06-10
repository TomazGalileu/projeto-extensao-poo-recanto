# Sistema de Gerenciamento para Instituição de Longa Permanência para Idosos

Aplicação desktop desenvolvida em Java para auxiliar o gerenciamento de informações da Casa de Repouso de Idosas Recanto do Sagrado Coração.

O sistema foi desenvolvido como projeto de extensão da disciplina de Programação Orientada a Objetos do curso de Análise e Desenvolvimento de Sistemas da Unichristus.

## Objetivo

O projeto tem como objetivo centralizar e organizar informações relacionadas às residentes da instituição, reduzindo a dependência de registros manuais e facilitando o acompanhamento de dados pessoais e de saúde.

A aplicação permite o gerenciamento de pacientes, consultas, prontuários médicos, prescrições, exames, vacinas, eventos sentinelas e relatórios.

## Funcionalidades

### Cadastro de pacientes

- Cadastro de residentes
- Consulta de dados cadastrados
- Edição de informações
- Controle de pacientes ativos e inativos
- Filtro por situação cadastral

### Consultas médicas

- Agendamento de consultas
- Associação da consulta a uma paciente
- Registro do profissional responsável
- Registro do tipo e motivo da consulta
- Registro de diagnóstico e anotações
- Visualização de múltiplas consultas por paciente

### Prontuário médico

- Associação entre paciente e prontuário
- Registro de consultas
- Registro de prescrições
- Registro de exames
- Histórico de internações
- Histórico de vacinação
- Geração de resumo do histórico médico

### Eventos sentinelas

- Cadastro de eventos sentinelas e agravos institucionais
- Associação do evento a uma paciente
- Registro da data ou período do ocorrido
- Consulta dos eventos cadastrados

### Controle de vacinas

- Registro da vacina aplicada
- Registro da data de aplicação
- Associação da vacina à paciente

### Relatórios

- Geração de relatório individual por paciente
- Exibição de informações pessoais
- Exibição de medicamentos prescritos
- Exibição do histórico de vacinas
- Cálculo do percentual de vacinação por vacina
- Cálculo do percentual de pacientes com eventos sentinelas

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Java Swing
- MySQL
- Docker
- Docker Compose
- Maven
- Swagger / OpenAPI
- Git
- GitHub

## Pré-requisitos

Antes de executar o projeto, instale:

- [Java JDK 17 ou superior](https://www.oracle.com/java/technologies/downloads/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/downloads)

Não é necessário instalar o Maven manualmente, pois o projeto utiliza o Maven Wrapper.

## Como executar o projeto

### 1. Clonar o repositório

Abra o PowerShell ou o terminal e execute:

```bash
git clone https://github.com/TomazGalileu/projeto-extensao-poo-recanto.git
```

Substitua `URL_DO_REPOSITORIO` pelo endereço do repositório no GitHub.

Em seguida, entre na pasta do projeto:

```bash
cd lp2-java-unichristus
```

### 2. Iniciar o banco de dados

Certifique-se de que o Docker Desktop esteja aberto.

Na pasta principal do projeto, execute:

```bash
docker compose up -d
```

Esse comando cria e inicia o contêiner do MySQL utilizado pela aplicação.

Para verificar se o contêiner está em execução, utilize:

```bash
docker ps
```

### 3. Compilar o projeto

No Windows PowerShell, execute:

```powershell
.\mvnw.cmd clean compile
```

Aguarde até que a mensagem `BUILD SUCCESS` seja exibida.

### 4. Executar a aplicação

Após a compilação, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Aguarde a inicialização do Spring Boot. A interface desktop será aberta automaticamente.

## Comandos úteis

### Iniciar o banco de dados

```bash
docker compose up -d
```

### Encerrar o banco de dados

```bash
docker compose down
```

### Verificar os contêineres em execução

```bash
docker ps
```

### Compilar o projeto

```powershell
.\mvnw.cmd clean compile
```

### Executar a aplicação

```powershell
.\mvnw.cmd spring-boot:run
```

## Documentação da API

Com a aplicação em execução, a documentação dos endpoints pode ser acessada pelo Swagger no navegador:

```text
http://localhost:8080/swagger-ui/index.html
```

## Estrutura do projeto

```text
src/main/java/com/ProjetoExtensao/Projeto
├── Config
├── infra
├── models
├── repositorios
├── servicos
└── view
```

### Camadas principais

| Camada | Responsabilidade |
|---|---|
| `models` | Entidades da aplicação |
| `repositorios` | Comunicação com o banco de dados |
| `servicos` | Regras de negócio e navegação |
| `view` | Telas desenvolvidas com Java Swing |
| `infra` | Classes utilitárias e componentes auxiliares |
| `Config` | Configurações gerais do projeto |

## Entidades principais

Entre as entidades utilizadas pelo sistema, destacam-se:

- Paciente
- Consulta
- Prontuário médico
- Prescrição
- Exame
- Vacina
- Evento sentinela

## Contexto do projeto

Instituições de longa permanência para idosos precisam lidar com informações relacionadas à saúde, ao histórico clínico e à rotina das residentes.

Quando esses dados estão dispersos em documentos físicos, planilhas ou sistemas não integrados, podem ocorrer erros, retrabalho e perda de informações relevantes.

O sistema busca centralizar esses dados em uma única aplicação, facilitando o acompanhamento das residentes e apoiando as atividades administrativas da instituição.

## Melhorias futuras

- Aprimoramento da interface gráfica
- Expansão dos relatórios
- Criação de níveis de acesso por usuário
- Inclusão de testes automatizados
- Exportação de relatórios em PDF
- Ampliação das validações de dados
- Criação de novos filtros de busca

## Equipe

- Tomaz Galileu Pessoa Nogueira Silva do Nascimento
- Maria Calara Sousa Freitas
- Magnum do Vale Freire

## Licença

Projeto acadêmico desenvolvido para fins educacionais.