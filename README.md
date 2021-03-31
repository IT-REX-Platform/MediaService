# MediaService

## Architecture Documentation

Documentation of the whole IT-Rex system can be found [here](https://github.com/IT-REX-Platform/Wiki/wiki/Application-Architecture).

Especially relevant documentation for the internal data model of this service:
* [User Role Model](https://github.com/IT-REX-Platform/Wiki/wiki/Application-Architecture--Data-Model--User)
* [Content Data Model](https://github.com/IT-REX-Platform/Wiki/wiki/Application-Architecture--Data-Model--Content)

## Development

Documentation of the recommended development environment setup can be found [here](https://github.com/IT-REX-Platform/Wiki/wiki/Development--Environment-Setup).

To start your application in the dev profile, run:

```
./gradlew -Pdev bootRun
```

The service can be provided as a Docker image. To build the Docker image and load it into your local registry execute:

```
./gradlew -Pdev jibDockerBuild
```

Further documentation on how to start the service can be found [here](https://github.com/IT-REX-Platform/Wiki/wiki/Development--How-to-start-a-backend-service).


## JHipster

This application was generated using JHipster 6.10.5, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v6.10.5](https://www.jhipster.tech/documentation-archive/v6.10.5).

This is a "microservice" application intended to be part of a microservice architecture, please refer to the [Doing microservices with JHipster][] page of the documentation for more information.

This application is configured for Service Discovery and Configuration with the JHipster-Registry. On launch, it will refuse to start if it is not able to connect to the JHipster-Registry at [http://localhost:8761](http://localhost:8761). For more information, read our documentation on [Service Discovery and Configuration with the JHipster-Registry][].

