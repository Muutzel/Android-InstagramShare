package com.example.instagramshare.helpers

import android.content.Context
import kotlin.text.replace
import kotlin.text.startsWith

fun PrepareUsername(username: String): String{
    if(!username.startsWith("@"))
        return "@$username"
    else
        return username
}

fun GetSocialmediaLink(domain: String, username: String): String{
    var result = username;

    if(username.startsWith("@"));
        result = username.replace("@","");

    return "https://$domain/$result";
}

fun SaveInfosOnPhone(
    context: Context,
    username: String
) {
    val prefs = context.getSharedPreferences("instagram_share_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("instagram_username", username).apply()
}

fun ReadInfosOnPhone(
    context: Context
):String {
    val prefs = context.getSharedPreferences("instagram_share_prefs", Context.MODE_PRIVATE)
    var username = prefs.getString("instagram_username", "") ?: ""

    if(username == "")
        username = "NAME BEARBEITEN"

    return PrepareUsername(username)
}