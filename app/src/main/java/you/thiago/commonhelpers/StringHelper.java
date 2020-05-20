package you.thiago.commonhelpers;

import android.content.Context;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@SuppressWarnings({"unused", "WeakerAccess"})
public class StringHelper {

    private static final ArrayList<String> COMMON_NON_CAPITALIZED_WORDS = new ArrayList<String>() {
        {
            add("da");
            add("de");
            add("do");
        }
    };

    public static String capitalize(@Nullable String str) {
        return capitalize(str, null, null);
    }

    public static String capitalizeWithUpper(@Nullable String str, List<String> withUpper) {
        return capitalize(str, withUpper, null);
    }

    public static String capitalizeWithLower(@Nullable String str, List<String> withLower) {
        return capitalize(str, null, withLower);
    }

    public static String capitalizeWithUpper(@Nullable String str, String[] withUpper) {
        return capitalize(str, Arrays.asList(withUpper), null);
    }

    public static String capitalizeWithLower(@Nullable String str, String[] withLower) {
        return capitalize(str, null, Arrays.asList(withLower));
    }

    public static String capitalize(@Nullable String str, List<String> withUpper, List<String> withLower) {
        if (str == null || str.trim().isEmpty() || str.toLowerCase().equals("null")) {
            str = "";
        }

        if (withUpper == null) {
            withUpper = new ArrayList<>();
        }
        if (withLower == null) {
            withLower = new ArrayList<>();
        }

        StringBuilder s = new StringBuilder();
        String[] words = str.split(" ");

        for (String word : words) {
            if (withUpper.contains(word.toLowerCase())) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            } else  if (withLower.contains(word.toLowerCase())) {
                word = word.toUpperCase();
            } else if (word.length() > 2) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            } else {
                if (word.length() == 2 && !COMMON_NON_CAPITALIZED_WORDS.contains(word.toLowerCase())) {
                    word = word.toUpperCase();
                } else {
                    word = word.toLowerCase();
                }
            }

            s.append(word);
            s.append(" ");
        }

        return s.toString().trim();
    }

    public static String validateEndpoint(String endpoint) {
        String protocol = "";

        if (endpoint.contains("http://")) {
            protocol = "http://";
            endpoint = endpoint.replace("http://", "");
        } else if (endpoint.contains("https://")) {
            protocol = "https://";
            endpoint = endpoint.replace("https://", "");
        }

        endpoint = endpoint.replaceAll("/+", "/");

        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }

        return protocol + endpoint;
    }

    public static Spanned fromHtml(Context context, int valueResource) {
        return StringHelper.fromHtml(context.getString(valueResource));
    }

    public static Spanned fromHtml(String value) {
        return HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY);
    }

    public static String toString(double value) {
        return toString(value, 2, false);
    }

    public static String toString(double value, int precision) {
        return toString(value, precision, false);
    }

    public static String toString(double value, int precision, boolean useDotSeparator) {
        Locale locale = Locale.getDefault();
        if (useDotSeparator) {
            locale = Locale.ROOT;
        }

        return String.format(locale, "%." + precision + "f", value);
    }

    public static int toInt(String value) {
        int _value = 0;

        try {
            if (Validator.isNotEmpty(value)) {
                _value = Integer.parseInt(value);
            }
        } catch (Exception e) {
            Log.e(StringHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return _value;
    }

    public static String escapeToJson(String s) {
        s = s.replaceAll("'", "\\'");
        s = s.replaceAll("´", "");
        s = s.replaceAll("`", "");

        return s;
    }
}
