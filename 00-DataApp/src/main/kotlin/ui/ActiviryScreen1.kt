package ui

import alertDialog.Alert
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.DatabaseHelper
import dataclass.Cliente
import dataclass.Impostazioni
import dataclass.Materiale
import dataclass.Rapportino
import printdata.generaPdf

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

    var selectedMaterialeUsato by remember { mutableStateOf<Pair<Materiale, Double>?>(null) }


    // -----------------------------------------
    // Materiale - Rapportino
    // -----------------------------------------
    var listaMateriali by remember { mutableStateOf(DatabaseHelper.getAllMateriale()) }
    var selectedMateriale by remember { mutableStateOf<Materiale?>(null) }
    var quantita by remember { mutableStateOf("") }

    // Materiali già usati dal cliente (storico)
    var materialiRiepilogo by remember { mutableStateOf(listOf<Pair<Materiale, Double>>()) }

    // Materiali del nuovo rapportino
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

            /* COLONNA SINISTRA DATI RAPPORTINO */

            // ---------------------------
            // COLONNA SINISTRA - DATI LAVORO
            // ---------------------------
            Column(modifier = Modifier

                .weight(1f)
                .padding(horizontal = 10.dp)
                //.padding(end = 10.dp)

            ){
                // Testo Nome
                Text("Nome", fontSize = 16.sp)
                // Spacer
                Spacer(Modifier.height(10.dp))

                BasicTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 13.sp,
                        color = Color.Blue   // 🔥 testo visibile
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
                        color = Color.Blue   // 🔥 testo visibile
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
                                "Ore lavoro",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Testo digitato
                        innerTextField()
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Testo Cliente
                Text("Cliente", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                BasicTextField(
                    value = clienteSelezionato?.fullName ?: "", // Se clienteSelezionato.fullName NON è Null usa quel valore, altrimenti usa la stringa vuota
                    onValueChange = {  },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 13.sp,
                        color = Color.Blue   // 🔥 testo visibile
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) { innerTextField ->

                    Box {
                        // Placeholder
                        if ((clienteSelezionato?.fullName ?: "").isEmpty()) {
                            Text(
                                "Cliente",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Testo digitato
                        innerTextField()
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Testo Tipologia Lavoro
                Text("Tipologia Lavoro", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                BasicTextField(
                    value = clienteSelezionato?.tipologia ?: "", // Se clienteSelezionato?.tipologia NON è Null usa quel valore, altrimenti usa la stringa vuota
                    onValueChange = {  },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 13.sp,
                        color = Color.Blue   // 🔥 testo visibile
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) { innerTextField ->

                    Box {
                        // Placeholder
                        if ((clienteSelezionato?.tipologia ?: "").isEmpty()) {
                            Text(
                                "Tipologia lavoro",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Testo digitato
                        innerTextField()
                    }
                }

                Spacer(Modifier.height(15.dp))

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

                // RIGA ICONE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    // ICONA SALVA
                    IconButton(
                        onClick = {
                            try {
                                if (clienteSelezionato != null &&
                                    nome.isNotBlank() &&
                                    oreLavoro.isNotBlank() &&
                                    oreLavoro.toDoubleOrNull() != null
                                ) {

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
                                    selectedMateriale = null
                                    materialiUsati = emptyList()
                                    materialiRiepilogo = emptyList()

                                } else {
                                    alertMessage = "Controlla tutti i campi!"
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
                                contentDescription = "Salva Rapportino",
                                tint = if(clienteSelezionato != null)
                                    Color(0xFF0D47A1)   // blu scuro elegante
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )

                            Text(
                                "Salva",
                                fontSize = 10.sp,
                                color = if (clienteSelezionato != null)
                                    Color(0xFF0D47A1)
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }

                    // ICONA STAMPA PDF
                    IconButton(
                        onClick = {
                            if (clienteSelezionato != null) {
                                generaPdf(
                                    cliente = clienteSelezionato!!,
                                    totaleOre = totaleOre,
                                    tariffaOraria = DatabaseHelper.getImpostazioni().tariffaOraria,
                                    materialiRiepilogo = materialiRiepilogo
                                )
                            }
                        },
                        enabled = clienteSelezionato != null
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = "Stampa PDF",
                                tint = if (clienteSelezionato != null)
                                    Color(0xFF1B5E20)     // verde scuro attivo
                                else
                                    Color.Gray.copy(alpha = 0.4f)  // disattivato
                            )

                            Text(
                                "Stampa PDF",
                                fontSize = 10.sp,
                                color = if (clienteSelezionato != null)
                                    Color(0xFF1B5E20)
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }

                    // ICONA ELIMINA CLIENTE
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        enabled = clienteSelezionato != null
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Elimina Cliente",
                                tint = if (clienteSelezionato != null)
                                    Color(0xFFC62828)   // rosso scuro elegante
                                else
                                    Color.Gray.copy(alpha = 0.4f) // disattivato
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                "Elimina Cliente",
                                fontSize = 10.sp,
                                color = if (clienteSelezionato != null)
                                    Color(0xFFC62828)
                                else
                                    Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }

                    // ICONA TORNA AL MENU
                    IconButton(
                        onClick = onBack
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


                    // Alert Dialog per eliminazione Cliente+Dipendenze
                    if (showDeleteConfirm) {
                        AlertDialog(
                            onDismissRequest = { showDeleteConfirm = false },
                            title = { Text("Conferma eliminazione") },
                            text = { Text("Sei sicuro di voler eliminare questo cliente e tutti i suoi rapportini?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        try {
                                            clienteSelezionato?.let {
                                                DatabaseHelper.deleteClienteConRapportini(it.id)
                                            }

                                            showDeleteConfirm = false
                                            alertMessage = "Cliente e rapportini eliminati!"
                                            showAlert = true

                                            // Reset selezione e aggiorna lista
                                            clienteSelezionato = null
                                            clienti = DatabaseHelper.getAllClienti()

                                        } catch (e: Exception) {
                                            alertMessage = "Errore nella cancellazione dati: ${e.message}"
                                            showAlert = true
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        Color(0xFF1976D2),   // blu deciso
                                        contentColor = Color.White            // testo bianco
                                    )
                                )
                                {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDeleteConfirm = false },
                                    colors = ButtonDefaults.buttonColors(
                                        Color(0xFF1976D2),   // blu deciso
                                        contentColor = Color.White            // testo bianco
                                    )

                                    ) {
                                    Text("Annulla")
                                }
                            }
                        )
                    }

                    // ICONA ANNULLA SELEZIONE CLIENTE
                    IconButton(
                        onClick = {

                            nome = ""
                            oreLavoro = ""
                            clienteSelezionato = null
                            selectedMateriale = null
                            materialiUsati = emptyList()
                            materialiRiepilogo = emptyList()
                            totaleOre = 0.0
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Annulla Selezione",
                                tint = Color(0xFF64B5F6)   // azzurro medio
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                "Annulla Selezione Cliente",
                                fontSize = 10.sp,
                                color = Color(0xFF64B5F6)
                            )
                        }
                    }

                    // BUTTON Aggiungi materiale
                    val quantitaDouble = quantita.toDoubleOrNull()

                    IconButton(
                        onClick = {
                            if (selectedMateriale != null && quantita.isNotBlank() && quantitaDouble != null) {
                                materialiUsati =
                                    mergeMateriali(materialiUsati + (selectedMateriale!! to quantitaDouble))

                                quantita = ""
                                selectedMateriale = null   // 🔥 reset selezione tabella
                            } else {
                                alertMessage = "Inserire un Numero nel campo Quantità!"
                                showAlert = true
                            }
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aggiungi materiale",
                                tint = Color(0xFF64B5F6)   // blu scuro elegante
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                "Aggiungi Materiale",
                                fontSize = 10.sp,
                                color = Color(0xFF64B5F6)
                            )
                        }
                    }

                    // Icona Elimina Materiale inserito
                    IconButton(
                        onClick = {
                            if (selectedMaterialeUsato != null) {
                                materialiUsati = materialiUsati.filter { it != selectedMaterialeUsato }
                                selectedMaterialeUsato = null
                            } else {
                                alertMessage = "Seleziona un materiale da eliminare!"
                                showAlert = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Elimina materiale usato",
                            tint = if (selectedMaterialeUsato != null) Color(0xFFD32F2F) else Color.Gray.copy(alpha = 0.4f)
                        )
                    }

                }

                Spacer(Modifier.height(10.dp))

                // -----------------------------------------
                // MATERIALI DEL NUOVO RAPPORTINO
                // -----------------------------------------
                Text(
                    "Materiali aggiunti ora:",
                    fontSize = 16.sp)

                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, Color.LightGray)
                ) {
                    items(materialiUsati) { item ->

                        val (mat, qty) = item

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                                .border(
                                    1.dp,
                                    if (selectedMaterialeUsato == item) Color.Blue else Color.LightGray
                                )
                                .clickable {
                                    selectedMaterialeUsato = item
                                }
                                .padding(4.dp)
                        ) {
                            Text("${mat.marca} ${mat.modello}", modifier = Modifier.weight(1f), fontSize = 12.sp)
                            Text("x $qty", modifier = Modifier.weight(0.3f), fontSize = 12.sp)
                        }
                    }
                }


            }

            // DIVIDER
            Divider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
            )

            /* FINE COLONNA SINISTRA RAPPORTINO */


            /* COLONNA CENTRALE CLIENTI E MATEIRALE */

            // ---------------------------
            // COLONNA SINISTRA: CLIENTI (TABELLA + RICERCA)
            // ---------------------------

            Column(
                modifier = Modifier
                    .weight(1.4f)
                    .padding(horizontal = 10.dp)
                    //.padding(end = 10.dp)
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
                                    materialiRiepilogo = DatabaseHelper.getMaterialiUsatiDaCliente(cliente.id) // Riepilogo storico dal DB
                                    materialiUsati = emptyList()// Materiali del nuovo rapportino → si parte da zero
                                }
                        ) {
                            Text(cliente.fullName, modifier = Modifier.weight(1f), fontSize = 11.sp)
                            Text(cliente.tipologia, modifier = Modifier.weight(1f), fontSize = 11.sp)
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))



                Spacer(Modifier.height(10.dp))

                // -----------------------------------------
                // TABELLA MATERIALE
                // -----------------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Etichetta Materiale
                    Text(
                        "Materiale", fontSize = 16.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    // 🔹 Campo Quantità piccolo e a destra
                    TextField(
                        value = quantita,
                        onValueChange = { quantita = it },
                        label = { Text("Quantità", fontSize = 12.sp, modifier = Modifier.padding(start = 0.dp)) },
                        modifier = Modifier
                            .width(150.dp)   // 🔥 più piccolo
                            .focusRequester(quantitaFocusRequester)
                            .onKeyEvent { event ->
                                val quantitaDouble = quantita.toDoubleOrNull()

                                if (event.type == KeyEventType.KeyUp && event.key == Key.Enter) {
                                    if (selectedMateriale != null && quantita.isNotBlank() && quantitaDouble != null) {

                                        materialiUsati = mergeMateriali(
                                            materialiUsati + (selectedMateriale!! to quantitaDouble)
                                        )

                                        quantita = ""
                                        selectedMateriale = null
                                        quantitaFocusRequester.requestFocus()

                                    } else {
                                        alertMessage = "Inserire un Numero nel campo Quantità!"
                                        showAlert = true
                                    }
                                    true
                                } else false
                            }
                            .padding(start = 20.dp),

                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF64B5F6),
                            unfocusedIndicatorColor = Color(0xFF90CAF9),
                            cursorColor = Color(0xFF0D47A1),
                            focusedLabelColor = Color(0xFF0D47A1),
                            unfocusedLabelColor = Color.Gray
                        ),
                        singleLine = true
                    )
                }

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
            /* FIME COLONNA CENTRALE CLIENTI E MATERIALE */


            /* COLONNA DESTRA RIEPILOGO */

            // ---------------------------
            // COLONNA DESTRA: RIEPILOGO CLIENTI
            // ---------------------------
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(horizontal = 10.dp)
                    .padding(end = 10.dp)
            ) {

                Text("Riepilogo Cliente", fontSize = 18.sp)
                Spacer(Modifier.height(20.dp))

                // Nome CLiente
                Text("Nome:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = clienteSelezionato?.fullName ?: "—",
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(10.dp))

                // Tipologia Cantiere
                Text("Tipologia:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = clienteSelezionato?.tipologia ?: "—",
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(10.dp))

                // Totale ORE lavorate
                Text("Totale Ore Lavorate:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "%.2f".format(totaleOre),
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50)
                )

                Spacer(Modifier.height(10.dp))

                // Conteggio ORE
                val orelavorateValore = DatabaseHelper.getImpostazioni()
                val tariffaOraria = orelavorateValore.tariffaOraria

                Text("Conteggio Ore:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "%.2f €".format(totaleOre * tariffaOraria),
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50)
                )

                Spacer(Modifier.height(10.dp))

                // Conteggio Valore Materiale
                val rincaroMateriale = DatabaseHelper.getImpostazioni()
                var totaleMateriali = materialiRiepilogo.sumOf { (materiale,  quantita) ->
                  materiale.prezzo * quantita * (1 + rincaroMateriale.rincaroMateriale / 100)
                }

                Text("Conteggio Materiali:", fontSize = 14.sp, color = Color.Gray)

                Text(
                    text = "%.2f €".format(totaleMateriali),
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50)
                )

                Spacer(Modifier.height(10.dp))

                // Conteggio Totale Ore + Materiale
                var totaleAssoluto = totaleMateriali + (totaleOre * tariffaOraria)

                Text("Totale:", fontSize = 14.sp, color = Color.Gray)

                Text(
                    text = "%.2f €".format(totaleAssoluto),
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50)
                )

                Spacer(Modifier.height(30.dp))

                // -----------------------------------------
                // RIEPILOGO MATERIALE USATO (STORICO)
                // -----------------------------------------
                Text(
                    "Materiali utilizzati (storico):",
                    fontSize = 16.sp)

                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .height(150.dp)
                        .border(1.dp, Color.LightGray)
                ) {
                    items(materialiRiepilogo) { (mat, qty) ->
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

               // Spacer(Modifier.height(30.dp))

            }
            /* FINE COLONNA DESTRA RIEPILOGO */
        } // chiusura ROW
    } // chiusura box
}


// Funzione per evitare dopppioni nella tabella "Materiali aggiunti ora"
fun mergeMateriali(lista: List<Pair<Materiale, Double>>): List<Pair<Materiale, Double>> {
    return lista
        .groupBy { it.first.id }   // raggruppa per ID materiale per unificare i materiali uguali basati sull'ID
        .map { (_, items) -> // items è la lista di coppie che hanno lo stesso materiale
            val materiale = items.first().first // Prende il materiale dal primo elemento del gruppo (stesso ID)
            val quantitaTotale = items.sumOf { it.second } // Somma le quantità dello stesso gruppo
            materiale to quantitaTotale // Ricrea una nuova coppia
        }
}


