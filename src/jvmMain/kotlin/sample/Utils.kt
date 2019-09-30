package sample

import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


suspend fun <T> execBlocking(vx: Vertx, fn: () -> T): T =
    withContext(Dispatchers.Default) {
        val handler = Handler { promise: Promise<T> ->
            try {
                promise.complete(fn())
            } catch (t: Throwable) {
                promise.fail(t)
            }
        }
        awaitResult<T> { vx.executeBlocking(handler, it) }
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