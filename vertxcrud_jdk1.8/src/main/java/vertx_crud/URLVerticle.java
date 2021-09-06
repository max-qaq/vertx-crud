package vertx_crud;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

/**
 * @program: vertxcrud_jdk1.8
 * @description:
 * @author: max-qaq
 * @create: 2021-09-05 20:55
 **/

public class URLVerticle extends AbstractVerticle {
  Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    router = Router.router(vertx);

    //http://localhost:8888/test?page=1
    router.route("/test").handler(req -> {
      String page = req.request().getParam("page");
      req.response()
        .putHeader("content-type", "text/plain")
        .end(page);
    });

    //http://localhost:8888/test/1
    router.route("/test/:page").handler(req -> {
      String page = req.request().getParam("page");
      req.response()
        .putHeader("content-type", "text/plain")
        .end(page);
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
