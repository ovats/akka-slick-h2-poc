# akka-slick-h2-poc

## Overview

This is a POC for learning Slick and H2 db.

Project working in progress.

The project is divided in two parts:

- api: code related for API Rest.
- common: all common classes, objects and traits.

## Requirements

You will need:

- Java 11
- sbt

## API

There are three endpoints implemented:

- POST /products => add new products
- DELETE /products/{id} => delete products
- GET /products?vendor=name => retrieve the list of products (all products or filtered by vendor)
- GET /ping

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

### DELETE /products/{id}

Delete a product from the store:

```
curl -X DELETE http://localhost:8080/products/123
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
- Add UPDATE (CRUD)
- Add business validation (Cats.Validated)
- Unit tests
- Documentation of how to replace H2 with other databases like MySql, Postgres, etc.
- OAS Specification (documentation)
- Docker 
 