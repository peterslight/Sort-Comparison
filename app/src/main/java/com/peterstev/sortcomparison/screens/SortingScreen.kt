package com.peterstev.sortcomparison.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterstev.sortcomparison.contracts.AlgoType
import com.peterstev.sortcomparison.contracts.Algorithm
import com.peterstev.sortcomparison.contracts.BarItem

@Composable
fun SortingScreen(
    bars: List<BarItem>,
    highlighted: List<Int>,
    timer: String,
    onAlgoSelected: (AlgoType) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val topPadding = screenHeight * 0.01f
    val availableHeight = screenHeight * 0.5f

    val algo = listOf(
        Algorithm(AlgoType.BUBBLE, "Bubble Sort"),
        Algorithm(AlgoType.MERGE, "Merge Sort"),
        Algorithm(AlgoType.QUICK, "Quick Sort"),
    )

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BarsLayout(
            bars,
            highlighted,
            Modifier
                .padding(top = topPadding)
                .height(availableHeight)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier
                .padding(4.dp),
            text = "timer: $timer",
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
        )

        Spacer(Modifier.height(24.dp))

        AlgorithmPicker(
            algorithms = algo,
            onSelect = { chosen ->
                onAlgoSelected(chosen)
            }
        )

        Text(
            modifier = Modifier
                .padding(24.dp),
            text = "Note: Delay added to visually simulate sorting in realtime",
            fontSize = 16.sp,
        )
    }
}