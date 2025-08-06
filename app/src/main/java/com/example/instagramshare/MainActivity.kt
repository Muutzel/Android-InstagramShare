package com.example.instagramshare

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.instagramshare.ui.theme.InstagramShareTheme
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrColors
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrLogo
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.brush
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

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
    val logoPainter : Painter = painterResource(R.drawable.camera_orange)

    val painter = rememberQrCodePainter(
        data = getInstagramLink(username, context),
        logo = QrLogo(
            painter = logoPainter,
            padding = QrLogoPadding.Natural(.1f),
            shape = QrLogoShape.circle(),
            size = 0.2f
        ),
        shapes = QrShapes(
            ball = QrBallShape.circle(),
            darkPixel = QrPixelShape.circle(0.7f),
            frame = QrFrameShape.roundCorners(.25f)
        ),
        colors = QrColors(
            dark = QrBrush.brush { sizePx ->
                Brush.linearGradient(
                    listOf(Color(0xFFFFA500), Color.Red),
                    start = Offset(0f, 0f),
                    end = Offset(sizePx, sizePx)
                )
            }
        )
    )

    Box(
        modifier = Modifier
            .size(320.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(25.dp)),
        contentAlignment = Alignment.Center
    )
    {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .clickable{val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getInstagramLink(username,context)))
                    context.startActivity(intent)}
        )
    }
}
