@file:OptIn(FlowPreview::class)

package com.peterstev.sortcomparison.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterstev.sortcomparison.SortViewModel
import kotlinx.coroutines.FlowPreview

@Composable
fun ComparisonSection(
    viewModel: SortViewModel
) {
    val bubbleProgress by viewModel.bubbleProgress
    val mergeProgress by viewModel.mergeProgress
    val quickProgress by viewModel.quickProgress
    val isGenerating by viewModel.loading

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        if (isGenerating) {
            Text("Generating data...")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Algorithm Progress", style = MaterialTheme.typography.titleMedium)

                AlgorithmProgressRow(
                    label = "Bubble Sort",
                    progress = bubbleProgress
                )

                AlgorithmProgressRow(
                    label = "Merge Sort",
                    progress = mergeProgress
                )

                AlgorithmProgressRow(
                    label = "Quick Sort",
                    progress = quickProgress
                )

                Button(onClick = { viewModel.sortComparison() }) {
                    Text("Start", style = MaterialTheme.typography.titleMedium)
                }

                Text(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    text = "Note: No delay added",
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Composable
fun AlgorithmProgressRow(label: String, progress: Float) {
    Column {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}