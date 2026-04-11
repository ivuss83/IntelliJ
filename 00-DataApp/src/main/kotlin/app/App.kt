package app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import database.DatabaseHelper
import kotlinx.coroutines.delay
import ui.Activity1Screen
import ui.ClienteActivity
import ui.MaterialeActivity
import ui.MenuScreen
import kotlin.time.Duration.Companion.milliseconds

enum class Screen {
    MENU,
    ACTIVITY1,
    CLIENTI,
    MATERIALE
}

@Composable
fun App(windowState: WindowState) {

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

            Screen.MENU -> {
                LaunchedEffect(Unit) {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = DpSize(900.dp, 600.dp)
                    windowState.position = WindowPosition.Aligned(Alignment.Center)
                }

                MenuScreen(
                    // torna alla dimensione normale x MENU SCREEN
                    onActivity1 = { currentScreen = Screen.ACTIVITY1 },
                    onClienti = { currentScreen = Screen.CLIENTI },
                    onMateriale = { currentScreen = Screen.MATERIALE }
                )
            }

            Screen.ACTIVITY1 -> {
                LaunchedEffect(Unit) {
                    windowState.placement = WindowPlacement.Maximized
                }

                Activity1Screen(
                    onBack = { currentScreen = Screen.MENU },
                )
            }

            Screen.CLIENTI -> {
                LaunchedEffect(Unit) {
                    windowState.placement = WindowPlacement.Floating
                    windowState.position = WindowPosition.Aligned(Alignment.Center)
                }

                ClienteActivity(
                    onBackToMenu = { currentScreen = Screen.MENU },
                    onAddCliente = { nome, cognome, tipologia ->
                        DatabaseHelper.insertCliente(
                            nome,
                            cognome,
                            tipologia
                        )
                    }
                )
            }

            Screen.MATERIALE -> {
                LaunchedEffect(Unit) {
                    windowState.placement = WindowPlacement.Maximized
                    windowState.position = WindowPosition.Aligned(Alignment.Center)

                }
                MaterialeActivity(
                    onBackToMenu = { currentScreen = Screen.MENU },
                )
            }
        }
    }
}

//✅ 2) Modifica la tua funzione App() per accettare il windowState
//kotlin

//@Composable
//fun App(windowState: WindowState) {
//✅ 3) Aggiungi il cambio dimensione dentro il tuo when()
//Ecco la tua funzione riscritta correttamente:
//
//kotlin
//@Composable
//fun App(windowState: WindowState) {
//
//    LaunchedEffect(Unit) {
//        DatabaseHelper.createRapportinoTableIfNeeded()
//        DatabaseHelper.createClientiTableIfNeeded()
//        DatabaseHelper.createMaterialeTableIfNeeded()
//        DatabaseHelper.createRapportinoMaterialeTableIfNeeded()
//    }
//
//    var currentScreen by remember { mutableStateOf(Screen.MENU) }
//
//    MaterialTheme {
//        when (currentScreen) {
//
//            Screen.MENU -> {
//                // torna alla dimensione normale
//                LaunchedEffect(Unit) {
//                    windowState.placement = WindowPlacement.Floating
//                    windowState.size = DpSize(900.dp, 600.dp)
//                }
//
//                MenuScreen(
//                    onActivity1 = { currentScreen = Screen.ACTIVITY1 },
//                    onClienti = { currentScreen = Screen.CLIENTI },
//                    onMateriale = { currentScreen = Screen.MATERIALE }
//                )
//            }
//
//            Screen.ACTIVITY1 -> {
//                // fullscreen solo qui
//                LaunchedEffect(Unit) {
//                    windowState.placement = WindowPlacement.Fullscreen
//                }
//
//                Activity1Screen(
//                    onBack = { currentScreen = Screen.MENU }
//                )
//            }
//
//            Screen.CLIENTI -> {
//                LaunchedEffect(Unit) {
//                    windowState.placement = WindowPlacement.Floating
//                    windowState.size = DpSize(900.dp, 600.dp)
//                }
//
//                ClienteActivity(
//                    onBackToMenu = { currentScreen = Screen.MENU },
//                    onAddCliente = { nome, cognome, tipologia ->
//                        DatabaseHelper.insertCliente(nome, cognome, tipologia)
//                    }
//                )
//            }
//
//            Screen.MATERIALE -> {
//                LaunchedEffect(Unit) {
//                    windowState.placement = WindowPlacement.Floating
//                    windowState.size = DpSize(900.dp, 600.dp)
//                }
//
//                MaterialeActivity(
//                    onBackToMenu = { currentScreen = Screen.MENU }
//                )
//            }
//        }
//    }
//}