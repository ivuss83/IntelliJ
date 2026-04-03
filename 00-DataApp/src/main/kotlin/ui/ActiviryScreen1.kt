package ui

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
import androidx.compose.material.Button
import androidx.compose.material.Divider
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
                    color = Color(0xFF4CAF50) // verde elegante
                )
            }

            // DIVIDER
            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .padding(horizontal = 10.dp)
            )

            // ---------------------------
            // COLONNA SINISTRA: DROPDOWN MENU --> CLIENTI
            // ---------------------------
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 50.dp)
                .padding(end = 10.dp)) {

                Text("Seleziona Cliente", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Scegli cliente")
                }

                androidx.compose.material.DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    clienti.forEach { cliente ->
                        androidx.compose.material.DropdownMenuItem(onClick = {
                            clienteSelezionato = cliente
                            expanded = false
                            totaleOre = DatabaseHelper.getTotaleOreCliente(cliente.fullName)
                        }) {
                            Text("${cliente.fullName} - ${cliente.tipologia}")
                        }
                    }
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
            // COLONNA DESTRA: DATI LAVORO
            // ---------------------------
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)) {

                Text("Inserisci Nome", fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))

                // Inserimento nome
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

                // Inserimento ore lavoro
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

                // Visualizzazione cliente
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


                // Visualizzazione Tipologia Lavoro
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


                // Salva Nel DB
                Button(onClick = {
                    if (clienteSelezionato?.fullName?.isNotBlank() == true && nome.isNotBlank() && oreLavoro.isNotBlank()) {
                        DatabaseHelper.insertRapportino(
                            nome = nome,
                            ore = oreLavoro.toDouble(),
                            cliente = clienteSelezionato!!.fullName,
                            tipologia = clienteSelezionato!!.tipologia
                        )
                        message = "Rapportino salvato!"
                        nome = ""
                        oreLavoro = ""
                        clienteSelezionato = null
                    } else {
                        message = "Compila tutti i campi!"
                    }
                }) {
                    Text("Salva nel database")
                }

                // PDF
                Button(onClick = {
                    if (clienteSelezionato?.fullName?.isNotBlank() == true && nome.isNotBlank() && oreLavoro.isNotBlank()) {
                        DatabaseHelper.insertRapportino(
                            nome = nome,
                            ore = oreLavoro.toDouble(),
                            cliente = clienteSelezionato!!.fullName,
                            tipologia = clienteSelezionato!!.tipologia
                        )
                        generaPdf("$clienteSelezionato - $nome", oreLavoro.toDouble())
                    }
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
    }
}