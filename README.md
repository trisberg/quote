# Function project for stock quotes

This sample project contains a single function based on Spring Cloud Function: `functions.CloudFunctionApplication.quote()`, which returns stock price for a stock symbol passed via HTTP request.

For the instructions below, make sure that your current directory is named `quote` and containes the quote function source.

## Local execution

Make sure that `Java 17 SDK` or later is installed.

To start server locally run `./mvnw spring-boot:run`.
The command starts http server and automatically watches for changes of source code.
If source code changes the change will be propagated to running server. It also opens debugging port `5005`
so a debugger can be attached if needed.

### Building

This command builds an OCI image for the function. This uses Jib to build a multi-arch image so you can run this sample on Intel as well as ARM based systems. Set a `FUNC_REGISTRY` environment variable with your prferred registry, e.g. 'docker.io/docker-id'

```shell
export FUNC_REGISTRY=<your preferred registry>
./mvnw compile jib:build -Dimage=${FUNC_REGISTRY}/quote
```

### Running

This command runs the func locally in a container
using the image created above.

```shell
docker pull ${FUNC_REGISTRY}/quote
func --build=false run
```

### Deploying

This command will build and deploy the function into cluster.

```shell
func deploy --build=false --push=false --image=${FUNC_REGISTRY}/quote
```

## Function invocation

For the examples below, please be sure to set the `URL` variable to the route of your function.

You get the route by following command.

```shell
func info
```

Note the value of **Routes:** from the output, set `$URL` to its value.

__TIP__:

If you use `kn` then you can set the URL env var for later use with CURL:

```shell
# kn service describe <function name> and show route url
export URL=$(kn service describe $(basename $PWD) -ourl)
```

### func

Using `func invoke` command:

```shell
func invoke --data '{"^DJI": [""]}'
```

using CURL:

```shell
curl $URL --data "^DJI"
```

## Cleanup

To clean the deployed function run:

```shell
func delete
```
