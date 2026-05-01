package ui

import alertDialog.Alert
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    var showDeleteMaterialConfirm by remember { mutableStateOf(false) }

    var materialiUsatoInRapportini by remember { mutableStateOf(0) }


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
                onValueChange = { prezzo = it.replace(',', '.') },
                label = { Text("Prezzo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(Modifier.height(10.dp))

            // Alert Dialog
            Alert().CustomAlertDialog(
                show = showAlert,
                title = "Avviso",
                message = alertMessage,
                onClose = { showAlert = false }
            )

                // RIGA ICONE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // ICONA SALVA MATERIALE
                    IconButton(
                        onClick = {
                            try {
                                if (
                                    marca.isNotBlank() &&
                                    modello.isNotBlank() &&
                                    codice.isNotBlank() &&
                                    prezzo.isNotBlank()
                                ) {

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

                                    alertMessage = "Materiale Salvato!"
                                    showAlert = true

                                } else {
                                    alertMessage = "Compila tutti i campi!"
                                    showAlert = true
                                }
                            } catch (e: Exception) {
                                alertMessage = "Errore nel Salvataggio: ${e.message}"
                                showAlert = true
                            }
                        }
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Salva Materiale",
                                tint = Color(0xFF0D47A1)
                            )

                            Text(
                                "Salva",
                                fontSize = 10.sp,
                                color = Color(0xFF0D47A1)
                            )
                        }
                    }


                    // ICONA AGGIORNA MATERIALE
                    IconButton(
                        onClick = {
                            try {
                                if (selectedMateriale != null) {

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

                                    alertMessage = "Materiale Aggiornato!"
                                    showAlert = true

                                } else {
                                    alertMessage = "Seleziona un materiale da aggiornare!"
                                    showAlert = true
                                }
                            } catch (e: Exception) {
                                alertMessage = "Errore nell'Aggiornamento: ${e.message}"
                                showAlert = true
                            }
                        }
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Aggiorna Materiale",
                                tint = if (selectedMateriale != null)
                                    Color(0xFF0D47A1)
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )

                            Text(
                                "Aggiorna",
                                fontSize = 10.sp,
                                color = if (selectedMateriale != null)
                                    Color(0xFF0D47A1)
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }


                    // ICONA ELIMINA MATERIALE
                    IconButton(
                        onClick = {
                            if (selectedMateriale != null) {

                                // 1️⃣ Conta quanti rapportini usano questo materiale
                                val count = DatabaseHelper.countRapportiniConMateriale(selectedMateriale!!.id!!)

                                if (count > 0) {
                                    // 2️⃣ Salvo il numero per mostrarlo nella dialog
                                    materialiUsatoInRapportini = count

                                    // 3️⃣ Apro la dialog di conferma speciale
                                    showDeleteMaterialConfirm = true

                                } else {
                                    // 4️⃣ Eliminazione diretta (nessuna dipendenza)
                                    DatabaseHelper.deleteMaterialeConDipendenze(selectedMateriale!!.id!!)
                                    listaMateriali = DatabaseHelper.getAllMateriale()

                                    selectedMateriale = null
                                    marca = ""
                                    modello = ""
                                    codice = ""
                                    prezzo = ""

                                    alertMessage = "Materiale Eliminato!"
                                    showAlert = true
                                }

                            } else {
                                alertMessage = "Seleziona un materiale da eliminare!"
                                showAlert = true
                            }
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Elimina Materiale",
                                tint = if (selectedMateriale != null)
                                    Color(0xFFD32F2F)
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )

                            Text(
                                "Elimina",
                                fontSize = 10.sp,
                                color = if (selectedMateriale != null)
                                    Color(0xFFD32F2F)
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }

                    // ICONA TORNA AL MENU
                    IconButton(
                        onClick = onBackToMenu
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Torna al menu",
                                tint = Color(0xFF64B5F6)   // azzurro medio
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                "Menu",
                                fontSize = 10.sp,
                                color = Color(0xFF64B5F6)
                            )
                        }
                    }
                }

            if (showDeleteMaterialConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteMaterialConfirm = false },
                    title = { Text("Conferma eliminazione") },
                    text = {
                        Text(
                            "Questo materiale è stato usato in $materialiUsatoInRapportini " +
                                    "rapportini. Sei sicuro di volerlo eliminare?"
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectedMateriale?.let {
                                    DatabaseHelper.deleteMaterialeConDipendenze(it.id!!)
                                }

                                showDeleteMaterialConfirm = false
                                alertMessage = "Materiale eliminato!"
                                showAlert = true

                                selectedMateriale = null
                                listaMateriali = DatabaseHelper.getAllMateriale()

                                marca = ""
                                modello = ""
                                codice = ""
                                prezzo = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                Color(0xFF1976D2),   // blu deciso
                                contentColor = Color.White            // testo bianco
                            )
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDeleteMaterialConfirm = false },
                            colors = ButtonDefaults.buttonColors(
                                Color(0xFF1976D2),   // blu deciso
                                contentColor = Color.White            // testo bianco
                            )
                            )
                        {
                            Text("Annulla")
                        }
                    }
                )
            }

                Spacer(Modifier.height(10.dp))

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
                            .padding(2.dp)
                            .border(
                                1.dp,
                                if (selectedMateriale?.id == materiale.id) Color.Blue else Color.LightGray
                            )
                            .padding(2.dp)
                            .clickable {
                                selectedMateriale = materiale
                                marca = materiale.marca
                                modello = materiale.modello
                                codice = materiale.codice
                                prezzo = materiale.prezzo.toString()
                            }
                    ) {
                        Text(materiale.marca, modifier = Modifier.weight(2f). padding(all = 3.dp), fontSize = 12.sp)
                        Text(materiale.modello, modifier = Modifier.weight(2f).padding(all = 3.dp), fontSize = 12.sp)
                        Text(materiale.codice, modifier = Modifier.weight(1f).padding(all = 3.dp), fontSize = 12.sp)
                        Text("${materiale.prezzo} €", modifier = Modifier.weight(1f).padding(all = 3.dp), fontSize = 12.sp)
                    }
                }
            }
        }
    }

}