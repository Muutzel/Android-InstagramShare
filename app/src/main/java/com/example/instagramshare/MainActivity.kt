package com.example.instagramshare

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramshare.helpers.readInfosFromDevice
import com.example.instagramshare.helpers.saveInfosOnPhone
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
                    val context = LocalContext.current

                    readInfosFromDevice(context = context)

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
    val imageQr = painterResource(R.drawable.marcel_892__qr_orange)

    //Box 1: Hintergrund
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )

    QrAndTexts(imageQr,context)
}

@Composable
fun PersonenInfos(
    modifier: Modifier = Modifier,
    context: Context
) {
    var isEditing by remember { mutableStateOf(false) }

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
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            //Freiraum
            Spacer(modifier = Modifier.width(20.dp))

            // mitte: Senkrechter Strich als visuelle Trennung
            Image(
                painter = painterResource(R.drawable.strich),
                contentDescription = null,
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
                        value = AppGlobals.UserInfos.username,
                        onValueChange = {
                            AppGlobals.UserInfos.username = it
                        },
                        singleLine = true,
                        modifier = Modifier
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions.Default,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isEditing = false
                                saveInfosOnPhone(
                                    context
                                )
                            }
                        )
                    )
                } else {
                    Text(
                        text = AppGlobals.UserInfos.username,
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

                //Bio
                Text(
                    text = AppGlobals.UserInfos.bio,
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}