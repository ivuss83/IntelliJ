package alertDialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

class Alert {

    @Composable
    fun CustomAlertDialog(
        show: Boolean,
        title: String = "Avviso",
        message: String,
        onClose: () -> Unit
    ) {
        if (show) {
            AlertDialog(
                onDismissRequest = onClose,
                title = { Text(title) },
                text = { Text(message) },
                confirmButton = {
                    Button(onClick = onClose) {
                        Text("OK")
                    }
                }
            )
        }
    }
}