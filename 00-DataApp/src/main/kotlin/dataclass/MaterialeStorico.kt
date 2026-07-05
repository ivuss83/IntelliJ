package dataclass

data class MaterialeStorico(
    val idRiga: Int,
    val idRapportino: Int,
    val materiale: Materiale,
    val quantita: Double
)