package com.example.instagramshare

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun PersonenInfos(
    modifier: Modifier = Modifier,
    username: String,
    bio: String,
    onUsernameChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit,
    context: Context
) {
    var isEditingUsername by remember { mutableStateOf(false) }
    var isEditingBio by remember { mutableStateOf(false) }
    var textUsername by remember { mutableStateOf(username) }
    var textBio by remember { mutableStateOf(bio) }

    //alles in Spalte
    Column(
        modifier = modifier
            .padding(20.dp)
    ) {
        //mehrere Dinge nebeneinander
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // links: Profil- oder anderes Bild
            var imageUri by rememberSaveable { mutableStateOf(loadSavedImageUri(context)) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri: Uri? ->
                    uri?.let {
                        imageUri = saveImageToAppStorage(context, it)
                        AlertDialog.Builder(context)
                            .setTitle("Hinweis")
                            .setMessage("Wenn das Bild nach Änderung nicht da ist: starte die App neu. Ist noch ein Bug -.-")
                            .setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            )

            Box(modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { launcher.launch("image/*") })
            {
                if (imageUri != "") {
                    Image(
                        painter = rememberAsyncImagePainter("file://$imageUri"),
                        contentDescription = "Profilbild von $username",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Platzhalterbild anzeigen
                    Image(
                        painter = painterResource(R.drawable.placeholder_picture),
                        contentDescription = "Profilbild von $username",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            //Freiraum
            Spacer(modifier = Modifier.width(30.dp))

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
            Spacer(modifier = Modifier.width(30.dp))

            // rechts: Mehrere Text-Zeilen untereinander
            Column {
                //Username
                if (isEditingUsername) {
                    TextField(
                        value = textUsername,
                        onValueChange = {
                            textUsername = it
                            onUsernameChanged(it) // Hochmelden, z.B. Parent State aktualisieren
                        },
                        singleLine = true,
                        modifier = Modifier,
                        keyboardOptions = KeyboardOptions.Default,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isEditingUsername = false
                                saveInfosOnPhone(
                                    context,
                                    textUsername,
                                    textBio
                                )
                            }
                        )
                    )
                } else {
                    Text(
                        text = "@$textUsername",
                        modifier = Modifier
                            .clickable { isEditingUsername = true },
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                //freiraum
                Spacer(modifier = Modifier.height(12.dp))

                //Text
                if (isEditingBio) {
                    TextField(
                        value = textBio,
                        onValueChange = {
                            textBio = it
                            onBioChanged(it) // Hochmelden, z.B. Parent State aktualisieren
                        },
                        singleLine = false,
                        modifier = Modifier,
                        keyboardOptions = KeyboardOptions.Default
                    )
                    Button(
                        onClick = {
                            isEditingBio = false
                            saveInfosOnPhone(context, textUsername, textBio)
                            //onBioChanged(textBio) // Jetzt hochmelden; falls im Parent gespeichert wird
                        }
                    ) {
                        Text("Speichern")
                    }
                } else {
                    Text(
                        text = textBio,
                        modifier = Modifier
                            .clickable { isEditingBio = true },
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}