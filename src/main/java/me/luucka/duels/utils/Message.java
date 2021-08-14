package me.luucka.duels.utils;

import me.luucka.duels.DuelsPlugin;

public class Message {

    public static String RELOAD = DuelsPlugin.yamlManager.cfg("messages").getString("reload");

    public static String NO_PERM_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("no-perm");

    public static String MUST_BE_PLAYER_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("no-console");

    public static String NO_ARENAS_SETUP_YET_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("no-arena-setup");

    public static String ARENA_NOT_EXISTS_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("arena-not-exists");

    public static String DELETE_ARENA_SUCCESS = DuelsPlugin.yamlManager.cfg("messages").getString("delete-arena-success");

    public static String ALREADY_IN_SETUP_MODE_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("already-in-setup");

    public static String JOIN_SETUP_MODE = DuelsPlugin.yamlManager.cfg("messages").getString("join-setup-mode");

    public static String CANT_JOIN_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("cant-join");

    public static String ALREADY_JOIN_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("already-join");

    public static String NOT_IN_ARENA_ERROR = DuelsPlugin.yamlManager.cfg("messages").getString("not-in-arena");

    public static String LEFT_ARENA = DuelsPlugin.yamlManager.cfg("messages").getString("left-arena");

    public static String CANT_SAVE_ARENA = DuelsPlugin.yamlManager.cfg("messages").getString("cant-save-arena");
}
