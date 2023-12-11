package com.github.metallnt.modact.util;

import java.util.Collection;

/**
 * Class com.github.metallnt.modact.util
 * <p>
 * Date: 29.12.2021 11:13 29 12 2021
 *
 * @author Metall
 */
public class ModActTools {

    /**
     * Найдите наиболее близкое предложение для данной строки и данного списка строк.
     *
     * @param input Строка для сравнения
     * @param list  Список строк для поиска ближайшего предложения в нем.
     * @return строку, ближайшую к строке ввода в данном списке.
     */
    public static String findClosestSuggestion(String input, Collection<String> list) {
        int lowestDistance = Integer.MAX_VALUE;
        String bestSuggestion = null;

        for (String possibility : list) {
            int dist = editDistance(input, possibility);

            if (dist < lowestDistance) {
                lowestDistance = dist;
                bestSuggestion = possibility;
            }
        }

        return bestSuggestion + ";" + lowestDistance;
    }

    /**
     * Рассчитывает расстояние редактирования двух строк (расстояние Левенштейна)
     *
     * @param a Первая строка для сравнения
     * @param b Вторая строка для сравнения
     * @return Расстояние Левенштейна двух строк.
     */
    public static int editDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    /**
     * Создать строку, которая показывает все элементы данного списка
     * Конечный разделитель - это последнее слово, используемое для второго последнего элемента.
     * Пример: список с {1,2,3,4,5,6,7,8,9,0} и конечным разделителем 'или'
     * Показывает: 1, 2, 3, 4, 5, 6, 7, 8, 9 или 0.
     *
     * @param collection Массив, из которого нужно получить элементы.
     * @param endDivider Последнее слово, используемое для разделения второго с конца и последнего слова.
     * @return строка со всеми элементами.
     */
    public static String seperateList(final Collection<?> collection, final String endDivider) {
        final Object[] array = collection.toArray();
        if (array.length == 1)
            return array[0].toString();

        if (array.length == 0)
            return null;

        final StringBuilder string = new StringBuilder();

        for (int i = 0; i < array.length; i++) {

            if (i == (array.length - 1)) {
                string.append(array[i]);
            } else if (i == (array.length - 2)) {
                // Second last
                string.append(array[i]).append(" ").append(endDivider).append(" ");
            } else {
                string.append(array[i]).append(", ");
            }
        }

        return string.toString();
    }
}
