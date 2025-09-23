package com.match.matchmate.presentation.matchMate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction
import com.match.matchmate.presentation.matchMate.contracts.MatchmateEvent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateState
import com.match.matchmate.presentation.matchMate.viewmodel.MatchmateViewModel

/**
 * Composable entry point for the Matchmate feature.
 */
@Composable
fun MatchMateRoot(
    viewModel: MatchmateViewModel = hiltViewModel(),
    onEvent: (MatchmateEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            onEvent(event)
        }
    }

    MatchmateScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

/**
 * A stateless composable that draws the UI for the Matchmate feature.
 */
@Composable
private fun MatchmateScreen(
    state: MatchmateState,
    onAction: (MatchmateAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = "Feature: Matchmate")
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun PreviewMatchmateScreen() {
    MatchmateScreen(
        state = MatchmateState(isLoading = false),
        onAction = {}
    )
}