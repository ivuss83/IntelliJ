package app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import database.DatabaseHelper
import ui.Activity1Screen
import ui.ClienteActivity
import ui.MaterialeActivity
import ui.MenuScreen

enum class Screen {
    MENU,
    ACTIVITY1,
    CLIENTI,
    MATERIALE
}

@Composable
fun App() {

    // Inizializzo le tabelle del DB all'avvio e le creo se Necessario
    LaunchedEffect(Unit) {
        DatabaseHelper.createRapportinoTableIfNeeded()
        DatabaseHelper.createClientiTableIfNeeded()
        DatabaseHelper.createMaterialeTableIfNeeded()
        DatabaseHelper.createRapportinoMaterialeTableIfNeeded()
    }

    var currentScreen by remember { mutableStateOf(Screen.MENU) }

    MaterialTheme {
        when (currentScreen) {

            Screen.MENU -> MenuScreen(
                onActivity1 = { currentScreen = Screen.ACTIVITY1 },
                onClienti = { currentScreen = Screen.CLIENTI },
                onMateriale = { currentScreen = Screen.MATERIALE }
            )

            Screen.ACTIVITY1 -> Activity1Screen(
                onBack = { currentScreen = Screen.MENU }
            )

            Screen.CLIENTI -> ClienteActivity(
                onBackToMenu = { currentScreen = Screen.MENU },
                onAddCliente = {nome, cognome, tipologia -> DatabaseHelper.insertCliente(nome, cognome, tipologia) }
            )

            Screen.MATERIALE -> MaterialeActivity(
                onBackToMenu = { currentScreen = Screen.MENU },
            )
        }
    }
}