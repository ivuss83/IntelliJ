package ui


import alertDialog.Alert
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
import dataclass.MaterialeStorico
import dataclass.Rapportino
import org.example.project.app.alertDialog.showDeleteConfirm
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
    var selectedStorico by remember { mutableStateOf<MaterialeStorico?>(null) }
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
    var materialiRiepilogo by remember { mutableStateOf<List<MaterialeStorico>>(emptyList()) }


    // Materiali del nuovo rapportino
    var materialiUsati by remember { mutableStateOf(listOf<Pair<Materiale, Double>>()) }

    // Inserimento Manuale Materiale
    var marcaManuale by remember { mutableStateOf("") }
    var modelloManuale by remember { mutableStateOf("") }
    var codiceManuale by remember { mutableStateOf("") }
    var prezzoManuale by remember { mutableStateOf("") }

    var materialiMagazzino by remember { mutableStateOf(listOf<Materiale>()) }

    // Dropdownmenu ORE LAVORO
    var expandedOre by remember { mutableStateOf(false) }
    val oreDisponibili = listOf("0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "5", "6", "7", "8")

    // Dropdown Nome
    var expandedNome by remember { mutableStateOf(false) }
    var nomeDropMenu by remember { mutableStateOf("") }
    val nomiDisponibili = listOf("Stefano")

    // Dropdown Quantità
    var expandedQuantita by remember { mutableStateOf(false) }
    var quantitaDropDown by remember { mutableStateOf("") }

    // Puoi popolare questa lista come vuoi
    val quantitaDisponibili = listOf("1", "2", "3", "4", "5", "10", "20")


    LaunchedEffect(Unit) {
        clienti = DatabaseHelper.getAllClienti()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {

        /* 1 COLONNA */

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .clickable { expandedNome = true }
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🔵 Testo o placeholder
                    Text(
                        text = if (nomeDropMenu.isEmpty()) "Seleziona nome" else nomeDropMenu,
                        fontSize = 14.sp,
                        color = if (nomeDropMenu.isEmpty()) Color.Gray else Color(0xFF1976D2),
                        modifier = Modifier.weight(1f)
                    )

                    // 🔽 Freccia a destra
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // 🔵 Menu a tendina
                DropdownMenu(
                    expanded = expandedNome,
                    onDismissRequest = { expandedNome = false }
                ) {
                    nomiDisponibili.forEach { valore ->
                        DropdownMenuItem(
                            onClick = {
                                nomeDropMenu = valore
                                expandedNome = false
                            }
                        ) {
                            Text(valore)
                        }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // Testo Ore Lavoro
            Text("Ore Lavoro", fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .clickable { expandedOre = true }
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🔵 Testo o placeholder
                    Text(
                        text = if (oreLavoro.isEmpty()) "Ore lavorate" else oreLavoro,
                        fontSize = 14.sp,
                        color = if (oreLavoro.isEmpty()) Color.Gray else Color(0xFF1976D2),
                        modifier = Modifier.weight(1f)
                    )

                    // 🔽 Freccia a destra
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // 🔵 Menu a tendina
                DropdownMenu(
                    expanded = expandedOre,
                    onDismissRequest = { expandedOre = false }
                ) {
                    oreDisponibili.forEach { valore ->
                        DropdownMenuItem(
                            onClick = {
                                oreLavoro = valore
                                expandedOre = false
                            }
                        ) {
                            Text(valore)
                        }
                    }
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
                    color = Color(0xFF1976D2)   // 🔥 testo visibile
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
                    color = Color(0xFF1976D2)   // 🔥 testo visibile
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
                        .clickable (
                            enabled = clienteSelezionato != null
                        )
                        {
                            try {
                                if (clienteSelezionato != null &&
                                    nomeDropMenu.isNotBlank() &&
                                    oreLavoro.isNotBlank() &&
                                    oreLavoro.toDoubleOrNull() != null
                                ) {

                                    // Se non ci sono materiali inseriti
                                    if (materialiUsati.isEmpty()) {
                                        showConfirmNoMaterials = true
                                    } else {

                                        // Salvo il rapportino
                                        DatabaseHelper.insertRapportino(
                                            nome = nomeDropMenu,
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
                                        nomeDropMenu = ""
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
                            .background(
                                if (clienteSelezionato != null)
                                    Color(0xFFE3F2FD)          // verde chiaro attivo
                                else
                                    Color.LightGray.copy(alpha = 0.2f) // disattivo
                            )
                            .padding(8.dp)
                    ) {

                        // Icona principale
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Salva Rapportino",
                            tint = if (clienteSelezionato != null)
                                Color(0xFF0D47A1)
                            else
                                Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier.size(28.dp)
                        )

                        // Testo descrittivo
                        Text(
                            "Salva\nRapp.",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = if (clienteSelezionato != null)
                                Color(0xFF1B5E20)
                            else
                                Color.Gray.copy(alpha = 0.4f)
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

                // SHOWDELETECONFIRM
                showDeleteConfirm().ShowDeleteConfirmDialog(
                    showDeleteConfirm = showDeleteConfirm,
                    clienteSelezionato = clienteSelezionato,

                    onDeleteSuccess = {
                        alertMessage = "Cliente e rapportini eliminati!"
                        showAlert = true
                        clienteSelezionato = null
                    },

                    onDeleteError = { msg ->
                        alertMessage = msg
                        showAlert = true
                    },

                    onDismiss = { showDeleteConfirm = false },

                    updateClienti = {
                        clienti = DatabaseHelper.getAllClienti()
                    }
                )

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

                    // 🔵 Focus Requester per mettere il cursore nel TextField
                    val focusRequester = remember { FocusRequester() }

                    AlertDialog(
                        onDismissRequest = { showDeleteMaterialConfirm = false },
                        title = {
                            Text("Elimina materiale", color = Color(0xFF1976D2))
                        },
                        text = {
                            Column {

                                Text(
                                    "Quanti pezzi vuoi eliminare?",
                                    color = Color(0xFF1976D2)
                                )

                                OutlinedTextField(
                                    value = quantitaDaEliminare,
                                    onValueChange = { quantitaDaEliminare = it },
                                    label = { Text("Quantità", color = Color(0xFF1976D2)) },
                                    singleLine = true,
                                    modifier = Modifier.focusRequester(focusRequester),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        unfocusedBorderColor = Color(0xFF1976D2),
                                        cursorColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2),
                                        unfocusedLabelColor = Color(0xFF1976D2),
                                        textColor = Color.Black
                                    )
                                )

                                // 🔥 Appena il dialogo appare → focus automatico
                                LaunchedEffect(Unit) {
                                    focusRequester.requestFocus()
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    try {
                                        selectedStorico?.let { storico ->

                                            val qtyToRemove = quantitaDaEliminare.toDoubleOrNull() ?: 0.0

                                            if (qtyToRemove <= 0) {
                                                alertMessage = "Quantità non valida"
                                                showAlert = true
                                                return@let
                                            }

                                            if (qtyToRemove > storico.quantita) {
                                                alertMessage = "Non puoi eliminare più della quantità presente"
                                                showAlert = true
                                                return@let
                                            }

                                            if (storico.quantita - qtyToRemove > 0) {
                                                DatabaseHelper.updateQuantitaMateriale(
                                                    storico.idRiga,
                                                    storico.quantita - qtyToRemove
                                                )
                                            } else {
                                                DatabaseHelper.deleteMaterialeDaRapportino(storico.idRiga)
                                            }

                                            materialiRiepilogo = DatabaseHelper.getMaterialiUsatiDaCliente(
                                                clienteSelezionato!!.id
                                            )

                                            quantitaDaEliminare = ""
                                            selectedStorico = null
                                            alertMessage = "Materiale aggiornato!"
                                            showAlert = true
                                        }

                                        showDeleteMaterialConfirm = false

                                    } catch (e: Exception) {
                                        alertMessage = "Errore: ${e.message}"
                                        showAlert = true
                                    }
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


            // -----------------------------------------
            // TABELLA MATERIALI DEL NUOVO RAPPORTINO
            // -----------------------------------------

            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )

                    {
                        selectedMaterialeUsato = null
                    }
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
            color = Color(0xFFE8F5E9),
            modifier = Modifier
                .fillMaxHeight()
                .width(10.dp)
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

             // Alert Dialog per selezione CLIENTE
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


        /* 2 COLONNA */

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

            // RIGA INSERIMENTO MANUALE MATERIALE — SENZA CARD

            Text("Inserimento Manuale", fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // MARCA
                OutlinedTextField(
                    value = marcaManuale,
                    onValueChange = { marcaManuale = it },
                    label = { Text("Marca") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1976D2),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                // MODELLO
                OutlinedTextField(
                    value = modelloManuale,
                    onValueChange = { modelloManuale = it },
                    label = { Text("Modello") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1976D2),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                // CODICE
                OutlinedTextField(
                    value = codiceManuale,
                    onValueChange = { codiceManuale = it },
                    label = { Text("Codice") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1976D2),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                // PREZZO
                OutlinedTextField(
                    value = prezzoManuale,
                    onValueChange = { prezzoManuale = it.replace(".", ",") },
                    label = { Text("Prezzo") },
                    singleLine = true,
                    modifier = Modifier.width(100.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1976D2),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                // CAMPO QUANTITÀ — VERSIONE DROPDOWN

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(70.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))   // 🔥 stesso stile card
                        .background(Color(0xFFE3F2FD))                         // 🔥 stesso sfondo
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .clickable { expandedQuantita = true }
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // 🔵 Testo o placeholder
                        Text(
                            text = if (quantita.isEmpty()) "Quantità" else quantita,
                            fontSize = 20.sp,
                            color = if (quantita.isEmpty()) Color.Gray else Color(0xFF0D47A1),
                            modifier = Modifier.weight(1f)
                        )

                        // 🔽 Freccia
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    // 🔵 MENU A TENDINA
                    DropdownMenu(
                        expanded = expandedQuantita,
                        onDismissRequest = { expandedQuantita = false }
                    ) {
                        quantitaDisponibili.forEach { valore ->
                            DropdownMenuItem(
                                onClick = {
                                    quantita = valore
                                    expandedQuantita = false
                                }
                            ) {
                                Text(
                                    valore,
                                    fontSize = 18.sp,
                                    color = Color(0xFF0D47A1)
                                )
                            }
                        }
                    }
                }

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
                            .clickable(enabled = clienteSelezionato != null) {

                                val q = quantita.replace(",", ".").trim().toDoubleOrNull()

                                // 🔵 CASO 1 — MATERIALE DA MAGAZZINO
                                if (selectedMateriale != null && quantita.isNotBlank() && q != null) {

                                    // ❗ controllo duplicato nella lista materiali usati
                                    if (materialiUsati.any { it.first.codice == selectedMateriale!!.codice }) {
                                        alertMessage = "Materiale già presente nel rapportino!"
                                        showAlert = true
                                        return@clickable
                                    }

                                    materialiUsati = materialiUsati + (selectedMateriale!! to q)

                                    quantita = ""
                                    selectedMateriale = null
                                    return@clickable
                                }

                                // 🔵 CASO 2 — MATERIALE MANUALE
                                val prezzoDouble = prezzoManuale.toDoubleOrNull()

                                if (
                                    marcaManuale.isNotBlank() &&
                                    modelloManuale.isNotBlank() &&
                                    codiceManuale.isNotBlank() &&
                                    prezzoDouble != null &&
                                    quantita.isNotBlank() &&
                                    q != null
                                ) {

                                    // ❗ controllo duplicato nel magazzino
                                    if (materialiMagazzino.any { it.codice == codiceManuale }) {
                                        alertMessage = "Materiale già presente nel magazzino!"
                                        showAlert = true
                                        return@clickable
                                    }

                                    // ❗ controllo duplicato nella lista materiali usati
                                    if (materialiUsati.any { it.first.codice == codiceManuale }) {
                                        alertMessage = "Materiale già presente nel rapportino!"
                                        showAlert = true
                                        return@clickable
                                    }

                                    // 🔥 Inserimento nel DB con gestione errori
                                    val nuovoId = try {
                                        DatabaseHelper.insertMaterialeManuale(
                                            marcaManuale,
                                            modelloManuale,
                                            codiceManuale,
                                            prezzoDouble
                                        )
                                    } catch (e: Exception) {
                                        alertMessage = e.message ?: "Errore inserimento materiale"
                                        showAlert = true
                                        return@clickable
                                    }

                                    // 🔄 Aggiorno il magazzino in tempo reale
                                    materialiMagazzino = DatabaseHelper.getAllMateriale()

                                    // 🔥 Creo oggetto materiale manuale con ID reale
                                    val materialeManualeObj = Materiale(
                                        id = nuovoId,
                                        marca = marcaManuale,
                                        modello = modelloManuale,
                                        codice = codiceManuale,
                                        prezzo = prezzoDouble
                                    )

                                    materialiUsati = materialiUsati + (materialeManualeObj to q)

                                    // Reset campi manuali
                                    marcaManuale = ""
                                    modelloManuale = ""
                                    codiceManuale = ""
                                    prezzoManuale = ""
                                    quantita = ""

                                    alertMessage = "Materiale manuale aggiunto!"
                                    showAlert = true

                                } else {
                                    alertMessage = "Seleziona un materiale dal magazzino oppure compila tutti i campi del materiale manuale!\nVerifica la Quantità!"
                                    showAlert = true
                                }
                            },
                        elevation = 6.dp,
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    if (clienteSelezionato != null)
                                        Color(0xFFE3F2FD)
                                    else
                                        Color.LightGray.copy(alpha = 0.2f)
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
                                    Color(0xFF0D47A1)
                                else
                                    Color.LightGray.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(bottom = 8.dp)
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
                                marcaManuale = ""
                                modelloManuale = ""
                                codiceManuale = ""
                                prezzoManuale = ""
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

            // BARRA DI RICERCA MATERIALE

            Text("Materiale a Magazzino", fontSize = 16.sp)
            Spacer(Modifier.height(20.dp))

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

            Spacer(Modifier.height(8.dp))

            LaunchedEffect(Unit) {
                materialiMagazzino = DatabaseHelper.getAllMateriale()
            }

            // Filtraggio Materiale
            val materialiFiltrati = materialiMagazzino.filter {
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
            color = Color(0xFFE8F5E9),
            modifier = Modifier
                .fillMaxHeight()
                .width(10.dp)
        )


        /* 3 COLONNA */

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
                    Text(clienteSelezionato?.fullName ?: "—", fontSize = 14.sp, color = Color(0xFF1976D2))

                    Spacer(Modifier.height(6.dp))

                    Text("Tipologia:", fontSize = 16.sp, color = Color.Gray)
                    Text(clienteSelezionato?.tipologia ?: "—", fontSize = 14.sp, color = Color (0xFF1976D2))

                    Spacer(Modifier.height(6.dp))

                    Text("Totale Ore Lavorate:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f".format(totaleOre),
                        fontSize = 14.sp,
                        color = Color(0xFF1976D2)
                    )
                }

                // ---------------------------
                // COLONNA DESTRA
                // ---------------------------
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    val impostazioni = DatabaseHelper.getImpostazioni()
                    val tariffaOraria = impostazioni.tariffaOraria
                    val rincaro = impostazioni.rincaroMateriale

                    Text("Conteggio Ore:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f €".format(totaleOre * tariffaOraria),
                        fontSize = 14.sp,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(Modifier.height(6.dp))

                    val totaleMateriali = materialiRiepilogo.sumOf { storico ->
                        val materiale = storico.materiale
                        val quantita = storico.quantita

                        materiale.prezzo * quantita * (1 + rincaro / 100)
                    }

                    Text("Conteggio Materiali:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f €".format(totaleMateriali),
                        fontSize = 14.sp,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(Modifier.height(6.dp))

                    val totaleAssoluto = totaleMateriali + (totaleOre * tariffaOraria)

                    Text("Totale:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "%.2f €".format(totaleAssoluto),
                        fontSize = 14.sp,
                        color = Color(0xFF1976D2)
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
                items(materialiRiepilogo) { storico ->
                    val mat = storico.materiale
                    val qty = storico.quantita

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (selectedStorico == storico) Color(0xFFE3F2FD)
                                else Color.Transparent
                            )
                            .clickable {
                                selectedStorico = storico
                                idRapportinoCorrente = storico.idRapportino

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



