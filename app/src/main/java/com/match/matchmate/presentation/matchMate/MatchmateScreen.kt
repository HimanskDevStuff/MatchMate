package com.match.matchmate.presentation.matchMate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.match.matchmate.presentation.base.components.CircularProgressComponent
import com.match.matchmate.presentation.matchMate.components.VerticalPagerComponent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction
import com.match.matchmate.presentation.matchMate.contracts.MatchmateEvent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateState
import com.match.matchmate.presentation.matchMate.viewmodel.MatchmateViewModel

/**
 * Composable entry point for the Matchmate feature.
 */
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
    val pagerState =
        rememberPagerState(initialPage = 0, pageCount = { state.matchMateResponse.results.size })
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
                    VerticalPagerComponent(
                        userData = state.matchMateResponse.results[index]
                    )
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