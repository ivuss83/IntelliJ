package alertDialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF1976D2),   // blu deciso
                            contentColor = Color.White            // testo bianco
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}