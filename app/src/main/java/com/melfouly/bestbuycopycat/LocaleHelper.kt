package com.melfouly.bestbuycopycat

import android.content.Context
import android.util.Log
import java.util.Locale

object LocaleHelper {
    // Setting the locale based on the value in the sharedPreferences.
    fun setLanguage(context: Context): Context {
        val defaultLanguage = "en"

        val languagePref =
            context.getSharedPreferences("languagePref", Context.MODE_PRIVATE)
        val savedLanguage = languagePref.getString("language", defaultLanguage)

        val locale = Locale(savedLanguage!!)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        Log.d("Locale", "setLanguage: ${Locale.getDefault().language}")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context

    }

    // Changing the locale if the saved language in sharedPreferences is English swap it
    // to Arabic and vice versa, and save the new value in the sharedPreferences.
    fun changeLocale(context: Context) {
        val defaultLanguage = "en"
        val languagePref =
            context.getSharedPreferences("languagePref", Context.MODE_PRIVATE)
        var savedLanguage = languagePref.getString("language", defaultLanguage)
        Log.d("Locale", "changeLocale: saved language before change is $savedLanguage")

        savedLanguage = if (savedLanguage == "en") {
            "ar"
        } else {
            "en"
        }

        languagePref.edit().putString("language", savedLanguage).apply()
    }
}