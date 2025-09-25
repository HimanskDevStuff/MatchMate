package com.match.matchmate.presentation.matchMate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.match.matchmate.data.model.MatchStatus
import com.match.matchmate.presentation.base.components.CircularProgressComponent
import com.match.matchmate.presentation.matchMate.components.MatchScreen
import com.match.matchmate.presentation.matchMate.components.NoMatchScreen
import com.match.matchmate.presentation.matchMate.components.VerticalPagerComponent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction
import com.match.matchmate.presentation.matchMate.contracts.MatchmateEvent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateState
import com.match.matchmate.presentation.matchMate.viewmodel.MatchmateViewModel
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
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { state.matchMateResponse.results.size })
   val currentPage = remember { mutableStateOf(pagerState.currentPage) }
    val coroutineScope = rememberCoroutineScope()
   LaunchedEffect(pagerState.currentPage) {
       snapshotFlow { pagerState.currentPage }
           .collectLatest { page ->
               currentPage.value = page
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
            if (state.isLoading) {
                CircularProgressComponent()
            } else {
                VerticalPager(
                    modifier = Modifier
                        .fillMaxSize(),
                    userScrollEnabled = true,
                    state = pagerState,
                    key = { index ->
                        state.matchMateResponse.results[index].login.uuid
                    }) { index ->
                    when(state.matchMateResponse.results[index].matchStatus){
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
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMatchmateScreen() {
    MatchmateScreen(
        state = MatchmateState(isLoading = false), onAction = {})
}