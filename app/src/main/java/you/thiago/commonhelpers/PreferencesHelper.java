package you.thiago.commonhelpers;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings({"unused"})
public class PreferencesHelper {

    /**
     * Singleton instance to get preferences
     */
    private static SharedPreferences preferences;

    /**
     * Preferences resource name (use package name as default)
     * Add resource into values.xml to set a different preferences name
     */
    private static final String preferencesNameRes = "preferences_name";

    /**
     * Get class instance inner class
     */
    private static class PreferencesHelperInstance {
        private static final PreferencesHelper INSTANCE = new PreferencesHelper();
    }

    private PreferencesHelper() {}

    /**
     * Get singleton instance in thread safe mode
     */
    private static PreferencesHelper getInstance() {
        return PreferencesHelperInstance.INSTANCE;
    }

    private SharedPreferences getPreferences(Context context) {
        if (preferences == null) {
            int resId = context.getResources().getIdentifier(preferencesNameRes, "string", context.getPackageName());

            if (resId != 0) {
                preferences = context.getSharedPreferences(context.getString(resId), Context.MODE_PRIVATE);
            } else {
                preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            }
        }

        return preferences;
    }

    public static SharedPreferences get(Context context) {
        return getInstance().getPreferences(context);
    }

    public static SharedPreferences.Editor edit(Context context) {
        return getInstance().getPreferences(context).edit();
    }

    public static boolean has(Context context, String... keys) {
        if (keys == null || keys.length == 0) {
            return false;
        }

        for (String key : keys) {
            if (!getInstance().getPreferences(context).contains(key)) {
                return false;
            }
        }

        return true;
    }

    public static boolean notHas(Context context, String... keys) {
        if (keys == null || keys.length == 0) {
            return true;
        }

        for (String key : keys) {
            if (key == null || !getInstance().getPreferences(context).contains(key)) {
                return true;
            }
        }

        return false;
    }

    public static String getString(Context context, String key) {
        return getInstance().getPreferences(context).getString(key, null);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getInstance().getPreferences(context).getString(key, defaultValue);
    }

    public static Boolean getBoolean(Context context, String key) {
        return getInstance().getPreferences(context).getBoolean(key, false);
    }

    public static Boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getInstance().getPreferences(context).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key) {
        return getInstance().getPreferences(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getInstance().getPreferences(context).getInt(key, defaultValue);
    }

    public static String getString(SharedPreferences preferences, String key) {
        return preferences.getString(key, null);
    }

    public static Boolean getBoolean(SharedPreferences preferences, String key) {
        return preferences.getBoolean(key, false);
    }

    public static int getInt(SharedPreferences preferences, String key) {
        return preferences.getInt(key, 0);
    }

    public static String getString(SharedPreferences preferences, String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static Boolean getBoolean(SharedPreferences preferences, String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static int getInt(SharedPreferences preferences, String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public static void save(Context context, String key, Object value) {
        SharedPreferences.Editor editor = getInstance().getPreferences(context).edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }

        editor.apply();
    }

    public static void save(SharedPreferences preferences, String key, Object value) {
        SharedPreferences.Editor editor = preferences.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }

        editor.apply();
    }

    public static void remove(Context context, String key) {
        SharedPreferences preferences = getInstance().getPreferences(context);
        if (preferences.contains(key)) {
            preferences.edit().remove(key).apply();
        }
    }

    public static void remove(SharedPreferences preferences, String key) {
        if (preferences.contains(key)) {
            preferences.edit().remove(key).apply();
        }
    }

    public static boolean isNotEmptyInt(Context context, String... keys) {
        if (keys == null || keys.length == 0) {
            return false;
        }

        for (String key : keys) {
            if (getInstance().getPreferences(context).getInt(key, 0) <= 0) {
                return false;
            }
        }

        return true;
    }
}
