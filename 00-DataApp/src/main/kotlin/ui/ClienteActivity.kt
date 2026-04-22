package ui

import alertDialog.Alert
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.sql.Connection
import java.sql.DriverManager

@Composable
fun ClienteActivity(
    onBackToMenu: () -> Unit,
    onAddCliente: (String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var cognome by remember { mutableStateOf("") }
    var tipologia by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                "Gestione Clienti",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Campo Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome", fontSize = 12.sp) },
                modifier = Modifier.width(300.dp),
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            // Campo Cognome
            OutlinedTextField(
                value = cognome,
                onValueChange = { cognome = it },
                label = { Text("Cognome", fontSize = 12.sp) },
                modifier = Modifier.width(300.dp),
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            // Campo Tipologia di lavoro
            OutlinedTextField(
                value = tipologia,
                onValueChange = { tipologia = it },
                label = { Text("Tipologia lavoro", fontSize = 12.sp) },
                modifier = Modifier.width(300.dp),
                singleLine = true
            )

            // MEssaggio di Errore se campi vuoti
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Pulsante Torna al menu (in basso a sinistra)
        Button(
            onClick = onBackToMenu,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .width(300.dp)
                .padding(10.dp),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                Color(0xFF1976D2),   // blu deciso
                contentColor = Color.White            // testo bianco
            )
        ) {
            Text("Torna al menu")
        }

        // Alert Dialog
        Alert().CustomAlertDialog(
            show = showAlert,
            title = "Avviso",
            message = alertMessage,
            onClose = { showAlert = false }
        )

        // Pulsante + (in basso a destra)
        FloatingActionButton(
            onClick = {
                if(nome.isEmpty() || cognome.isEmpty() || tipologia.isEmpty()) {
                    alertMessage = "Compila tutti i Campi!"
                    showAlert = true
                } else {
                    onAddCliente(nome, cognome, tipologia)
                    nome = ""
                    cognome = ""
                    tipologia = ""
                    errorMessage = ""
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            backgroundColor = Color(0xFF90CAF9),
            contentColor = Color.White
        ) {
            Text("+")
        }

    }
}

