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
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.DragData
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.DatabaseHelper
import dataclass.Cliente
import dataclass.Impostazioni
import dataclass.Materiale
import dataclass.Rapportino
import printdata.generaPdf

@Composable
fun Activity1Screen(
    onBack: () -> Unit) {

    // Variabili per Barre di ricerca
    var clienti by remember { mutableStateOf(listOf<Cliente>()) }
    var clienteSelezionato by remember { mutableStateOf<Cliente?>(null) }

    var nome by remember { mutableStateOf("") }
    var oreLavoro by remember { mutableStateOf("") }
    var totaleOre by remember { mutableStateOf(0.0) }

    var searchTextMat by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val quantitaFocusRequester = remember { FocusRequester() }
    var showConfirmNoMaterials by remember { mutableStateOf(false) }

    // Alert Dialog
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showDeleteMaterialeUsatoConfirm by remember { mutableStateOf(false) }
    var showDeleteMaterialConfirm by remember { mutableStateOf(false) }

    var selectedMaterialeUsato by remember { mutableStateOf<Pair<Materiale, Double>?>(null) }
    var selectedStorico by remember { mutableStateOf<Pair<Materiale, Double>?>(null) }
    var idRapportinoCorrente by remember { mutableStateOf(0) }

    var listaMateriali by remember { mutableStateOf(DatabaseHelper.getAllMateriale()) }
    var selectedMateriale by remember { mutableStateOf<Materiale?>(null) }
    var quantita by remember { mutableStateOf("") }
    var quantitaDaEliminare by remember { mutableStateOf("") }

    // VARIABILI PER FINESTRA CLIENTE
    var showClientiDialog by remember { mutableStateOf(false) }
    var clienteSelezionatoTemp by remember { mutableStateOf<Cliente?>(null) }
    var searchDialogText by remember { mutableStateOf("") }
    val focusRequesterDialog = remember { FocusRequester() }


    // Materiali già usati dal cliente (storico)
    var materialiRiepilogo by remember { mutableStateOf(listOf<Pair<Materiale, Double>>()) }

    // Materiali del nuovo rapportino
    var materialiUsati by remember { mutableStateOf(listOf<Pair<Materiale, Double>>()) }

    LaunchedEffect(Unit) {
        clienti = DatabaseHelper.getAllClienti()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {

        /* COLONNA SINISTRA DATI RAPPORTINO */

        // -------------------------------- //
        // COLONNA SINISTRA - DATI LAVORO   //
        // -------------------------------- //

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
            //.padding(end = 10.dp)

        ) {
            Text("Dati Rapportino", fontSize = 16.sp)

            Spacer(Modifier.height(10.dp))

            // Testo Nome
            Text("Nome", fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))

            BasicTextField(
                value = nome,
                onValueChange = { nome = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF0D47A1)   // 🔥 testo visibile
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
                            "",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Testo digitato
                    innerTextField()
                }
            }

            Spacer(Modifier.height(6.dp))

            // Testo Ore Lavoro
            Text("Ore Lavoro", fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))

            BasicTextField(
                value = oreLavoro,
                onValueChange = { oreLavoro = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF0D47A1)   // 🔥 testo visibile
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
                            "",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Testo digitato
                    innerTextField()
                }
            }

            Spacer(Modifier.height(6.dp))

            // Testo Cliente
            Text("Cliente", fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))

            BasicTextField(
                value = clienteSelezionato?.fullName ?: "", // Se clienteSelezionato.fullName NON è Null usa quel valore, altrimenti usa la stringa vuota
                onValueChange = { },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF0D47A1)   // 🔥 testo visibile
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
                            "",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Testo digitato
                    innerTextField()
                }
            }

            Spacer(Modifier.height(6.dp))

            // Testo Tipologia Lavoro
            Text("Tipologia Lavoro", fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))

            BasicTextField(
                value = clienteSelezionato?.tipologia
                    ?: "", // Se clienteSelezionato?.tipologia NON è Null usa quel valore, altrimenti usa la stringa vuota
                onValueChange = { },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF0D47A1)   // 🔥 testo visibile
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
                            "",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Testo digitato
                    innerTextField()
                }
            }

            Spacer(Modifier.height(6.dp))

            // ----------------------------------------- //
            // CARD
            // ----------------------------------------- //

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

                // ICONA SELEZIONA CLIENTE
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)
                        .clickable {
                            showClientiDialog = true
                            clienteSelezionatoTemp = null
                                   },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(Color(0xFFE3F2FD))   // azzurro chiarissimo
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Seleziona Cliente",
                            tint = Color(0xFF0D47A1),          // blu coerente con le altre card
                            modifier = Modifier
                                .size(28.dp)
                                .padding(bottom = 8.dp)
                        )

                        Text(
                            "Cliente\n",
                            fontSize = 10.sp,
                            color = Color(0xFF0D47A1),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ICONA SALVA — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)
                        .clickable {
                            try {
                                if (clienteSelezionato != null &&
                                    nome.isNotBlank() &&
                                    oreLavoro.isNotBlank() &&
                                    oreLavoro.toDoubleOrNull() != null
                                ) {

                                    // Se non ci sono materiali inseriti
                                    if (materialiUsati.isEmpty()) {
                                        showConfirmNoMaterials = true
                                    } else {

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

                                        // Messaggio di conferma
                                        alertMessage = "Rapportino Salvato!"
                                        showAlert = true

                                        // Reset campi
                                        nome = ""
                                        oreLavoro = ""
                                        clienteSelezionato = null
                                        selectedMateriale = null
                                        materialiUsati = emptyList()
                                        materialiRiepilogo = emptyList()
                                        totaleOre = 0.0
                                    }

                                } else {
                                    alertMessage = "Controlla tutti i campi!"
                                    showAlert = true
                                }
                            } catch (e: Exception) {
                                alertMessage = "Errore nel Salvataggio: ${e.message}"
                                showAlert = true
                            }
                        },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(Color(0xFFE3F2FD)) // sfondo card
                            .padding(8.dp)
                    ) {

                        // Icona principale
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Salva Rapportino",
                            tint = Color(0xFF0D47A1),
                            modifier = Modifier.size(28.dp)
                        )

                        // Testo descrittivo
                        Text(
                            "Salva\nRapp.",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF0D47A1)
                        )
                    }
                }

                // ICONA STAMPA PDF — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)
                        .clickable(
                            enabled = clienteSelezionato != null
                        ) {
                            generaPdf(
                                cliente = clienteSelezionato!!,
                                totaleOre = totaleOre,
                                tariffaOraria = DatabaseHelper.getImpostazioni().tariffaOraria,
                                materialiRiepilogo = materialiRiepilogo
                            )
                        },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                if (clienteSelezionato != null)
                                    Color(0xFFE8F5E9)          // verde chiaro attivo
                                else
                                    Color.LightGray.copy(alpha = 0.2f) // disattivo
                            )
                            .padding(8.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Stampa PDF",
                            tint = if (clienteSelezionato != null)
                                Color(0xFF1B5E20)     // verde scuro attivo
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier.size(28.dp)
                        )

                        Text(
                            "Stampa\nPDF",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = if (clienteSelezionato != null)
                                Color(0xFF1B5E20)
                            else
                                Color.Gray.copy(alpha = 0.4f)
                        )
                    }
                }

                // ICONA ELIMINA CLIENTE — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)   // stessa larghezza delle altre card
                        .clickable(enabled = clienteSelezionato != null) {
                            showDeleteConfirm = true
                        },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                if (clienteSelezionato != null)
                                    Color(0xFFFFEBEE)              // rosso chiarissimo attivo
                                else
                                    Color.LightGray.copy(alpha = 0.2f) // disattivo
                            )
                            .padding(8.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Elimina Cliente",
                            tint = if (clienteSelezionato != null)
                                Color(0xFFC62828)   // rosso scuro elegante
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier.size(28.dp)
                        )

                        Text(
                            "Elimina Cliente",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = if (clienteSelezionato != null)
                                Color(0xFFC62828)
                            else
                                Color.Gray.copy(alpha = 0.4f)
                        )
                    }
                }


                // ICONA TORNA AL MENU — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)   // stessa larghezza delle altre card
                        .clickable { onBack() },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(Color(0xFFE3F2FD))   // azzurro chiarissimo
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Torna al Menu",
                            tint = Color(0xFF0D47A1),         // blu scuro elegante
                            modifier = Modifier.size(28.dp)
                        )

                        Text(
                            "Menu\n",
                            fontSize = 10.sp,
                            color = Color(0xFF0D47A1),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ICONA ANNULLA SELEZIONE CLIENTE — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)
                        .clickable(enabled = clienteSelezionato != null)
                        {
                            nome = ""
                            oreLavoro = ""
                            clienteSelezionato = null
                            selectedMateriale = null
                            materialiUsati = emptyList()
                            materialiRiepilogo = emptyList()
                            totaleOre = 0.0
                            clienteSelezionatoTemp = null
                        },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                if (clienteSelezionato != null)
                                    Color(0xFFE3F2FD)  // azzurro chiarissimo
                                else
                                    Color.LightGray.copy(alpha = 0.2f) // disattivo
                            )
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Annulla Selezione Cliente",
                            tint = if (clienteSelezionato != null)
                                Color(0xFF0D47A1)
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier
                                .size(28.dp)
                                .padding(bottom = 8.dp)   // ← padding icona richiesto
                        )

                        Text(
                            "Annulla\nCliente",
                            fontSize = 10.sp,
                            color = if (clienteSelezionato != null)
                                Color(0xFF0D47A1)
                            else Color.Gray.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


                // ICONA ELIMINA MATERIALE INSERITO — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)
                        .clickable {
                            if (selectedMaterialeUsato != null) {
                                showDeleteMaterialeUsatoConfirm = true
                            } else {
                                alertMessage = "Seleziona un materiale da eliminare!"
                                showAlert = true
                            }
                        },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                if (selectedMaterialeUsato != null)
                                    Color(0xFFFFEBEE)              // rosso chiarissimo attivo
                                else
                                    Color.LightGray.copy(alpha = 0.2f) // disattivo
                            )
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Elimina materiale inserito",
                            tint = if (selectedMaterialeUsato != null)
                                Color(0xFFD32F2F)   // rosso scuro elegante
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier
                                .size(28.dp)
                                .padding(bottom = 8.dp)   // padding icona richiesto
                        )

                        Text(
                            "Elimina\nMateriale",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = if (selectedMaterialeUsato != null)
                                Color(0xFFD32F2F)
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ------------------------------ //
                // ALERT DIALOG
                // ------------------------------ //

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
                            Button(
                                onClick = { showDeleteConfirm = false },
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

                // Alert Dialog per eliminazione MAteriale Inserito
                if (showDeleteMaterialeUsatoConfirm) {
                    AlertDialog(
                        onDismissRequest = { showDeleteMaterialeUsatoConfirm = false },
                        title = { Text("Conferma eliminazione") },
                        text = {
                            Text("Sei sicuro che vuoi eliminare il materiale selezionato?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // elimina il materiale selezionato
                                    materialiUsati = materialiUsati.filter { it != selectedMaterialeUsato }

                                    selectedMaterialeUsato = null
                                    showDeleteMaterialeUsatoConfirm = false

                                    alertMessage = "Materiale rimosso!"
                                    showAlert = true
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
                            Button(
                                onClick = { showDeleteMaterialeUsatoConfirm = false },
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


                // Alert Dialog per chiedere conferma se procedere con salvataggio anche SENZA materiale
                if (showConfirmNoMaterials) {
                    AlertDialog(
                        onDismissRequest = { showConfirmNoMaterials = false },
                        title = { Text("Nessun materiale inserito") },
                        text = { Text("Vuoi salvare comunque il rapportino senza materiali?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showConfirmNoMaterials = false

                                    // Salvataggio effettivo
                                    DatabaseHelper.insertRapportino(
                                        nome = nome,
                                        ore = oreLavoro.toDouble(),
                                        clienteId = clienteSelezionato!!.id,
                                        tipologia = clienteSelezionato!!.tipologia
                                    )

                                    val idRapportino = DatabaseHelper.getLastRapportinoId()

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
                                    totaleOre = 0.0
                                },
                                colors = ButtonDefaults.buttonColors(
                                    Color(0xFF1976D2),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showConfirmNoMaterials = false },
                                colors = ButtonDefaults.buttonColors(
                                    Color(0xFF1976D2),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Annulla")
                            }
                        }
                    )
                }

                // Alert Dialog per eliminazione Materiale STORICO dal Rapportino
                if (showDeleteMaterialConfirm) {
                    AlertDialog(
                        onDismissRequest = { showDeleteMaterialConfirm = false },
                        title = { Text("Conferma eliminazione") },
                        text = { Text("Sei sicuro di voler eliminare questo materiale dal rapportino?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    try {
                                        selectedStorico?.let { (mat, _) ->

                                            // 🔥 Elimina SOLO dal rapportino corrente
                                            DatabaseHelper.deleteMaterialeDaRapportino(
                                                mat.id!!,
                                                idRapportinoCorrente
                                            )

                                            // Aggiorna lista materiali del rapportino
                                            materialiRiepilogo = DatabaseHelper.getMaterialiUsatiNelRapportino(
                                                idRapportinoCorrente
                                            )

                                            // Reset selezione
                                            totaleOre = 0.0
                                            selectedStorico = null
                                            idRapportinoCorrente = 0
                                            alertMessage = "Materiale rimosso dal rapportino!"
                                            showAlert = true
                                        }

                                        showDeleteMaterialConfirm = false

                                    } catch (e: Exception) {
                                        alertMessage = "Errore nella cancellazione materiale: ${e.message}"
                                        showAlert = true
                                    }

                                },
                                colors = ButtonDefaults.buttonColors(
                                    Color(0xFF1976D2),   // blu deciso
                                    contentColor = Color.White
                                )
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDeleteMaterialConfirm = false },
                                colors = ButtonDefaults.buttonColors(
                                    Color(0xFF1976D2),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Annulla")
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                "Materiali aggiunti ora:",
                fontSize = 16.sp
            )

            Spacer(Modifier.height(10.dp))


            // -----------------------------------------
            // TABELLA MATERIALI DEL NUOVO RAPPORTINO
            // -----------------------------------------

            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
            ) {
                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .height(150.dp)
                        .border(1.dp, Color.LightGray)
                ) {
                    items(materialiUsati) { item ->

                        val (mat, qty) = item

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                                .background(Color(0xFFE8F5E9))   // 💚 verde chiaro elegante
                                .border(
                                    1.dp,
                                    if (selectedMaterialeUsato == item) Color.Yellow else Color.LightGray
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

        } // Fine colonna di Sinistra


        // DIVIDER
        Divider(
            color = Color(0xFFE0E0E0),
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
        )

        // -----------------------------------------
        // DIALOG TABELLA CLIENTI
        // -----------------------------------------

        if (showClientiDialog) {

            // Filtraggio clienti nel dialog
            val clientiFiltratiDialog = clienti.filter {
                it.fullName.contains(searchDialogText, ignoreCase = true) ||
                        it.tipologia.contains(searchDialogText, ignoreCase = true)
            }

            AlertDialog(
                onDismissRequest = { showClientiDialog = false },
                title = { Text("Seleziona Cliente", fontSize = 16.sp) },

                text = {
                    Column {

                        // -----------------------------------------
                        // BARRA DI RICERCA CLIENTI (NEL DIALOG)
                        // -----------------------------------------
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                                .clickable { focusRequesterDialog.requestFocus() }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            BasicTextField(
                                value = searchDialogText,
                                onValueChange = { searchDialogText = it },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 12.sp,
                                    color = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .focusRequester(focusRequesterDialog),
                                decorationBox = { innerTextField ->
                                    if (searchDialogText.isEmpty()) {
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

                        // -----------------------------------------
                        // TABELLA CLIENTI (IDENTICA ALLA TUA)
                        // -----------------------------------------
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .border(1.dp, Color.LightGray)
                                .padding(4.dp)
                        ) {
                            items(clientiFiltratiDialog) { cliente ->

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(2.dp)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(1.dp))
                                        .background(
                                            if (clienteSelezionatoTemp?.id == cliente.id)
                                                Color(0xFFE3F2FD)
                                            else
                                                Color.Transparent
                                        )
                                        .clickable {
                                            clienteSelezionatoTemp = cliente   // selezione temporanea
                                        }
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        cliente.fullName,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        cliente.tipologia,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                },

                // -----------------------------------------
                // PULSANTE OK → APPLICA LA SELEZIONE
                // -----------------------------------------
                confirmButton = {
                    Button(
                        onClick = {
                            clienteSelezionatoTemp?.let { cliente ->

                                // Applica la selezione SOLO quando premi OK
                                clienteSelezionato = cliente
                                totaleOre = DatabaseHelper.getTotaleOreCliente(cliente.id)
                                materialiRiepilogo =
                                    DatabaseHelper.getMaterialiUsatiDaCliente(cliente.id)
                                materialiUsati = emptyList()
                            }

                            showClientiDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF1976D2),   // blu deciso
                            contentColor = Color.White
                        )
                    ) {
                        Text("OK")
                    }
                },

                // -----------------------------------------
                // PULSANTE CHIUDI → NON APPLICA NULLA
                // -----------------------------------------
                dismissButton = {
                    Button(
                        onClick = { showClientiDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF1976D2),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Chiudi")
                    }
                }
            )
        }

        /* FINE COLONNA SINISTRA RAPPORTINO */

        // ------------------------------------------------------ //
        // MATERIALE MAGAZZINO                                    //
        // ------------------------------------------------------ //

        Column(
            modifier = Modifier
                .weight(1.4f)
                .padding(horizontal = 10.dp)
        ) {

            Text("Materiale", fontSize = 16.sp)
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                // 🔹 Campo Quantità piccolo e a destra
                TextField(
                    value = quantita,
                    onValueChange = { quantita = it },
                    label = { Text("Quantità", modifier = Modifier.padding(bottom = 10.dp)) },
                    modifier = Modifier
                        .width(150.dp)
                        .height(70.dp)
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
                                    alertMessage =
                                        "Verifica se hai selezionato materiale da inserire oppure manca la quantità!"
                                    showAlert = true
                                }
                                true
                            } else false
                        },

                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),   // 🔥 stile card
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        color = Color(0xFF0D47A1)
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFE3F2FD),          // 🔥 sfondo card
                        focusedIndicatorColor = Color.Transparent,     // 🔥 niente underline
                        unfocusedIndicatorColor = Color.Transparent,   // 🔥 niente underline
                        cursorColor = Color(0xFF0D47A1),
                        focusedLabelColor = Color(0xFF0D47A1),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),   // 🔥 distanza controllata
                    modifier = Modifier.padding(start = 20.dp)
                ) {

                    // ICONA AGGIUNGI MATERIALE — versione moderna in Card
                    Card(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(70.dp)
                            .clickable (
                                enabled = clienteSelezionato != null,
                            )

                            {
                                val q = quantita.toDoubleOrNull()
                                if (selectedMateriale != null && quantita.isNotBlank() && q != null) {
                                    materialiUsati =
                                        mergeMateriali(materialiUsati + (selectedMateriale!! to q))
                                    quantita = ""
                                    selectedMateriale = null
                                } else {
                                    alertMessage =
                                        "Verifica se hai selezionato materiale da inserire oppure manca la quantità!"
                                    showAlert = true
                                }
                            },
                        elevation = 6.dp,
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    if (clienteSelezionato != null)
                                        Color(0xFFE3F2FD)              // blu chiarissimo attivo
                                    else
                                        Color.LightGray.copy(alpha = 0.2f) // disattivo
                                )
                                .padding(8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aggiungi materiale",
                                tint = if (clienteSelezionato != null)
                                    Color(0xFF0D47A1)         // blu scuro elegante
                                            else
                                    Color.LightGray.copy(alpha = 0.2f), // disattivo
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(bottom = 8.dp)      // padding icona richiesto
                            )

                            Text(
                                "Aggiungi\nMateriale",
                                fontSize = 10.sp,
                                color = if (clienteSelezionato != null)
                                    Color(0xFF0D47A1)
                                else Color.Gray.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // ICONA ANNULLA SELEZIONE TABELLA MATERIALE — versione moderna in Card
                    Card(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(70.dp)
                            .clickable {
                                selectedMateriale = null
                                quantita = ""
                            },
                        elevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFFE3F2FD))   // azzurro chiarissimo come le altre card
                                .padding(8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Annulla selezione",
                                tint = Color(0xFF0D47A1),         // blu scuro elegante
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(bottom = 8.dp)      // padding icona
                            )

                            Text(
                                "Annulla\nSelezione",
                                fontSize = 10.sp,
                                color = Color(0xFF0D47A1),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(15.dp))

            // BARRA DI RICERCA MATERIALE
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

            Spacer(Modifier.height(6.dp))

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
                    .border(1.dp, Color.LightGray)
                    .padding(horizontal = 2.dp)
            ) {
                items(materialiFiltrati) { materiale ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .padding(top = 3.dp)
                            .background(if (selectedMateriale?.id == materiale.id) Color(0xFFE3F2FD) else Color.Transparent)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(1.dp))
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
        }

        // DIVIDER
        Divider(
            color = Color(0xFFE0E0E0),
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
        )


        // ----------------------------------------------------------//
        // COLONNA DESTRA: RIEPILOGO CLIENTI (DIVISO IN DUE COLONNE) //
        // ----------------------------------------------------------//

        Column(
            modifier = Modifier
                .weight(0.8f)
                .padding(horizontal = 10.dp)
                .padding(end = 10.dp)
        ) {

            Text("Riepilogo Cliente", fontSize = 16.sp, color = Color.Black)
            Spacer(Modifier.height(10.dp))

            // 🔹 RIEPILOGO DIVISO IN DUE COLONNE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // ---------------------------
                // COLONNA SINISTRA
                // ---------------------------
                Column(modifier = Modifier.weight(1f)) {

                    Text("Nome:", fontSize = 16.sp, color = Color.Gray)
                    Text(clienteSelezionato?.fullName ?: "—", fontSize = 14.sp)

                    Spacer(Modifier.height(6.dp))

                    Text("Tipologia:", fontSize = 16.sp, color = Color.Gray)
                    Text(clienteSelezionato?.tipologia ?: "—", fontSize = 14.sp)

                    Spacer(Modifier.height(6.dp))

                    Text("Totale Ore Lavorate:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f".format(totaleOre),
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50)
                    )
                }

                // ---------------------------
                // COLONNA DESTRA
                // ---------------------------
                Column(modifier = Modifier.weight(1f)) {

                    val impostazioni = DatabaseHelper.getImpostazioni()
                    val tariffaOraria = impostazioni.tariffaOraria
                    val rincaro = impostazioni.rincaroMateriale

                    Text("Conteggio Ore:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f €".format(totaleOre * tariffaOraria),
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50)
                    )

                    Spacer(Modifier.height(6.dp))

                    val totaleMateriali = materialiRiepilogo.sumOf { (materiale, quantita) ->
                        materiale.prezzo * quantita * (1 + rincaro / 100)
                    }

                    Text("Conteggio Materiali:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f €".format(totaleMateriali),
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50)
                    )

                    Spacer(Modifier.height(6.dp))

                    val totaleAssoluto = totaleMateriali + (totaleOre * tariffaOraria)

                    Text("Totale:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f €".format(totaleAssoluto),
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            // -----------------------------------------
            // RIEPILOGO MATERIALE USATO (STORICO)
            // -----------------------------------------

            Spacer(Modifier.height(120.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // ICONA ELIMINA MATERIALE STORICO — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)
                        .clickable(enabled = selectedStorico != null) {
                            showDeleteMaterialConfirm = true
                        },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                if (selectedStorico != null)
                                    Color(0xFFFFEBEE)   // rosso chiarissimo (come feedback di azione pericolosa)
                                else
                                    Color.LightGray.copy(alpha = 0.2f)
                            )
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Elimina materiale storico",
                            tint = if (selectedStorico != null)
                                Color(0xFFD32F2F)   // rosso acceso
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier
                                .size(28.dp)
                                .padding(bottom = 8.dp)
                        )

                        Text(
                            "Elimina\nMateriale",
                            fontSize = 10.sp,
                            color = if (selectedStorico != null)
                                Color(0xFFD32F2F)
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ICONA ANNULLA SELEZIONE MATERIALE STORICO — versione moderna in Card
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(70.dp)
                        .clickable(enabled = selectedStorico != null) {
                            selectedStorico = null
                            idRapportinoCorrente = 0
                        },
                    elevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                if (selectedStorico != null)
                                    Color(0xFFE3F2FD)   // azzurro chiarissimo
                                else
                                    Color.LightGray.copy(alpha = 0.2f) // disattivo
                            )
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Annulla Selezione Storico",
                            tint = if (selectedStorico != null)
                                Color(0xFF0D47A1)
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier
                                .size(28.dp)
                                .padding(bottom = 8.dp)
                        )

                        Text(
                            "Ann.\nStorico",
                            fontSize = 10.sp,
                            color = if (selectedStorico != null)
                                Color(0xFF0D47A1)
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // TABELLA MATERIALE STORICO

            Text("Materiale Storico:", fontSize = 16.sp, color = Color.Black)
            Spacer(Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .height(150.dp)
                    .border(1.dp, Color.LightGray)
            ) {
                items(materialiRiepilogo) { item ->
                    val (mat, qty) = item

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (selectedStorico == item) Color(0xFFE3F2FD)
                                else Color.Transparent
                            )
                            .clickable {
                                selectedStorico = item
                                val rappId = DatabaseHelper.getRapportinoIdDaMateriale(mat.id!!)
                                if (rappId != null) idRapportinoCorrente = rappId
                            }
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
} // chiusura Activity



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


