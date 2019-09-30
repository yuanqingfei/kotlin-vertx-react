package sample

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    val name: String
}

data class Country(val name: String, val code: String)

data class Island(val name: String, val country: Country)

fun hello(): String = "Hello from ${Platform.name}"

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val albumTitle: String? = null
)