package com.example.childcare.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.example.childcare.model.UserType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public final class Prefs {

    private static final String DEFAULT_SUFFIX = "_preferences";
    private static final String LENGTH = "#LENGTH";
    private static SharedPreferences mPrefs;
    private final static String SHARED_PRESENCES_NAME = "com.example.childcare.userType";
    private final static String USER_TYPE_KEY = "userType";
    private final static String CHILD = "child";
    private final static String PARENT = "parent";

    public static void initPrefs(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PRESENCES_NAME, Context.MODE_PRIVATE);
    }


    /**
     * Returns the underlying SharedPreference instance
     *
     * @return an instance of the SharedPreference
     * @throws RuntimeException if SharedPreference instance has not been instantiated yet.
     */
    public static SharedPreferences getPreferences() {
        if (mPrefs != null) {
            return mPrefs;
        }
        throw new RuntimeException(
                "Prefs class not correctly instantiated. Please call Builder.setContext().build() in the Application class onCreate.");
    }

    /**
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     * @see SharedPreferences#getAll()
     */
    public static Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    /**
     * Retrieves a stored int value.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not
     *                            an int.
     * @see SharedPreferences#getInt(String, int)
     */
    public static int getInt(final String key, final int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    /**
     * Retrieves a stored boolean value.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a boolean.
     * @see SharedPreferences#getBoolean(String, boolean)
     */
    public static boolean getBoolean(final String key, final boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    /**
     * Retrieves a stored long value.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see SharedPreferences#getLong(String, long)
     */
    public static long getLong(final String key, final long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    /**
     * Returns the double that has been saved as a long raw bits value in the long preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue the double Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see SharedPreferences#getLong(String, long)
     */
    public static double getDouble(final String key, final double defaultValue) {
        return Double.longBitsToDouble(getPreferences().getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    /**
     * Retrieves a stored float value.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a float.
     * @see SharedPreferences#getFloat(String, float)
     */
    public static float getFloat(final String key, final float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    /**
     * Retrieves a stored String value.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a String.
     * @see SharedPreferences#getString(String, String)
     */
    public static String getString(final String key, final String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    /**
     * Retrieves a Set of Strings as stored by {@link #putStringSet(String, Set)}. On Honeycomb and
     * later this will call the native implementation in SharedPreferences, on older SDKs this will
     * call {@link #getOrderedStringSet(String, Set)}.
     * <strong>Note that the native implementation of {@link SharedPreferences#getStringSet(String,
     * Set)} does not reliably preserve the order of the Strings in the Set.</strong>
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defaultValues otherwise.
     * @throws ClassCastException if there is a preference with this name that is not a Set.
     * @see SharedPreferences#getStringSet(String, Set)
     * @see #getOrderedStringSet(String, Set)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(final String key, final Set<String> defaultValue) {
        SharedPreferences prefs = getPreferences();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return prefs.getStringSet(key, defaultValue);
        } else {
            // Workaround for pre-HC's missing getStringSet
            return getOrderedStringSet(key, defaultValue);
        }
    }

    /**
     * Retrieves a Set of Strings as stored by {@link #putOrderedStringSet(String, Set)},
     * preserving the original order. Note that this implementation is heavier than the native
     * {@link #getStringSet(String, Set)} method (which does not guarantee to preserve order).
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValues otherwise.
     * @throws ClassCastException if there is a preference with this name that is not a Set of
     *                            Strings.
     * @see #getStringSet(String, Set)
     */
    public static Set<String> getOrderedStringSet(String key, final Set<String> defaultValue) {
        SharedPreferences prefs = getPreferences();
        if (prefs.contains(key + LENGTH)) {
            LinkedHashSet<String> set = new LinkedHashSet<>();
            int stringSetLength = prefs.getInt(key + LENGTH, -1);
            if (stringSetLength >= 0) {
                for (int i = 0; i < stringSetLength; i++) {
                    set.add(prefs.getString(key + "[" + i + "]", null));
                }
            }
            return set;
        }
        return defaultValue;
    }

    /**
     * Stores a long value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putLong(String, long)
     */
    public static void putLong(final String key, final long value) {
        final Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Stores an integer value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putInt(String, int)
     */
    public static void putInt(final String key, final int value) {
        final Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Converts an object to String using Gson, and then saves to preferences.
     *
     * @param key         The name of the preference to modify.
     * @param valueObject The object (value) to be serialized and saved in preferences.
     */

    public static void putObjectGson(final String key, Object valueObject) {
        final Editor editor = getPreferences().edit();
        Gson gson = new GsonBuilder().create();
        String value = gson.toJson(valueObject);
        Log.i("Prefs", "putGsonString: key = " + key + " value = " + value);

        editor.putString(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static Object getObjectGson(final String key, final String defaultValue, Class<?> objectClass) {
        Gson gson = new GsonBuilder().create();
        Object object = null;
        String serializedString = getString(key, defaultValue);

        if (!serializedString.equals(defaultValue)) {
            object = gson.fromJson(serializedString, objectClass);
        }
        return object;
    }

    /**
     * Stores a double value as a long raw bits value.
     *
     * @param key   The name of the preference to modify.
     * @param value The double value to be save in the preferences.
     * @see Editor#putLong(String, long)
     */
    public static void putDouble(final String key, final double value) {
        final Editor editor = getPreferences().edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Stores a float value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putFloat(String, float)
     */
    public static void putFloat(final String key, final float value) {
        final Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Stores a boolean value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putBoolean(String, boolean)
     */
    public static void putBoolean(final String key, final boolean value) {
        final Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Stores a String value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putString(String, String)
     */
    public static void putString(final String key, final String value) {
        final Editor editor = getPreferences().edit();
        editor.putString(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Stores a Set of Strings. On Honeycomb and later this will call the native implementation in
     * SharedPreferences.Editor, on older SDKs this will call {@link #putOrderedStringSet(String,
     * Set)}.
     * <strong>Note that the native implementation of {@link Editor#putStringSet(String,
     * Set)} does not reliably preserve the order of the Strings in the Set.</strong>
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putStringSet(String, Set)
     * @see #putOrderedStringSet(String, Set)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putStringSet(final String key, final Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final Editor editor = getPreferences().edit();
            editor.putStringSet(key, value);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                editor.commit();
            } else {
                editor.apply();
            }
        } else {
            // Workaround for pre-HC's lack of StringSets
            putOrderedStringSet(key, value);
        }
    }

    /**
     * Stores a Set of Strings, preserving the order.
     * Note that this method is heavier that the native implementation {@link #putStringSet(String,
     * Set)} (which does not reliably preserve the order of the Set). To preserve the order of the
     * items in the Set, the Set implementation must be one that as an iterator with predictable
     * order, such as {@link LinkedHashSet}.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see #putStringSet(String, Set)
     * @see #getOrderedStringSet(String, Set)
     */
    public static void putOrderedStringSet(String key, Set<String> value) {
        final Editor editor = getPreferences().edit();
        int stringSetLength = 0;
        if (mPrefs.contains(key + LENGTH)) {
            // First read what the value was
            stringSetLength = mPrefs.getInt(key + LENGTH, -1);
        }
        editor.putInt(key + LENGTH, value.size());
        int i = 0;
        for (String aValue : value) {
            editor.putString(key + "[" + i + "]", aValue);
            i++;
        }
        for (; i < stringSetLength; i++) {
            // Remove any remaining values
            editor.remove(key + "[" + i + "]");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Removes a preference value.
     *
     * @param key The name of the preference to remove.
     * @see Editor#remove(String)
     */
    public static void remove(final String key) {
        SharedPreferences prefs = getPreferences();
        final Editor editor = prefs.edit();
        if (prefs.contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            int stringSetLength = prefs.getInt(key + LENGTH, -1);
            if (stringSetLength >= 0) {
                editor.remove(key + LENGTH);
                for (int i = 0; i < stringSetLength; i++) {
                    editor.remove(key + "[" + i + "]");
                }
            }
        }
        editor.remove(key);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * Checks if a value is stored for the given key.
     *
     * @param key The name of the preference to check.
     * @return {@code true} if the storage contains this key value, {@code false} otherwise.
     * @see SharedPreferences#contains(String)
     */
    public static boolean contains(final String key) {
        return getPreferences().contains(key);
    }

    /**
     * Removed all the stored keys and values.
     *
     * @return the {@link Editor} for chaining. The changes have already been committed/applied
     * through the execution of this method.
     * @see Editor#clear()
     */
    public static Editor clear() {
        final Editor editor = getPreferences().edit().clear();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
        return editor;
    }

    /**
     * Returns the Editor of the underlying SharedPreferences instance.
     *
     * @return An Editor
     */
    public static Editor edit() {
        return getPreferences().edit();
    }

    public static UserType getUserType() {
        String userType = mPrefs.getString(USER_TYPE_KEY, null);
        if (userType != null)
            switch (userType) {
                case CHILD:
                    return UserType.CHILD;
                case PARENT:
                    return UserType.PARENT;
            }
        return null;
    }

    public static void saveUserType(UserType userType) {
        switch (userType) {
            case CHILD:
                putString(USER_TYPE_KEY, CHILD);
                break;
            case PARENT:
                putString(USER_TYPE_KEY, PARENT);
                break;

        }
    }

    /**
     * Builder class for the EasyPrefs instance. You only have to call this once in the Application
     * onCreate. And in the rest of the code base you can call Prefs.method name.
     */

    public final static class Builder {

        private String mKey;
        private Context mContext;
        private int mMode = -1;
        private boolean mUseDefault = false;

        public Builder setPrefsName(final String prefsName) {
            mKey = prefsName;
            return this;
        }

        public Builder setContext(final Context context) {
            mContext = context;
            return this;
        }

        public Builder setMode(final int mode) {
            if (mode == ContextWrapper.MODE_PRIVATE || mode == ContextWrapper.MODE_WORLD_READABLE || mode == ContextWrapper.MODE_WORLD_WRITEABLE || mode == ContextWrapper.MODE_MULTI_PROCESS) {
                mMode = mode;
            } else {
                throw new RuntimeException("The mode in the SharedPreference can only be set too ContextWrapper.MODE_PRIVATE, ContextWrapper.MODE_WORLD_READABLE, ContextWrapper.MODE_WORLD_WRITEABLE or ContextWrapper.MODE_MULTI_PROCESS");
            }

            return this;
        }

        public Builder setUseDefaultSharedPreference(boolean defaultSharedPreference) {
            mUseDefault = defaultSharedPreference;
            return this;
        }

        /**
         * Initialize the SharedPreference instance to used in the application.
         *
         * @throws RuntimeException if Context has not been set.
         */
        public void build() {
            if (mContext == null) {
                throw new RuntimeException("Context not set, please set context before building the Prefs instance.");
            }

            if (TextUtils.isEmpty(mKey)) {
                mKey = mContext.getPackageName();
            }

            if (mUseDefault) {
                mKey += DEFAULT_SUFFIX;
            }

            if (mMode == -1) {
                mMode = ContextWrapper.MODE_PRIVATE;
            }

            Prefs.initPrefs(mContext);
        }
    }
}