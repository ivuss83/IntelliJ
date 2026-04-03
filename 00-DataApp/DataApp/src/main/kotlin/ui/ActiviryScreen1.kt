package org.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.DatabaseHelper
import printdata.generaPdf

@Composable
fun Activity1Screen(onBack: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var oreLavoro by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Inserisci Nome",
            fontSize = 16.sp)
        Spacer(Modifier.height(10.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            singleLine = true,
        )

        Spacer(Modifier.height(16.dp))

        Text("Ore Lavoro",
            fontSize = 16.sp)
        Spacer(Modifier.height(10.dp))

        TextField(
            value = oreLavoro,
            onValueChange = { oreLavoro = it },
            label = { Text("Ore Lavoro", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            singleLine = true,
        )

        /*** Button SALVA ***/
        Button(onClick = {
            if (nome.isNotBlank() && oreLavoro.isNotBlank()) {
                DatabaseHelper.insertRapportino(nome, oreLavoro.toDouble())
                message = "Nome \"$nome\" salvato nel DB!"
                nome = ""
                oreLavoro = ""
            } else {
                message = "Inserisci i Dati!"
            }
        }) {
            Text("Salva nel database")
        }

        /*** Button PDF ***/
        Button(onClick = {
            DatabaseHelper.insertRapportino(nome, oreLavoro.toDouble())
            generaPdf(nome, oreLavoro.toDouble())
        }) {
            Text("Salva e genera PDF")
        }

        if (message.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(message)
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Torna al menu")
        }
    }
}