## Info
an android app to show a visual simulation of how fast common DSA sorting algorithms run.

INFO:

Single sort section: 
dataset: 10 items
delay: 100ms delay added per comparison to show items being swapped on the ui.

here we test each algorithm in isolation, we can see that Quick Sort and Merge Sort are faster than Bubble Sort algorithm, and this difference in speed is wider as the data set grows larger, because bubble sort would have to do O(n^2) work in its worst case, while merge and quick sort would do only O(n log n) work at worst case.

Comparison section:
dataset: 10,000 items
delay: non added, except computational delays from emitting a progress state.

here we compare all 3 algorithms with each other and we can see that with a dataset of 10k items, merge and quick sort are way faster than bubble sort.

## how to run
download latest android studio
min sdk 30
target sdk 36
