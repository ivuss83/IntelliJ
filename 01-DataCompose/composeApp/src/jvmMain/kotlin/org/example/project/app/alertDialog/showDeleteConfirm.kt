package org.example.project.app.alertDialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import database.DatabaseHelper
import dataclass.Cliente

class showDeleteConfirm {

    // Funzione di gestione del dialog di conferma eliminazione Cliente + Rapportini
    // Tutti i valori necessari vengono passati come parametri.
    @Composable
    fun ShowDeleteConfirmDialog(
        showDeleteConfirm: Boolean,                 // Stato visibilità dialog
        clienteSelezionato: Cliente?,               // Cliente selezionato
        onDeleteSuccess: () -> Unit,                // Callback dopo eliminazione
        onDeleteError: (String) -> Unit,            // Callback in caso di errore
        onDismiss: () -> Unit,                      // Chiusura dialog
        updateClienti: () -> Unit                   // Aggiornamento lista clienti
    ) {

        // Mostra l'AlertDialog solo quando il flag showDeleteConfirm è attivo
        if (showDeleteConfirm) {

            AlertDialog(
                // Chiusura del dialog quando si clicca fuori o si preme ESC
                onDismissRequest = onDismiss,

                // Titolo del popup di conferma
                title = { Text("Conferma eliminazione") },

                // Testo esplicativo del contenuto del dialog
                text = { Text("Sei sicuro di voler eliminare questo cliente e tutti i suoi rapportini?") },

                // Pulsante di conferma (OK)
                confirmButton = {
                    Button(
                        onClick = {
                            // Blocco protetto da try/catch per gestire eventuali errori di cancellazione
                            try {
                                // Se esiste un cliente selezionato, esegue la cancellazione completa
                                clienteSelezionato?.let {
                                    // Cancellazione cliente e tutti i suoi rapportini dal database
                                    DatabaseHelper.deleteClienteConRapportini(it.id)
                                }

                                // Callback di successo (gestito dalla MainActivity o dal Composable chiamante)
                                onDeleteSuccess()

                                // Aggiornamento della lista clienti dopo la cancellazione
                                updateClienti()

                                // Chiude il dialog
                                onDismiss()

                            } catch (e: Exception) {
                                // In caso di errore, invia il messaggio al chiamante
                                onDeleteError("Errore nella cancellazione dati: ${e.message}")
                            }
                        },
                        // Stile del pulsante: blu deciso con testo bianco (coerente con il resto della UI)
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF1976D2),   // Colore di sfondo del pulsante
                            contentColor = Color.White // Colore del testo del pulsante
                        )
                    ) {
                        // Testo mostrato sul pulsante di conferma
                        Text("OK")
                    }
                },

                // Pulsante di annullamento (chiude il dialog senza eseguire la cancellazione)
                dismissButton = {
                    Button(
                        onClick = onDismiss, // Chiude semplicemente il dialog
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF1976D2),   // Stesso blu per coerenza visiva
                            contentColor = Color.White
                        )
                    ) {
                        // Testo mostrato sul pulsante di annullamento
                        Text("Annulla")
                    }
                }
            )
        }
    }
}


