package android.team.unitalk.unitalk.settings;

import android.os.Bundle;
import android.team.unitalk.unitalk.R;


/**
 * Created by Muehlenstaedt on 22.12.16.
 */

public class SettingsActivity extends PreferencesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.preferences);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
