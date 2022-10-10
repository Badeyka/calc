import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String input = new Scanner(System.in).nextLine();
        String res = calc(input);
        System.out.println(res);
    }

    public static String calc(String input) {
        String strIn = validate(input);
        boolean roman = Arrays.stream(strIn.split(""))
                .anyMatch(s -> s.equals("I") || s.equals("V") || s.equals("X") || s.equals("L") || s.equals("C"));
        String[] splitStrIn = strIn.split("[-+*/]");

        if (!roman) {
            int x = Integer.parseInt(splitStrIn[0]);
            int y = Integer.parseInt(splitStrIn[1]);

            if (strIn.contains("+")) return String.valueOf(x + y);
            if (strIn.contains("-")) return String.valueOf(x - y);
            if (strIn.contains("*")) return String.valueOf(x * y);
            if (strIn.contains("/")) return String.valueOf(x / y);

        } else {
            int x = romanToArabic(splitStrIn[0]);
            int y = romanToArabic(splitStrIn[1]);

            if (strIn.contains("+")) return arabicToRoman(x + y);
            if (strIn.contains("-")) {
                if (x - y < 1) {
                    System.out.println("throws Exception //т.к. в римской системе нет отрицательных чисел");
                    System.exit(0);
                }
                return arabicToRoman(x - y);
            }
            if (strIn.contains("*")) return arabicToRoman(x * y);
            if (strIn.contains("/")) {
                if (x / y < 1) {
                    System.out.println("throws Exception //т.к. в римской системе нет отрицательных чисел");
                    System.exit(0);
                }
                return arabicToRoman(x / y);
            }
        }
        return "Что то пошло не так";
    }

    private static String validate(String input) {
        //Удаление пробелова из строки
        String withoutSpaces = Arrays.stream(input.split(""))
                .filter(s -> !s.matches(".*\\s.*"))
                .collect(Collectors.joining());

        String[] arraySplitStringEmpty = withoutSpaces.split("");
        String[] arraySplitArithmeticOperations = withoutSpaces.split("[-+*/]");


        //Проверка на числа с плавающей точкой
        if (Arrays.stream(arraySplitStringEmpty)
                .anyMatch(s -> s.equals(".") || s.equals(","))) {
            System.out.println("Калькулятор умеет работать только с целыми числами");
            System.exit(0);
        }

        //Проверка на недопустимые символы
        if (Arrays.stream(arraySplitStringEmpty)
                .filter(s -> !s.matches("\\d"))
                .filter(s -> !s.equals("I") && !s.equals("V") && !s.equals("X") && !s.equals("L") && !s.equals("C"))
                .anyMatch(s -> !s.equals("+") && !s.equals("-") && !s.equals("*") && !s.equals("/"))) {
            System.out.println("Введен хотя бы один недопустимый символ." +
                    " Калькулятор умеет выполнять операции сложения(+), вычитания(-), умножения(*) и деления(/)" +
                    " только с целыми арабскими и римскими числами");
            System.exit(0);
        }

        //Проверка на количество операций и отрицательные числа
        if (Arrays.stream(arraySplitStringEmpty)
                .filter(s -> s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/"))
                .count() > 1) {
            System.out.println("Калькулятор умеет работать только с одной операцией и с положительными числами");
            System.exit(0);
        }

        //Проверка на отсутствие ввода операции
        if (Arrays.stream(arraySplitStringEmpty)
                .noneMatch(s -> s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/"))) {
            System.out.println("throws Exception //т.к. строка не является математической операцией");
            System.exit(0);
        }

        //Проверка на количество чисел
        if (Arrays.stream(arraySplitArithmeticOperations)
                .filter(s -> !s.isEmpty())
                .count() != 2) {
            System.out.println("Калькулятор умеет работать только с двумя числами");
            System.exit(0);
        }

        //Проверка на ввод арабской и римской цифрами в одной операции
        boolean roman = Arrays.stream(arraySplitArithmeticOperations)
                .anyMatch(s -> !s.matches(".*\\d.*"));
        if (roman) {
            if (Arrays.stream(arraySplitArithmeticOperations)
                    .anyMatch(s -> s.matches(".*\\d.*"))) {
                System.out.println("throws Exception //т.к. используются одновременно разные системы счисления");
                System.exit(0);
            }
        }

        //Проверка на входные числа от 1 до 10
        if (roman) {
            try {
                romanToArabic(arraySplitArithmeticOperations[0]);
                romanToArabic(arraySplitArithmeticOperations[1]);
            } catch (NullPointerException e) {
                System.out.println("Калькулятор выполняет операции только с числами от 1 до 10");
                System.exit(0);
            }
        } else {
            int x = Integer.parseInt(arraySplitArithmeticOperations[0]);
            int y = Integer.parseInt(arraySplitArithmeticOperations[1]);
            if (x < 1 || x > 10 || y < 1 || y > 10) {
                System.out.println("Калькулятор выполняет операции только с числами от 1 до 10");
                System.exit(0);
            }
        }

        return withoutSpaces;
    }

    private static int romanToArabic(String roman) {
        Map<String, Integer> map = new HashMap<>();
        map.put("I", 1);
        map.put("II", 2);
        map.put("III", 3);
        map.put("IV", 4);
        map.put("V", 5);
        map.put("VI", 6);
        map.put("VII", 7);
        map.put("VIII", 8);
        map.put("IX", 9);
        map.put("X", 10);

        return map.get(roman);
    }

    private static String arabicToRoman(int arabic) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "I");
        map.put(2, "II");
        map.put(3, "III");
        map.put(4, "IV");
        map.put(5, "V");
        map.put(6, "VI");
        map.put(7, "VII");
        map.put(8, "VIII");
        map.put(9, "IX");
        map.put(10, "X");
        map.put(20, "XX");
        map.put(30, "XXX");
        map.put(40, "XL");
        map.put(50, "L");
        map.put(60, "LX");
        map.put(70, "LXX");
        map.put(80, "LXXX");
        map.put(90, "XC");
        map.put(100, "C");

        if (arabic <= 10) return map.get(arabic);
        if (arabic % 10 == 0) return map.get(arabic);
        return map.get(arabic / 10 * 10) +
                map.get(arabic % 10);
    }
}



