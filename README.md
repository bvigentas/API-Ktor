# API Ktor [![Build Status](https://travis-ci.org/bvigentas/API-Ktor.svg?branch=master)](https://travis-ci.org/bvigentas/API-Ktor)

| Tool                                             | Version             | 
|--------------------------------------------------|---------------------|
| Kotlin                                           | 1.3.31              | 
| Ktor                                             | 1.2.0               |
| Exposed                                          | 0.13.7              |
| Intellij                                         | 2018.3.4            |
| Postgres                                         | 9.1-901-1.jdbc4     |


## Endpoints
#### Get(/usuarios)
#### Get(/usuarios/{id})
#### Post(/usuarios)
#### Put(/usuarios/{id})
#### Delete(/usuarios/{id})
#### Delete(/usuarios)
#### Get(/comandas)
#### Get(/comandas/{id})
#### Post(/comandas)
#### Put(/comandas/{id})
#### Delete(/comandas/{id})


## Swagger
Para exibir a documentação da API, é necessário rodar o comando docker abaixo. Informar o diretório do arquivo swagger.json em <diretório>.

URL execução: http://localhost:8080/
```bash
docker run -p 8081:8080 -e SWAGGER_JSON=/mnt/swagger -v <diretório>:/mnt --name swagger swaggerapi/swagger-ui
```