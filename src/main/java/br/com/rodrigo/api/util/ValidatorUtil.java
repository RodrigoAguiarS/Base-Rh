package br.com.rodrigo.api.util;

public class ValidatorUtil {


    private ValidatorUtil() {
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static Object isEmpty(Object o, Object ret) {
        return isEmpty(o) ? ret : o;
    }

    public static boolean isEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }

    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String)
            return isEmpty((String) o);
        if (o instanceof Number) {
            Number i = (Number) o;
            return (i.intValue() == 0);
        }
        return false;
    }

    public static boolean isNotEqual(Object o1, Object o2) {
        return !isEmpty(o1) && !isEmpty(o2) && !o1.equals(o2);
    }
}
