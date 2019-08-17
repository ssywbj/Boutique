package com.suheng.java;

public class SortAlgorithm {

    //https://blog.csdn.net/u014596378/article/details/80406438
    static void bubbleSort(int[] array) {
        boolean isSwap;
        int startElement, compareElement;
        /*
         减1是因为在最坏的情况下如果剩最后一对数字还是无序的，此时它们交换后，数组已经是全部有序的了，
         具体可传入类似顺序的数组如{6, 5, 4, 3, 2, 1}，然后打印排序过程观察，所以n个元素的冒泡排序最
         多需要(n-1)趟就可以有序了，而不是n趟。
         */
        for (int i = 0; i < array.length - 1; i++) {
            isSwap = false;

            //减1是为防止数组越界，减i是因为每次排序后大的数都放在了后面，不再需要和它们比较
            for (int j = 0; j < array.length - i - 1; j++) {
                startElement = array[j];
                compareElement = array[j + 1];
                //两两比对，如果前面大于后面就交换，造成结果：小的数往前移，大的数往后移
                if (startElement > compareElement) {
                    isSwap = true;
                    array[j] = compareElement;
                    array[j + 1] = startElement;
                }
            }

            System.out.println("第" + (i + 1) + "次：" + buildString(array));

            if (!isSwap) {//当某一轮没有顺序交换，说明数组已全部有序，此时终止循环
                System.out.println("=====数组已全部有序，终止循环=====");
                return;
            }
        }
    }

    static int[] quickSort(int[] array, int start, int end) {
        int pivot = array[start];
        int i = start;
        int j = end;
        while (i < j) {
            while ((i < j) && (array[j] > pivot)) {
                j--;
            }
            while ((i < j) && (array[i] < pivot)) {
                i++;
            }
            if ((array[i] == array[j]) && (i < j)) {
                i++;
            } else {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        if (i - 1 > start) array = quickSort(array, start, i - 1);
        if (j + 1 < end) array = quickSort(array, j + 1, end);

        return (array);
    }

    private static String buildString(int[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]).append(", ");
        }

        return stringBuilder.deleteCharAt(stringBuilder.length() - 2).toString();//减2而不是减1，是因为后面多了一个空格
    }

    static void print(int[] array) {
        System.out.println(buildString(array));
    }
}
