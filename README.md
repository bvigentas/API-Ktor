# API Ktor [![Build Status](https://travis-ci.org/bvigentas/API-Ktor.svg?branch=master)](https://travis-ci.org/bvigentas/API-Ktor)

| Tool                                             | Version             | 
|--------------------------------------------------|---------------------|
| Kotlin                                           | 1.3.31              | 
| Ktor                                             | 1.2.0               |
| Exposed                                          | 0.13.7              |
| Intellij                                         | 2018.3.4            |
| Postgre                                          | 11.3                |
<br />

## Guia Ferramentas

### Kotlin
Used as main language in the project.<br />
[https://kotlinlang.org/](https://kotlinlang.org/)

### Ktor
Ktor is a framework developed by the JetBrains team to create RESTful APIs using Kotlin.<br />
[https://ktor.io/servers/index.html](https://ktor.io/servers/index.html)

### Exposed
Exposed is another tool made by JetBrains, this one serving as an ORM. <br />
[https://github.com/JetBrains/Exposed/wiki](https://github.com/JetBrains/Exposed/wiki)

### Intellij
Made by JetBrains, Intellij is one of the bests IDEs to develop in Kotlin.<br />
[https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)

### Travis CI
Travis CI was used to make sure no pull request would break the code in the master branch<br />
[https://travis-ci.org/](https://travis-ci.org/)

### Postgresql
Database used in the project.<br />
[https://www.postgresql.org/](https://www.postgresql.org/)

### Gradle
Build tool used in the project.<br />
[https://gradle.org/](https://gradle.org/)
<br />

## Required Configurations
To run the OAuth correctly, some configurations are needed.
First you need to have an Google API Credentials. If you don't have one, follow the next steps:
1. Go to (https://console.developers.google.com) and login with your google account.
2.

You will also need to create a file in you user home directory with the name 'api-ktor.properties'
`C:\Users\<User_Name>\api-ktor.properties`
 In this file you should add those three properties, changing what is in `<` and `>` for yours respective keys.
 ```properties
google.clientId=<YOUR_clientId_FROM_GOOGLE_CREDENTIALS>
google.clientSecret=<YOUR_clientSecret_FROM_GOOGLE_CREDENTIALS>
oauth.secretSignKey=239203972390479
```
  ### Swagger
To view the API documentation, you need to run the docker command below. Inform the directory of the swagger.json file in <directory>.

URL Execution: http://localhost:8080/
 ```bash
 docker run -p 8081:8080 -e SWAGGER_JSON=/mnt/swagger -v <directory>:/mnt --name swagger swaggerapi/swagger-ui
 ```
