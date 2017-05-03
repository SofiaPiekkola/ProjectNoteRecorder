package com.sofia.noterecorder.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.sofia.noterecorder.R;

public class Settings extends BaseActivity {
    static int language = 1;
    public static int recordType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

        finish();
    }
}
