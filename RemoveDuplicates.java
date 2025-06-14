import java.util.*;

public class RemoveDuplicates {
    public static void main(String[] args) {
        int[] array = {1, 3, 2, 4, 2, 1, 5, 3};
        Set<Integer> set = new LinkedHashSet<>();
        for (int num : array) {
            set.add(num);
        }
        int[] uniqueArray = new int[set.size()];
        int i = 0;
        for (int num : set) {
            uniqueArray[i++] = num;
        }
        System.out.println("Array after removing duplicates:");
        for (int num : uniqueArray) {
            System.out.print(num + " ");
        }
    }
}
