import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;

public class Solution2 extends AbstractVerticle {

    public void start() {
        Router router = Router.router(vertx);

        router.get("/")            .handler(this::rootHandler);
        router.get("/customer/:id").handler(this::custHandler);
        router.get("/customer/:id/address/:index").handler(this::addrHandler);
        router.getWithRegex("^/product/(\\d+)").handler(this::regexHandler);

        vertx.createHttpServer()             // Create a new HttpServer
            .requestHandler(router::accept) // Register a request handler
            .listen(8080, "127.0.0.1");      // Listen on 127.0.0.1:8080
    }

    void rootHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject()
                .put("ok", true).put("path", ctx.request().path()).encode());
    }

    void custHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject()
                .put("ok", false)
                .put("custID", ctx.request().getParam("id"))
                .encode());
    }

    protected void addrHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject()
                .put("ok", false)
                .put("custID", ctx.request().getParam("id"))
                .put("addrIndex", ctx.request().getParam("index"))
                .encode());
    }

    void regexHandler(RoutingContext ctx) {
        ctx.response().end(new JsonObject()
                                    .put("ok", false)
                                    .put("product", ctx.request().getParam("param0"))
                                    .encode());
    }
}