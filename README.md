
# 💳 Sistema de Transações de Cartões - CPS

Projeto desenvolvido como parte de um desafio técnico proposto pela **W3 Automação e Sistemas**. O sistema é responsável por gerenciar o ciclo completo de operações com cartões de crédito e débito, aplicando práticas de **Domain-Driven Design (DDD)** e **Clean Architecture**.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem**: Java  
- **Framework**: Spring Boot  
- **Banco de Dados**: MySQL  
- **Build Tool**: Maven  
- **Execução**: Ambiente local (`mvn spring-boot:run`)

---

## 📦 Funcionalidades Implementadas (API REST)

1. ✅ **Solicitação de Cartão**  
2. ✅ **Aprovação de Cartão**  
3. ✅ **Ativação de Cartão**  
4. ✅ **Bloqueio Temporário**  
5. ✅ **Bloqueio Definitivo (Perda/Roubo)**  

---

## 🧾 Funcionalidade em Lote (Processamento Batch)

- ✅ **Bloqueio de Cartões em Lote**

O sistema processa arquivos `.IN` contendo múltiplos registros de bloqueios de cartões. Cada entrada é validada conforme as regras de negócio, e os registros inválidos são armazenados em um arquivo `.ERR` com seus respectivos códigos e descrições de erro.

### 📂 Exemplo de Arquivo de Entrada (.IN)

```
00 20250703 123456  
02 20250703 000001 0001 000000000000001 01 000123  
02 20250703 000002 0001 000000000000002 02 000123  
99 20250703 000003  
```

### ❌ Exemplo de Arquivo de Erro (.ERR)

```
02 20250703 000002 0600B Conta não pode ser bloqueada
```

---

## 📁 Estrutura do Projeto (Clean Architecture)

```
src/  
├── domain/  
│   ├── model/  
│   └── services/  
├── application/  
│   └── usecases/  
├── infrastructure/  
│   ├── persistence/  
│   └── batch/  
└── interfaces/  
    └── controllers/  
```

---

## ⚙️ Como Rodar Localmente

1. Clone o repositório:  
```bash
git clone https://github.com/seu-usuario/seu-repo.git  
cd seu-repo  
```

2. Configure o banco MySQL local (exemplo):  
```sql
CREATE DATABASE safecard_db;
```

3. Atualize o `application.properties` ou `application.yml` com suas credenciais do MySQL.  

4. Rode o projeto:  
```bash
mvn spring-boot:run
```

---

## 🧠 Conceitos Aplicados

### Domain-Driven Design (DDD)  
- Separação por contextos: Cartão, Cliente, Transação, Segurança  
- Uso de Aggregates, Entities e Value Objects  

### Clean Architecture  
- Camadas independentes e desacopladas  
- Inversão de dependência via interfaces  

---

## 🛠 Regras de Negócio Implementadas

- CPF com validação algorítmica  
- Idade mínima de 18 anos  
- Senhas de 6 dígitos (sem repetições ou sequências)  
- Cartão só pode ser ativado se estiver aprovado ou entregue  
- Bloqueio definitivo impede qualquer operação subsequente  

---

## 📌 Status do Projeto

- Etapa 1 (API REST) com 5 funcionalidades completas  
- Etapa 2 (Processamento em lote - Bloqueio) implementado  

---

## 📄 Licença

Este projeto é apenas para fins educacionais.
