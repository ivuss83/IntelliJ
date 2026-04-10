
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Stefano",
        state = rememberWindowState(
            // position = WindowPosition.Aligned(Alignment.Center)
            placement = WindowPlacement.Fullscreen,
        )
    ) {
        App()
    }
}

// ✅ 1) Modifica il tuo main()
//Aggiungi il windowState e passalo ad App():
//
//kotlin
//fun main() = application {
//
//    val windowState = rememberWindowState(
//        width = 900.dp,
//        height = 600.dp
//    )
//
//    Window(
//        onCloseRequest = ::exitApplication,
//        state = windowState,
//        title = "Rapportino"
//    ) {
//        App(windowState)
//    }
//}
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