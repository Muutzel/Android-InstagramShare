package com.example.instagramshare

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.instagramshare.ui.theme.InstagramShareTheme
import com.lightspark.composeqr.DotShape
import com.lightspark.composeqr.QrCodeColors
import com.lightspark.composeqr.QrCodeView

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
fun ContentInSurface() {
    val context = LocalContext.current

    //Daten initial holen
    val usernameInitial = readInfosOnPhone(context).username
    val bioInitial = readInfosOnPhone(context).bio

    //Daten in Variable speichern, die sich mit der Zeit verändern
    var username by rememberSaveable { mutableStateOf(usernameInitial) }
    var bio by rememberSaveable { mutableStateOf(bioInitial) }

    //Box 1: Hintergrund
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )

    // Box 2 qrCode und Box 3 Infos – je nach Ausrichtung
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    //ausrichtung Gerät
    if (isPortrait) {
        // Im Portrait: untereinander als Spalte
        Column(
            modifier = Modifier
                .fillMaxSize()            // nimmt die gesamte Fläche ein
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,     // vertikale Zentrierung
            horizontalAlignment = Alignment.CenterHorizontally // horizontale Zentrierung
        ) {
            //qrCode als Bild
            GenerateQrCode(username,context)
//            Image(
//                painter = rememberQrCodePainter(
//                    getInstagramLink(username,context),
//                    shapes = QrShapes(
//                        darkPixel = QrPixelShape.roundCorners() // für runde "Punkte"
//                    ),
//                    colors = QrColors(
//                        dark = QrBrush.solid(Color.Magenta),
//                        light = QrBrush.solid(Color.White)
//                    )
//                ),
//                contentDescription = null,
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    .height(230.dp)
//                    //.clip(RoundedCornerShape(16.dp))
//            )

            //unsichtbare Trennung
            Spacer(modifier = Modifier.height(50.dp))

            //Infos
            PersonenInfos(
                username = username,
                bio = bio,
                onUsernameChanged = { username = it },
                onBioChanged = { bio = it },
                context = context
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    GenerateQrCode(username,context)
//                    Image(
//                        painter = imageQr,
//                        contentDescription = null,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .height(230.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    PersonenInfos(
                        username = username,
                        bio = bio,
                        onUsernameChanged = { username = it },
                        onBioChanged = { bio = it },
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
fun GenerateQrCode(username: String, context: Context) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(24.dp)) // Rundung an den Kanten, z.B. 24.dp
            .background(Color.Black),
        contentAlignment = Alignment.Center
    )
    {
        QrCodeView(
            data = getInstagramLink(username,context),
            modifier = Modifier.size(260.dp),
            colors = QrCodeColors(
                background = Color.Black,
                foreground = Color.White
            ),
            dotShape = DotShape.Circle
        )
//        {
//            Box(
//                modifier = Modifier
//                    .size(56.dp) // Größe des leeren Bereichs
//                    .background(Color.White) // Oder Color.Transparent für transparenten Bereich
//            )
//        }
    }
//
    //                    Image(
//                        painter = imageQr,
//                        contentDescription = null,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .height(230.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                    )
}