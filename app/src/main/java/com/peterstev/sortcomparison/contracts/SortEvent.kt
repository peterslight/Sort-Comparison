package com.peterstev.sortcomparison.contracts

sealed class SortEvent {
    data class Compare(val firstIndex: Int, val secondIndex: Int) : SortEvent()
    data class Progress(val progress: Float) : SortEvent()
}