# Projeto - Cidades ESGInteligentes

Este repositório contém uma API Java (Spring Boot) e um frontend estático simples (em `src/main/resources/static`) para cadastro de lotes e eventos, com persistência em PostgreSQL.

## Como executar localmente com Docker

Pré-requisitos:
- Docker + Docker Compose

Passo a passo:
1. Subir banco + aplicação:
   - `docker compose up -d --build`
2. Acompanhar logs da aplicação:
   - `docker compose logs -f app`
3. Acessar:
   - App/Web: `http://localhost:8080/`
   - Link em produção não disponivel pois meus créditos na Azure acabaram

Configuração de banco (via variáveis de ambiente no `docker-compose.yml`):
- `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres`
- `SPRING_DATASOURCE_USERNAME=postgres`
- `SPRING_DATASOURCE_PASSWORD=postgres`

## Pipeline CI/CD

O CI/CD é feito com GitHub Actions e deploy em Azure App Service (Web App) usando o action `azure/webapps-deploy@v2`.

Workflow:
- Arquivo: `.github/workflows/main_agro-api-esg.yml`

Ferramentas/serviços usados:
- GitHub Actions (orquestração do pipeline)
- Maven (build do projeto Java)
- Azure App Service (host do backend)
- Azure Publish Profile (secret usado no deploy)

Etapas (visão geral):
1. Checkout do repositório
2. Setup do Java 17 (Temurin) + cache do Maven
3. Build do projeto (`mvn package`, com `-DskipTests` no empacotamento)
4. Listagem e verificação do JAR gerado em `target/`
5. Deploy do JAR para o Azure Web App

Secrets/variáveis esperadas no repositório (GitHub):
- `AzureAppService_PublishProfile_9fe7e2c40da94504b99fe27de4dceddb` (publish profile do Web App)
- (opcional, dependendo da configuração do App Service) `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`

Observação importante:
- Variáveis definidas no workflow **não** garantem que o App Service vai usar essas variáveis em runtime. Em geral, o correto é setar essas chaves em **App Settings** do Azure Web App.

## Containerização

A aplicação é containerizada usando um Dockerfile multi-stage: a primeira etapa compila o JAR com Maven e a segunda executa com Java 17.

Conteúdo do `Dockerfile`:

```dockerfile
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Estratégias adotadas:
- Multi-stage build para reduzir dependências no container final
- Build com `-DskipTests` para acelerar a geração do artefato no build de imagem
- Execução simples via `java -jar` expondo a porta `8080`

## Prints do funcionamento

Inclua aqui as evidências de execução/deploy. Sugestão de itens:

- Execução local (Docker):
  - Print do `docker compose ps`
  - Print do log da aplicação subindo e ouvindo na porta 8080

- CI (GitHub Actions):
  - Link do run:
    - TODO: cole aqui a URL do workflow run
  - Print da etapa de build/deploy concluída

- Azure (staging e produção):
  - URL do App Service (produção):
    - TODO: cole aqui
  - URL do slot de staging (se aplicável):
    - TODO: cole aqui
  - Prints:
    - TODO: adicione imagens em um caminho como `docs/prints/` e referencie aqui

Exemplo de referência de imagem (depois de adicionar o arquivo no repo):

```md
![Deploy no Azure](docs/prints/deploy-azure.png)
```

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Security + OAuth2 Resource Server
- Springdoc OpenAPI (Swagger UI)
- PostgreSQL
- Flyway
- Maven
- Docker / Docker Compose
- GitHub Actions
- Azure App Service (Web App)
