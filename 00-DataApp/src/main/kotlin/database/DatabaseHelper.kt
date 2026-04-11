package database

import dataclass.Cliente
import dataclass.Materiale
import java.sql.Connection
import java.sql.DriverManager

object DatabaseHelper {

    private const val DB_PATH = "C:\\IntelliJ\\Db.db"

    private fun connect(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
    }

    /* RAPPORTINO */

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

    /* CLIENTE */

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
        val sql = "SELECT id, nome || ' ' || cognome AS fullName, tipologia FROM clienti"

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                while (rs.next()) {
                    clienti.add(
                        Cliente(
                            id = rs.getInt("id"),
                            fullName = rs.getString("fullName"),
                            tipologia = rs.getString("tipologia")
                        )
                    )
                }
            }
        }
        return clienti
    }


    // Riepilogo totale ore cliente
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

    /* MATERIALE */

    // Creo Tabella Materiale se necesasrio
    fun createMaterialeTableIfNeeded() {
        val sql = """
            CREATE TABLE IF NOT EXISTS Materiale (
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            marca TEXT,
            modello TEXT,
            codice TEXT,
            prezzo Real
);
        """.trimIndent()

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        }
    }

    // Insert Materiale
    fun insertMateriale(marca: String, modello: String, codice: String, prezzo: Double) {
        val sql = "INSERT INTO Materiale(marca, modello, codice, prezzo) VALUES(?, ?, ?, ?)"

        connect().use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, marca)
                pstmt.setString(2, modello)
                pstmt.setString(3, codice)
                pstmt.setDouble(4, prezzo)
                pstmt.executeUpdate()
            }
        }
    }

    // Select Materiale
    fun getAllMateriale(): List<Materiale> {
        val materiali = mutableListOf<Materiale>()
        val sql = "SELECT * FROM Materiale"

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                while (rs.next()) {
                    materiali.add(
                        Materiale(
                            id = rs.getInt("id"),
                            marca = rs.getString("marca"),
                            modello = rs.getString("modello"),
                            codice = rs.getString("codice"),
                            prezzo = rs.getDouble("prezzo")
                        )
                    )
                }
            }
        }

        return materiali
    }

    // Update Materiale
    fun updateMateriale(id: Int, marca: String, modello: String, codice: String, prezzo: Double) {
        val sql = """
        UPDATE Materiale 
        SET marca = ?, modello = ?, codice = ?, prezzo = ?
        WHERE id = ?
    """.trimIndent()

        connect().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, marca)
                stmt.setString(2, modello)
                stmt.setString(3, codice)
                stmt.setDouble(4, prezzo)
                stmt.setInt(5, id)
                stmt.executeUpdate()
            }
        }
    }

    // Delete Materiale
    fun deleteMateriale(id: Int) {
        val sql = "DELETE FROM Materiale WHERE id = ?"

        connect().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }

    /* Rapportino - Materiale */
    fun createRapportinoMaterialeTableIfNeeded() {
        val sql = """
        CREATE TABLE IF NOT EXISTS RapportinoMateriale (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            rapportinoId INTEGER NOT NULL,
            materialeId INTEGER NOT NULL,
            quantita REAL NOT NULL,
            FOREIGN KEY (rapportinoId) REFERENCES Rapportino(id),
            FOREIGN KEY (materialeId) REFERENCES Materiale(id)
        );
    """.trimIndent()

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        }
    }

    // Inserimento Rapportino-Materiale
    fun insertRapportinoMateriale(rapportinoId: Int, materialeId: Int, quantita: Double) {
        val sql = "INSERT INTO RapportinoMateriale (rapportinoId, materialeId, quantita) VALUES (?, ?, ?)"

        connect().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, rapportinoId)
                stmt.setInt(2, materialeId)
                stmt.setDouble(3, quantita)
                stmt.executeUpdate()
            }
        }
    }

    // Select Materiale utilizzato nel Rapportino
    fun getMaterialiUsatiDaCliente(cliente: String): List<Pair<Materiale, Double>> {
        val result = mutableListOf<Pair<Materiale, Double>>()

        val sql = """
        SELECT m.*, SUM(rm.quantita) AS totaleQuantita
        FROM RapportinoMateriale rm
        JOIN Materiale m ON m.id = rm.materialeId
        JOIN Rapportino r ON r.id = rm.rapportinoId
        WHERE r.cliente = ?
        GROUP BY m.id
    """.trimIndent()

        connect().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, cliente)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val materiale = Materiale(
                        id = rs.getInt("id"),
                        marca = rs.getString("marca"),
                        modello = rs.getString("modello"),
                        codice = rs.getString("codice"),
                        prezzo = rs.getDouble("prezzo")
                    )
                    val quantita = rs.getDouble("totaleQuantita")
                    result.add(materiale to quantita)
                }
            }
        }

        return result
    }

    // Costo totale MAteriali nel Rapportino
    fun getTotaleMaterialiRapportino(rapportinoId: Int): Double {
        val sql = """
        SELECT SUM(m.prezzo * rm.quantita) AS totale
        FROM RapportinoMateriale rm
        JOIN Materiale m ON m.id = rm.materialeId
        WHERE rm.rapportinoId = ?
    """.trimIndent()

        connect().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, rapportinoId)
                val rs = stmt.executeQuery()
                return rs.getDouble("totale")
            }
        }
    }

    // Recupero ID ultimo rapportino inserito
    fun getLastRapportinoId(): Int {
        val sql = "SELECT id FROM Rapportino ORDER BY id DESC LIMIT 1"

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                if (rs.next()) {
                    return rs.getInt("id")
                }
            }
        }
        return -1 // in caso non ci siano rapportini
    }
}
