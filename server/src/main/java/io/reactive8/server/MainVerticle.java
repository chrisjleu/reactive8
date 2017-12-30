package io.reactive8.server;


import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.StaticHandler;


public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        // Setup serving of static content
        StaticHandler staticHandler = StaticHandler.create().setIndexPage("client/index.html");

        router.route("/assets/*").handler(staticHandler);
        router.route("/api/*").handler(request -> request.response().end("REST API"));
        router.route("/").handler(request -> request.reroute("/assets/"));

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .rxListen(8080)
            .doOnSuccess(result -> System.out.println("HTTP server started on port 8080"))
            .doOnError(e -> System.out.println("failed to start owing to " + e.getMessage()))
            .subscribe();
    }
}
