package sample

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.config.getConfigAwait
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.*
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

suspend fun main() {

    val myModule = module {
        single<Vertx> {Vertx.vertx()}

        single<ConfigStoreOptions> {ConfigStoreOptions()
            .setConfig(json { obj("path" to "conf/application.json") })
            .setType("file")
            .setFormat("json")}

        single<JsonObject>(named("topConfig")) {
            runBlocking {
                ConfigRetriever
                    .create(get(), ConfigRetrieverOptions().addStore(get()))
                    .getConfigAwait()
            }
        }

        single<JsonObject>(named("sqlConfig")){
                get<JsonObject>(named("topConfig"))
                    .getJsonObject("db").getJsonObject("sql")
        }

        single<UserService>{
                UserService(vx = get(), config = get(named("sqlConfig")))
        }

        single<UserController>{
                UserController(userService = get())
        }

        factory<WebVerticle>(named("${WebVerticle::class.java.canonicalName}")) {
            WebVerticle(userController = get())
        }
    }

    val koin = startKoin{
        modules(myModule)
    }.koin

    val vertx = koin.get<Vertx>()
    vertx.registerVerticleFactory(KoinVerticleFactory)
    try {
        vertx.deployVerticleAwait("${KoinVerticleFactory.prefix()}:${WebVerticle::class.java.canonicalName}")
        println("Application started")
    } catch (exception: Throwable) {
        println("Could not start application")
        exception.printStackTrace()
    }
}

class WebVerticle(private val userController: UserController) : CoroutineVerticle() {
    override suspend fun start() {
        Json.mapper.registerModule(KotlinModule())
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        router.post("/user").coroutineHandler { userController.create(it) }
        router.post("/user").coroutineHandler { userController.updateById(it) }
        router.get("/user").coroutineHandler { userController.getAll(it) }
        router.get("/user/:id").coroutineHandler { userController.getById(it) }
        router.delete("/user/:id").coroutineHandler { userController.deleteById(it) }
        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listenAwait(8080)
    }
}

fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit): Route = handler { ctx ->
    GlobalScope.launch(ctx.vertx().dispatcher()) {
        try {
            fn(ctx)
        } catch (e: Exception) {
            ctx.fail(e)
        }
    }
}
