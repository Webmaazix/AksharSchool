package com.akshar.one.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle

/**
 * Check if activity has been initialized (Foreground) or not
 */
fun AppCompatActivity.isInForeground(): Boolean = lifecycle.currentState
        .isAtLeast(Lifecycle.State.INITIALIZED)