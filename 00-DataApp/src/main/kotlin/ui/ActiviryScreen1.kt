package ui

import alertDialog.Alert
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.DatabaseHelper
import dataclass.Cliente
import dataclass.Materiale
import dataclass.Rapportino



@Composable
fun Activity1Screen(onBack: () -> Unit) {
    // Variabili per Barre di ricerca
    var clienti by remember { mutableStateOf(listOf<Cliente>()) }
    var clienteSelezionato by remember { mutableStateOf<Cliente?>(null) }

    var nome by remember { mutableStateOf("") }
    var oreLavoro by remember { mutableStateOf("") }
    var totaleOre by remember { mutableStateOf(0.0) }

    var searchText by remember { mutableStateOf("") }
    var searchTextMat by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val quantitaFocusRequester = remember { FocusRequester() }

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var showDeleteConfirm by remember { mutableStateOf(false) }


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


            /* COLONNA SINISTRA CLIENTI */

            // ---------------------------
            // COLONNA SINISTRA: CLIENTI (TABELLA + RICERCA)
            // ---------------------------

            Column(
                modifier = Modifier
                    .weight(1.4f)
                    .padding(horizontal = 10.dp)
                    .padding(end = 10.dp)
                    // .background(Color(0xFFF7F7F7)) // grigio chiarissimo
            ) {

                Text("Seleziona Cliente", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                // -----------------------------------------
                // BARRA DI RICERCA CLIENTI
                // -----------------------------------------

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .clickable { focusRequester.requestFocus() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 12.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxSize()               // 🔥 ora il click funziona ovunque
                            .focusRequester(focusRequester),
                        decorationBox = { innerTextField ->
                            if (searchText.isEmpty()) {
                                Text(
                                    "Cerca cliente...",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    )
                }

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
                        .height(300.dp)
                        .border(1.dp, Color.Gray)
                        .padding(horizontal = 2.dp)
                ) {
                    items(clientiFiltrati) { cliente ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                                .border(
                                    1.dp,
                                    if (clienteSelezionato?.id == cliente.id) Color.Blue else Color.LightGray
                                )
                                .padding(3.dp)
                                .clickable {
                                    clienteSelezionato = cliente // Selezione Cliente
                                    totaleOre = DatabaseHelper.getTotaleOreCliente(cliente.id) // Riepilogo ore
                                    materialiUsati = DatabaseHelper.getMaterialiUsatiDaCliente(cliente.id) // Riepilogo materiali usati
                                }
                        ) {
                            Text(cliente.fullName, modifier = Modifier.weight(1f), fontSize = 11.sp)
                            Text(cliente.tipologia, modifier = Modifier.weight(1f), fontSize = 11.sp)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // -----------------------------------------
                // TABELLA MATERIALE
                // -----------------------------------------

                Text("Materiale", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                // Barra di Ricerca Materiale
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .clickable { focusRequester.requestFocus() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    BasicTextField(
                        value = searchTextMat,
                        onValueChange = { searchTextMat = it },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 12.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxSize()               // 🔥 ora il click funziona ovunque
                            .focusRequester(focusRequester),
                        decorationBox = { innerTextField ->
                            if (searchTextMat.isEmpty()) {
                                Text(
                                    "Cerca Materiale...",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Filtraggio Materiale
                val materialiFiltrati = listaMateriali.filter {
                    it.marca.contains(searchTextMat, ignoreCase = true) ||
                            it.modello.contains(searchTextMat, ignoreCase = true) ||
                                it.codice.contains(searchTextMat, ignoreCase = true)
                }

                // Tabella Materiale
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .border(1.dp, Color.Gray)
                        .padding(horizontal = 2.dp)
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
                                .padding(3.dp)
                                .clickable {
                                    selectedMateriale = materiale
                                    quantitaFocusRequester.requestFocus()
                                }
                        ) {
                            Text(materiale.marca, modifier = Modifier.weight(1f), fontSize = 11.sp)
                            Text(materiale.modello, modifier = Modifier.weight(1f), fontSize = 11.sp)
                            Text(materiale.codice, modifier = Modifier.weight(1f), fontSize = 11.sp)
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

            }

            // DIVIDER
            Divider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
            )
            /* FIME COLONNA SINISTRA CLIENTI */



            /* COLONNA CENTRALE DATI LAVORO */

            // ---------------------------
            // COLONNA CENTRALE - DATI LAVORO
            // ---------------------------
            Column(modifier = Modifier

                .weight(1f)
                .padding(horizontal = 10.dp)
                .padding(end = 10.dp)

            ){
                // Testo Nome
                Text("Inserisci Nome", fontSize = 16.sp)
                    // Spacer
                        Spacer(Modifier.height(10.dp))

                BasicTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 13.sp,
                        color = Color.Black   // 🔥 testo visibile
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) { innerTextField ->

                    Box {
                        // Placeholder
                        if (nome.isEmpty()) {
                            Text(
                                "Nome",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Testo digitato
                        innerTextField()
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Testo Ore Lavoro
                    Text("Ore Lavoro", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                BasicTextField(
                    value = oreLavoro,
                    onValueChange = { oreLavoro = it },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 13.sp,
                        color = Color.Black   // 🔥 testo visibile
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) { innerTextField ->

                    Box {
                        // Placeholder
                        if (oreLavoro.isEmpty()) {
                            Text(
                                "Nome",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Testo digitato
                        innerTextField()
                    }
                }

                Spacer(Modifier.height(10.dp))
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

                Spacer(Modifier.height(10.dp))
                    Text("Tipologia Lavoro", fontSize = 16.sp)
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

                // Alert Dialog
                Alert().CustomAlertDialog(
                    show = showAlert,
                    title = "Avviso",
                    message = alertMessage,
                    onClose = { showAlert = false }
                )

                // BUTTON SALVA
                Button(onClick = {
                    if (clienteSelezionato != null && nome.isNotBlank() && oreLavoro.isNotBlank() && oreLavoro.toDoubleOrNull() != null) {

                        // Salvo il rapportino
                        DatabaseHelper.insertRapportino(
                            nome = nome,
                            ore = oreLavoro.toDouble(),
                            clienteId = clienteSelezionato!!.id,
                            tipologia = clienteSelezionato!!.tipologia
                        )

                        // Recupero ID ultimo rapportino
                        val idRapportino = DatabaseHelper.getLastRapportinoId()

                        // Salvo materiali usati
                        materialiUsati.forEach { (mat, qty) ->
                            DatabaseHelper.insertRapportinoMateriale(
                                idRapportino,
                                mat.id!!,
                                qty
                            )
                        }

                        alertMessage = "Rapportino Salvato!"
                        showAlert = true
                        nome = ""
                        oreLavoro = ""
                        clienteSelezionato = null
                        materialiUsati = emptyList()

                    } else {

                        alertMessage = "Controlla tutti i campi!"
                        showAlert = true

                    }
                },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(10.dp)
                )

                {
                    Text("Salva Rapportino")
                }

                Spacer(Modifier.height(6.dp))

                // BUTTON Torna al Menu
                Button(onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(10.dp),)


                {
                    Text("Torna al menu")
                }

                Spacer(Modifier.height(6.dp))

                // Alert Dialog per eliminazione Cliente+Dipendenze
                if (showDeleteConfirm) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirm = false },
                        title = { Text("Conferma eliminazione") },
                        text = { Text("Sei sicuro di voler eliminare questo cliente e tutti i suoi rapportini?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    clienteSelezionato?.let {
                                        DatabaseHelper.deleteClienteConRapportini(it.id)
                                    }

                                    showDeleteConfirm = false
                                    alertMessage = "Cliente e rapportini eliminati!"
                                    showAlert = true

                                    // Reset selezione e aggiorna lista
                                    clienteSelezionato = null
                                    clienti = DatabaseHelper.getAllClienti()
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteConfirm = false }) {
                                Text("Annulla")
                            }
                        }
                    )
                }
                // BUTTON Elimina Cliente
                Button(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.Red,
                        Color.White,
                    ),
                    enabled = clienteSelezionato != null
                ) {
                    Text("Elimina Cliente")
                }

                // Spacer per tenere il button "Aggiungi Materiale" in fondo alla pagina
                Spacer(Modifier.weight(1f))

                // Text Quantità
                TextField(
                    value = quantita,
                    onValueChange = { quantita = it },
                    label = { Text("Quantità", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().focusRequester(quantitaFocusRequester),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    singleLine = true,
                )

                Spacer(Modifier.height(10.dp))

                // BUTTON Aggiungi materiale al rapportino
                val quantitaDouble = quantita.toDoubleOrNull()

                Button(
                    onClick = {
                        if (selectedMateriale != null && quantita.isNotBlank() && quantitaDouble != null) {
                            materialiUsati = materialiUsati + (selectedMateriale!! to quantitaDouble)
                            quantita = ""
                        } else {
                            alertMessage = "Inserire un Numero nel campo Quantità!"
                            showAlert = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Aggiungi materiale")
                }
            }

            // DIVIDER
            Divider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
            )

            /* FINE COLONNA CENTRALE DATI LAVORO */


            /* COLONNA DESTRA RIEPILOGO */

            // ---------------------------
            // COLONNA DESTRA: RIEPILOGO CLIENTI
            // ---------------------------
            Column( modifier = Modifier
                .weight(0.6f)
                .padding(horizontal = 10.dp)
                .padding(end = 10.dp)
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
                        .height(300.dp)
                        .border(1.dp, Color.LightGray)
                ) {
                    items(materialiUsati) { (mat, qty) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                        ) {
                            Text("${mat.marca} ${mat.modello}", modifier = Modifier.weight(1f), fontSize = 12.sp)
                            Text("x $qty", modifier = Modifier.weight(0.3f), fontSize = 12.sp)
                        }
                    }
                }
            }
            /* FINE COLONNA DESTRA RIEPILOGO */
        } // chiusura ROW
    } // chiusura box
}

