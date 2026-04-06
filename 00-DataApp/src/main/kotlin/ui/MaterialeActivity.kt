package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import database.DatabaseHelper
import dataclass.Materiale

@Composable
fun MaterialeActivity(
    onBackToMenu: () -> Unit,
) {

    // --- Stati per input ---
    var marca by remember { mutableStateOf("") }
    var modello by remember { mutableStateOf("") }
    var codice by remember { mutableStateOf("") }
    var prezzo by remember { mutableStateOf("") }

    // --- Stato tabella ---
    var listaMateriali by remember { mutableStateOf(DatabaseHelper.getAllMateriale()) }

    // --- Selezione Riga ---
    var selectedMateriale by remember { mutableStateOf<Materiale?>(null) }

    // --- Stato ricerca ---
    var searchQuery by remember { mutableStateOf("") }

    val materialiFiltrati = listaMateriali.filter {
        it.marca.contains(searchQuery, ignoreCase = true) ||
                it.modello.contains(searchQuery, ignoreCase = true) ||
                it.codice.contains(searchQuery, ignoreCase = true)
    }

    Row(modifier = Modifier.fillMaxSize().padding(20.dp)) {

        // -------------------------
        // COLONNA SINISTRA: FORM
        // -------------------------
        Column(
            modifier = Modifier.weight(1f).padding(end = 20.dp)
        ) {
            Text("Inserisci Materiale", style = MaterialTheme.typography.h5)

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = modello,
                onValueChange = { modello = it },
                label = { Text("Modello") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = codice,
                onValueChange = { codice = it },
                label = { Text("Codice") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = prezzo,
                onValueChange = { prezzo = it },
                label = { Text("Prezzo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(Modifier.height(10.dp))

            // Button Salva
            Button(
                onClick = {
                    if (marca.isNotBlank() && modello.isNotBlank() && codice.isNotBlank() && prezzo.isNotBlank()) {
                        DatabaseHelper.insertMateriale(
                            marca,
                            modello,
                            codice,
                            prezzo.toDoubleOrNull() ?: 0.0
                        )
                        listaMateriali = DatabaseHelper.getAllMateriale()

                        // pulizia campi
                        marca = ""
                        modello = ""
                        codice = ""
                        prezzo = ""
                    }
                }
            ) {
                Text("Salva Materiale")
            }

            Spacer(Modifier.height(10.dp))

            // Button Update
                Button(
                    onClick = {
                        DatabaseHelper.updateMateriale(
                            id = selectedMateriale!!.id!!,
                            marca = marca,
                            modello = modello,
                            codice = codice,
                            prezzo = prezzo.toDoubleOrNull() ?: 0.0
                        )

                        listaMateriali = DatabaseHelper.getAllMateriale()
                        selectedMateriale = null

                        marca = ""
                        modello = ""
                        codice = ""
                        prezzo = ""
                    }
                ) {
                    Text("Aggiorna Materiale")
                }

            Spacer(Modifier.height(10.dp))

            // Button Delete
                Button(
                    onClick = {
                        DatabaseHelper.deleteMateriale(selectedMateriale!!.id!!)
                        listaMateriali = DatabaseHelper.getAllMateriale()

                        selectedMateriale = null
                        marca = ""
                        modello = ""
                        codice = ""
                        prezzo = ""
                    }

                ) {
                    Text("Elimina Materiale")
                }

            Spacer(Modifier.height(10.dp))

            // Button torna al menu
            Button(onClick = onBackToMenu) {
                Text("Torna al Menu")
            }
        }

        // -------------------------
        // COLONNA DESTRA: TABELLA
        // -------------------------
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("Materiali Inseriti", style = MaterialTheme.typography.h5)

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cerca...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(Modifier.height(10.dp))

            // TABELLA MATERIALI
            LazyColumn(
                modifier = Modifier.fillMaxSize().border(1.dp, Color.Gray)
            ) {
                items(materialiFiltrati) { materiale ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(
                                1.dp,
                                if (selectedMateriale?.id == materiale.id) Color.Blue else Color.LightGray
                            )
                            .padding(8.dp)
                            .clickable {
                                selectedMateriale = materiale
                                marca = materiale.marca
                                modello = materiale.modello
                                codice = materiale.codice
                                prezzo = materiale.prezzo.toString()
                            }
                    ) {
                        Text(materiale.marca, modifier = Modifier.weight(1f))
                        Text(materiale.modello, modifier = Modifier.weight(1f))
                        Text(materiale.codice, modifier = Modifier.weight(1f))
                        Text("${materiale.prezzo} €", modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

}