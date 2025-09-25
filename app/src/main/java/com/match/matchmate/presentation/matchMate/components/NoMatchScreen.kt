package com.match.matchmate.presentation.matchMate.components


import androidx.compose.animation.core.EaseInOut
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.random.Random

@Composable
fun NoMatchScreen(
    userImageRes: String,
    matchImageRes: String = "",
    matchName: String = "Himanshu",
    onKeepSwiping: () -> Unit
) {
    val brokenHeartParticles = remember { mutableStateListOf<BrokenHeartParticle>() }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1200)
            brokenHeartParticles.add(
                BrokenHeartParticle(
                    x = Random.nextFloat() * 0.8f + 0.1f,
                    y = Random.nextFloat() * 0.4f + 0.3f,
                    size = Random.nextFloat() * 6f + 8f,
                    symbol = listOf("💔", "😢", "😔").random()
                )
            )

            if (brokenHeartParticles.size > 8) {
                brokenHeartParticles.removeAt(0)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Transparent
            )
    ) {
        brokenHeartParticles.forEach { particle ->
            BrokenHeartAnimation(particle = particle)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            ProfilePicturesWithBrokenHeart(
                userImageRes = userImageRes,
                matchImageRes = matchImageRes
            )

            Spacer(modifier = Modifier.height(60.dp))

            NoMatchTitle()

            Spacer(modifier = Modifier.height(24.dp))

            NoMatchSubtitle(matchName = matchName)

            Spacer(modifier = Modifier.weight(1f))

            ActionButtonsNoMatch(onKeepSwiping)

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun ProfilePicturesWithBrokenHeart(
    userImageRes: String,
    matchImageRes: String
) {
    val shakeAnimation by rememberInfiniteTransition(label = "shake").animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "shake"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ProfilePictureNoMatch(imageRes = userImageRes)

        Spacer(modifier = Modifier.width(32.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer(translationX = shakeAnimation),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "💔",
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        ProfilePictureNoMatch(imageRes = matchImageRes)
    }
}

@Composable
fun ProfilePictureNoMatch(imageRes: String) {
    Card(
        modifier = Modifier.size(110.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = imageRes,
                contentDescription = null,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun NoMatchTitle() {
    Text(
        text = "Not a Match",
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun NoMatchSubtitle(matchName: String) {
    val messages = listOf(
        "Looks like you and $matchName didn't vibe this time",
        "The stars weren't aligned with $matchName today",
        "Sometimes chemistry just isn't there with $matchName",
        "Not quite the spark you were looking for with $matchName"
    )

    Text(
        text = messages.random(),
        fontSize = 18.sp,
        color = Color.White.copy(alpha = 0.8f),
        textAlign = TextAlign.Center,
        lineHeight = 24.sp
    )
}

@Composable
fun ActionButtonsNoMatch(
    onKeepSwiping: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Button(
            onClick = { onKeepSwiping.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE91E63).copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(29.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "Keep Swiping",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

data class BrokenHeartParticle(
    val x: Float,
    val y: Float,
    val size: Float,
    val symbol: String
)

@Composable
fun BrokenHeartAnimation(particle: BrokenHeartParticle) {
    var animationStarted by remember { mutableStateOf(false) }

    val animatedY by animateFloatAsState(
        targetValue = if (animationStarted) particle.y - 0.4f else particle.y,
        animationSpec = tween(durationMillis = 4000, easing = EaseOut),
        label = "particleFloat"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 0f else 0.6f,
        animationSpec = tween(durationMillis = 4000, easing = EaseInOut),
        label = "particleFade"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (animationStarted) 0.3f else 1f,
        animationSpec = tween(durationMillis = 4000, easing = EaseInOut),
        label = "particleScale"
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .offset(
                x = (particle.x * 350).dp,
                y = (animatedY * 700).dp
            )
    ) {
        Text(
            text = particle.symbol,
            fontSize = particle.size.sp,
            modifier = Modifier.graphicsLayer(
                alpha = animatedAlpha,
                scaleX = animatedScale,
                scaleY = animatedScale
            )
        )
    }
}