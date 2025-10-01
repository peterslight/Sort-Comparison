package com.peterstev.sortcomparison

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterstev.sortcomparison.contracts.AlgoType
import com.peterstev.sortcomparison.contracts.BarItem
import com.peterstev.sortcomparison.contracts.SortEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@FlowPreview
class SortViewModel : ViewModel() {
    private val _bars = mutableStateListOf<BarItem>()
    val bars: List<BarItem> get() = _bars

    private val _highlightedIndices = mutableStateListOf<Int>()
    val highlightedIndices: List<Int> get() = _highlightedIndices

    private val _elapsedTime = mutableStateOf("")
    val elapsedTime: State<String> get() = _elapsedTime

    private val _bubbleProgress = mutableFloatStateOf(0f)
    val bubbleProgress: State<Float> get() = _bubbleProgress

    private val _mergeProgress = mutableFloatStateOf(0f)
    val mergeProgress: State<Float> get() = _mergeProgress

    private val _quickProgress = mutableFloatStateOf(0f)
    val quickProgress: State<Float> get() = _quickProgress

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading

    private var randomNumbers: List<BarItem> = emptyList()

    private var bubbleJob: Job? = null
    private var mergeJob: Job? = null
    private var quickJob: Job? = null

    private fun reset() {
        _mergeProgress.floatValue = 0f
        _quickProgress.floatValue = 0f
        _bubbleProgress.floatValue = 0f

        bubbleJob?.cancel()
        mergeJob?.cancel()
        quickJob?.cancel()
    }

    fun sortComparison() {
        viewModelScope.launch {
            _loading.value = true

            if (randomNumbers.isEmpty()) {
                randomNumbers = generateNumbers()
            }
            _loading.value = false

            reset()

            val bubbleBars = randomNumbers.toMutableList()
            val mergeBars = randomNumbers.toMutableList()
            val quickBars = randomNumbers.toMutableList()

            bubbleJob = launch {
                bubbleSortEvents(bubbleBars)
                    .filterIsInstance<SortEvent.Progress>()
                    .map { it.progress }
                    .conflate()
                    .flowOn(Dispatchers.Default)
                    .collect { _bubbleProgress.floatValue = it }
            }

            quickJob = launch {
                quickSortEvents(quickBars)
                    .filterIsInstance<SortEvent.Progress>()
                    .map { it.progress }
                    .conflate()
                    .flowOn(Dispatchers.Default)
                    .collect { _quickProgress.floatValue = it }
            }

            mergeJob = launch {
                mergeSortEvents(mergeBars)
                    .filterIsInstance<SortEvent.Progress>()
                    .map { it.progress }
                    .conflate()
                    .flowOn(Dispatchers.Default)
                    .collect { _mergeProgress.floatValue = it }
            }
        }
    }

    fun performSort(input: List<Int>, type: AlgoType) {
        setBars(input)
        val algo = when (type) {
            AlgoType.BUBBLE -> bubbleSortEvents(_bars)
            AlgoType.MERGE -> mergeSortEvents(_bars)
            AlgoType.QUICK -> quickSortEvents(_bars)
        }

        val startTime = System.nanoTime()

        viewModelScope.launch {
            algo.collect { event ->
                if (event is SortEvent.Compare) {
                    _highlightedIndices.clear()
                    _highlightedIndices.addAll(listOf(event.firstIndex, event.secondIndex))
                    delay(100)
                }
            }
            _highlightedIndices.clear()

            val endTime = System.nanoTime()
            val elapsedNanos = endTime - startTime
            val elapsedMillis = elapsedNanos / 1_000_000
            val seconds = elapsedMillis / 1000
            val millisRemainder = elapsedMillis % 1000
            val formatted = "${seconds}s ${millisRemainder}ms"
            val combined = "${seconds * 1000 + millisRemainder}ms ($formatted)"
            _elapsedTime.value = combined
        }
    }

    private fun setBars(values: List<Int>) {
        val baseColors = listOf(
            Color.Blue, Color.Cyan, Color.LightGray, Color.Red,
            Color.Yellow, Color.Green, Color.Magenta, Color.Gray
        )
        _bars.clear()
        _bars.addAll(values.map { value ->
            BarItem(value, baseColors.random())
        })
    }

    private suspend fun generateNumbers(): List<BarItem> = withContext(Dispatchers.Default) {
        return@withContext try {
            val random = Random(System.currentTimeMillis())
            List(10_000) { BarItem(random.nextInt(0, 50_000)) }
        } catch (e: OutOfMemoryError) {
            print("reduce number of generated items: ${e.message}")
            emptyList()
        }
    }
}