package com.match.matchmate.presentation.base.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.match.matchmate.core.util.leftSwipeModifier
import com.match.matchmate.core.util.rightSwipeModifier
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun ShaadiSwipeCard(
    disableSwipe: Boolean = false,
    swipeLimit: Float = 400f,
    dragSensitivity: Float = 2.5f,
    onSwipeLeftAction: () -> Unit,
    onSwipeRightAction: () -> Unit,
    cardContent: @Composable () -> Unit
) {
    if (disableSwipe) return cardContent()
    var horizontalOffset by remember { mutableFloatStateOf(0f) }
    val screenDensity = LocalDensity.current.density
    val progress = (horizontalOffset / swipeLimit).coerceIn(-1f, 1f)
    val easedFraction = FastOutSlowInEasing.transform(progress.absoluteValue)

    Box(
        modifier = Modifier
            .background(Color.Black)
            .offset { IntOffset(horizontalOffset.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onDragEnd = {
                    when {
                        horizontalOffset > swipeLimit -> {
                            onSwipeRightAction.invoke()
                        }

                        horizontalOffset < -swipeLimit -> {
                            onSwipeLeftAction.invoke()
                        }
                    }
                    horizontalOffset = 0f
                }) { change, dragDelta ->
                    horizontalOffset += (dragDelta / screenDensity) * dragSensitivity
                    if (change.positionChange() != Offset.Zero) change.consume()
                }
            }
            .graphicsLayer(
                rotationZ = horizontalOffset / 50, //Rotation degree of card on swipe
                translationX = horizontalOffset
            )
    ) {
        cardContent()

        // Right Swipe Indicator with Rotation
        Box(
            modifier = Modifier
                .rightSwipeModifier(
                    offsetX = (swipeLimit - (horizontalOffset * easedFraction * 2.5f)).roundToInt(),
                    sizeFraction = (horizontalOffset / swipeLimit).coerceIn(
                        0f,
                        1f
                    ),
                    rotationFraction = (horizontalOffset / swipeLimit).coerceIn(0f, 1f)
                )
                .size(50.dp)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                imageVector = Icons.Filled.Favorite,
                tint = Color.Red,
                contentDescription = "Right Swipe Tick"
            )
        }

        // Left Swipe Indicator
        Box(
            modifier = Modifier
                .leftSwipeModifier(
                    offsetX = (-swipeLimit - (horizontalOffset * easedFraction * 2.5f)).roundToInt(),
                    sizeFraction = (-horizontalOffset / swipeLimit).coerceIn(
                        0f,
                        1f
                    ),
                    rotationFraction = (horizontalOffset / swipeLimit).coerceIn(0f, 1f)
                )
                .size(50.dp)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                imageVector = Icons.Filled.Close,
                tint = Color.White,
                contentDescription = "Right Swipe Tick"
            )
        }

    }
}