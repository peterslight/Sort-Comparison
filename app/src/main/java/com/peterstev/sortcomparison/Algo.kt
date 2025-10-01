package com.peterstev.sortcomparison

import com.peterstev.sortcomparison.contracts.BarItem
import com.peterstev.sortcomparison.contracts.SortEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.log2

fun bubbleSortEvents(numbers: MutableList<BarItem>): Flow<SortEvent> = flow {
    val totalSize = numbers.size
    val maxSteps = totalSize - 1

    for (passIndex in 0 until maxSteps) {
        var swappedInThisPass = false
        for (currentIndex in 0 until totalSize - passIndex - 1) {
            val nextIndex = currentIndex + 1
            emit(SortEvent.Compare(currentIndex, nextIndex))

            if (numbers[currentIndex].value > numbers[nextIndex].value) {
                numbers.swap(currentIndex, nextIndex)
                swappedInThisPass = true
            }
        }

        if (swappedInThisPass) {
            val progress = (passIndex + 1) / maxSteps.toFloat()
            emit(SortEvent.Progress(progress))
        } else {
            break
        }
    }
    emit(SortEvent.Progress(1f))
}

fun MutableList<BarItem>.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

fun quickSortEvents(bars: MutableList<BarItem>): Flow<SortEvent> = flow {
    val totalSize = bars.size
    // Worst-case quicksort does O(n log n) comparisons/swaps
    val maxSteps = (totalSize * log2(totalSize.toDouble())).toInt().coerceAtLeast(1)
    var steps = 0

    suspend fun partition(startIndex: Int, endIndex: Int): Int {
        val pivotValue = bars[endIndex].value
        var smallerElementPointer = startIndex
        for (currentIndex in startIndex until endIndex) {
            emit(SortEvent.Compare(currentIndex, endIndex))
            steps++
            emit(SortEvent.Progress((steps.toFloat() / maxSteps).coerceIn(0f, 1f)))

            if (bars[currentIndex].value <= pivotValue) {
                bars.swap(smallerElementPointer, currentIndex)
                steps++
                emit(SortEvent.Progress((steps.toFloat() / maxSteps).coerceIn(0f, 1f)))
                smallerElementPointer++
            }
        }
        bars.swap(smallerElementPointer, endIndex)
        steps++
        emit(SortEvent.Progress((steps.toFloat() / maxSteps).coerceIn(0f, 1f)))
        return smallerElementPointer
    }

    suspend fun quickSort(startIndex: Int, endIndex: Int) {
        if (startIndex >= endIndex) return
        val pivotIndex = partition(startIndex, endIndex)
        quickSort(startIndex, pivotIndex - 1)
        quickSort(pivotIndex + 1, endIndex)
    }

    quickSort(0, bars.lastIndex)

    emit(SortEvent.Progress(1f))
}

fun mergeSortEvents(bars: MutableList<BarItem>): Flow<SortEvent> = flow {
    val totalSize = bars.size
    // MergeSort does O(n log n) comparisons/swaps worst case
    val maxSteps = (totalSize * log2(totalSize.toDouble())).toInt().coerceAtLeast(1)
    var steps = 0

    suspend fun merge(startIndex: Int, middleIndex: Int, endIndex: Int) {
        val leftPart = bars.subList(startIndex, middleIndex + 1).map { it }
        val rightPart = bars.subList(middleIndex + 1, endIndex + 1).map { it }

        var leftPointer = 0
        var rightPointer = 0
        var writePointer = startIndex

        while (leftPointer < leftPart.size && rightPointer < rightPart.size) {
            emit(SortEvent.Compare(startIndex + leftPointer, middleIndex + 1 + rightPointer))
            steps++
            emit(SortEvent.Progress((steps.toFloat() / maxSteps).coerceIn(0f, 1f)))

            if (leftPart[leftPointer].value <= rightPart[rightPointer].value) {
                bars[writePointer] = leftPart[leftPointer]
                leftPointer++
            } else {
                bars[writePointer] = rightPart[rightPointer]
                rightPointer++
            }
            steps++
            emit(SortEvent.Progress((steps.toFloat() / maxSteps).coerceIn(0f, 1f)))
            writePointer++
        }

        while (leftPointer < leftPart.size) {
            bars[writePointer] = leftPart[leftPointer]
            leftPointer++
            writePointer++
            steps++
            emit(SortEvent.Progress((steps.toFloat() / maxSteps).coerceIn(0f, 1f)))
        }

        while (rightPointer < rightPart.size) {
            bars[writePointer] = rightPart[rightPointer]
            rightPointer++
            writePointer++
            steps++
            emit(SortEvent.Progress((steps.toFloat() / maxSteps).coerceIn(0f, 1f)))
        }
    }

    suspend fun mergeSort(startIndex: Int, endIndex: Int) {
        if (startIndex >= endIndex) return
        val middleIndex = (startIndex + endIndex) / 2
        mergeSort(startIndex, middleIndex)
        mergeSort(middleIndex + 1, endIndex)
        merge(startIndex, middleIndex, endIndex)
    }

    mergeSort(0, bars.lastIndex)

    emit(SortEvent.Progress(1f))
}