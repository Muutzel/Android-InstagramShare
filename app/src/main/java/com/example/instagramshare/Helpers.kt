package com.example.instagramshare.helpers

import android.content.Context
import com.example.instagramshare.AppGlobals
import com.example.instagramshare.R
import kotlin.text.replace
import kotlin.text.startsWith

fun prepareUsername(username: String): String{
    if(username?.startsWith("@") == false)
        return "@$username"
    else
        return username
}

fun getSocialmediaLink(
    domain: String = R.string.device_storage_prefs_instagram_domain.toString(),
    username: String
): String{
    var resultUsername = username;
    var resultDomain = domain;

    if(username?.startsWith("@") == true);
    resultUsername = username.replace("@","");

    if(!domain.startsWith("https//"))
        resultDomain = "https://$resultDomain"

    return "$resultDomain/$resultUsername";
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
    else
        username = prepareUsername(username)

    if(bio == "")
        bio = "TEXT BEARBEITEN"

    AppGlobals.UserInfos.username = username;
    AppGlobals.UserInfos.bio = bio;
}