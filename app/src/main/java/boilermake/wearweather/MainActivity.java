package boilermake.wearweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.*;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    String apiText;
    String result;

    static TextView currentDay;
    static TextView hiTempTextView;
    static TextView lowTempTextView;
    static TextView humidityTextView;
    static TextView windTextView;
    static ImageView img;
    static TextView realTempTextView;
    static TextView descriptionStringTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // /img.setImageResource(R.drawable.sun);
        hiTempTextView = (TextView) findViewById(R.id.hiTemp);
        lowTempTextView = (TextView) findViewById(R.id.loTemp);
        currentDay = (TextView) findViewById(R.id.currentDay);
        humidityTextView = (TextView) findViewById(R.id.humidityValueTextView);
        windTextView = (TextView) findViewById(R.id.windValueTextView);
        img = (ImageView)findViewById(R.id.weather);
        realTempTextView = (TextView)findViewById(R.id.realTempTextView);
        descriptionStringTextView = (TextView) findViewById(R.id.descriptionStringTextView);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("DEBUG", "Returning - permission");
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

//       double lat = location.getLatitude();
        double lat = 40;


//        double lon = location.getLongitude();
        double lon = -86;

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date currTime = Calendar.getInstance().getTime();

        String currentTime = df.format(currTime);

        int time = Integer.valueOf(currentTime.substring(11, 13));
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                currentDay.setText("Sunday");

            case Calendar.MONDAY:
                currentDay.setText("Monday");

            case Calendar.TUESDAY:
                currentDay.setText("Tuesday");

            case Calendar.THURSDAY:
                currentDay.setText("Thursday");

            case Calendar.WEDNESDAY:
                currentDay.setText("Wednesday");

            case Calendar.FRIDAY:
                currentDay.setText("Friday");

            case Calendar.SATURDAY:
                currentDay.setText("Saturday");

        }


        WeatherAPI task = new WeatherAPI();
        String urlStr = "http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(lat) + "&lon=" + String.valueOf(lon) + "&appid=c5e1f2658752e350a9e5702e334e689d";
        task.execute(urlStr);


    }


    public void buttonOnClick(View v) {
        Button button = (Button) v;
        ((Button) v).setText("clicked");
    }


    public class WeatherAPI extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                final int bufferSize = 1024;
                final char[] buffer = new char[bufferSize];
                final StringBuilder out = new StringBuilder();
                Reader in2 = new InputStreamReader(in, "UTF-8");
                for (; ; ) {
                    int rsz = in2.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
                result = out.toString();

                return result;


            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {

            try {

                JSONObject jsonObject = new JSONObject(result);

                JSONObject weatherDatas = new JSONObject(jsonObject.getString("main"));

                double tempIntMax = Double.parseDouble(weatherDatas.getString("temp_max"));
                int tempMax = (int) ((tempIntMax - 273) * 1.8 + 32);
                double tempIntMin = Double.parseDouble(weatherDatas.getString("temp_min"));
                int tempMin = (int) ((tempIntMin - 273) * 1.8 + 32);
                double tempInt = Double.parseDouble(weatherDatas.getString("temp"));
                int temp = (int) ((tempInt - 273) * 1.8 + 32);
                JSONObject wind = new JSONObject(jsonObject.getString("wind"));
                double windValue = Double.parseDouble((wind.getString("speed")));

                double humidityValue = Double.parseDouble(weatherDatas.getString("humidity"));


                JSONArray weatherData = jsonObject.getJSONArray("weather");
                JSONObject y = weatherData.getJSONObject(0);
                String description = y.getString("description");
                if (description.equals("clear sky")) {
                    img.setImageResource(R.drawable.clearsky);
                }
                else if (description.equals("few clouds")) {
                    img.setImageResource(R.drawable.fewclouds);
                }
                else if (description.equals("scattered clouds") || description.equals("broken clouds") ) {
                    img.setImageResource(R.drawable.scatteredclouds);
                }
                else if (description.equals("shower rain")) {
                    img.setImageResource(R.drawable.showerrain);
                }
                else if (description.equals("rain")) {
                    img.setImageResource(R.drawable.rain);
                }
                else if (description.equals("thunderstorm")) {
                    img.setImageResource(R.drawable.thunderstorm);
                }
                else if (description.equals("snow")) {
                    img.setImageResource(R.drawable.snow);
                }
                else if (description.equals("mist") || description.equals("haze")) {
                    img.setImageResource(R.drawable.mist);
                }

                


                String placeName = jsonObject.getString("name");
                realTempTextView.setText(String.valueOf(temp));
                descriptionStringTextView.setText(String.valueOf(description).toUpperCase());

                hiTempTextView.setText(String.valueOf(tempMax));
                lowTempTextView.setText(String.valueOf(tempMin));
                windTextView.setText(String.valueOf(windValue));
                humidityTextView.setText(String.valueOf(humidityValue));


            } catch (Exception e) {
                Log.e("", e.toString());
            }

            super.onPostExecute(result);
        }
    }

    public void extendSettings(View view) {
        Intent intent = new Intent(this, SettingsMenu.class);
        startActivity(intent);
    }

    public void extendWeather(View view) {
        Intent intent = new Intent(this, ExtendedWeather.class);
        startActivity(intent);
    }

    public void extendClothing(View view) {
        Intent intent = new Intent(this, ClothingSuggestions.class);
        startActivity(intent);
    }


}
