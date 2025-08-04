package com.example.instagramshare


import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramshare.helpers.saveInfosOnPhone
import android.content.res.Configuration
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun Space(height: Int = 0, width: Int = 0){
    Spacer(
        modifier = Modifier
        .width(width.dp)
        .height(height.dp)
    )
}

@Composable
fun EditableText(
    text: String,
    singleLine: Boolean,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // State: Editiermodus
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            singleLine = singleLine,
            modifier = modifier.onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    isEditing = false
                    onTextChange(text) // Neuer Wert erst beim Speichern/Verlassen
                }
            },
            keyboardActions = KeyboardActions(onDone = { isEditing = false })
        )
    } else {
        Text(
            text = text,
            modifier = modifier
                .clickable { isEditing = true },
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun showPersonenInfos(
    modifier: Modifier = Modifier,
    context: Context
) {
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingBio by remember { mutableStateOf(false) }

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
            Space(width = 20)

            // mitte: Senkrechter Strich als visuelle Trennung
            Image(
                painter = painterResource(com.example.instagramshare.R.drawable.strich),
                contentDescription = null,
                modifier = Modifier
                    //.size(60.dp)
                    .height(150.dp)
                    .rotate(180F)
            )

            //Freiraum
            Space(width = 20)

            // rechts: Mehrere Text-Zeilen untereinander
            Column {
                var username by remember { mutableStateOf(AppGlobals.UserInfos.username) }
                var bio by remember { mutableStateOf(AppGlobals.UserInfos.bio) }

                //Username
                EditableText(
                    text = username,
                    singleLine = true,
                    onTextChange = {
                        username = it
                        AppGlobals.UserInfos.username = it
                        saveInfosOnPhone(context)
                    }
                )

                //freiraum
                Space(height = 8)

                //Text
                EditableText(
                    text = bio,
                    singleLine = false,
                    onTextChange = {
                        bio = it
                        AppGlobals.UserInfos.bio = it
                        saveInfosOnPhone(context)
                    }
                )
            }
        }
    }
}

@Composable
fun QrAndTexts(
    imageQr: Painter,
    context: Context
){
    // (1) Hole initial einmalig die Werte, sobald Compose startet!
    val initialUsername = AppGlobals.UserInfos.username
    val initialBio = AppGlobals.UserInfos.bio
    var username by remember { mutableStateOf(initialUsername) }
    var bio by remember { mutableStateOf(initialBio) }

    Modifier.padding(8.dp)

    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QrImage(imageQr)
            Space(height = 50)
            showPersonenInfos(
                context = context
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                QrImage(imageQr)
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                PersonenInfos(
                    context = context
                )
            }
        }
    }
}

@Composable
fun QrImage(imageQr: Painter) {
    Image(
        painter = imageQr,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .height(230.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}
