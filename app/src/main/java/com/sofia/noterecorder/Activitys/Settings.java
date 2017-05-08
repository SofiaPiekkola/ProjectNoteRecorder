package com.sofia.noterecorder.Activitys;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sofia.noterecorder.R;

public class Settings extends BaseActivity {
    public static int language = 1;
    public static int recordType = 1;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        getValues();
    }

    void getValues(){
        RadioButton rb = (RadioButton)findViewById(R.id.langEnglish);
        rb.setChecked(sharedPref.getBoolean("langEnglish", true));
        rb = (RadioButton)findViewById(R.id.langFinnish);
        rb.setChecked(sharedPref.getBoolean("langFinnish", false));

        rb = (RadioButton)findViewById(R.id.typeTHREE_GPP);
        rb.setChecked(sharedPref.getBoolean("typeTHREE_GPP", true));
        rb = (RadioButton)findViewById(R.id.typeMPEG_4);
        rb.setChecked(sharedPref.getBoolean("typeMPEG_4", false));
    }

    public void saveSettings(View view) {
        RadioGroup langGroup = (RadioGroup) findViewById(R.id.radioLanguage);
        int selectedLang = langGroup.getCheckedRadioButtonId();

        RadioGroup typeGroup = (RadioGroup) findViewById(R.id.radioType);
        int selectedType = typeGroup.getCheckedRadioButtonId();

        if (selectedLang == R.id.langEnglish)language = 1;
        else language = 2;

        if (selectedType == R.id.typeTHREE_GPP) recordType = 1;
        else recordType = 2;

        saveSelected();
        finish();
    }

    void saveSelected(){
        editor = sharedPref.edit();
        RadioButton rb = (RadioButton)findViewById(R.id.langEnglish);
        editor.putBoolean("langEnglish", rb.isChecked());
        rb = (RadioButton)findViewById(R.id.langFinnish);
        editor.putBoolean("langFinnish", rb.isChecked());

        rb = (RadioButton)findViewById(R.id.typeTHREE_GPP);
        editor.putBoolean("typeTHREE_GPP", rb.isChecked());
        rb = (RadioButton)findViewById(R.id.typeMPEG_4);
        editor.putBoolean("typeMPEG_4", rb.isChecked());

        editor.apply();
    }
}
