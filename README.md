# akka-slick-h2-poc

## Overview

This is a POC for learning Slick and H2 db.

Project working in progress.

The project is divided in two parts:

- api: code related for API Rest.
- common: all common classes, objects and traits.

## API

There are three endpoints implemented:

- POST /products/uuid => add new products
- GET /products?vendor=name => retrieve the list of products (all products or filtered by vendor)
- GET /ping

### POST /products

```
curl -X POST \
  http://localhost:8080/products/52ae4ed7-d318-44ac-8c33-a4ad6a3c0d35 \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "iphone",
    "vendor": "apple",
    "price": 100,
    "expirationDate": "2021-05-05"
}'
```

The field `expirationDate` is optional, so the following request is also valid:

```
curl -X POST \
  http://localhost:8080/products/a96ff2e7-5453-4946-9ffe-492521222a5e \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "macbook pro",
    "vendor": "apple",
    "price": 200
}'
```

### GET /products?vendor=name

You can get the list of all products:

```
curl -X GET http://localhost:8080/products 
```

Or you can filter by `vendor`:

```
curl -X GET 'http://localhost:8080/products?vendor=apple' 
```

### GET /ping

This is just an endpoint health check.

```
curl -X GET  http://localhost:8080/ping 
```

## Pending 

- Improvements in design in Database layer.
- Unit tests
- Documentation of how to replace H2 with other databases like MySql, Postgres, etc.
- OAS Specification (documentation)
- Instructions to how to run with sbt in local environment (documentation)
- Docker 
 