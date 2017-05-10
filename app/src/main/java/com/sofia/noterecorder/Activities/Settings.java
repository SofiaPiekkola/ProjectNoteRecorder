package com.sofia.noterecorder.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sofia.noterecorder.R;

import java.util.Locale;

public class Settings extends BaseActivity {
    private static int language = 1;
    public static int recordType = 1;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        getValues();
    }

    @SuppressWarnings("UnusedParameters")
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

    @SuppressWarnings("deprecation")
    private void getValues(){
        Locale current;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            current = getResources().getConfiguration().getLocales().get(0);
        else current = getResources().getConfiguration().locale;
        boolean fi = current.toString().contains("FI");
        if (fi) language = 2;

        RadioButton rb = (RadioButton)findViewById(R.id.langEnglish);
        rb.setChecked(sharedPref.getBoolean("langEnglish", !fi));
        rb = (RadioButton)findViewById(R.id.langFinnish);
        rb.setChecked(sharedPref.getBoolean("langFinnish", fi));

        rb = (RadioButton)findViewById(R.id.typeTHREE_GPP);
        rb.setChecked(sharedPref.getBoolean("typeTHREE_GPP", true));
        rb = (RadioButton)findViewById(R.id.typeMPEG_4);
        rb.setChecked(sharedPref.getBoolean("typeMPEG_4", false));

        String lang;
        if (language == 1) lang = "en";
        else lang = "fi";
        setLocale(lang);
    }

    private void saveSelected(){
        SharedPreferences.Editor editor = sharedPref.edit();
        RadioButton rb = (RadioButton)findViewById(R.id.langEnglish);
        editor.putBoolean("langEnglish", rb.isChecked());
        rb = (RadioButton)findViewById(R.id.langFinnish);
        editor.putBoolean("langFinnish", rb.isChecked());

        rb = (RadioButton)findViewById(R.id.typeTHREE_GPP);
        editor.putBoolean("typeTHREE_GPP", rb.isChecked());
        rb = (RadioButton)findViewById(R.id.typeMPEG_4);
        editor.putBoolean("typeMPEG_4", rb.isChecked());

        editor.apply();

        String lang;
        if (language == 1) lang = "en";
        else lang = "fi";
        setLocale(lang);
    }

    @SuppressWarnings("deprecation")
    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            createConfigurationContext(conf);
        else res.updateConfiguration(conf, dm);
    }
}
