[![Build Status](https://travis-ci.org/theborakompanioni/gn.svg?branch=master)](https://travis-ci.org/theborakompanioni/gn)

good news, everyone.
====

## Build
```
mvn clean package
java -jar gn-web/target/gn-web-<version>.jar 
```

## Development
```
mvn install
mvn spring-boot:run -pl gn-web
```

## Test
```
mvn clean test
```