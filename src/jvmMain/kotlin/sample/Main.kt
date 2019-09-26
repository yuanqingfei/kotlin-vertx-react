package sample

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle


actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

suspend fun main() {
    try {
        Vertx.vertx().deployVerticleAwait(MainVerticle())
        println("Application started")
    } catch (exception: Throwable) {
        println("Could not start application")
        exception.printStackTrace()
    }
}

class MainVerticle : CoroutineVerticle() {
    override suspend fun start() {
        val router = createRouter()

        // Start the server
        vertx.createHttpServer()
            .requestHandler(router)
            .listenAwait(config.getInteger("http.port", 8080))
    }

    private fun createRouter() = Router.router(vertx).apply {
        get("/").handler(handlerRoot)
        get("/islands").handler(handlerIslands)
        get("/countries").handler(handlerCountries)
        get().handler(StaticHandler.create("kotlin-vertx-react.js"));
    }

    //
    // Handlers
    val handlerRoot = Handler<RoutingContext> { req ->
        req.response().end(hello() + "  /  " + Sample().checkMe())
    }

    val handlerIslands = Handler<RoutingContext> { req ->
        req.response().endWithJson(MOCK_ISLANDS)
    }

    val handlerCountries = Handler<RoutingContext> { req ->
        req.response().endWithJson(MOCK_ISLANDS.map { it.country }.distinct().sortedBy { it.code })
    }

    //
    // Mock data

    private val MOCK_ISLANDS by lazy {
        listOf(
            Island("Kotlin", Country("Russia", "RU")),
            Island("Stewart Island", Country("New Zealand", "NZ")),
            Island("Cockatoo Island", Country("Australia", "AU")),
            Island("Tasmania", Country("Australia", "AU"))
        )
    }

    //
    // Utilities

    /**
     * Extension to the HTTP response to output JSON objects.
     */
    fun HttpServerResponse.endWithJson(obj: Any) {
        this.putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encodePrettily(obj))
    }
}
