package com.peterstev.sortcomparison.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterstev.sortcomparison.contracts.BarItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BarsLayout(
    bars: List<BarItem>,
    highlighted: List<Int>,
    modifier: Modifier = Modifier
) {
    val maxValue = bars.maxOfOrNull { it.value } ?: 1
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val barWidth = screenWidth / bars.size

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        itemsIndexed(bars, key = { index, _ -> index }) { index, value ->
            val barColor = if (highlighted.contains(index)) Color.Black else value.color
            val topOffset = (maxValue - value.value) * 15
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight()
                    .padding(
                        top = topOffset.dp,
                        start = 2.dp,
                        end = 2.dp
                    )
                    .background(
                        barColor, shape = RoundedCornerShape(
                            topStart = 12.dp, topEnd = 12.dp
                        )
                    )
                    .animateItem(tween(10)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    text = value.value.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    color = Color.White
                )
            }
        }
    }
}