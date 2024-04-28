package com.example.habits.utils

import androidx.annotation.StringRes

/**
 * Represents a String in a View that can either be a [String] or an [Int] resource.
 */
sealed interface ViewString
data class vString(val value: kotlin.String) : ViewString
data class vInteger(val value : String) : ViewString
