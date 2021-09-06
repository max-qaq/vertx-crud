package vertx_crud;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @program: vertxcrud_jdk1.8
 * @description:
 * @author: max-qaq
 * @create: 2021-09-05 21:05
 **/

public class BodyVerticle extends AbstractVerticle {
  Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    //http://localhost:8888/test?page=1
    //formdata
    router.route("/test/form").handler(req -> {
      String page = req.request().getFormAttribute("page");
      req.response()
        .putHeader("content-type", "text/plain")
        .end(page);
    });

    //http://localhost:8888/test/1
    //json
    router.route("/test/json").handler(req -> {
      JsonObject bodyAsJson = req.getBodyAsJson();
      req.response()
        .putHeader("content-type", "text/plain")
        .end(bodyAsJson.toString());
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

