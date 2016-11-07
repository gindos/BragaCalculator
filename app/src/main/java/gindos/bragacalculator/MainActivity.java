package gindos.bragacalculator;

import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    protected Integer intSugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText sugar = (EditText)findViewById(R.id.sugar);
        sugar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                String strSugar = sugar.getText().toString();
                if (strSugar.length() > 0) {
                    intSugar = Integer.parseInt(strSugar);
                } else {
                    intSugar = 0;
                }

                TextView volumeSugar = (TextView)findViewById(R.id.volumeSugar);
                volumeSugar.setText(String.format("%d", calcVolumeSugar()));

                TextView volumeWater = (TextView)findViewById(R.id.volumeWater);
                volumeWater.setText(String.format("%d", calcVolumeWater()));

                TextView allVolume = (TextView)findViewById(R.id.allVolume);
                allVolume.setText(String.format("%d", calcAllVolume()));

            }
        });

    }

    // расчёт объёма сахара
    public int calcVolumeSugar() {
        return (int)Math.round(intSugar / 1.587);
    }

    // расчёт объёма воды
    public int calcVolumeWater() {
        return intSugar * 5;
    }

    // расчёт общего объёма браги
    public int calcAllVolume() {
        return calcVolumeSugar() + calcVolumeWater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
