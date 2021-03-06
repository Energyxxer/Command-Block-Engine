package com.energyxxer.craftr.global;

import com.energyxxer.craftr.main.WorkspaceDialog;

import java.io.File;
import java.util.prefs.BackingStoreException;

public class Preferences {

    public static final String DEFAULT_WORKSPACE_PATH = System.getProperty("user.home") + File.separator + "Craftr" + File.separator + "workspace";
    private static java.util.prefs.Preferences prefs = java.util.prefs.Preferences
            .userNodeForPackage(Preferences.class);

    static {
        if(prefs.get("theme",null) == null) prefs.put("theme", "Electron Dark");
        if(prefs.get("workspace_dir", null) == null) {
            //promptWorkspace();
        }
        if(prefs.get("username",null) == null) prefs.put("username", "User");
        if(prefs.get("nativelib",null) == null) prefs.put("nativelib",System.getProperty("user.home") + File.separator + "Craftr" + File.separator + "natives" + File.separator + "src" + File.separator);
    }

    public static void promptWorkspace() {
        WorkspaceDialog.prompt();
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
