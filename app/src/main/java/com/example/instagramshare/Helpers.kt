package com.example.instagramshare.helpers

import android.content.Context
import com.example.instagramshare.AppGlobals
import com.example.instagramshare.R
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

fun saveInfosOnPhone(
    context: Context
) {
    val prefs = context.getSharedPreferences(R.string.device_storage_prefs.toString(), Context.MODE_PRIVATE)
    prefs.edit().putString(R.string.device_storage_pref_instagram_username.toString(), AppGlobals.UserInfos.username).apply()
    prefs.edit().putString(R.string.device_storage_pref_instagram_bio.toString(), AppGlobals.UserInfos.bio).apply()
}

fun readInfosFromDevice(context: Context) {
    val prefs = context.getSharedPreferences(R.string.device_storage_prefs.toString(), Context.MODE_PRIVATE)
    var username = prefs.getString(R.string.device_storage_pref_instagram_username.toString(), "") ?: ""
    var bio = prefs.getString(R.string.device_storage_pref_instagram_bio.toString(), "") ?: ""

    if(username == "")
        username = "NAME BEARBEITEN"
    else if(!username.startsWith("@"))
        username = "@" + username

    if(bio == "")
        bio = "TEXT BEARBEITEN"

    AppGlobals.UserInfos.username = username;
    AppGlobals.UserInfos.bio = bio;
}