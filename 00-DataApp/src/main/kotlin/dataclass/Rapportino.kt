package dataclass

data class Rapportino(
    val id: Int,
    val nome: String,
    val oreLavoro: Double,
    val clienteId: Int,
    val tipologia: String
)