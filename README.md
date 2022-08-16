# avaliacao-full-stack
Avaliação para vaga de desenvolvedor Java full-stack

[![](https://img.shields.io/discord/677642178083946580?color=%23768ACF&label=Discord)](https://discord.gg/U8NcPcHxW3)

### NOTAS:
Até o presente momento, fui capaz de em pouco mais de 24 horas criar o backend dessa aplicação spring, com spring boot; 

- 3 domínios `User`, `Account` e `Transaction`, um usuário pode ter várias contas e agendar várias transações
- criei algumas exceções personalizadas para manuseio de mensagens de erro
- implementei a regra para o cálculo das taxas a serem pagas de acordo com o que se pede
- criei até aqui, alguns testes unitários para os repositórios e serviços

### COMO TESTAR:

GET listar todas as contas: `localhost:8080/api/accounts`

---
GET encontrar conta por id ex 1: `localhost:8080/api/accounts/1`

---
GET encontrar conta por número ex 123456: `localhost:8080/api/accounts/find/123456`

---
POST criar conta: `localhost:8080/api/accounts/create`
```
{
    "name": "Fulano",
    "email": "fulano@gmail.com",
    "ssn": "123.456.789-01"
}
```
---
RESPONSE:
```
{
    "id": 1,
    "number": "131051",
    "balance": 10000.0,
    "user": {
        "id": 1,
        "name": "Fulano",
        "email": "fulano@gmail.com",
        "ssn": "123.456.789-01"
    }
}
```
---
POST agendar transferência: `localhost:8080/api/transaction/schedule-transfer`
```
{
    "depositorAccountNumber": "123456",
    "receiverAccountNumber": "654321",
    "amount": 1.0,
    "type": "A",
    "transferDate": "16/08/2022 08:35:00"
}
```
---
RESPONSE:
```
{
    "id": 1,
    "depositor": {
        "id": 1,
        "number": "131051",
        "balance": 9894.000,
        "user": {
            "id": 1,
            "name": "Fulano",
            "email": "fulano@gmail.com",
            "ssn": "123.456.789-01"
        }
    },
    "receiver": {
        "id": 2,
        "number": "884027",
        "balance": 10000.00,
        "user": {
            "id": 2,
            "name": "Thiago",
            "email": "thiago@gmail.com",
            "ssn": "124.456.789-01"
        }
    },
    "amount": 100.0,
    "tax": 6.000,
    "type": "A",
    "createdAt": "2022-08-16T08:34:21.9282166",
    "transferDate": "2022-08-16T08:35:00"
}
```
---
GET encontrar transação por número da conta ex: `localhost:8080/api/transaction/ofaccount/123456`
```
[
    {
        "id": 1,
        "number": "131051",
        "balance": 9894.00,
        "user": {
            "id": 1,
            "name": "Fulano",
            "email": "fulano@gmail.com",
            "ssn": "123.456.789-01"
        }
    },
    {
        "id": 2,
        "number": "884027",
        "balance": 10000.00,
        "user": {
            "id": 2,
            "name": "Thiago",
            "email": "thiago@gmail.com",
            "ssn": "124.456.789-01"
        }
    }
]
```
---
GET encontrar transação por número da conta ex: `localhost:8080/api/transaction/ofaccount/123456`

às 08:35 será efetuado o depósito
```
[
    {
        "id": 1,
        "number": "131051",
        "balance": 9894.00,
        "user": {
            "id": 1,
            "name": "Fulano",
            "email": "fulano@gmail.com",
            "ssn": "123.456.789-01"
        }
    },
    {
        "id": 2,
        "number": "884027",
        "balance": 10100.00, <<<<<<<<<<
        "user": {
            "id": 2,
            "name": "Thiago",
            "email": "thiago@gmail.com",
            "ssn": "124.456.789-01"
        }
    }
]
```
#### OBS: Dado meu tempo apertado e outros testes que devo fazer, não pude desenvolver a parte de segurança, documentação, métricas e não pude iniciar a parte do frontend, caso tenha interesse real em me contratar, posso concluir esse projeto posteriormente.
