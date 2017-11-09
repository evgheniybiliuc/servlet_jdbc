package jdbc.servlet.todo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {
    private static String DEFAULT_PATTERN = "^[a-zA-Z0-9._-]{3,}$";

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isAtLeastOneEmptyOrNull(String... arr) {
        for (String str : arr) {
            if (isNullOrEmpty(str)) return true;
        }
        return false;
    }

    public static boolean isValid(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isValid(String string) {
        return isValid(string, DEFAULT_PATTERN);
    }
}
