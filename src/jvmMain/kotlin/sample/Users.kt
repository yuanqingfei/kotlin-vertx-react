package sample

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object Users : Table() {

    val id = integer("id").primaryKey().autoIncrement()
    val seqId = integer("seqId").uniqueIndex()
    val name = varchar("name", length = 50)
    val email = varchar("email", length = 50)

    fun map(row: ResultRow) =
        User(
            id = row[Users.seqId],
            name = row[Users.name],
            email = row[Users.email]
        )

    fun of(user: User, st: UpdateBuilder<Int>) {
        st[seqId] = user.id?:0
        st[name] = user.name
        st[email] = user.email
    }
}
