package vertx_crud;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * @program: vertxcrud_jdk1.8
 * @description:
 * @author: max-qaq
 * @create: 2021-09-05 20:42
 **/

public class RouterVerticle extends AbstractVerticle {
  Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    router = Router.router(vertx);

    router.route("/hello").handler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    });

    vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
