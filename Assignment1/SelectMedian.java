package SelectMedian;

import java.util.Arrays;

/**
 * Template:
 * @author Rahnuma Islam Nishat
 * January 20, 2017
 * CSC 226 - Spring 2017
 *
 * Code by Nigel Decontie
 */
public class SelectMedian {
    
    /*Find and returns the kth element in linear time.*/
    public static int LinearSelect(int[] A, int k){
        if(A == null || k == 0 || k > A.length) return -1;
        if(A.length%2 == 1){
            return (A == null || k == 0 || k > A.length) ? -1 : linearSelect(A, 0, A.length - 1, k);
        } else return (A == null || k == 0 || k > A.length) ? -1 : linearSelect(A, 0, A.length - 1, k-1);
    }

    /*Used by LinearSelect to find the kth element.*/
    private static int linearSelect(int[] A, int L, int G, int k) {
        if (L == G) {return A[L];}

        int p = pickCleverPivot(A, L, G);
        p = getIndex(A, L, G, p);
        p = partition(L,G,A,p);

        if (k == p) {
            return A[p];
        } else if (k < p) {
            return linearSelect(A, L, p-1, k);
        } else {
            return linearSelect(A, p+1, G, k);
        }
    }

    /* Recursively sorts and selects the median of medians to achieve overall O(n) time complexity.*/
    private static int pickCleverPivot(int[] A, int L, int G) {
        if(G - L < 5) {
            Arrays.sort(A, L, G);
            return A[A.length/2];
        }

        int[] tmp = null;
        int[] medians = new int[(int)Math.ceil((double)(G - L + 1)/5)];
        int medianIndex = 0;
        while(L <= G) {
            tmp = new int[Math.min(5,G-L+1)];
            for(int j = 0; j < tmp.length && L <= G; j++) {
                tmp[j] = A[L];
                L++;
            }
            Arrays.sort(tmp);
            medians[medianIndex] = tmp[tmp.length/2];
            medianIndex++;
        }
        return pickCleverPivot(medians, 0, medians.length-1);
    }

    /*Partitions the array around the chosen pivot.*/
    private static int partition(int L, int G, int[] A, int p) {
        int pivotValue = A[p];
        swap(A, G, p);
        int storeIndex = L;
        for(int i = L; i < G; i++) {
            if(A[i] < pivotValue) {
                swap(A, storeIndex, i);
                storeIndex++;
            }
        }
        swap(A, storeIndex, G);
        return storeIndex;
    }

    /*Determines the index of the pivot element.*/
    private static int getIndex(int[] A, int L, int G, int p) {
        int index = G;
        for(int j = L; j < G; j++) {
            if(A[j] == p) {
                index = j;
            }
        }
        return index;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /*Main method primarily used as a testing harness.*/
    public static void main(String[] args) {
        int[] A = {50, 54, 49, 49, 48, 49, 56, 52, 51, 52, 50, 59};
        int[] B = {3, 75, 12, 20};
        int[] C = null;
        int[] D = {1, 2, 3, 5, 4};
        System.out.println("The median weight is " + LinearSelect(A, A.length/2));
        System.out.println("The median weight is " + LinearSelect(B, B.length/2));
        System.out.println("The median weight is " + LinearSelect(C, 12));
        System.out.println("The median weight is " + LinearSelect(A, 0));
        System.out.println("The median weight is " + LinearSelect(A, 13));
        System.out.println("The median weight is " + LinearSelect(D, B.length/2));
    }

}
