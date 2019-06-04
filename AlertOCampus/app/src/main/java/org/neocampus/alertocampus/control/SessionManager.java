package org.neocampus.alertocampus.control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.neocampus.alertocampus.activities.MainActivity;


/**
 * Created by pc2 on 19/04/2017.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Sharedpref file name
    private static final String PREF_NAME = "Status";

    // All Shared Preferences Keys
    private static final String KEEP_LOGIN = "false";

    // User name (make variable public to access from outside)
    public static final String KEY_LOGIN = "login";

    public static final String KEY_PASS = "pass";

    public static final String KEY_MAIL = "mail";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createKeepLoginSession(String login, String pass, String mail) {
        // Storing login value as TRUE
        editor.putBoolean(KEEP_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_LOGIN, login);

        editor.putString(KEY_PASS, pass);
        editor.putString(KEY_MAIL, mail);


        // commit changes
        editor.commit();
    }

    public void createNoKeepLoginSession(String login, String pass, String mail) {
        // Storing login value as TRUE
        editor.putBoolean(KEEP_LOGIN, false);

        // Storing name in pref
        editor.putString(KEY_LOGIN, login);

        editor.putString(KEY_PASS, pass);
        editor.putString(KEY_MAIL, mail);


        // commit changes
        editor.commit();
    }

    public void setKeyMail(String mail) {
        editor.remove(KEY_MAIL);
        editor.putString(KEY_MAIL, mail);
        editor.apply();
    }

    public void logout() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        // Staring Login Activity
        _context.startActivity(i);
        Toast.makeText(_context, "Vous êtes deconnecté !", Toast.LENGTH_LONG).show();
    }


    public String getLogin() {
        return pref.getString(KEY_LOGIN, null);
    }

    public String getKeyPass() {
        return pref.getString(KEY_PASS, null);
    }

    public String getKeyMail() {
        return pref.getString(KEY_MAIL, null);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(KEEP_LOGIN, false);
    }
}


