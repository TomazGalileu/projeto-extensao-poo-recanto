# 🚀 Como Executar o Projeto - Guia Completo

Este guia vai te ensinar passo a passo como rodar o projeto **Recanto do Sagrado Coração** no seu computador, mesmo se você nunca usou Docker ou MySQL antes.

---

## 📋 O que você vai precisar

Antes de começar, você precisa instalar 3 programas no seu computador:

1. **Java 17** (ou superior)
2. **Docker Desktop**
3. **Git** (opcional, mas recomendado)

---

## 📥 PASSO 1: Instalar o Java 17

### Windows

1. **Baixar o Java:**
   - Acesse: https://www.oracle.com/java/technologies/downloads/
   - Clique em **"Windows"**
   - Baixe o instalador **"x64 Installer"**

2. **Instalar:**
   - Execute o arquivo baixado (ex: `jdk-17_windows-x64_bin.exe`)
   - Clique em **"Next"** até finalizar
   - Deixe todas as opções padrão

3. **Verificar se instalou:**
   - Abra o **Prompt de Comando** (CMD) do Windows
   - Digite: `java -version`
   - Deve aparecer algo como: `java version "17.0.x"`

✅ **Java instalado com sucesso!**

---

## 🐳 PASSO 2: Instalar o Docker Desktop

### Windows

1. **Baixar o Docker Desktop:**
   - Acesse: https://www.docker.com/products/docker-desktop/
   - Clique em **"Download for Windows"**
   - Aguarde o download (arquivo grande, ~500MB)

2. **Instalar:**
   - Execute o arquivo `Docker Desktop Installer.exe`
   - Marque a opção **"Use WSL 2 instead of Hyper-V"** (recomendado)
   - Clique em **"Ok"**
   - Aguarde a instalação (pode demorar alguns minutos)

3. **Reiniciar o computador:**
   - Após a instalação, o Windows vai pedir para reiniciar
   - **IMPORTANTE:** Reinicie o computador!

4. **Abrir o Docker Desktop:**
   - Após reiniciar, procure por **"Docker Desktop"** no menu Iniciar
   - Abra o programa
   - Aguarde até aparecer **"Docker Desktop is running"** (ícone verde na bandeja)
   - Pode demorar 1-2 minutos na primeira vez

5. **Verificar se está funcionando:**
   - Abra o **Prompt de Comando** (CMD)
   - Digite: `docker --version`
   - Deve aparecer algo como: `Docker version 29.0.1`

✅ **Docker instalado com sucesso!**

---

## 📂 PASSO 3: Obter o Projeto

1. **Copie a pasta do projeto** para o seu computador
   - Coloque em um local fácil de acessar (ex: `C:\Users\SeuUsuario\Desktop\Projeto-main`)

2. **Abra o Prompt de Comando** e navegue até a pasta:
   ```cmd
   cd C:\Users\SeuUsuario\Desktop\Projeto-main
   ```
   
   ⚠️ **Importante:** Substitua `SeuUsuario` pelo seu nome de usuário do Windows

---

## 🗄️ PASSO 4: Iniciar o Banco de Dados MySQL

Agora vamos criar o banco de dados usando Docker. É muito simples!

1. **Certifique-se que o Docker Desktop está rodando:**
   - Veja se o ícone do Docker na bandeja está verde
   - Se não estiver, abra o Docker Desktop e aguarde

2. **Abrir o Prompt de Comando na pasta do projeto:**
   - Navegue até a pasta `Projeto-main`:
     ```cmd
     cd C:\Users\SeuUsuario\Desktop\Projeto-main
     ```

3. **Iniciar o MySQL:**
   - Digite o comando:
     ```cmd
     docker compose up -d
     ```
   - Aguarde o download da imagem do MySQL (primeira vez demora ~2 minutos)
   - Quando terminar, deve aparecer: `✔ Container projeto-mysql Started`

4. **Verificar se o MySQL está rodando:**
   ```cmd
   docker ps
   ```
   - Deve aparecer uma linha com `projeto-mysql` e status `Up`

✅ **MySQL rodando com sucesso!**

---

## 🔧 PASSO 5: Configurar o Usuário do Banco de Dados

Este passo é **MUITO IMPORTANTE** para o projeto funcionar!

1. **Criar o usuário no MySQL:**
   - No Prompt de Comando, digite:
   ```cmd
   docker exec projeto-mysql mysql -uroot -p123456 -e "CREATE USER IF NOT EXISTS 'app'@'localhost' IDENTIFIED WITH mysql_native_password BY 'app123'; GRANT ALL PRIVILEGES ON *.* TO 'app'@'localhost' WITH GRANT OPTION; FLUSH PRIVILEGES;"
   ```

2. **Verificar se o usuário foi criado:**
   ```cmd
   docker exec projeto-mysql mysql -uroot -p123456 -e "SELECT user, host FROM mysql.user WHERE user='app';"
   ```
   - Deve aparecer:
     ```
     user    host
     app     %
     app     localhost
     ```

✅ **Usuário configurado com sucesso!**

---

## ▶️ PASSO 6: Executar o Projeto

Agora vamos rodar a aplicação!

1. **Certifique-se que está na pasta do projeto:**
   ```cmd
   cd C:\Users\SeuUsuario\Desktop\Projeto-main
   ```

2. **Executar o projeto:**
   - **Windows:**
     ```cmd
     mvnw.cmd spring-boot:run
     ```

3. **Aguardar a aplicação iniciar:**
   - Vai aparecer várias mensagens no console
   - Aguarde até ver: `Preenchimento do banco de dados concluído.`
   - Isso significa que está tudo pronto!
   - **Uma janela do sistema vai abrir automaticamente** com a tela de login

4. **Se a janela não abrir:**
   - Verifique se não está minimizada na barra de tarefas
   - Procure por uma janela chamada "Tela 2 - Geral" ou "Login"

✅ **Aplicação rodando com sucesso!**

---

## 🔐 PASSO 7: Fazer Login no Sistema

Quando a tela de login aparecer:

### Usuários de Teste Disponíveis:

| Email | Senha | Nome |
|-------|-------|------|
| `ana.silva@saude.com` | `1234` | Ana Paula da Silva |
| `joao.lima@saude.com` | `abcd` | João Carlos Lima |
| `mariana.costa@saude.com` | `pass` | Mariana Costa Oliveira |
| `felipe.almeida@saude.com` | `4321` | Felipe Gomes de Almeida |
| `larissa.oliveira@saude.com` | `qwer` | Larissa Moura de Oliveira |

**Exemplo:**
1. No campo **"Email"**, digite: `ana.silva@saude.com`
2. No campo **"Senha"**, digite: `1234`
3. Clique em **"Logar"**

✅ **Você está dentro do sistema!**

---

## 🎯 Funcionalidades Disponíveis

Após fazer login, você terá acesso ao **Painel Administrativo** com os seguintes módulos:

### ✅ Módulos Funcionais:
- **Pacientes** - Cadastrar e visualizar pacientes (idosas)
- **Consultas** - Agendar e visualizar consultas médicas

### 🚧 Módulos em Desenvolvimento:
- Família
- Documentos
- Eventos Sentinelas
- Prontuários
- Vacinas
- Relatórios

---

## 🛑 Como Parar o Projeto

### Parar a Aplicação Java:
- No Prompt de Comando onde o projeto está rodando, pressione: `Ctrl + C`

### Parar o MySQL (Docker):
```cmd
cd C:\Users\SeuUsuario\Desktop\Projeto-main
docker compose down
```

### Parar o Docker Desktop:
- Clique com botão direito no ícone do Docker na bandeja
- Selecione **"Quit Docker Desktop"**

---

## 🔄 Como Executar Novamente (Próximas Vezes)

Nas próximas vezes que for usar o projeto, os passos são mais simples:

1. **Abrir o Docker Desktop** (aguardar ficar verde)

2. **Iniciar o MySQL:**
   ```cmd
   cd C:\Users\SeuUsuario\Desktop\Projeto-main
   docker compose up -d
   ```

3. **Executar o projeto:**
   ```cmd
   mvnw.cmd spring-boot:run
   ```

4. **Fazer login** com um dos emails de teste

Pronto! 🎉

---

## ❓ Problemas Comuns e Soluções

### Problema 1: "Docker não está rodando"
**Solução:**
- Abra o Docker Desktop
- Aguarde até o ícone ficar verde na bandeja
- Tente novamente

### Problema 2: "Porta 3306 já está em uso"
**Solução:**
- Você já tem outro MySQL rodando
- Pare o outro MySQL ou mude a porta no `docker-compose.yml`

### Problema 3: "Access denied for user"
**Solução:**
- Execute novamente o comando do PASSO 5 para criar o usuário
- Certifique-se de copiar o comando completo

### Problema 4: "Email inválido" na tela de login
**Solução:**
- Você precisa digitar um **email completo**, não apenas "admin"
- Use um dos emails da tabela acima (ex: `ana.silva@saude.com`)

### Problema 5: "mvnw.cmd não é reconhecido"
**Solução:**
- Certifique-se que está na pasta correta do projeto
- Use o comando: `cd C:\Users\SeuUsuario\Desktop\Projeto-main`

### Problema 6: A janela não abre
**Solução:**
- Verifique se não está minimizada
- Procure na barra de tarefas
- Verifique se não há erros no console

---

## 📞 Precisa de Ajuda?

Se encontrar algum problema:

1. **Verifique os logs no console** - geralmente a mensagem de erro explica o problema
2. **Certifique-se que seguiu todos os passos** na ordem correta
3. **Verifique se o Docker Desktop está rodando** (ícone verde)
4. **Reinicie o computador** - às vezes resolve problemas estranhos

---

## 📊 Dados de Teste Incluídos

O sistema já vem com dados de exemplo para você testar:

- **9 Pacientes** (idosas) cadastradas
- **5 Responsáveis de Saúde** (usuários do sistema)
- **5 Consultas** agendadas

Você pode visualizar, editar e adicionar novos registros!

---

## 🔒 Informações Técnicas

Para desenvolvedores ou curiosos:

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3.5.0
- **Interface:** Java Swing (Desktop)
- **Banco de Dados:** MySQL 8.0 (via Docker)
- **ORM:** Hibernate/JPA
- **Porta da Aplicação:** 8080
- **Porta do MySQL:** 3306

### Credenciais do Banco de Dados:
- **Host:** localhost (127.0.0.1)
- **Porta:** 3306
- **Database:** projetoextensao
- **Usuário:** app
- **Senha:** app123

---

## 📝 Notas Importantes

⚠️ **Este é um projeto acadêmico/educacional**
- As senhas estão em texto plano (não use em produção!)
- Não há criptografia de dados sensíveis
- É recomendado usar apenas em ambiente de desenvolvimento

✅ **Dados são persistentes**
- Os dados ficam salvos no Docker
- Mesmo fechando o projeto, os dados permanecem
- Para limpar tudo: `docker compose down -v`

---

**Última atualização:** 21 de Novembro de 2025  
**Versão:** 1.0

**Bom uso! 🚀**
