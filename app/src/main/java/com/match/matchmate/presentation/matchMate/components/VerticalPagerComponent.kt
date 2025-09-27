package com.match.matchmate.presentation.matchMate.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.match.matchmate.core.util.extractYearFromIsoAndroid
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalPagerComponent(
    index: Int,
    userData: MatchMateDto.Result = MatchMateDto.Result(),
    onAction: (MatchmateAction) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Card(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = userData.picture.large,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color(0xFF443B30)
                                    )
                                )
                            )
                    )

                    // Country indicator
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                Color.Black.copy(alpha = 0.7f), RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Black, Color.Red, Color.Yellow
                                        )
                                    ), CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = userData.location.country,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Profile dots (top right)
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        repeat(2) { index ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (index == 0) Color.White else Color.White.copy(alpha = 0.5f),
                                        CircleShape
                                    )
                            )
                            if (index < 1) Spacer(modifier = Modifier.width(4.dp))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.Black.copy(alpha = 0.8f)
                                    ), startY = 0f, endY = 300f
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.width(250.dp),
                                    text = userData.name.title + " " + userData.name.first + " " + userData.name.last,
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(
                                        fontFamily = FontFamily.Serif

                                    ),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${userData.dob.age}",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Location",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${userData.location.city}, ${userData.location.state} - ${userData.location.country}",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                ProfileBadge(
                                    icon = Icons.Filled.Person, text = userData.gender
                                )
                                ProfileBadge(
                                    icon = Icons.Filled.CheckCircle, text = "Verified"
                                )
                                ProfileBadge(
                                    icon = Icons.Filled.DateRange,
                                    text = "Joined : ${extractYearFromIsoAndroid(userData.registered.date)}"
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ActionButton(
                    icon = Icons.Filled.Close,
                    backgroundColor = Color.White,
                    iconColor = Color.Gray,
                    height = 56.dp,
                    width = 100.dp
                ) {
                    onAction.invoke(MatchmateAction.DislikeClicked(
                        userData.login.uuid,
                        index
                    ))
                }

                ActionButton(
                    icon = Icons.Filled.Favorite,
                    backgroundColor = Color.White,
                    iconColor = Color.Red,
                    height = 56.dp,
                    width = 100.dp
                ) {
                    onAction.invoke(MatchmateAction.LikeClicked(
                        userData.login.uuid,
                        index
                    ))
                }
            }
        }

        // Down arrow

    }
}

@Composable
fun ProfileBadge(
    icon: ImageVector, text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .background(
                Color.Black.copy(alpha = 0.3f), RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text, color = Color.White, fontSize = 12.sp
        )
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    width: Dp,
    height: Dp,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .height(
                height
            )
            .width(
                width
            ),
        containerColor = backgroundColor,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DatingAppScreenPreview() {
    MaterialTheme {
        VerticalPagerComponent(0)
    }
}