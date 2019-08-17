package com.suheng.java;

import org.junit.Test;

public class SortAlgorithmTest {
    private int[] array = {6, 5, 4, 3, 2, 1};

    @Test
    public void bubbleSort() {
        SortAlgorithm.bubbleSort(array);
    }

    @Test
    public void quickSort() {
        int[] arr = {4, 7, 6, 5, 1, 3};
        SortAlgorithm.quickSort(arr, 0, arr.length - 1);
        SortAlgorithm.print(arr);
    }

    @Test
    public void print() {
        SortAlgorithm.print(array);
    }
}