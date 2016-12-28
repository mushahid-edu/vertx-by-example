### **Exercise 1:** Simple Vert.x Hello World Web Server

1. Download the FULL Vert.x distribution from [http://vertx.io/](http://vertx.io/) and ensure that the **vertx** command is in your path (vertx/bin/vertx)

2. Create a new file **Exercise1.groovy** with the following contents:

[Exercise1.groovy](Exercise1/Exercise1.groovy)
```groovy
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.core.json.JsonObject

class Exercise1 extends GroovyVerticle {

    void start() {
        // Create a new HttpServer
        def server = vertx.createHttpServer()
        
        // Create a JSON response
        def response = new JsonObject([ok: true]).encode()

        // Register a request handler for the HttpServer
        server.requestHandler({ req ->    
                 req.response().end(response) })

        // Listen on port 8080 and interface `127.0.0.1`
        server.listen(8080, '127.0.0.1')
    }
}
```


3. Run the verticle with the command `**vertx run Exercise1.groovy**`

    1. You should see a message like: `Succeeded in deploying verticle`

4. Open a browser and point it at: [http://localhost:8080/](http://localhost:8080/)

Next Steps: (see [HttpServerResponse](http://vertx.io/docs/apidocs/io/vertx/core/http/HttpServerResponse.html))

* Modify the example above to add a `Content-Type` response header

* Modify the example above to add an HTTP response code of 201 to the response

* Modify the example above to add an HTTP reason phrase of ‘IDUNNO’ to the response

### **Exercise 2**: Are you fluent?!?!

Vert.x APIs are written to be [fluent](https://en.wikipedia.org/wiki/Fluent_interface). This means that you can chain method calls together so that they form a sort of domain specific language which CAN be easier to read. We will modify our first example to use the fluent API in Vert.x to perform the same operations.

[Exercise2.groovy](Exercise2/Exercise2.groovy)
```groovy
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.core.json.JsonObject

class Exercise2 extends GroovyVerticle {

    void start() {
        // Create a JSON response
        def response = new JsonObject([ok: true]).encode()

        vertx.createHttpServer()         // Create a new HttpServer
             .requestHandler({ req ->    // Register a request handler for the HttpServer
                 req.response().end(response) })
             .listen(8080, '127.0.0.1')  // Listen on port 8080 and interface `127.0.0.1`
    }
}
```


You’ll see that we chained the createHttpServer() method, which returns an HttpServer object, to the requestHandler() method. We then chained the requestHandler() method to the listen() method. Each of these chained methods returns the original HttpServer object so that we can make subsequent calls in a fluent manner.

### **Exercise 3:** Handlers

A handler in Vert.x is a form of [Callback](https://en.wikipedia.org/wiki/Callback_(computer_programming)). Handlers are passed as arguments to some Vert.x methods so that the callback can be executed once a particular asynchronous operation has been completed. Handlers for Vert.x can be written in Groovy in several ways:

#### **Exercise 3.1:** Handler classes

The basic Handler in Vert.x is any class which implements the [Handler](http://vertx.io/docs/apidocs/io/vertx/core/Handler.html) interface. For example:

[Exercise3_1.groovy](Exercise3/Exercise3_1.groovy)
```groovy
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.core.json.JsonObject

class Exercise3_1 extends GroovyVerticle {

    private class RequestHandler implements Handler<HttpServerRequest> {
        void handle(HttpServerRequest req) {
            def response = new JsonObject([ok: true]).encode()
            req.response().end(response)
        }
    }

    void start() {
        // Create a JSON response
        def response = new JsonObject([ok: true]).encode()

        vertx.createHttpServer()         // Create a new HttpServer
             .requestHandler(new RequestHandler())
             .listen(8080, '127.0.0.1')  // Listen on port 8080 and interface `127.0.0.1`
    }
}
```


As you can see, we pass an instance of the RequestHandler class to the requestHandler() method on the HttpServer object and that instance will handle the HTTP requests.

#### **Exercise 3.2:** Method References

Another way to implement handlers removes some of the boiler-plate of having a separate hanlder class for each Callback we want to register. It’s called a [Method Reference](http://docs.groovy-lang.org/latest/html/documentation/#method-pointer-operator). A method reference is a way of assigning a method to behave as a callback without having to implement a Handler interface on a new class.

[Exercise3_2.groovy](Exercise3/Exercise3_2.groovy)
```groovy
import io.vertx.groovy.core.http.HttpServerRequest
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.core.json.JsonObject

class Exercise3_2 extends GroovyVerticle {

    /**
     * Handle HttpServerRequests
     */
    void handleRequest(HttpServerRequest req) {
        def response = new JsonObject([ok: true]).encode()
        req.response().end(response)
    }

    void start() {
        // Create a JSON response
        def response = new JsonObject([ok: true]).encode()

        vertx.createHttpServer()         // Create a new HttpServer
             .requestHandler(this.&handleRequest) // Register a request handler
             .listen(8080, '127.0.0.1')  // Listen on port 8080 and interface `127.0.0.1`
    }
}
```


#### **Exercise 3.3:** Closures

Finally, in Groovy we can use [Closures](http://docs.groovy-lang.org/latest/html/documentation/#_closures). Closures are a way of writing a bit of code which can be passed as a value . . . in-line…

[Exercise3_3.groovy](Exercise3/Exercise3_3.groovy)
```groovy
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.core.json.JsonObject

class HelloWorld extends GroovyVerticle {

    void start() {
        // Create a JSON response
        def response = new JsonObject([ok: true]).encode()

        vertx.createHttpServer()         // Create a new HttpServer
             .requestHandler({ req ->    // Register a request handler
                 req.response().end(response)
             })
             .listen(8080, '127.0.0.1')  // Listen on port 8080 and interface `127.0.0.1`
    }
}
```


An alternate way of declaring that closure would be to assign the closure to a variable and then pass the variable into the requestHandler() method as shown below:

[Exercise3_3_1.groovy](Exercise3/Exercise3_3_1.groovy)
```groovy
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.http.HttpServerRequest
import io.vertx.lang.groovy.GroovyVerticle

class Exercise3_3_1 extends GroovyVerticle {

    void start() {

        def reqHandler = { HttpServerRequest req ->
            // Create a JSON response
            def response = new JsonObject([ok: true]).encode()
            req.response().end(response)
        }

        vertx.createHttpServer()         // Create a new HttpServer
             .requestHandler(reqHandler) // Register a request handler
             .listen(8080, '127.0.0.1')  // Listen on port 8080 and interface `127.0.0.1`
    }
}
```


Next Steps: (see [HttpServerRequest](http://vertx.io/docs/apidocs/io/vertx/core/http/HttpServerRequest.html))

* Modify the example above to include the requested path as an item in the JSON response body

* Modify the example above to include the request headers as a nested JSON object within the response body

### **Exercise 4:** Using Routers

So far, we have seen that we can add a requestHandler() to an HTTP server, but what if we want to have a number of different paths which do different things in our web application? This is where the Vert.x Web module comes in. It gives us a new features like [Router](http://vertx.io/docs/apidocs/io/vertx/ext/web/Router.html) and [RoutingContext](http://vertx.io/docs/apidocs/io/vertx/ext/web/RoutingContext.html).

[Exercise4.groovy](Exercise4/Exercise4.groovy)
```groovy
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.RoutingContext
import io.vertx.core.json.JsonObject

class Exercise4 extends GroovyVerticle {

    void start() {
        def router = Router.router(vertx)

        router.get('/')              .handler(this.&rootHandler)
        router.get('/something/else').handler(this.&otherHandler)

        vertx.createHttpServer()             // Create a new HttpServer
             .requestHandler(router.&accept) // Register a request handler
             .listen(8080, '127.0.0.1')      // Listen on 127.0.0.1:8080
    }

    void rootHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject([ok: true, path: ctx.request().path()]).encode())
    }

    void otherHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject([ok: false, message: 'Something Else']).encode())
    }
}
```

1. You see that we added 2 different routes to the Router instance

2. Each route has a separate handler set via a method reference

3. Finally, we pass the Router’s accept method via a method reference as a handler for the HttpServer’s requestHandler() method.

### **Exercise 5:** Routes with Path Parameters

In the previous example, we saw that we could specify different paths with different handlers, but what about if we want to capture information FROM the path in a programmatic manner?

[Exercise5.groovy](Exercise5/Exercise5.groovy)
```groovy
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.RoutingContext
import io.vertx.core.json.JsonObject

class Exercise5 extends GroovyVerticle {

    void start() {
        def router = Router.router(vertx)

        router.get('/')            .handler(this.&rootHandler)
        router.get('/customer/:id').handler(this.&custHandler)

        vertx.createHttpServer()             // Create a new HttpServer
            .requestHandler(router.&accept) // Register a request handler
            .listen(8080, '127.0.0.1')      // Listen on 127.0.0.1:8080
    }

    void rootHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject([ok: true, path: ctx.request().path()]).encode())
    }

    void custHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject([
           ok: false,
           custID: ctx.request().getParam('id')
        ]).encode())
    }
}
```


Next Steps: (see [Router](http://vertx.io/docs/apidocs/io/vertx/ext/web/Router.html), [Routing With Regular Expressions](http://vertx.io/docs/vertx-web/groovy/#_routing_with_regular_expressions), [Routing Based On MIME Types](http://vertx.io/docs/vertx-web/groovy/#_routing_based_on_mime_type_of_request), [Request Body Handling](http://vertx.io/docs/vertx-web/groovy/#_request_body_handling))

* Modify the example above to have a new route which had multiple path parameters

* Modify the example above to use a route with regular expressions

* Modify the example to add a new HTTP POST endpoint which consumes JSON and produces the POSTed JSON

### **Exercise 6:** Programmatically Deploy Verticles

So far, our exercised have done all of their work in a single Verticle (HelloWorld). This is fine for simple applications, but it does not scale well for larger and more complex applications. Each Verticle is single-threaded; so in order to utilize our CPU cores effectively, we need to distribute workloads across multiple Verticles.

[EventVerticle.groovy](Exercise6/EventVerticle.groovy)
```groovy
import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.groovy.core.eventbus.Message
import io.vertx.core.json.JsonObject

class EventVerticle extends GroovyVerticle {

    @Override
    void start() {
        vertx.eventBus().consumer('event.verticle', this.&doSomething)
    }

    void doSomething(Message<JsonObject> msg) {
        if ((Math.round(Math.random()*1))==1) { // Randomly choose to return or fail
            msg.reply(msg.body())
        } else {
            msg.fail(1, 'Random Failure')
        }
    }
}
```

[Exercise6.groovy](Exercise6/Exercise6.groovy)
```groovy
// .. SNIP ..

class Exercise6 extends GroovyVerticle {

    void start() {
        def router = Router.router(vertx)

        router.get().handler(this.&rootHandler)

        vertx.createHttpServer()            // Create a new HttpServer
            .requestHandler(router.&accept) // Register a request handler
            .listen(8080, '127.0.0.1')      // Listen on 127.0.0.1:8080
        vertx.deployVerticle('groovy:EventVerticle.groovy')
    }

    // .. SNIP ..
}
```


(**NOTE:** When using `**vertx run <VerticleName>**` to launch Vert.x applications, the files should be in the current 
working directory or a child directory referenced by it's relative path)

Several new concepts have been introduced in this example:

* The [EventBus](http://vertx.io/docs/vertx-core/groovy/#event_bus) - Used to communicate between Verticles in a 
thread-safe manner

* Deploying Verticles Programmatically

* Handling [AsyncResult](http://vertx.io/docs/apidocs/io/vertx/core/AsyncResult.html)s via Callback

* Using [Message](http://vertx.io/docs/apidocs/io/vertx/core/eventbus/Message.html) objects - Message objects consist of JsonObject or String contents and can be replied to

### **Exercise 7:** Deploy With Futures
Often, the application will need to ensure that certain Verticles are 
already up and running before proceeding to do other actions. To allow for 
this, Vert.x provides a way of deploying Verticles with a callback once 
the deployment is complete.

[Example7.groovy](Example7/Example7.groovy)
```groovy
// .. SNIP ..
class Exercise7 extends GroovyVerticle {

    void start() {
        vertx.deployVerticle('groovy:EventVerticle.groovy', this.&deployHandler)
    }

	// .. SNIP ..

    void deployHandler(AsyncResult<String> res) {
        if (res.succeeded()) {
            LoggerFactory.getLogger(Exercise7).info('Successfully deployed EventVerticle')

            // If the EventVerticle successfully deployed, configure and start the HTTP server
            def router = Router.router(vertx)

            router.get().handler(this.&rootHandler)

            vertx.createHttpServer()            // Create a new HttpServer
                .requestHandler(router.&accept) // Register a request handler
                .listen(8080, '127.0.0.1')      // Listen on 127.0.0.1:8080
        } else {
            // Otherwise, exit the application
            LoggerFactory.getLogger(Exercise7).error('Failed to deploy EventVerticle', res.cause())
            vertx.close()
        }
    }
}
```

[EventVerticle.groovy](Exercise7/EventVerticle.groovy)

Next Steps:
* Modify the example above to attempt to redeploy EventVerticle in case of a failure (Use maximum of 3 retries)
* Modify the example above to deploy more than one Verticle and call the new Verticle `AnotherVerticle.groovy`

### Exercise 8: Asynchronous Coordination
It is useful to coordinate several asynchronous operations in a single handler for certain situations. To 
facilitate this, Vert.x provides a [CompositeFuture](http://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html)

[Exercise8.groovy](Exercise8/Exercise8.groovy)
```groovy
// .. SNIP ..

class Exercise8 extends GroovyVerticle {

    void start() {

        Future eventVerticleFuture = Future.future()
        Future anotherVerticleFuture = Future.future()

        CompositeFuture.join(eventVerticleFuture, anotherVerticleFuture).setHandler(this.&deployHandler)

        vertx.deployVerticle('groovy:EventVerticle.groovy', eventVerticleFuture.completer())
        vertx.deployVerticle('groovy:AnotherVerticle.groovy', anotherVerticleFuture.completer())
    }

    // .. SNIP ..

    void deployHandler(CompositeFuture cf) {
        if (cf.succeeded()) {
            LoggerFactory.getLogger(Exercise8).info('Successfully deployed all verticles')

            // If the EventVerticle successfully deployed, configure and start the HTTP server
            def router = Router.router(vertx)

            router.get().handler(this.&rootHandler)

            vertx.createHttpServer()            // Create a new HttpServer
                .requestHandler(router.&accept) // Register a request handler
                .listen(8080, '127.0.0.1')      // Listen on 127.0.0.1:8080
        } else {
            def range = 0..(cf.size() - 1)
            range.each { x ->
                if (cf.failed(x)) {
                    LoggerFactory.getLogger(Exercise8).error('Failed to deploy verticle', cf.cause(x))
                }
            }
            vertx.close()
        }
    }
}
```

Next Steps: (see [CompositeFuture](http://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html) and 
[Async Coordination](http://vertx.io/docs/vertx-core/groovy/#_sequential_composition))
* Modify the example above to use a List of futures instead of specifying each future as a parameter.
* Remove the CompositeFuture and use composed [Future](http://vertx.io/docs/apidocs/io/vertx/core/Future.html)s to 
  load one verticle after another

### Exercise 9: Using Shared Data
While you can coordinate between verticles very well using String and JsonObject instances over the EventBus, it is 
sometimes better to share certain objects across multiple verticles. Vert.x makes this possible via 2 facilities.

#### Exercise 9.1: Shared Local Map
The [Vertx.sharedData()](http://vertx.io/docs/apidocs/io/vertx/core/Vertx.html#sharedData--) method allows us to get an
instance of [LocalMap](http://vertx.io/docs/apidocs/io/vertx/core/shareddata/LocalMap.html) which can store most 
Immutable data types as well as custom types which implement the [Shareable](http://vertx.io/docs/apidocs/io/vertx/core/shareddata/Shareable.html)
interface. Storing data in a these LocalMap instances makes those objects available to other Verticles without having to
use the EventBus to send those objects. **The Shared Local Map has no concurrency controls, so the last writer is always
the winner. If assurance of ordered writes is required, then the user must implement their own concurrency controls or
only use data structures which ensure thread safety.**

[Exercise9_1.groovy](Exercise9/Exercise9_1.groovy)
```groovy
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.groovy.GroovyVerticle

class Exercise9_1 extends GroovyVerticle {

    @Override
    void start() throws Exception {

        // Deploy AnotherVerticle 10 times
        (1 .. 10).each {
            vertx.deployVerticle('groovy:AnotherVerticle.groovy')
        }

        vertx.setPeriodic(100, this.&showDeployedVerticles)
    }

    void showDeployedVerticles(Long t) {
        // Print the list of deployment IDs stored in the shared data local Map
        LoggerFactory.getLogger(Exercise9_1).info('Polling shared data map')
        def localMap = vertx.sharedData().getLocalMap('shared')
        localMap.getDelegate().keySet().each {
            println "${it} - ${localMap.get(it)}".toString()
        }
        println ''
        println ''
    }
}
```

[AnotherVerticle.groovy](Exercise9/AnotherVerticle.groovy)
```groovy
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.groovy.GroovyVerticle

class AnotherVerticle extends GroovyVerticle {

    @Override
    void start() throws Exception {
        vertx.sharedData().getLocalMap('shared').put(context.deploymentID(), Thread.currentThread().name)

        LoggerFactory.getLogger(AnotherVerticle).info("Deployed AnotherVerticle: ${context.deploymentID()}")
    }
}
```

#### Exercise 9.2: Clustered Async Map
When running in a clustered configuration, sharing objects across Vert.x nodes requires a special feature known as
[AsyncMap](http://vertx.io/docs/apidocs/io/vertx/core/shareddata/AsyncMap.html). The AsyncMap is handled by the Vert.x 
[ClusterManager](http://vertx.io/docs/apidocs/io/vertx/core/spi/cluster/ClusterManager.html), which is responsible for
ensuring that access to the AsyncMap data is handled in a cluster/thread-safe way. In order to use the AsyncMap, Vert.x 
**MUST** be started in a clustered mode using `vertx run -cluster <Verticle>`

[Exercise9_2.groovy](Exercise9/Exercise9_2.groovy)
```groovy
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.groovy.GroovyVerticle

class Exercise9_1 extends GroovyVerticle {

    @Override
    void start() throws Exception {

        // Get a reference to clusterWide map called 'shared'
        vertx.sharedData().getClusterWideMap('shared', { res ->
            if (res.succeeded()) {
                // Write to the map and await success
                res.result().put('deployments', Arrays.asList(context.deploymentID()), { res1 ->
                    // Deploy AnotherVerticle 10 times
                    (1 .. 10).each {
                        vertx.deployVerticle('groovy:ClusteredVerticle.groovy')
                    }
                })
            }
        })

        vertx.setPeriodic(100, this.&showDeployedVerticles)
    }

    void showDeployedVerticles(Long t) {
        // Print the list of deployment IDs stored in the shared data local Map
        LoggerFactory.getLogger(Exercise9_1).info('Polling shared data map')

        // Get reference to clusterWide map called 'shared'
        def clusteredMap = vertx.sharedData().getClusterWideMap('shared', { res ->
            if (res.succeeded()) {
                // Get the 'deployments' value from the AsyncMap
                res.result().get('deployments', { res1 ->

                    // Iterate over list of values
                    res1.result().each {
                        println it
                    }
                    println ''
                    println ''
                })
            }
        })
    }
}
```

[ClusteredVerticle.groovy](Exercise9/ClusteredVerticle.groovy)
```groovy
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.groovy.GroovyVerticle

class ClusteredVerticle extends GroovyVerticle {

    @Override
    void start() throws Exception {
        // Get a reference to clusterWide map called 'shared'
        vertx.sharedData().getClusterWideMap('shared', { res ->
            if (res.succeeded()) {
                // Get the 'deployments' list
                res.result().get('deployments', { res1 ->
                    List<String> deploymentList = res1.result()
                    deploymentList.add(context.deploymentID())

                    // Update the 'deployments' list
                    res.result().put('deployments', deploymentList, { res2 ->
                        LoggerFactory.getLogger(ClusteredVerticle).info("Deployed ClusteredVerticle: ${context.deploymentID()}")
                    })
                })
            }
        })
    }
}
```

This cluster-wide data coordination is complex, so it is always advisable to send shared data via the EventBus where 
possible. The ClusterManager and the AsyncMap implementations ensure that access to and writing of clustered resources
are synchronized properly across the entire cluster and thust prevents race conditions. The negative impact being that 
access to read/write clustered data is much slower.

### Exercise 10 - A TCP Echo Server
As a quick introduction to the network server capabilities of Vert.x, Let's implement a TCP Echo Server. An echo server 
is a network socket server which accepts incoming data and sends the same data back as a response.
 
```groovy
import io.vertx.core.AbstractVerticle
import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.net.NetServer
import io.vertx.core.net.NetServerOptions
import io.vertx.core.net.NetSocket

class EchoServer extends AbstractVerticle {

    void start() {
        NetServerOptions opts = new NetServerOptions()
                .setHost("0.0.0.0")
                .setPort(1080)
                .setLogActivity(true)

        NetServer server = vertx.createNetServer(opts)
        server.connectHandler(this.&connectHandler).listen()
    }

    void connectHandler(NetSocket socket) {
            socket.handler(this.&dataHandler.curry(socket))
    }

    void dataHandler(NetSocket socket, Buffer b) {
        LoggerFactory.getLogger(EchoServer).info(b.toString())
        socket.write(b)
    }
}
```

There are some new things to learn in this example. For one, there is the introduction of NetServer and it's associated 
options; but that it mostly self-explanitory. The other thing to make note of is the use of the 
[Buffer](http://vertx.io/docs/apidocs/io/vertx/core/buffer/Buffer.html) object. From the Vert.x API documentation:

> Most data is shuffled around inside Vert.x using buffers.
> A buffer is a sequence of zero or more bytes that can read from or written to and which expands automatically as necessary to accommodate any bytes written to it. You can perhaps think of a buffer as smart byte array.

You can think of *Buffer*s as a way of pushing around streams of bytes. Buffers also have some convenience methods
like `toString()`, `toJsonObject()`, and `toJsonArray()`. You can append to a Buffer using one of the provided append
methods which can handle input types like Int/Float/Short/Unsigned/String/Byte/Long/Double. There are also append 
methods for storing data in the buffer in little-endian byte order.

Next Steps:
* Modify the EchoServer above to take in some text (Latin characters, numbers, spaces, newlines ONLY), ignore non-text, 
and send back `Hello <text>`.


### Exercise 11

Vert.x also has the ability to create UDP servers. Let's see what a UDP echo server would look like in Vert.x:

```groovy
import io.vertx.core.AsyncResult
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.datagram.DatagramPacket
import io.vertx.groovy.core.datagram.DatagramSocket
import io.vertx.lang.groovy.GroovyVerticle

class UDPEchoServer extends GroovyVerticle {
    private final Logger LOG = LoggerFactory.getLogger(UDPEchoServer)

    @Override
    void start() throws Exception {
        DatagramSocket socket = vertx.createDatagramSocket()

        socket.listen(1080, "0.0.0.0", { res ->
            socketHandler(res)
        })
    }

    void socketHandler(AsyncResult<DatagramSocket> res) {
        if (res.succeeded()) {
            // Successfully received a datagram
            def socket = res.result()
            socket.handler(this.&datagramHandler.curry(socket))
        }
    }

    void datagramHandler(DatagramSocket socket, DatagramPacket p) {
        socket.send(p.data(), p.sender().port(), p.sender().host(), { sent ->
            sendHandler(sent)
        })
    }

    void sendHandler(sent) {
        if (sent.succeeded()) {
            LOG.info("SUCCESS")
        } else {
            LOG.error("FAILED")
        }
    }
}
```

Next Steps:
* Modify the EchoServer above to take in some text (Latin characters, numbers, spaces, newlines ONLY), ignore non-text, 
and send back `Hello <text>`.

### Exercise 12

HTTP is a mainstay of software these days, so being able to make and handle HTTP requests is vital. Let's see how Vert.x
make HTTP requests in an asynchronous manner:

```groovy
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.lang.groovy.GroovyVerticle

class Exercise12 extends GroovyVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(Exercise12)

    @Override
    void start() throws Exception {
        def client = vertx.createHttpClient()
                          .getNow('www.google.com', '/', this.&responseHandler)
    }

    void responseHandler(HttpClientResponse response) {
        if (response.statusCode()==200 && response.statusMessage()=='OK') {
            LOG.info('Success!')
        } else {
            LOG.warn("Got ${response.statusCode()} as the response code.")
        }
        vertx.close()
    }
}
```

Next Steps
* Make an HTTP GET request which uses HTTP Basic Authentication
* Make an HTTP POST request which sends a JSON body

### Exercise 13

We've covered a number of individual features of Vert.x, Async, and non-blocking APIs in Vert.x, but in this exercise we
will try to put a few different ones together. Here's the scenario:

* An HTTP server listening on port 8080
* A web browser will make a request to the '/merged/' endpoint
* The Vert.x application will execute several operations in parallel
  * Request the `www.google.com` index page
  * Read a file (Your choice, but make it a simple short file) from the filesystem
  * Perform a DNS lookup on `www.google.com`
* Once all of the parallel operations are complete, insert the file contents and the dns results into a `<pre>` block 
  before the ending `</body>` tag in the html retrieved from Google (Note: [Groovy Regex Replace](http://naleid.com/blog/2008/05/19/dont-fear-the-regexp))
* Return the modified Google index page to the browser
* If ANY one of the async operations fails, return a 500 HTTP response with the exception's `localizedMessage` value.

There is only one component here which you are not already familiar with, and that is the
[DNSClient](http://vertx.io/docs/vertx-core/groovy/#_dns_client). The DNS client relatively simple, and it will be left
up to you to read the documentation and use it.

Next Steps:
* Modify the solution for Exercise 13 so that you can 
  [pass in](http://vertx.io/docs/vertx-core/groovy/#_the_vertx_command_line) a `config.json` file from which the 
  application will [read](http://vertx.io/docs/vertx-core/groovy/#_passing_configuration_to_a_verticle) the settings 
  for:
  * HTTP client host
  * HTTP client URI
  * HTTP client port
  * HTTP client SSL enable/disable
  * DNS hostname to be resolved
  * Filename to be read

### Exercise 14

Let's jump back into `vertx-web` again a little deeper... One interesting aspect of the `Router` and `RoutingContext` is
that routes can be *chained*. What this means is that if you have a path which starts with `/rest`, and all routes
under that path will all do some of the same tasks, you can extract those operations into an earlier route which then
calls `RoutingContext.next()` and the request will be processed by other routes which might match. Here's an example:

```groovy
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.RoutingContext
import io.vertx.lang.groovy.GroovyVerticle

import static io.netty.handler.codec.http.HttpResponseStatus.OK
import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED

class Exercise14 extends GroovyVerticle {

    @Override
    void start() throws Exception {
        Router router = Router.router(vertx)

        router.route().handler(this.&authHandler)
        router.route('/rest/*').handler(this.&restHandler)
        router.get('/rest/customer/:id').handler(this.&customerByIdHandler)

        vertx.createHttpServer().requestHandler(router.&accept).listen(8080, '0.0.0.0')
    }

    void authHandler(RoutingContext ctx) {
        // Do something to validate authentication
        ctx.put('authenticated', true)
        ctx.next()
    }

    void restHandler(RoutingContext ctx) {
        // All REST requests will have certain common requirements

        ctx.response()
            .putHeader('Content-Type', 'application/json') // Set the response Content-Type to application/json
            .putHeader('Cache-Control', 'nocache') // Disable caching for browsers which respect this header
            .putHeader('Expires', 'Tue, 15 Nov 1994 12:45:26 GMT') // Set some expiry date in the past to help prevent caching
        ctx.next()
    }

    void customerByIdHandler(RoutingContext ctx) {
        if (ctx.get('authenticated')) {
            // The 'authenticated' value is set in the authHandler method/route, and so it should be present here!!
            ctx.response().setStatusCode(OK.code())
                          .setStatusMessage(OK.reasonPhrase())
                          .end(new JsonObject([:]).encodePrettily())  // The headers set in restHandler are already set as well!
        } else {
            ctx.response().setStatusCode(UNAUTHORIZED.code())
                          .setStatusMessage(UNAUTHORIZED.reasonPhrase())
                          .end(new JsonObject([:]).encodePrettily())  // The headers set in restHandler are already set as well!
        }
    }
}
```

In this, admittedly contrived, example; we see that any request which matches '/rest/customer/:id' will match all of the
previous routes as well. Since the handlers for each of those routes are calling `RoutingContext.next()` on the 
RoutingContext object, ALL of these handlers will be applied in order!

Next Steps:
* Do some research in the Vert.x documentation, and determine how to add a *catch-all* route which will handle any 
  previously unhandled requests by sending a custom JSON 404 response
* Create a *catch-all* route which will instead serve up [static filesystem resources](http://vertx.io/docs/vertx-web/groovy/#_serving_static_resources)