package pl.marek.knx.preferences;

import java.io.File;

import pl.marek.knx.MainApplication;
import pl.marek.knx.R;
import pl.marek.knx.utils.FileUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class ResetSettingsFragment extends SettingsFragment implements OnPreferenceClickListener{
	
	private Preference resetPreferences;
	private Preference resetApplication;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.reset_prefs);
		loadPreferencesObjects();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		addActionBar();
		addPreferencesListeners();
	}
	
	private void loadPreferencesObjects(){
		resetPreferences = getPreferenceScreen().findPreference(getString(R.string.reset_settings_key));
		resetApplication = getPreferenceScreen().findPreference(getString(R.string.reset_application_key));
	}
	
	private void addPreferencesListeners(){
		resetPreferences.setOnPreferenceClickListener(this);
		resetApplication.setOnPreferenceClickListener(this);
	}

	public boolean onPreferenceClick(Preference preference) {
		if(preference.equals(resetPreferences)){
			showResetSettingsConfirmationDialog();
		}else if(preference.equals(resetApplication)){
			showResetApplicationConfirmationDialog();
		}
		return false;
	}
	
	private void resetSettings(){
		SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
		preferences.edit().clear().commit();
	}
	
	private void resetApplication(){
		File cache = getActivity().getCacheDir().getAbsoluteFile();
        File appDir = new File(cache.getParentFile().getAbsolutePath());
        if (appDir.exists()) {
            File[] childrens = appDir.listFiles();
            for (File children : childrens) {
                if (!children.getName().equals("lib")) {
                    FileUtils.delete(children.getAbsolutePath());
                }
            }
        }
        
       MainApplication app = (MainApplication)getActivity().getApplication();
       app.restartApplication();
        
	}
	
	private void showResetSettingsConfirmationDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.reset_settings_confirmation_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.reset_settings_confirmation_msg));
		builder.setPositiveButton(getString(android.R.string.yes), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				resetSettings();
			}
		});
		builder.setNegativeButton(getString(android.R.string.no), new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.create().show();
	}
	
	private void showResetApplicationConfirmationDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.reset_application_confirmation_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.reset_application_confirmation_msg));
		builder.setPositiveButton(getString(android.R.string.yes), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				resetApplication();
			}
		});
		builder.setNegativeButton(getString(android.R.string.no), new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.create().show();
	}

}
