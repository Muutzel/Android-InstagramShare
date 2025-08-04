package com.example.instagramshare

import android.content.Context
import com.example.instagramshare.UserInfos
import kotlin.text.replace
import kotlin.text.startsWith

fun prepareUsername(username: String): String{
    if(!username.startsWith("@"))
        return "@$username"
    else
        return username
}

fun getSocialmediaLink(domain: String, username: String): String{
    var result = username;

    if(username.startsWith("@"));
        result = username.replace("@","");

    return "https://$domain/$result";
}

fun saveInfosOnPhone(
    context: Context,
    username: String,
    bio: String
) {
    if(username?.startsWith("@") == true)
        username.replace("@","")
    
    val prefs = context.getSharedPreferences(context.getString(R.string.instagram_share_prefs), Context.MODE_PRIVATE)
    prefs.edit().putString(context.getString(R.string.instagram_share_pref_username), username).apply()
    prefs.edit().putString(context.getString(R.string.instagram_share_pref_bio), bio).apply()
}

fun readInfosOnPhone(
    context: Context
): UserInfos {
    val prefs = context.getSharedPreferences(context.getString(R.string.instagram_share_prefs), Context.MODE_PRIVATE)
    var username = prefs.getString(context.getString(R.string.instagram_share_pref_username), "") ?: ""
    var bio = prefs.getString(context.getString(R.string.instagram_share_pref_bio), "") ?: ""
    if(username == "")
        username = "NAME BEARBEITEN"
    else if(username?.startsWith("@") == true)
        username = username.replace("@","")

    if(bio == "")
        bio = "TEXT BEARBEITEN"

    return UserInfos(username, bio)
}