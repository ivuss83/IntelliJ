package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClienteActivity(
    onBackToMenu: () -> Unit,
    onAddCliente: (String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var cognome by remember { mutableStateOf("") }
    var tipologia by remember { mutableStateOf("") }

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

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome", fontSize = 12.sp) },
                modifier = Modifier.width(250.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = cognome,
                onValueChange = { cognome = it },
                label = { Text("Cognome", fontSize = 12.sp) },
                modifier = Modifier.width(250.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = tipologia,
                onValueChange = { tipologia = it },
                label = { Text("Tipologia lavoro", fontSize = 12.sp) },
                modifier = Modifier.width(250.dp)
            )
        }

        // Pulsante Torna al menu (in basso a sinistra)
        Button(
            onClick = onBackToMenu,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text("Torna al menu")
        }

        // Pulsante + (in basso a destra)
        FloatingActionButton(
            onClick = {
                onAddCliente(nome, cognome, tipologia)
                nome = ""
                cognome = ""
                tipologia = ""
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Text("+")
        }
    }
}
