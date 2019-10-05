package sample

import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

object KoinVerticleFactory : VerticleFactory, KoinComponent {
    override fun prefix(): String = "koin"

    override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
        return get(named(verticleName.substringAfter("koin:")))
    }
}