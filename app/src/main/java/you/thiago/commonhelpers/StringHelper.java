package you.thiago.commonhelpers;

import android.content.Context;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;

import java.util.Locale;

@SuppressWarnings({"unused", "WeakerAccess"})
public class StringHelper {

    public static String capitalize(@Nullable String str) {
        if (str == null || str.trim().isEmpty() || str.toLowerCase().equals("null")) {
            str = "";
        }

        StringBuilder s = new StringBuilder();

        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {
            if (ch == ' ' && str.charAt(i) != ' ') {
                s.append(Character.toUpperCase(str.charAt(i)));
            } else {
                s.append(Character.toLowerCase(str.charAt(i)));
            }

            ch = str.charAt(i);
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
