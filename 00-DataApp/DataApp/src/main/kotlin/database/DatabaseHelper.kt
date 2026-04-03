package org.example.database

import java.sql.Connection
import java.sql.DriverManager

object DatabaseHelper {

    private const val DB_PATH = "C:\\DesktopApp\\Db.db"

    private fun connect(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
    }

    fun createRapportinoTableIfNeeded() {
        val sql = """
            CREATE TABLE IF NOT EXISTS Rapportino (
                Nome TEXT NOT NULL
            );
        """.trimIndent()

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        }
    }

    fun insertRapportino(nome: String, ore: Double) {
        val sql = "INSERT INTO Rapportino(Nome, OreLavoro) VALUES(?, ?)"

        connect().use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, nome)
                pstmt.setDouble(2, ore)
                pstmt.executeUpdate()
            }
        }
    }
}