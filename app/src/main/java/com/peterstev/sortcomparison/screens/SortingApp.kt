@file:OptIn(FlowPreview::class)

package com.peterstev.sortcomparison.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.peterstev.sortcomparison.SortViewModel
import com.peterstev.sortcomparison.contracts.AlgoType
import kotlinx.coroutines.FlowPreview

@Composable
fun SortingSection(viewModel: SortViewModel) {
    val data = mutableListOf(15, 1, 14, 20, 28, 5, 11, 24, 2, 8)
    val timer by viewModel.elapsedTime
    LaunchedEffect(Unit) {
        viewModel.performSort(data, AlgoType.BUBBLE)
    }

    SortingScreen(
        bars = viewModel.bars,
        timer = timer,
        highlighted = viewModel.highlightedIndices,
        onAlgoSelected = { viewModel.performSort(data, it) }
    )
}