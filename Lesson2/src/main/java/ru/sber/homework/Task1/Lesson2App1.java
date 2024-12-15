package ru.sber.homework.Task1;

import java.util.Arrays;
import java.util.Map;

public class Lesson2App1 {
    public static void main(String[] args) {
        String[] strings = new String[]{"ABC", "BCD", "CDE", "ABC", "DEF", "EFG", "FGH", "BCD", "Fly", "ABC", "FLY",
                "Array", "DEF", "List", "Map", "FGH", "Set"};

        System.out.println("Дан массив: " + Arrays.toString(strings));

        Map<String, Integer> duplicatesString = DuplicatesStringUtils.findDuplicatesString(strings);

        DuplicatesStringUtils.printInfoArray(duplicatesString);
    }

}
