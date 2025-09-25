package com.example.tumatchapp_prueba;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SessionManager {
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_LOGGED = "logged_in";
    private static final String KEY_USER = "logged_user";
    private static final String KEY_USERS_DB = "users_db"; // JSON map email->passwordHash

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED, false);
    }

    public String getLoggedUserEmail() {
        return prefs.getString(KEY_USER, null);
    }

    public void logout() {
        prefs.edit().putBoolean(KEY_LOGGED, false).remove(KEY_USER).apply();
    }

    // Register a new user. Returns true if registration successful, false if user exists or error.
    public boolean register(String email, String password) {
        if (email == null || password == null) return false;
        try {
            JSONObject db = getUsersDb();
            if (db.has(email)) {
                return false; // user already exists
            }
            String hash = hash(password);
            db.put(email, hash);
            saveUsersDb(db);
            // log the user in
            prefs.edit().putBoolean(KEY_LOGGED, true).putString(KEY_USER, email).apply();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login with email and password
    public boolean login(String email, String password) {
        if (email == null || password == null) return false;
        try {
            JSONObject db = getUsersDb();
            if (!db.has(email)) return false;
            String storedHash = db.optString(email, null);
            if (storedHash == null) return false;
            String hash = hash(password);
            if (storedHash.equals(hash)) {
                prefs.edit().putBoolean(KEY_LOGGED, true).putString(KEY_USER, email).apply();
                return true;
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private JSONObject getUsersDb() throws JSONException {
        String s = prefs.getString(KEY_USERS_DB, null);
        if (s == null) return new JSONObject();
        return new JSONObject(s);
    }

    private void saveUsersDb(JSONObject db) {
        prefs.edit().putString(KEY_USERS_DB, db.toString()).apply();
    }

    // Simple SHA-256 hashing for local storage. Not a replacement for proper auth.
    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return input; // fallback (shouldn't happen)
        }
    }
}
