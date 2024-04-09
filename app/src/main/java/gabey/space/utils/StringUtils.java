package gabey.space.utils;

import java.util.List;

public class StringUtils {
    public static String removeHtmlTags(String s) {
        return s.replaceAll("<[^>]*>", "");
    }

    public static String readMore(String s, int max) {

        if (s.length() > max) {
            return s.substring(0, max) + "...";
        }

        return s;
    }

    public static String joinAsString(List<String> arr, String join) {
        if (arr.isEmpty()) {
            return "";
        } else {
            String res = "Genres: " + arr.get(0);
            for (int i = 1; i < arr.size(); i++) {
                res += join + arr.get(i);
            }
            return res;
        }
    }
}