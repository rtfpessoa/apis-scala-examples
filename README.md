# akka-grpc-scala-api-example

This is an example application that shows how to do an gRPC API using:
* HTTP Server - [akka-http](https://github.com/akka/akka-http)
* Codegen - [akka-grpc](https://github.com/akka/akka-grpc)

## Summary

## Requirements

* Java JDK 8+
* SBT
* Docker
* protoc-gen-doc
  * `go get -u github.com/pseudomuto/protoc-gen-doc/cmd/protoc-gen-doc`
* grpcurl
  * `go get github.com/fullstorydev/grpcurl/cmd/grpcurl`
* grpcui
  * `go install github.com/fullstorydev/grpcui/cmd/grpcui`
* prototool
  * `go get github.com/uber/prototool/cmd/prototool@dev`
* gRPC Gateway
  * `go get github.com/grpc-ecosystem/grpc-gateway/protoc-gen-grpc-gateway`
  * `go get github.com/grpc-ecosystem/grpc-gateway/protoc-gen-swagger`
  * `go get github.com/golang/protobuf/protoc-gen-go`

## Start Server

```sh
sbt run
```

## Format

```sh
prototool format -f -w
```

## Validate Specification

```sh
prototool compile
prototool lint
```

## Call gRPC

```sh
sbt run
prototool grpc --address localhost:8080 --method helloworld.GreeterService/SayHello --data '{ "name": "Alice" }'
```

```sh
sbt run
grpcurl -import-path /usr/local/include \
    -import-path . \
    -import-path $GOPATH/src \
    -import-path $GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
    -proto src/main/protobuf/helloworld.proto \
    -plaintext -d '{ "name": "Alice" }' localhost:8080 helloworld.GreeterService/SayHello
```

## Reverse REST Proxy

### gRPC Gateway

```sh
sbt run
mkdir -p $PWD/grpc_gateway/stubs
protoc -I/usr/local/include -I. \
    -I$GOPATH/src \
    -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
    --go_out=plugins=grpc:$PWD/grpc_gateway/stubs \
    grpc_gateway/helloworld-gateway.proto
protoc -I/usr/local/include -I. \
    -I$GOPATH/src \
    -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
    --grpc-gateway_out=logtostderr=true:$PWD/grpc_gateway/stubs \
    grpc_gateway/helloworld-gateway.proto
cd ./grpc_gateway/stubs/src/main/protobuf
go get .
cd -
go run ./grpc_gateway/server/helloworld.proxy.go
```

### Envoy with gRPC-JSON transcoder

> Reference example: https://blog.jdriven.com/2018/11/transcoding-grpc-to-http-json-using-envoy/

```sh
protoc -I/usr/local/include -I. \
    -I$GOPATH/src \
    -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
    --include_imports \
    --include_source_info \
    --descriptor_set_out=/tmp/helloworld.pb \
    grpc_gateway/helloworld-gateway.proto

docker run \
    -v /tmp/helloworld.pb:/tmp/envoy/proto.pb \
    -v $PWD/envoy.yaml:/etc/envoy/envoy.yaml \
    --rm=true \
    --net=host \
    -p 8081:8081 -p 8082:8082 -p 8083:8083 \
    envoyproxy/envoy:latest
```

## Generate Documentation

### HTML

```sh
protoc -I/usr/local/include -I. \
    -I$GOPATH/src \
    -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
    --doc_out=/tmp \
    src/main/protobuf/helloworld.proto
```

### OpenAPI/Swagger 2.0

```sh
protoc -I/usr/local/include -I. \
    -I$GOPATH/src \
    -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
    --swagger_out=/tmp \
    grpc_gateway/helloworld-gateway.proto
```

## Unit tests

```sh
sbt test
```

## Manual tests

```sh
sbt run
grpcui -plaintext -proto src/main/protobuf/helloworld.proto localhost:8080
```
