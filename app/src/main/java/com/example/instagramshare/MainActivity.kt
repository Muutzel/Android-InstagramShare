package com.example.instagramshare

import android.R
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.instagramshare.ui.theme.InstagramShareTheme
import android.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.EncodeHintType
import java.util.EnumMap
import android.graphics.Paint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
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
            GenerateQrCode2(username,context)
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
                    GenerateQrCode1(username,context)
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
fun GenerateQrCode1(username: String, context: Context) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(24.dp)) // Rundung an den Kanten, z.B. 24.dp
            .background(Color.White),
        contentAlignment = Alignment.Center
    )
    {
        QrCodeView(
            data = getInstagramLink(username,context),
            modifier = Modifier
                .size(260.dp),
            colors = QrCodeColors(
                background = Color.White,
                foreground = Color.DarkGray
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

@Composable
fun GenerateQrCode2(username: String, context: Context) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.style = Paint.Style.FILL
    paint.strokeJoin = Paint.Join.ROUND
    paint.strokeCap = Paint.Cap.ROUND
    paint.isDither = true

    val qrCodeWriter = QRCodeWriter()

    val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
        put(EncodeHintType.MARGIN, 0)
        put(EncodeHintType.CHARACTER_SET, "UTF-8")
    }

    val targetSizePx = 320
    val bitMatrix = qrCodeWriter.encode(getInstagramLink(username,context), BarcodeFormat.QR_CODE, targetSizePx, targetSizePx, hints)
    val moduleCount = bitMatrix.width
    val moduleSize = targetSizePx / moduleCount.toFloat()


    val bitmap = Bitmap.createBitmap(targetSizePx, targetSizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val radius = moduleSize / (2f * 0.5f)
    //val radius = 0.05f

    for (y in 0 until moduleCount) {
        for (x in 0 until moduleCount) {
            if (bitMatrix.get(x, y)) {
                val tX = x.toFloat() / (moduleCount - 1)
                val tY = y.toFloat() / (moduleCount - 1)
                val t = (tX + tY) / 2f

                // Beispiel: kräftigeres Gelb zu Rot
                val red = 255
                val green = (180 * (1 - t)).toInt()
                val blue = (60 * t).toInt()

                paint.color = android.graphics.Color.argb(255, red, green, blue)
                //paint.color = android.graphics.Color.rgb(255,255,255)

                val cx = (x + 0.5f) * moduleSize
                val cy = (y + 0.5f) * moduleSize

                //canvas.drawCircle(cx, cy, radius, paint)
                canvas.drawCircle(cx, cy, radius, paint)
            }
        }
    }

    Image(
        bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier
            .size(320.dp)
    )
}