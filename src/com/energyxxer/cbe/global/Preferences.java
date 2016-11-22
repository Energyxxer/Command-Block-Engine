package com.energyxxer.cbe.global;

import com.energyxxer.cbe.main.WorkspaceSelector;

import java.io.File;
import java.util.prefs.BackingStoreException;

public class Preferences {

    public static final String DEFAULT_WORKSPACE_PATH = System.getProperty("user.home") + File.separator + "Command Block Engine" + File.separator + "workspace";
    private static java.util.prefs.Preferences prefs = java.util.prefs.Preferences
            .userNodeForPackage(Preferences.class);

    static {
        if (prefs.get("workspace_dir", null) == null) {
            prefs.put("workspace_dir", WorkspaceSelector.prompt());
        }
    }

    public static void reset() {
        try {
            prefs.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key, String def) {
        return prefs.get(key, def);
    }

    public static String get(String key) {
        return prefs.get(key, null);
    }

    public static void put(String key, String value) {
        prefs.put(key, value);
    }

    public static void remove(String key) {
        prefs.remove(key);
    }
}