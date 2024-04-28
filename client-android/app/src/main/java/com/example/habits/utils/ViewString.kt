package com.example.habits.utils

import androidx.annotation.StringRes

/**
 * Represents a String in a View that can either be a [String] or an [Int] resource.
 */
sealed interface ViewString
data class String(val value: kotlin.String) : ViewString
data class Integer(@StringRes val resId: Int) : ViewString
