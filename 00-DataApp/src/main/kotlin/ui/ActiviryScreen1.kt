package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import database.DatabaseHelper
import dataclass.Cliente
import dataclass.Materiale
import printdata.generaPdf

@Composable
fun Activity1Screen(onBack: () -> Unit) {

    var clienti by remember { mutableStateOf(listOf<Cliente>()) }
    var clienteSelezionato by remember { mutableStateOf<Cliente?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var nome by remember { mutableStateOf("") }
    var oreLavoro by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var totaleOre by remember { mutableStateOf(0.0) }

    var searchText by remember { mutableStateOf("") }

    // -----------------------------------------
    // Materiale - Rapportino
    // -----------------------------------------
    var listaMateriali by remember { mutableStateOf(DatabaseHelper.getAllMateriale()) }
    var selectedMateriale by remember { mutableStateOf<Materiale?>(null) }
    var quantita by remember { mutableStateOf("") }
    var materialiUsati by remember { mutableStateOf(listOf<Pair<Materiale, Double>>()) }

    LaunchedEffect(Unit) {
        clienti = DatabaseHelper.getAllClienti()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {

            // ---------------------------
            // COLONNA SINISTRA: CLIENTI (TABELLA + RICERCA)
            // ---------------------------

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 50.dp)
                    .padding(end = 10.dp)
            ) {

                Text("Seleziona Cliente", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                // -----------------------------------------
                // BARRA DI RICERCA CLIENTI
                // -----------------------------------------
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Cerca cliente...") },
                    singleLine = true
                )

                Spacer(Modifier.height(10.dp))

                // Filtraggio clienti
                val clientiFiltrati = clienti.filter {
                    it.fullName.contains(searchText, ignoreCase = true) ||
                            it.tipologia.contains(searchText, ignoreCase = true)
                }

                // -----------------------------------------
                // TABELLA CLIENTI
                // -----------------------------------------
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .border(1.dp, Color.Gray)
                        .padding(horizontal = 5.dp)
                ) {
                    items(clientiFiltrati) { cliente ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                                .border(
                                    1.dp,
                                    if (clienteSelezionato?.id == cliente.id) Color.Blue else Color.LightGray
                                )
                                .padding(8.dp)
                                .clickable {
                                    clienteSelezionato = cliente
                                    totaleOre = DatabaseHelper.getTotaleOreCliente(cliente.fullName)
                                }
                        ) {
                            Text(cliente.fullName, modifier = Modifier.weight(1f))
                            Text(cliente.tipologia, modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // -----------------------------------------
                // SELEZIONE MATERIALE (TABELLA)
                // -----------------------------------------
                Text("Materiale utilizzato", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(1.dp, Color.Gray)
                        .padding(horizontal = 5.dp)
                ) {
                    items(listaMateriali) { materiale ->
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
                                }
                        ) {
                            Text(materiale.marca, modifier = Modifier.weight(1f))
                            Text(materiale.modello, modifier = Modifier.weight(1f))
                            Text(materiale.codice, modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Quantità
                TextField(
                    value = quantita,
                    onValueChange = { quantita = it },
                    label = { Text("Quantità", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    singleLine = true,
                )

                Spacer(Modifier.height(10.dp))

                // Aggiungi materiale al rapportino
                Button(
                    onClick = {
                        if (selectedMateriale != null && quantita.isNotBlank()) {
                            materialiUsati = materialiUsati + (selectedMateriale!! to quantita.toDouble())
                            quantita = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aggiungi materiale")
                }
            }

            // DIVIDER
            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(horizontal = 10.dp)
            )

            // ---------------------------
            // COLONNA CENTRALE - DATI LAVORO
            // ---------------------------
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)) {

                Text("Inserisci Nome", fontSize = 16.sp)
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

                Text("Ore Lavoro", fontSize = 16.sp)
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

                Spacer(Modifier.height(16.dp))

                Text("Cliente", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                TextField(
                    value = clienteSelezionato?.fullName ?: "",
                    onValueChange = { },
                    label = { Text("Cliente", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    singleLine = true,
                    readOnly = true,
                )

                Spacer(Modifier.height(40.dp))

                Text("Cliente", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                TextField(
                    value = clienteSelezionato?.tipologia ?: "",
                    onValueChange = { },
                    label = { Text("Tipologia Lavoro", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    singleLine = true,
                    readOnly = true,
                )

                Spacer(Modifier.height(40.dp))

                // -----------------------------------------
                // Salva Rapportino + Materiali
                // -----------------------------------------
                Button(onClick = {
                    if (clienteSelezionato != null && nome.isNotBlank() && oreLavoro.isNotBlank()) {

                        // 1) Salvo il rapportino
                        DatabaseHelper.insertRapportino(
                            nome = nome,
                            ore = oreLavoro.toDouble(),
                            cliente = clienteSelezionato!!.fullName,
                            tipologia = clienteSelezionato!!.tipologia
                        )

                        // 2) Recupero ID ultimo rapportino
                        val idRapportino = DatabaseHelper.getLastRapportinoId()

                        // 3) Salvo materiali usati
                        materialiUsati.forEach { (mat, qty) ->
                            DatabaseHelper.insertRapportinoMateriale(
                                idRapportino,
                                mat.id!!,
                                qty
                            )
                        }

                        message = "Rapportino salvato!"
                        nome = ""
                        oreLavoro = ""
                        clienteSelezionato = null
                        materialiUsati = emptyList()

                    } else {
                        message = "Compila tutti i campi!"
                    }
                }) {
                    Text("Salva nel database")
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

            // DIVIDER
            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .padding(horizontal = 10.dp)
            )

            Spacer(Modifier.width(40.dp))

            // ---------------------------
            // COLONNA DESTRA: RIEPILOGO CLIENTI
            // ---------------------------
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp)
            ) {

                Text("Riepilogo Cliente", fontSize = 18.sp)
                Spacer(Modifier.height(20.dp))

                Text("Nome:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = clienteSelezionato?.fullName ?: "—",
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(20.dp))

                Text("Tipologia:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = clienteSelezionato?.tipologia ?: "—",
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(20.dp))

                Text("Totale ore lavorate:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "%.2f".format(totaleOre),
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50)
                )

                Spacer(Modifier.height(30.dp))

                // -----------------------------------------
                // RIEPILOGO MATERIALE USATO
                // -----------------------------------------
                Text("Materiali utilizzati:", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.LightGray)
                ) {
                    items(materialiUsati) { (mat, qty) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        ) {
                            Text("${mat.marca} ${mat.modello}", modifier = Modifier.weight(1f))
                            Text("x $qty", modifier = Modifier.weight(0.3f))
                        }
                    }
                }
            }
        } // chiusura ROW
    } // chiusura box
}