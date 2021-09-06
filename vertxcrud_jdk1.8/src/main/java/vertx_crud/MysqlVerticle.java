package vertx_crud;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.*;

/**
 * @program: vertxcrud_jdk1.8
 * @description:
 * @author: max-qaq
 * @create: 2021-09-05 21:17
 **/

public class MysqlVerticle extends AbstractVerticle {
  Router router;
  MySQLConnectOptions connectOptions = new MySQLConnectOptions()
    .setPort(3306)
    .setHost("localhost")
    .setDatabase("vertx")
    .setUser("root")
    .setPassword("4587889");

  // Pool options
  PoolOptions poolOptions = new PoolOptions()
    .setMaxSize(5);

  // Create the client pool
  MySQLPool client;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    client = MySQLPool.pool(vertx, connectOptions, poolOptions);

    //http://localhost:8888/insert
    router.post("/insert").handler(req -> {
      JsonObject bodyAsJson = req.getBodyAsJson();
      String name = bodyAsJson.getString("name");
      String password = bodyAsJson.getString("password");
      client
        .preparedQuery("INSERT INTO `user` (name , password) VALUES (? , ?)")
        .execute(Tuple.of(name, password), ar -> {
          if (ar.succeeded()) {
            RowSet<Row> rows = ar.result();
            System.out.println("Got " + rows.size() + " rows ");
          } else {
            System.out.println("Failure: " + ar.cause().getMessage());
          }
        });

      req.response()
        .putHeader("content-type", "application/json")
        .end("插入成功");
    });

    //http://localhost:8888/delete
    router.post("/delete").handler(req -> {
      router.route().handler(BodyHandler.create());
      JsonObject bodyAsJson = req.getBodyAsJson();
      String name = bodyAsJson.getString("name");
      String password = bodyAsJson.getString("password");
      client
        .preparedQuery("DELETE FROM `user` WHERE `name` = ? AND `password` = ?")
        .execute(Tuple.of(name, password), ar -> {
          if (ar.succeeded()) {
            RowSet<Row> rows = ar.result();
            System.out.println("Got " + rows.size() + " rows ");
          } else {
            System.out.println("Failure: " + ar.cause().getMessage());
          }
        });

      req.response()
        .putHeader("content-type", "application/json")
        .end("删除成功");
    });

    //http://localhost:8888/queryByname/{name}
    router.get("/queryByname/:name").handler(req -> {
      String name = req.request().getParam("name");
      List<JsonObject> list = new ArrayList<>();
      client
        .preparedQuery("SELECT * FROM `user` WHERE `name` = ? ")
        .execute(Tuple.of(name), ar -> {
          if (ar.succeeded()) {
//            RowSet<Row> rows = ar.result();
//            System.out.println("Got " + rows.size() + " rows ");
//            for (Row row : rows) {
//              JsonObject jsonObject = new JsonObject();
//              jsonObject.put("id",row.getInteger(0)).put("name",row.getString(1)).put("password",row.getString(2));
//              list.add(jsonObject);
//            }
            ar.result().forEach(item ->{
              JsonObject jsonObject = new JsonObject();
              jsonObject.put("id",item.getInteger(0)).put("name",item.getString(1)).put("password",item.getString(2));
              list.add(jsonObject);
            });
            req.response()
              .putHeader("content-type", "application/json")
              .end(list.toString());
          } else {
            System.out.println("Failure: " + ar.cause().getMessage());
            req.response().putHeader("content-type", "application/json").end("Failure: " + ar.cause().getMessage());
          }
        });

    });

    //http://localhost:8888/queryAll?page=1
    router.get("/queryAll").handler(req -> {
      String page = req.request().getParam("page");
      int count = Integer.parseInt(page) - 1;
      List<JsonObject> lists = new ArrayList<>();
      client
        .preparedQuery("SELECT `name`,`password` FROM `user` LIMIT 5 OFFSET ?")
        .execute(Tuple.of(count), ar -> {
          if (ar.succeeded()) {
//            RowSet<Row> rows = ar.result();
//            System.out.println("Got " + rows.size() + " rows ");
//            for (Row row : rows) {
//              JsonObject jsonObject = new JsonObject();
//              jsonObject.put("name",row.getString(0)).put("user",row.getString(1));
//              lists.add(jsonObject);
//            }
            ar.result().forEach(item ->{
              JsonObject jsonObject = new JsonObject();
              jsonObject.put("name",item.getString(0)).put("password",item.getString(1));
              lists.add(jsonObject);
            });
            req.response()
              .putHeader("content-type", "application/json")
              .end(lists.toString());
          } else {
            System.out.println();
            req.response()
              .putHeader("content-type", "application/json")
              .end("Failure: " + ar.cause().getMessage());
          }
        });
    });

    //http://localhost:8888/update
    router.post("/update").handler(req -> {
      router.route().handler(BodyHandler.create());
      JsonObject bodyAsJson = req.getBodyAsJson();
      String name = bodyAsJson.getString("name");
      String password = bodyAsJson.getString("password");

      client
        .preparedQuery("UPDATE `user` SET `password` = ? WHERE `name` = ?")
        .execute(Tuple.of(password, name), ar -> {
          if (ar.succeeded()) {
            RowSet<Row> rows = ar.result();
            System.out.println("Got " + rows.size() + " rows ");
          } else {
            System.out.println("Failure: " + ar.cause().getMessage());
          }

          req.response()
            .putHeader("content-type", "application/json")
            .end("修改成功");
        });
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



