package ru.mail.park.bytcointy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_currency")) {
            setup();
        }
    }

    public void openSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void setup() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Float rate = sharedPref.getFloat("rate", 0f);

        setRate(rate);
    }

    private void setRate(Float rate) {
        SharedPreferences settingsPref = PreferenceManager.getDefaultSharedPreferences(this);
        String coin = settingsPref.getString("pref_currency", "$");

        EditText rateEditText = (EditText) findViewById(R.id.rateEditText);
        rateEditText.setText(rate.toString() + ' ' + coin);
    }

    public void update(View view) {
        new JTask().execute();
    }


    private class JTask extends AsyncTask<Void, Void, Double>{
        HttpURLConnection connection;
        BufferedReader reader;
        String jsonString;
        JSONObject objectResult;

        @Override
        protected Double doInBackground(Void... params) {
            //получаем данные с внешнего ресурса
            try {
                //строка соединения
                URL url = new URL("https://community-bitcointy.p.mashape.com/convert/1/RUS");
                //создаём и открываем соединение
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("X-Mashape-Key", "p1sAcyghgymshJ5bc2xpx7efkrBLp1dVqFbjsnxJzMwK9ABtlu");
                //отправляем запрос GET
                connection.setRequestMethod("GET");
                //открываем входной поток данных соединения
                InputStream inputStream = connection.getInputStream();
                //читатель потока
                reader = new BufferedReader(new InputStreamReader(inputStream));
                //буфер, в который будет проивзодится чтение и вспомогательная строка
                StringBuilder buffer = new StringBuilder();
                String line;
                //пока не конец json array в буфер читается строка, когда весь
                //JSON array считан, начинается парсинг
                //каждый распарсенный JSON object будет передаваться в publishProgress,
                //чтобы минимизировать задержку вывода данных
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                jsonString = buffer.toString();
                objectResult = new JSONObject(jsonString);
                return objectResult.getDouble("value");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cancel(true);
            return null;
        }//doInBackground

        @Override
        protected void onPostExecute(Double result) {
            super.onPostExecute(result);
            EditText rateEditText = (EditText) findViewById(R.id.rateEditText);
            rateEditText.setText(result.toString());
        }
    }//jtask
}
