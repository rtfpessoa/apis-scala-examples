# guardrail-scala-api-example

This is an example application that shows how to do an OpenAPI/Swagger 2.0 API using:
* HTTP Server - [akka-http](https://github.com/akka/akka-http)
* Codegen - [guardrail](https://github.com/twilio/guardrail)
* Automated API Specification Testing - [dredd](https://github.com/apiaryio/dredd)
* Documentation - 
* Validation - swagger-cli validate src/main/resources/helloworld.yaml 

## Summary

## Requirements

* Java JDK 8+
* SBT
* Docker
* swagger-cli
  * Yarn: `yarn global add swagger-cli`
  * NPM: `npm install -g swagger-cli`
* widdershins
  * Yarn: `yarn global add widdershins`
  * NPM: `npm install -g widdershins`
* api2html
  * Yarn: `yarn global add api2html`
  * NPM: `npm install -g api2html`

## Start Server

```sh
sbt run
```

## Validate Specification

```sh
swagger-cli validate src/main/resources/helloworld.yaml
```

## Generate Documentation

### Markdown

```sh
widdershins --expandBody src/main/resources/helloworld.yaml -o /tmp/helloworld.md
```

### HTML

```sh
api2html -o /tmp/helloworld.html src/main/resources/helloworld.yaml
```

### Swagger UI

```sh
docker run -p 80:8080 -e SWAGGER_JSON=/swagger-api/swagger.yaml -v $PWD/src/main/resources/helloworld.yaml:/swagger-api/swagger.yaml swaggerapi/swagger-ui
```

## Unit tests

```sh
sbt test
```

## Automated tests

```sh
sbt run
docker run --net=host -v $PWD:/api -w /api apiaryio/dredd dredd /api/src/main/resources/helloworld.yaml http://127.0.0.1:8080
```
