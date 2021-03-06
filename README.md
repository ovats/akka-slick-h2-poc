# akka-slick-h2-poc

## Overview

This is a POC for learning Slick and H2 DB.

Project working in progress.

The project contains two subprojects:

- api: code related for API Rest.
- common: all common classes, objects and traits.

## Requirements

You will need:

- Java 11
- sbt

## API

There are three endpoints implemented:

- `POST /products` => add new product
- `PUT /products/{id}` => update product
- `DELETE /products/{id}` => delete product
- `GET /products/{id}` => get product data of a given id
- `GET /products` => get the list of all products stored
- `GET /products?vendor=name` => retrieve the list of products filtered by vendor
- `GET /ping`

### POST /products

```
curl -X POST \
  http://localhost:8080/products \
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
  http://localhost:8080/products \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "macbook pro",
    "vendor": "apple",
    "price": 200
}'
```

### PUT /products/{id}

```
curl -X PUT \
  http://localhost:8080/products/123 \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "iphone",
    "vendor": "apple",
    "price": 100,
    "expirationDate": "2021-05-05"
}'
```

### DELETE /products/{id}

Delete a `product`:

```
curl -X DELETE http://localhost:8080/products/123
```

### GET /products/{id}

Get the data of a `product`:

```
curl -X GET http://localhost:8080/products/id/123456
```

### GET /products

Get the list of all `products`:

```
curl -X GET http://localhost:8080/products 
```

### GET /products?vendor=name

Get the list of `products` filtered by `vendor`:

```
curl -X GET 'http://localhost:8080/products?vendor=apple' 
```

### GET /ping

This is just an endpoint health check.

```
curl -X GET  http://localhost:8080/ping 
```

## Run in local environment

From the terminal just run:

```
sbt api/run
```

## Run tests

From a console/terminal run:

```
sbt test
```

## Pending 

- Improvements in design in Database layer.
- Add business validation (Cats.Validated)
- Unit tests
- Documentation of how to replace H2 with other databases like MySql, Postgres, etc.
- OAS Specification (documentation)
- Docker 
 