package com.match.matchmate.presentation.matchMate.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction
import kotlin.random.Random

@Composable
fun MatchScreen(
    userImageRes: String, // Replace with actual image resource
    matchImageRes: String, // Replace with actual image resource
    matchName: String = "Himanshu",
    onKeepSwiping: () -> Unit
) {
    val floatingHearts = remember { mutableStateListOf<FloatingHeart>() }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(800)
            floatingHearts.add(
                FloatingHeart(
                    x = Random.nextFloat() * 0.8f + 0.1f,
                    y = Random.nextFloat() * 0.3f + 0.7f,
                    size = Random.nextFloat() * 8f + 12f
                )
            )

            // Remove old hearts
            if (floatingHearts.size > 15) {
                floatingHearts.removeAt(0)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF6B6B),
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0),
                        Color(0xFF673AB7)
                    ),
                    radius = 1200f
                )
            )
    ) {
        // Floating hearts background
        floatingHearts.forEach { heart ->
            FloatingHeartAnimation(heart = heart)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            MatchTitle()

            Spacer(modifier = Modifier.height(60.dp))

            ProfilePicturesSection(
                userImageRes = userImageRes,
                matchImageRes = matchImageRes
            )

            Spacer(modifier = Modifier.height(40.dp))

            MatchText(matchName = matchName)

            Spacer(modifier = Modifier.weight(1f))

            ActionButtons(onKeepSwiping)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun MatchTitle() {
    Text(
        text = "It's a Match!",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ProfilePicturesSection(
    userImageRes: String,
    matchImageRes: String
) {
    val heartScale by rememberInfiniteTransition(label = "heartScale").animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "heartPulse"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // User profile picture
        ProfilePicture(imageRes = userImageRes)

        Spacer(modifier = Modifier.width(24.dp))

        // Animated heart in the middle
        Card(
            modifier = Modifier
                .size(48.dp)
                .scale(heartScale),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF251217).copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "❤️",
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Match profile picture
        ProfilePicture(imageRes = matchImageRes)
    }
}

@Composable
fun ProfilePicture(imageRes: String) {
    Card(
        modifier = Modifier.size(120.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageRes,
            contentDescription = null,
        )
    }
}

@Composable
fun MatchText(matchName: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You and $matchName liked each other",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "🎯",
            fontSize = 20.sp
        )
    }
}

@Composable
fun ActionButtons(
    onKeepSwiping: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Send Message Button
        Button(
            onClick = { /* Handle send message */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF1744)
            ),
            shape = RoundedCornerShape(28.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "SEND A MESSAGE",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Keep Swiping Button
        OutlinedButton(
            onClick = {
                onKeepSwiping.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White.copy(alpha = 0.1f),
                contentColor = Color.White
            ),
            border = null,
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "KEEP SWIPING",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Data class for floating hearts
data class FloatingHeart(
    val x: Float,
    val y: Float,
    val size: Float
)

@Composable
fun FloatingHeartAnimation(heart: FloatingHeart) {
    var animationStarted by remember { mutableStateOf(false) }

    val animatedY by animateFloatAsState(
        targetValue = if (animationStarted) -0.2f else heart.y,
        animationSpec = tween(durationMillis = 3000, easing = LinearEasing),
        label = "heartFloat"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 0f else 0.8f,
        animationSpec = tween(durationMillis = 3000, easing = EaseOut),
        label = "heartFade"
    )

    val animatedRotation by animateFloatAsState(
        targetValue = if (animationStarted) 360f else 0f,
        animationSpec = tween(durationMillis = 3000, easing = LinearEasing),
        label = "heartRotation"
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .offset(
                x = (heart.x * 400).dp,
                y = (animatedY * 800).dp
            )
    ) {
        Text(
            text = "💖",
            fontSize = heart.size.sp,
            modifier = Modifier.graphicsLayer(
                alpha = animatedAlpha,
                rotationZ = animatedRotation
            )
        )
    }
}