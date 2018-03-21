package ru.mail.park.bytcointy;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.EditText;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

    }
}