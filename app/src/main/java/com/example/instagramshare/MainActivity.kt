package com.example.instagramshare

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramshare.helpers.GetSocialmediaLink
import com.example.instagramshare.helpers.PrepareUsername
import com.example.instagramshare.helpers.ReadInfosOnPhone
import com.example.instagramshare.helpers.SaveInfosOnPhone
import com.example.instagramshare.ui.theme.InstagramShareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstagramShareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContentInSurface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(50.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstagramShareTheme {
        ContentInSurface()
    }
}

@Composable
fun ContentInSurface(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    //Daten initial holen
    val usernameInitial = ReadInfosOnPhone(context)
    val profiltextInitial = stringResource(R.string.social_instagram_profiltext)
    val surname = stringResource(R.string.surname)
    val imageQr = painterResource(R.drawable.marcel_892__qr_orange)

    //Daten in Variable speichern, die sich mit der Zeit verändern
    var username by rememberSaveable { mutableStateOf(usernameInitial) }
    var profiltext by rememberSaveable { mutableStateOf(profiltextInitial) }

    //Box 1: Hintergrund
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )

    // Box 2 qrCode und Box 3 Infos – je nach Ausrichtung
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //ausrichtung Gerät
    if (isLandscape) {
        // Im Landscape: nebeneinander als Reihe
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //qrCode als Bild
            Image(
                painter = imageQr,
                contentDescription = "Bild von $surname",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(230.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            //unsichtbare Trennung
            Spacer(modifier = Modifier.width(100.dp)) // Abstand individuell wählen

            //Infos
            PersonenInfos(
                username = username,
                profiltext = profiltext,
                onUsernameChanged = { username = it },
                onProfiltextChanged = { profiltext = it },
                context = context
            )
        }
    } else {
        // Im Portrait: untereinander als Spalte
        Column(
            modifier = Modifier
                .fillMaxSize()            // nimmt die gesamte Fläche ein
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,     // vertikale Zentrierung
            horizontalAlignment = Alignment.CenterHorizontally // horizontale Zentrierung
        ) {
            //qrCode als Bild
            Image(
                painter = imageQr,
                contentDescription = "Bild von $surname",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(230.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            //unsichtbare Trennung
            Spacer(modifier = Modifier.height(50.dp))

            //Infos
            PersonenInfos(
                username = username,
                profiltext = profiltext,
                onUsernameChanged = { username = it },
                onProfiltextChanged = { profiltext = it },
                context = context
            )
        }
    }
}

@Composable
fun PersonenInfos(
    modifier: Modifier = Modifier,
    username: String,
    profiltext: String,
    onUsernameChanged: (String) -> Unit,
    onProfiltextChanged: (String) -> Unit,
    context: Context
) {
    var isEditing by remember { mutableStateOf(false) }
    var textUsername by remember { mutableStateOf(username) }
    var textProfiltext by remember { mutableStateOf(profiltext) }

    //alles in Spalte
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        //mehrere Dinge nebeneinander
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // links: Profil- oder anderes Bild
            Image(
                painter = painterResource(R.drawable.psx_20181008_100739),
                contentDescription = "Profilbild von $username",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            //Freiraum
            Spacer(modifier = Modifier.width(20.dp))

            // mitte: Senkrechter Strich als visuelle Trennung
            Image(
                painter = painterResource(R.drawable.strich),
                contentDescription = "Profilbild von $username",
                modifier = Modifier
                    //.size(60.dp)
                    .height(150.dp)
                    .rotate(180F)
            )

            //Freiraum
            Spacer(modifier = Modifier.width(20.dp))

            // rechts: Mehrere Text-Zeilen untereinander
            Column {
                //Username
                if (isEditing) {
                    TextField(
                        value = textUsername,
                        onValueChange = {
                            textUsername = it
                            onUsernameChanged(it) // Hochmelden, z.B. Parent State aktualisieren
                        },
                        singleLine = true,
                        modifier = Modifier
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions.Default,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isEditing = false
                                SaveInfosOnPhone(
                                    context,
                                    textUsername
                                )
                                onUsernameChanged(textUsername)
                            }
                        )
                    )
                } else {
                    Text(
                        text = textUsername,
                        modifier = Modifier
                            .clickable { isEditing = true }
                            .padding(8.dp),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                //freiraum
                Spacer(modifier = Modifier.height(8.dp))

                //Text
                Text(
                    text = profiltext,
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}