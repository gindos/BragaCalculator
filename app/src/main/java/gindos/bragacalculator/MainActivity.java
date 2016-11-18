package gindos.bragacalculator;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Integer intSugar;
    private Boolean flagDryYeast;
    private Float sizeGidromodule;
    private SharedPreferences settingsApp;
    private Float allSoley;
    private Float tmpFloat;
    private ListView listBraga;
    private ArrayList<String> aBraga;
    private Snackbar mSnackbar;
    private long selectedItem;
    private AlertDialog.Builder dialogDelBraga;
    private classdbReceptsBraga dbReceptsBraga;
    private SQLiteDatabase db;
    private Cursor cursorRecepts;
    private SimpleCursorAdapter adapterBraga;

    private final static String TAG = settingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listBraga = (ListView)findViewById(R.id.listBraga);
        listBraga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        listBraga.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                selectedItem = parent.getItemIdAtPosition(position);

                dialogDelBraga.show();

                return true;
            }
        });

        dialogDelBraga = new AlertDialog.Builder(MainActivity.this);
        dialogDelBraga.setTitle(R.string.titleActionDeleteBraga);
        dialogDelBraga.setMessage(R.string.questionActionDeleteBraga);

        dialogDelBraga.setPositiveButton(R.string.buttonYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (db.delete(dbReceptsBraga.TABLE_NAME_RECEPTS, dbReceptsBraga.COLUMN_ID + " = " + Long.toString(selectedItem), null) > 0 ) {
                    cursorRecepts = db.rawQuery("select "
                            + dbReceptsBraga.COLUMN_ID + ", "
                            + dbReceptsBraga.COLUMN_NAME + ", "
                            + "strftime('%d.%m.%Y %H:%M:%S', "
                            + dbReceptsBraga.COLUMN_DATE_TIME
                            + ", 'unixepoch') as "
                            + dbReceptsBraga.COLUMN_DATE_TIME
                            + " from " + dbReceptsBraga.TABLE_NAME_RECEPTS, null);
                    adapterBraga.changeCursor(cursorRecepts);

                    Toast.makeText(getApplicationContext(),
                            "Рецепт #" + Long.toString(selectedItem) + " удалён.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogDelBraga.setNegativeButton(R.string.buttonNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dbReceptsBraga = new classdbReceptsBraga(getApplicationContext());




/*
        // прочитаем настройки
        settingsApp = getSharedPreferences("settingsApp", MODE_PRIVATE);
        flagDryYeast = settingsApp.getBoolean("flagDryYeast", true);
        sizeGidromodule = settingsApp.getFloat("sizeGidromodule", 5);
*/

/*
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
*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View dialogView = li.inflate(R.layout.dialog_new_recept, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                mDialogBuilder.setView(dialogView);
                final EditText userInput = (EditText) dialogView.findViewById(R.id.nameNewRecept);

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.buttonYes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        long  curTimeMillis = System.currentTimeMillis();
                                        ContentValues cv = new ContentValues();

                                        cv.put(dbReceptsBraga.COLUMN_NAME, userInput.getText().toString());
                                        cv.put(dbReceptsBraga.COLUMN_DATE_TIME, curTimeMillis / 1000);

                                        if (db.insert(dbReceptsBraga.TABLE_NAME_RECEPTS, null, cv) > 0) {
                                            cursorRecepts = db.rawQuery("select "
                                                    + dbReceptsBraga.COLUMN_ID + ", "
                                                    + dbReceptsBraga.COLUMN_NAME + ", "
                                                    + "strftime('%d.%m.%Y %H:%M:%S', "
                                                    + dbReceptsBraga.COLUMN_DATE_TIME
                                                    + ", 'unixepoch') as "
                                                    + dbReceptsBraga.COLUMN_DATE_TIME
                                                    + " from " + dbReceptsBraga.TABLE_NAME_RECEPTS, null);
                                            adapterBraga.changeCursor(cursorRecepts);
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.buttonNo,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        db = dbReceptsBraga.getWritableDatabase();

        cursorRecepts = db.rawQuery("select "
                + dbReceptsBraga.COLUMN_ID + ", "
                + dbReceptsBraga.COLUMN_NAME + ", "
                + "strftime('%d.%m.%Y %H:%M:%S', "
                + dbReceptsBraga.COLUMN_DATE_TIME
                + ", 'unixepoch') as "
                + dbReceptsBraga.COLUMN_DATE_TIME
                + " from " + dbReceptsBraga.TABLE_NAME_RECEPTS, null);

        adapterBraga = new SimpleCursorAdapter(this,
                R.layout.list_recepts,
                cursorRecepts,
                new String[] {dbReceptsBraga.COLUMN_NAME, dbReceptsBraga.COLUMN_DATE_TIME},
                new int[] {R.id.columnName, R.id.columnDateTime},
                0);
/*
        adapterBraga = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                cursorRecepts,
                headers,
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
*/

        listBraga.setAdapter(adapterBraga);

/*
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
*/
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        cursorRecepts.close();
    }
/*
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
*/

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
