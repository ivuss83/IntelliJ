package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable

fun MenuScreen(onActivity1: () -> Unit,
               onClienti: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 350.dp)   // larghezza massima del menu
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Entra per compilare il tuo rapportino",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                softWrap = false,
                textAlign = TextAlign.Center
                )
            Spacer(Modifier.height(16.dp))

            Button(onClick = onActivity1, modifier = Modifier.fillMaxWidth()) {
                Text("Compila")
            }

            Button(onClick = onClienti, modifier = Modifier.fillMaxWidth()) {
                Text("Clienti")
            }

        }
    }
}