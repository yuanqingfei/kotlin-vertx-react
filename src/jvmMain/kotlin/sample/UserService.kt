package sample

import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.createMissingTablesAndColumns
import org.jetbrains.exposed.sql.transactions.transaction

class UserService(private val vx: Vertx, private val config: JsonObject) {
    private val logger = getLogger("UserService")
    init {
        Database.connect(
            url = config.getString("url"),
            user = config.getString("user"),
            password = config.getString("password"),
            driver = config.getString("driver")
        )

        GlobalScope.launch {
            retry(times = 3, interval = 3000) {
                transaction {
                    createMissingTablesAndColumns(Users)
                }
            }
        }
    }

    suspend fun create(user: User): Unit? = execBlocking(vx) {
        transaction {
            logger.info("create begin")
            Users.insert { Users.of(user, it) }
            logger.info("create end")
        }
    }

    suspend fun updateById(user: User, id: Int): Int? = execBlocking(vx) {
        transaction {
            Users.update({ Users.id eq id }) {
                Users.of(user, it)
            }
        }
    }

    suspend fun getById(id: Int): User? = execBlocking(vx) {
        transaction {
            Users.map(Users.select { Users.id eq id }.first())
        }
    }

    suspend fun deleteById(id: Int): Int? = execBlocking(vx) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }

    suspend fun getAll(): List<User>? = execBlocking(vx) {
        transaction {
            Users.selectAll().map(Users::map)
        }
    }
}