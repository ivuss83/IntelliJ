
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Stefano",
        state = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center)
        )
    ) {
        App()
    }
}

