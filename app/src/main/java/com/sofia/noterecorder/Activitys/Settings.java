package com.sofia.noterecorder.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.sofia.noterecorder.R;

public class Settings extends BaseActivity {
    static int appTheme = 1;
    static int language = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void saveSettings(View view) {
        RadioGroup themeGroup = (RadioGroup) findViewById(R.id.radioTheme);
        int selectedTheme = themeGroup.getCheckedRadioButtonId();
        RadioGroup langGroup = (RadioGroup) findViewById(R.id.radioLanguage);
        int selectedLang = langGroup.getCheckedRadioButtonId();

        if(selectedTheme == R.id.themeDark) appTheme = 1;
        else if(selectedTheme == R.id.themeColor) appTheme = 2;
        else appTheme = 3;

        if (selectedLang == R.id.langEnglish)language = 1;
        else language = 2;

        finish();
    }
}
