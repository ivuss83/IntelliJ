package ui

import alertDialog.Alert
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.DatabaseHelper

@Composable
fun ImpostazioniActivity(onBack: () -> Unit) {

    // Recupero valori dal DB
    val impostazioni = DatabaseHelper.getImpostazioni()

    var tariffaOraria by remember { mutableStateOf(impostazioni.tariffaOraria.toString()) }
    var rincaroMateriale by remember { mutableStateOf(impostazioni.rincaroMateriale.toString()) }

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Impostazioni", fontSize = 22.sp)
        Spacer(Modifier.height(20.dp))

        // TARIFa ORARIA
        Text("Tariffa oraria (€ / ora)", fontSize = 14.sp)
        Spacer(Modifier.height(5.dp))

        BasicTextField(
            value = tariffaOraria,
            onValueChange = { tariffaOraria = it },
            singleLine = true,
            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        )

        Spacer(Modifier.height(20.dp))

        // RINCARO MATERIALE
        Text("Rincaro materiale (%)", fontSize = 14.sp)
        Spacer(Modifier.height(5.dp))

        BasicTextField(
            value = rincaroMateriale,
            onValueChange = { rincaroMateriale = it },
            singleLine = true,
            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        )

        Spacer(Modifier.height(30.dp))

        // BOTTONE SALVA

        // Alert Dialog
        Alert().CustomAlertDialog(
            show = showAlert,
            title = "Avviso",
            message = alertMessage,
            onClose = { showAlert = false }
        )

        Button(
            onClick = {
                var t = tariffaOraria.toDoubleOrNull()
                var r = rincaroMateriale.toDoubleOrNull()

                if (t == null || r == null) {
                    alertMessage = "Inserisci valori numerici validi"
                    showAlert = true

                } else {
                    DatabaseHelper.saveImpostazioni(t, r)
                    alertMessage = "Impostazioni salvate!"
                    showAlert = true

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salva")
        }

        Spacer(Modifier.height(20.dp))

        // TORNA AL MENU
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors( Color.Gray)
        ) {
            Text("Torna al menu")
        }

        // ALERT
        if (showAlert) {
            Spacer(Modifier.height(20.dp))
            Text(alertMessage, color = Color.Red)
        }
    }
}