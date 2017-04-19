package com.example.qrcodedetection;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class History extends ListActivity {

    public String LOG_TAG = "LogsDB";
    DBHelper dbHelper;
    SQLiteDatabase database;
    public List<String> values = new ArrayList<String>();
   // private static final int NAME_LENGTH = 20;
    public void onCreate(Bundle icicle) {

        dbHelper = new DBHelper(this);

        SQLiteDatabase database = dbHelper.getWritableDatabase(); //коннектор

        Log.d(LOG_TAG, "Выборка из таблицы строк");
        Cursor c = database.query(DBHelper.TABLE_HISTORY, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("_id");
            int textColIndex = c.getColumnIndex("text");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) + ", text = "
                                + c.getString(textColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false -
                // выходим из цикла
                values.add(c.getString(textColIndex));
            } while (c.moveToNext());

        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        super.onCreate(icicle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        dbHelper.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " выбран", Toast.LENGTH_LONG).show();
    }
}

