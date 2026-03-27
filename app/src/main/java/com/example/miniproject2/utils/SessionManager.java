package com.example.miniproject2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sp = context.getSharedPreferences("SHOPPING_SESSION", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveLoginSession(int userId) {
        editor.putBoolean("isLoggedIn", true);
        editor.putInt("userId", userId);
        editor.apply();
    }

    public boolean checkIsLoggedIn() {
        return sp.getBoolean("isLoggedIn", false);
    }

    public int getUserId() {
        return sp.getInt("userId", -1);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}