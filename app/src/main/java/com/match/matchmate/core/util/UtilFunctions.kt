package com.match.matchmate.core.util

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.lerp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun extractYearFromIsoAndroid(timestamp: String): Int {
    return try {
        // ISO 8601 format
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(timestamp)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.get(Calendar.YEAR)
    } catch (e: ParseException) {
        // Handle invalid format
        -1
    }
}

@Composable
fun Modifier.rightSwipeModifier(
    offsetX: Int,
    offsetY: Int = 0,
    sizeStart: Float = 0f,
    sizeStop: Float = 3.2f,
    rotationStart: Float = 0f,
    rotationStop: Float = -10f,
    sizeFraction: Float,
    rotationFraction: Float
): Modifier {
    return this
        .offset {
            IntOffset(
                (offsetX),
                offsetY
            )
        }
        .scale(
            lerp(
                start = sizeStart,
                stop = sizeStop,
                fraction = (sizeFraction)
            )
        )
        .graphicsLayer(
            rotationZ = lerp(
                start = rotationStart,
                stop = rotationStop,
                fraction = (rotationFraction)
            )
        )
}

@Composable
fun Modifier.leftSwipeModifier(
    offsetX: Int,
    offsetY: Int = 0,
    sizeStart: Float = 0f,
    sizeStop: Float = 3.2f,
    rotationStart: Float = 0f,
    rotationStop: Float = -10f,
    sizeFraction: Float,
    rotationFraction: Float
): Modifier {
    return this
        .offset {
            IntOffset(
                (offsetX),
                offsetY
            )
        }
        .scale(
            lerp(
                start = sizeStart,
                stop = sizeStop,
                fraction = (sizeFraction)
            )
        )
        .graphicsLayer(
            rotationZ = lerp(
                start = rotationStart,
                stop = rotationStop,
                fraction = (rotationFraction)
            )
        )
}

