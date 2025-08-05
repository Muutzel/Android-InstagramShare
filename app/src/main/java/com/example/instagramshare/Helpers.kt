package com.example.instagramshare

import android.content.Context
import kotlin.text.replace
import kotlin.text.startsWith
import androidx.core.content.edit

fun getInstagramLink(username: String, context: Context): String{

    return "https://${context.getString(R.string.instagram_domain)}/$username"
}

fun saveInfosOnPhone(
    context: Context,
    username: String,
    bio: String
) {
    if(username.startsWith("@"))
        username.replace("@","")

    val prefs = context.getSharedPreferences(context.getString(R.string.instagram_share_prefs), Context.MODE_PRIVATE)
    prefs.edit { putString(context.getString(R.string.instagram_share_pref_username), username) }
    prefs.edit { putString(context.getString(R.string.instagram_share_pref_bio), bio) }
}

fun readInfosOnPhone(
    context: Context
): UserInfos {
    val prefs = context.getSharedPreferences(context.getString(R.string.instagram_share_prefs), Context.MODE_PRIVATE)
    var username = prefs.getString(context.getString(R.string.instagram_share_pref_username), "") ?: ""
    var bio = prefs.getString(context.getString(R.string.instagram_share_pref_bio), "") ?: ""

    if(username == "")
        username = context.getString(R.string.placeholder_edit_name)
    else if(username.startsWith("@"))
        username = username.replace("@","")

    if(bio == "")
        bio = context.getString(R.string.placeholder_edit_bio)

    return UserInfos(username, bio)
}