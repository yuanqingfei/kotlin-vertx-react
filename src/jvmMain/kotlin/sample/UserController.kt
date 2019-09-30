package sample

import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.coroutines.awaitResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory.getLogger
import java.lang.Exception

class UserController(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun create(ctx: RoutingContext) {
        try{
            logger.info("creating user")
            val userJson = ctx.bodyAsJson
            println("user: " + userJson)
            val user = userJson.mapTo(User::class.java)
            println("user2: " + user)
//        awaitResult<SendMessageRequest, SendMessageResult> { sqs.sendMessageAsync(queueUrl, userJson.encode(), it) }
            userService.create(user)
            ctx.response().end()
        } catch(e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun updateById(ctx: RoutingContext) {
        logger.info("updating user")
        val id = ctx.request().getParam("id").toInt()
        val user = ctx.bodyAsJson.mapTo(User::class.java)
        userService.updateById(user, id)
        ctx.response().end()
    }

    suspend fun getById(ctx: RoutingContext) {
        logger.info("getting user")
        val id = ctx.request().getParam("id").toInt()
        val user = userService.getById(id)
//        val albumTitle = albumClient.getFirstAlbum(1).getString("title")
//        val userWithAlbumTitle = user.copy(albumTitle = albumTitle)
        ctx.response()
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end(JsonObject.mapFrom(user).encode())

    }

    suspend fun deleteById(ctx: RoutingContext) {
        logger.info("deleting user")
        val id = ctx.request().getParam("id").toInt()
        userService.deleteById(id)
        ctx.response().end()
    }

    suspend fun getAll(ctx: RoutingContext) {
        logger.info("getting all users")
        val users = userService.getAll()
        val jsons = Json.array(users.map(JsonObject::mapFrom))
        ctx.response()
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end(jsons.encode())
    }
}