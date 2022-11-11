# Function project for stock quotes

This sample project contains a Spring Boot app containing a single `quote` function based on Spring Cloud Function. The `quote` will look up and return the stock price for a stock symbol passed via a HTTP request.

For the instructions below, make sure that your current directory is named `quote` and contains the quote function source.

## Local build and run

Make sure that `Java 17 SDK` or later is installed.

To start server locally run:

```shell
./mvnw spring-boot:run
```

The command starts a server that listens on port 8080.

You can invoke the function using `curl`:

```shell
curl localhost:8080 --data "^DJI"
```

## Local build and deploying to cluster

### Building and deploying with Jib

> __NOTE:__ This is recommended for ARM-based Macs

The following command builds an OCI image for the function.
This uses [Jib](https://github.com/GoogleContainerTools/jib) to build a multi-arch image, and you can run this sample on Intel as well as ARM based systems.
Set a `FUNC_REGISTRY` environment variable with your preferred registry account, e.g. "docker.io/docker-id".

```shell
export FUNC_REGISTRY="<your preferred registry>"
./mvnw compile jib:build -Dimage=${FUNC_REGISTRY}/quote
```

Jib builds and pushes the image to the specified registry bypassing the Docker daemon.

The following command will deploy the function to the cluster. 
We specify `--build=false` since we alredy built the image and `--push=false` since Jib already pushed the image to the registry.

```shell
func deploy --build=false --push=false --image=${FUNC_REGISTRY}/quote
```

### Building native image with Maven

```shell
export FUNC_REGISTRY="<your preferred registry>"
```

On an `amd64` system, like systems with Intel or AMD processors. run:

```
./mvnw -Pnative spring-boot:build-image -Dspring-boot.build-image.imageName=$FUNC_REGISTRY/quote:amd64
```

On an `arm64` system, like new M1/M2 Macs, run:

```
./mvnw -Pnative,arm64 spring-boot:build-image -Dspring-boot.build-image.imageName=$FUNC_REGISTRY/quote:arm64
```

### Building and deploying with `func` and buildpacks

The following command will build the function using a buildpack using the local Docker daemon.

```shell
func build -v
```

The following command will push the image to the registry and deploy it to the cluster.

```shell
func deploy --build=false
```

## Function invocation

### Invocation using `func`

Using `func invoke` command:

```shell
func invoke --data '{"^DJI": [""]}'
```

### Invocation using `curl`

For the examples below, please be sure to set the `URL` variable to the route of your function.

You get the route by following command.

```shell
func info
```

Note the value of **Routes:** from the output, set `$URL` to its value.

> __TIP__: If you use `kn` then you can set the URL env var for later use with CURL:
> ```shell
> export URL=$(kn service describe $(basename $PWD) -ourl)
> ```

Invoking using `curl`:

```shell
curl $URL --data "^DJI"
```

## Cleanup

To clean the deployed function run:

```shell
func delete
```
