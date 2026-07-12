package dataclass

data class Materiale(
    val id: Int? = null,
    val marca: String,
    val modello: String,
    val codice: String,
    val prezzo: Double
)
