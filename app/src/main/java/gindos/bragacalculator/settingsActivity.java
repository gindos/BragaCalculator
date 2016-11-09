package gindos.bragacalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class settingsActivity extends AppCompatActivity {
    private Boolean flagDryYeast;
    private Float sizeGidromodule;
    private RadioButton rbDryYeast;
    private RadioButton rbComressedYeast;
    private EditText etSizeGidromodule;

    private SharedPreferences settingsApp;

//    private final static String TAG = settingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingsApp = getSharedPreferences("settingsApp", MODE_PRIVATE);
        flagDryYeast = settingsApp.getBoolean("flagDryYeast", true);
        sizeGidromodule = settingsApp.getFloat("sizeGidromodule", 5);

        etSizeGidromodule = (EditText)this.findViewById(R.id.etSizeGidromodule);

        etSizeGidromodule.setText(String.format(Locale.ENGLISH, "%(.2f", sizeGidromodule));

        rbDryYeast = (RadioButton)this.findViewById(R.id.rbDryYeast);
        rbComressedYeast = (RadioButton)this.findViewById(R.id.rbCompressedYeast);

        if (flagDryYeast) {
            rbDryYeast.setChecked(true);
        } else {
            rbComressedYeast.setChecked(true);
        }

        RadioGroup rgYeast = (RadioGroup)findViewById(R.id.rgYeast);

        int checkedRBId = rgYeast.getCheckedRadioButtonId();
        rgYeast.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbDryYeast:
                        rbDryYeast.setChecked(true);
                        rbComressedYeast.setChecked(false);
                        flagDryYeast = true;
                        break;
                    case R.id.rbCompressedYeast:
                        rbDryYeast.setChecked(false);
                        rbComressedYeast.setChecked(true);
                        flagDryYeast = false;
                        break;
                    default:
                        break;
                }
            }
        });

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

*/
    }

    public void onClickBtSaveSettings(View view) {
        // сохраним настройки приложения

        settingsApp = getSharedPreferences("settingsApp", MODE_PRIVATE);

        SharedPreferences.Editor settingsAppEditor = settingsApp.edit();
        settingsAppEditor.putBoolean("flagDryYeast", flagDryYeast);

        etSizeGidromodule = (EditText)this.findViewById(R.id.etSizeGidromodule);
        settingsAppEditor.putFloat("sizeGidromodule", Float.parseFloat(etSizeGidromodule.getText().toString()));

        settingsAppEditor.apply();

        Toast toast = Toast.makeText(this, R.string.messageSettingsSaved, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
