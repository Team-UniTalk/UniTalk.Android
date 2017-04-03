package android.team.unitalk.unitalk.settings;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.team.unitalk.unitalk.R;

import static android.team.unitalk.unitalk.Constants.serverAddress;


/**
 * Created by Muehlenstaedt on 22.12.16.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);


    }

    @Override
    public void onStop() {

        EditTextPreference editTextPreference = (EditTextPreference)
                getPreferenceManager().findPreference("serverAddress");
        serverAddress = editTextPreference.getText();

        getPreferenceManager().findPreference("notification_service");
        SwitchPreference switchPreference = (SwitchPreference)
                getPreferenceManager().findPreference("notification_service");
        /*if (switchPreference.isChecked()) {
            Context context = getActivity().getBaseContext();
            context.startService(new Intent(context, NotificationService.class));
        } else {
            Context context = getActivity().getBaseContext();
            context.stopService(new Intent(context, NotificationService.class));
        }*/

        super.onStop();
    }
}
