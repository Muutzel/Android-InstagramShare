package com.example.instagramshare

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PersonenInfos(
    modifier: Modifier = Modifier,
    username: String,
    bio: String,
    onUsernameChanged: (String) -> Unit,
    context: Context
) {
    var isEditing by remember { mutableStateOf(false) }
    var textUsername by remember { mutableStateOf(username) }
    var textBio by remember { mutableStateOf(bio) }

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
                                saveInfosOnPhone(
                                    context,
                                    textUsername,
                                    textBio
                                )
                                onUsernameChanged(textUsername)
                            }
                        )
                    )
                } else {
                    Text(
                        text = "@" + textUsername,
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
                    text = bio,
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}