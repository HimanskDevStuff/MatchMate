package com.match.matchmate.presentation.matchMate

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.match.matchmate.data.model.MatchStatus
import com.match.matchmate.presentation.base.components.CircularProgressComponent
import com.match.matchmate.presentation.base.components.ShaadiSwipeCard
import com.match.matchmate.presentation.matchMate.components.MatchScreen
import com.match.matchmate.presentation.matchMate.components.NoMatchScreen
import com.match.matchmate.presentation.matchMate.components.TutorialOverlay
import com.match.matchmate.presentation.matchMate.components.VerticalPagerComponent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction
import com.match.matchmate.presentation.matchMate.contracts.MatchmateEvent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateState
import com.match.matchmate.presentation.matchMate.viewmodel.MatchmateViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun MatchMateRoot(
    viewModel: MatchmateViewModel = hiltViewModel<MatchmateViewModel>(),
    onEvent: (MatchmateEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            onEvent(event)
        }
    }

    MatchmateScreen(
        state = state, onAction = viewModel::onAction
    )
}

@Composable
private fun MatchmateScreen(
    state: MatchmateState, onAction: (MatchmateAction) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {
        maxOf(1, state.matchMateResponse.results.size)
    })
    val currentPage = remember { mutableIntStateOf(pagerState.currentPage) }
    val coroutineScope = rememberCoroutineScope()
    val showNoInternetAvailable = remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        launch {
            snapshotFlow { pagerState.currentPage }
                .collectLatest { page ->

                    if(state.matchMateResponse.results.isNotEmpty() && page >= state.matchMateResponse.results.size - 4){
                        onAction(MatchmateAction.LoadNextPageData)
                    }


                    currentPage.intValue = page

                    currentPage.intValue = if (state.matchMateResponse.results.isEmpty()) {
                        0
                    } else {
                        currentPage.intValue.coerceIn(0, state.matchMateResponse.results.size - 1)
                    }

                }
        }
    }

    LaunchedEffect(state.isInternetAvailable) {
        showNoInternetAvailable.value = true
        delay(6000)
        if (state.isInternetAvailable) {
            showNoInternetAvailable.value = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            if (state.isLoading && state.matchMateResponse.results.isEmpty()) {
                CircularProgressComponent()
            } else {

                Log.d("ShaadiSwipeCard", "onSwipeLeftAction ::${currentPage.intValue}")

                val hasValidData =
                    state.matchMateResponse.results.isNotEmpty() && currentPage.intValue < state.matchMateResponse.results.size

                if (!hasValidData) {
                    CircularProgressComponent()
                } else {
                    ShaadiSwipeCard(
                        disableSwipe = state.matchMateResponse.results[currentPage.intValue].matchStatus != MatchStatus.NOT_DECIDED,
                        onSwipeLeftAction = {
                            onAction.invoke(
                                MatchmateAction.DislikeClicked(
                                    state.matchMateResponse.results[currentPage.intValue].login.uuid,
                                    currentPage.intValue
                                )
                            )
                        },
                        onSwipeRightAction = {
                            Log.d("ShaadiSwipeCard", "Right ${currentPage.intValue}")

                            onAction.invoke(
                                MatchmateAction.LikeClicked(
                                    state.matchMateResponse.results[currentPage.intValue].login.uuid,
                                    currentPage.intValue
                                )
                            )
                        }
                    ) {
                        if (state.matchMateResponse.results.isNotEmpty()) {
                            VerticalPager(
                                modifier = Modifier
                                    .fillMaxSize(),
                                userScrollEnabled = true,
                                state = pagerState,
                                key = { index ->
                                    if (index < state.matchMateResponse.results.size) {
                                        state.matchMateResponse.results[index].login.uuid
                                    } else {
                                        "loading_$index"
                                    }
                                }) { index ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    if (index < state.matchMateResponse.results.size) {
                                        when (state.matchMateResponse.results[index].matchStatus) {
                                            MatchStatus.NOT_DECIDED -> {
                                                VerticalPagerComponent(
                                                    index = index,
                                                    userData = state.matchMateResponse.results[index],
                                                    onAction = onAction
                                                )
                                            }

                                            MatchStatus.LIKED -> {
                                                MatchScreen(
                                                    userImageRes = "\"https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png\"",
                                                    matchImageRes = state.matchMateResponse.results[index].picture.thumbnail,
                                                    matchName = state.matchMateResponse.results[index].name.first,
                                                    onKeepSwiping = {
                                                        coroutineScope.launch {
                                                            pagerState
                                                                .animateScrollToPage(currentPage.value + 1)
                                                        }
                                                    }
                                                )
                                            }

                                            MatchStatus.DISLIKED -> {
                                                NoMatchScreen(
                                                    userImageRes = "https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png",
                                                    matchImageRes = state.matchMateResponse.results[index].picture.thumbnail,
                                                    matchName = state.matchMateResponse.results[index].name.first,
                                                    onKeepSwiping = {
                                                        coroutineScope.launch {
                                                            pagerState
                                                                .animateScrollToPage(currentPage.value + 1)
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    } else {
                                        CircularProgressComponent()
                                    }
                                }
                            }
                        } else {
                            CircularProgressComponent()
                        }
                    }
                }

            }

            if (showNoInternetAvailable.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .align(androidx.compose.ui.Alignment.BottomCenter)
                        .background(
                            if (state.isInternetAvailable) Color.Green else Color.Red
                        )
                ) {
                    Text(
                        text = if (state.isInternetAvailable) "You're Back Online" else "No Internet Connection Available",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            TutorialOverlay(
                isVisible = state.showTutorial,
                onDismiss = { onAction(MatchmateAction.DismissTutorial) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMatchmateScreen() {
    MatchmateScreen(
        state = MatchmateState(isLoading = false), onAction = {})
}