package sample

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import sample.Route.coroutineHandler


actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

suspend fun main() {
    val vertx = Vertx.vertx()
    try {
        vertx.deployVerticleAwait("sample.WebVerticle")
        println("Application started")
    } catch (exception: Throwable) {
        println("Could not start application")
        exception.printStackTrace()
    }
}

class WebVerticle : CoroutineVerticle() {
    override suspend fun start() {
        Json.mapper.registerModule(KotlinModule())

        val config = awaitResult<JsonObject> {
            val hocon = ConfigStoreOptions()
                .setConfig(json { obj("path" to "conf/application.json") })
                .setType("file")
                .setFormat("json")
            ConfigRetriever.create(vertx, ConfigRetrieverOptions().addStore(hocon)).getConfig(it)
        }

        val router = Router.router(vertx)
        val userService = UserService(vertx, config.getJsonObject("db").getJsonObject("sql"))
        val controller = UserController(userService)

        //routing
        router.route().handler(BodyHandler.create())
        router.post("/user").coroutineHandler { controller.create(it) }
        router.post("/user").coroutineHandler { controller.updateById(it) }
        router.get("/user").coroutineHandler { controller.getAll(it) }
        router.get("/user/:id").coroutineHandler { controller.getById(it) }
        router.delete("/user/:id").coroutineHandler { controller.deleteById(it) }

        awaitResult<HttpServer> {
            vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080)
        }
    }

    override suspend fun stop() {
        super.stop();
    }
}
