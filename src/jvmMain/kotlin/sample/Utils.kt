package sample

import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.kotlin.core.executeBlockingAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun getLogger(name: String): Logger = LoggerFactory.getLogger(name)

suspend fun <T> execBlocking(vx: Vertx, fn: () -> T): T? =
    withContext(Dispatchers.Default) {
        vx.executeBlockingAwait<T> { promise: Promise<T> ->
            try {
                promise.complete(fn())
            } catch (t: Throwable) {
                promise.fail(t)
            }
        }
    }

suspend fun <T> retry(times: Int, interval: Long, fn: () -> T?): T? {
    return try {
        fn()
    } catch (t: Throwable) {
        if (times == 0) {
            throw t
        }
        delay(interval)
        retry(times - 1, interval, fn)
    }
}