package database

import dataclass.Cliente
import java.sql.Connection
import java.sql.DriverManager

object DatabaseHelper {

    private const val DB_PATH = "C:\\IntelliJ\\Db.db"

    private fun connect(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
    }

    // Creo Tabella Rapportino se necessario
    fun createRapportinoTableIfNeeded() {
        val sql = """
            CREATE TABLE IF NOT EXISTS Rapportino (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT NOT NULL,
            oreLavoro REAL NOT NULL,
            cliente TEXT NOT NULL,
            tipologia TEXT NOT NULL
            );
        """.trimIndent()

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        }
    }

    // insert Rapportino
    fun insertRapportino(nome: String, ore: Double, cliente: String, tipologia: String) {
        val sql = "INSERT INTO Rapportino(Nome, OreLavoro, cliente, tipologia) VALUES(?, ?, ?, ?)"

        connect().use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, nome)
                pstmt.setDouble(2, ore)
                pstmt.setString(3, cliente)
                pstmt.setString(4, tipologia)
                pstmt.executeUpdate()
            }
        }
    }

    // Creo Tabella clienti se necesasrio
    fun createClientiTableIfNeeded() {
        val sql = """
            CREATE TABLE IF NOT EXISTS Clienti (
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            nome TEXT,
            cognome TEXT,
            tipologia TEXT
);
        """.trimIndent()

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        }
    }

    // Insert Cliente
    fun insertCliente(nome: String, cognome: String, tipologia: String) {
        val sql = "INSERT INTO Clienti (nome, cognome, tipologia) VALUES (?, ?, ?)"

        connect().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, nome)
                stmt.setString(2, cognome)
                stmt.setString(3, tipologia)
                stmt.executeUpdate()
            }
        }
    }

    // Select Cliente + Tipologia Lavoro
    fun getAllClienti(): List<Cliente> {
        val clienti = mutableListOf<Cliente>()
        val sql = "SELECT nome || ' ' || cognome AS fullName, tipologia FROM clienti"

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                while (rs.next()) {
                    clienti.add(
                        Cliente(
                            fullName = rs.getString("fullName"),
                            tipologia = rs.getString("tipologia")
                        )
                    )
                }
            }
        }
        return clienti
    }


    // Riepilogo totale cliente
    fun getTotaleOreCliente(cliente: String): Double {
        val sql = "SELECT SUM(oreLavoro) AS totale FROM Rapportino WHERE cliente = ?"
        var totale = 0.0

        connect().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, cliente)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    totale = rs.getDouble("totale")
                }
            }
        }

        return totale
    }
}
