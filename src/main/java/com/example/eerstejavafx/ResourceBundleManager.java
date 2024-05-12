package com.example.eerstejavafx;
//package com.strings.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleManager {
    private static final String BASE_NAME = "vertalingen";
    private static ResourceBundle bundle;

    static {
        // Stel de standaardtaal in (bijvoorbeeld Nederlands)
        Locale.setDefault(new Locale("nl"));
        // Laad de ResourceBundle
        bundle = ResourceBundle.getBundle(BASE_NAME, Locale.getDefault());
    }

    public static String getString(String key) {
        // Haal de vertaalde string op
        return bundle.getString(key);
    }

    public static void setLocale(Locale locale) {
        // Update de locale
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle(BASE_NAME, locale);
    }
}
