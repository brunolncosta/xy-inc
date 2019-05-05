# XY - INC Challenger

A code challenger based on spatial queries.

## Stack:

- **Maven** - Gerenciamento de dependências, Build e integrações (docker hub, testes unitário e de integração);
- **Sprint Boot** - Para criação da API Rest, injeção de dependências e suporte e testes de integração;
- **Mongo-DB** - Baso NoSql utilizado para dar suporte a consultas espaciais;
- **Querysql** - Para dar suporte consultas complexas;
- **Junit 5** - Utilizado nos testes de integração e unitários;
- **Rest Assured** - Utilizado para validar a API Rest nos testes de integração (teste de ponta a ponta);
- **Docker** - Para facilitar o deploy da APP e o desenvolvimento;
- **Swagger** - Utilizado para http client/documentação da API;

## Decisões arquiteturais:

- **Suporte a consulta Espaciais** - Para esse ponto foi considerado o seguinte [artigo](https://scholarworks.umass.edu/cgi/viewcontent.cgi?article=1088&context=foss4g), 
que indica ganhos consideráveis de performance da solução Mongo-db + GeoJson em relação a uma solução relacionais Postgis/Postgres + hibernate;

- **Testes de Integração** - Como a aplicação tem como foco principal consultas espaciais, as quais estão diretamente ligadas ao banco de dados, foi dado prioridade aos testes de integração.
Contudo, caso houvesse cálculos ou processamento de dados na aplicação em si, os testes unitário teria sido mais trabalhado.

- **Arquitetura de Software** - A arquitetura do projeto é modelo **bastante simplificado**, até pela natureza do projeto, baseado no padrão arquitetural DDD.

## A API

A possui basicamente os seguintes end-points:

- POST /poi/pontos-de-interesse - Cria um novo ponto de Interesse;
- GET /poi/pontos-de-interesse - Busca pontos de interesse através de QueryDsl (Exemplo: http://localhost:8080/poi/pontos-de-interesse?longitude=12)
- GET /poi/pontos-de-interesse/all - Busca todas os pontos de interesse cadastrados de forma paginada;
- GET /poi/pontos-de-interesse/near - Busca todos os pontos de interesse dado um determinado ponto de referencia em um determinado raio de distancia em metros;

## Como Excutar:

**Pre-requisitos**: Docker, Docker-compose e Git instalados;

```bash
$ git clone https://github.com/brunolncosta/xy-inc.git

$ cd xy-inc

$ docker-compose up -d
```

Alternativamente o projeto pode ser rodado através do Maven:

```bash
$ git clone https://github.com/brunolncosta/xy-inc.git

$ cd xy-inc

$ docker-compose up -d mongo

$ ./mvnw spring-boot:run 
```

#### A API deverá está disponível na URL: http://localhost:8080/poi 

#### Documentação/Client da API estará disponível na URL: http://localhost:8080/poi/swagger-ui.html;

## Como Executar Testes de Unitários:

```bash
$ ./mvnw test
```

## Como Executar Testes de Integração:

**Pre-requisitos**: O banco Mongo deve está levantado;

```bash
$ docker-compose up -d mongo

$ ./mvnw integration-test -P integration
```