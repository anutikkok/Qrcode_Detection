package com.example.qrcodedetection;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class MainActivity extends AppCompatActivity {

    Button history;

    Button history_del;

    DBHelper dbHelper;

    public String LOG_TAG = "myLogs";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView txtView = (TextView) findViewById(R.id.txtContent);

        Button button = (Button) findViewById(R.id.create);

        history = (Button) findViewById(R.id.history);

        history_del = (Button) findViewById(R.id.history_del);

        dbHelper = new DBHelper(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                history.setOnClickListener(this);

                history_del.setOnClickListener(this);

                String name = txtView.getText().toString();

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();

                Cursor cursor = database.query(DBHelper.TABLE_HISTORY, null, null, null, null, null, null);

                switch (view.getId()) {

                    case R.id.create:
                        contentValues.put(DBHelper.KEY_TEXT, name);

                        database.insert(DBHelper.TABLE_HISTORY, null, contentValues);

                        if (cursor.moveToFirst()) {
                            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                            int textIndex = cursor.getColumnIndex(DBHelper.KEY_TEXT);
                            do {
                                Log.d("mLog", "ID - " + cursor.getInt(idIndex) +
                                        ", text - " + cursor.getString(textIndex));
                            } while (cursor.moveToNext());
                        } else
                            Log.d("mLog", "0 rows");

                        cursor.close();
                        break;
                    case R.id.history_del:
                    database.delete(DBHelper.TABLE_HISTORY, null, null);
                    break;
                    case R.id.history:
                        //Cursor cursor = database.query(DBHelper.TABLE_HISTORY, null, null, null, null, null, null);

                        startActivity(new Intent(MainActivity.this, History.class));
                        break;


                }
                dbHelper.close();

                ImageView myImageView = (ImageView) findViewById(R.id.imgview);
                Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.qrcode1);
                //BitmapFactory - для декодирования ресурса R.drawable.qr в растровое изображение;
                myImageView.setImageBitmap(myBitmap); //Полученное растровое изображение передаем в ImageView

                //Создаем экземпляр класса BarcodeDetector, используя Builder, и настраиваем его на поиск QR-кодов;
                BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();
                if (!barcodeDetector.isOperational()) {
                    txtView.setText("Не удалось настроить детектор!");
                }

                //Теперь, когда наш детектор создан и мы знаем, что он работает,
                // создаем кадр из растрового изображения и передаем его детектору.
                // Тот возвращает нам массив штрих-кодов SparseArray
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);

                //Обычно, на этом этапе нужно пробежать по массиву SparseArray
                // и обработать каждый штрих-код отдельно. Нужно предусмотреть
                // возможность, что штрих-кодов может быть несколько, или ни одного.
                // В нашем случае мы знаем, что у нас есть только 1 штрих-код,
                // и можем прописать жесткий код для него. Для этого мы берем штрих-код,
                // называемый «thisCode», который будет первым элементом в массиве.
                // Затем присваиваем значение его поля rawValue текстовому полю textView — и все.
                Barcode thisCode = barcodes.valueAt(0);
                txtView.setText(thisCode.rawValue);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
