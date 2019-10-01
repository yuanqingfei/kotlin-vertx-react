//package sample
//
//import com.sun.corba.se.impl.presentation.rmi.IDLNameTranslatorImpl.get
//import io.vertx.core.Verticle
//import io.vertx.core.spi.VerticleFactory
//import org.koin.core.KoinComponent
//import org.koin.core.get
//
//object KoinVerticleFactory : VerticleFactory, KoinComponent {
//    override fun prefix(): String = "koin"
//
//    override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
//        return get(clazz = Class.forName(verticleName.substringAfter("koin:")).kotlin)
//
//    }
//}