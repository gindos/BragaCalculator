package gindos.bragacalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Integer intSugar;
    private Boolean flagDryYeast;
    private Float sizeGidromodule;
    private SharedPreferences settingsApp;
    private Float allSoley;
    private Float tmpFloat;

//    private final static String TAG = settingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // прочитаем настройки
        settingsApp = getSharedPreferences("settingsApp", MODE_PRIVATE);
        flagDryYeast = settingsApp.getBoolean("flagDryYeast", true);
        sizeGidromodule = settingsApp.getFloat("sizeGidromodule", 5);

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

                calcAll();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (settingsApp.contains("flagDryYeast")) {
            flagDryYeast = settingsApp.getBoolean("flagDryYeast", true);
        }
        if (settingsApp.contains("sizeGidromodule")) {
            sizeGidromodule = settingsApp.getFloat("sizeGidromodule", 5);
        }

        final EditText sugar = (EditText)findViewById(R.id.sugar);

        String strSugar = sugar.getText().toString();
        if (strSugar.length() > 0) {
            intSugar = Integer.parseInt(strSugar);
        } else {
            intSugar = 0;
        }

        calcAll();
    }

    // расчёт и вывод на экран всех величин
    public void calcAll() {
        TextView volumeSugar = (TextView)findViewById(R.id.volumeSugar);
        volumeSugar.setText(String.format(Locale.ENGLISH, "%(.2f", calcVolumeSugar()));

        TextView volumeWater = (TextView)findViewById(R.id.volumeWater);
        volumeWater.setText(String.format(Locale.ENGLISH, "%(.2f", calcVolumeWater()));

        TextView allVolume = (TextView)findViewById(R.id.allVolume);
        allVolume.setText(String.format(Locale.ENGLISH, "%(.2f", calcAllVolume()));

        TextView fieldYeast = (TextView)findViewById(R.id.fieldYeast);
        TextView yeast = (TextView)findViewById(R.id.Yeast);
        if (flagDryYeast) {
            // сухие дрожжи
            fieldYeast.setText(R.string.nameDryYeastGr);
            yeast.setText(String.format(Locale.ENGLISH, "%(.2f", calcDryYeast()));
        } else {
            // пресованные дрожжи
            fieldYeast.setText(R.string.nameCompressedYeastGr);
            yeast.setText(String.format(Locale.ENGLISH, "%(.2f", calcCompressedYeast()));
        }

        allSoley = (float)0.00;

        TextView massKarbamid = (TextView)findViewById(R.id.massKarbamid);
        tmpFloat = calcMassKarbamid();
        massKarbamid.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massMagniyaSulphat = (TextView)findViewById(R.id.massMagniyaSulphat);
        tmpFloat = calcMassMagniyaSulphat();
        massMagniyaSulphat.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massNatriyaKhlorid = (TextView)findViewById(R.id.massNatriyaKhlorid);
        tmpFloat = calcMassNatriyaKhlorid();
        massNatriyaKhlorid.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massKaliyaKhlorid = (TextView)findViewById(R.id.massKaliyaKhlorid);
        tmpFloat = calcMassKaliyaKhlorid();
        massKaliyaKhlorid.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massMargantsaSulphat = (TextView)findViewById(R.id.massMargantsaSulphat);
        tmpFloat = calcMassMargantsaSulphat();
        massMargantsaSulphat.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massTsinkaSulphat = (TextView)findViewById(R.id.massTsinkaSulphat);
        tmpFloat = calcMassTsinkaSulphat();
        massTsinkaSulphat.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massKaliyMonophosphat = (TextView)findViewById(R.id.massKaliyMonophosphat);
        tmpFloat = calcMassKaliyMonophosphat();
        massKaliyMonophosphat.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massAktiveUgol = (TextView)findViewById(R.id.massAktiveUgol);
        tmpFloat = calcMassAktiveUgol();
        massAktiveUgol.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massMel = (TextView)findViewById(R.id.massMel);
        tmpFloat = calcMassMell();
        massMel.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView volumeOlivkovoeMaslo = (TextView)findViewById(R.id.volumeOlivkovoeMaslo);
        tmpFloat = calcVolumeOlivkovoeMaslo();
        volumeOlivkovoeMaslo.setText(String.format(Locale.ENGLISH, "%(.2f", tmpFloat));
        allSoley += tmpFloat;

        TextView massPenogasitel = (TextView)findViewById(R.id.massPenogasitel);
        massPenogasitel.setText(String.format(Locale.ENGLISH, "%(.2f", calcMassPenogasitel()));

        TextView massAllSoley = (TextView)findViewById(R.id.massAllSoley);
        massAllSoley.setText(String.format(Locale.ENGLISH, "%(.2f", allSoley));

    }

    // расчёт объёма сахара
    public float calcVolumeSugar() {
        return round((float)(intSugar / 1.587), 2);
    }

    // расчёт объёма воды
    public float calcVolumeWater() {
        return intSugar * sizeGidromodule;
    }

    // расчёт общего объёма браги
    public float calcAllVolume() {
        return calcVolumeSugar() + calcVolumeWater();
    }

    // расчёт сухих дрожжей
    public float calcDryYeast() {
        return intSugar / 40;
    }

    // расчёт пресованных дрожжей
    public float calcCompressedYeast() {
        return intSugar / 10;
    }

    // расёт карбамида
    public float calcMassKarbamid() {
        return round((float)(intSugar / 500), 2);
    }

    // расчёт магния сульфат
    public float calcMassMagniyaSulphat() {
        return round((float)(intSugar / 500), 2);
    }

    // расчёт натрия хлорид
    public float calcMassNatriyaKhlorid() {
        return round((float)(intSugar / 250), 2);
    }

    // расчёт калия хлорид
    public float calcMassKaliyaKhlorid() {
        return round((float)(intSugar / 100), 2);
    }

    // расчёт марганца сульфат
    public float calcMassMargantsaSulphat() {
        return round((float)(intSugar * 0.0006), 2);
    }

    // расчёт цинка сульфат
    public float calcMassTsinkaSulphat() {
        return round((float)(intSugar * 0.0008), 2);
    }

    // расчёт калий монофосфат
    public float calcMassKaliyMonophosphat() {
        return round((float)(intSugar * 0.0045), 2);
    }

    // расчёт активированный уголь
    public float calcMassAktiveUgol() {
        return round((float)(intSugar / 1000), 2);
    }

    // расчёт мел
    public float calcMassMell() {
        return round((float)(intSugar / 1000), 2);
    }

    // расчёт оливковое масло
    public float calcVolumeOlivkovoeMaslo() {
        return (float)0.02;
    }

    // расчёт пеногаситель
    public float calcMassPenogasitel() {
        return round((float)(intSugar / 1600), 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings :
                Intent intentSettings = new Intent(this, settingsActivity.class);
                startActivity(intentSettings);
                return true;
            case R.id.action_about:
                Intent intentAbout = new Intent(this, aboutActivity.class);
                startActivity(intentAbout);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // функция округления до нужно количества знаков после запятой
    private static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
}
