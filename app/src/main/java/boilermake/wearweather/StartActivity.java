package boilermake.wearweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class StartActivity extends AppCompatActivity {

    String apiText;
    String result;


    static TextView weathertypeTextView;
    static TextView hightemperatureTextView;
    static TextView lowtemperatureTextView;
    static TextView windTextView;
    static TextView humidityTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);

        weathertypeTextView = (TextView) findViewById(R.id.textView1);
        hightemperatureTextView = (TextView) findViewById(R.id.textView2);
        lowtemperatureTextView = (TextView) findViewById(R.id.textView3);
        windTextView = (TextView) findViewById(R.id.textView4);
        humidityTextView = (TextView) findViewById(R.id.humidityTextView);


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
        double lon = -86.;
        WeatherAPI task = new WeatherAPI();
        String urlStr = "http://api.openweathermap.org/data/2.5/forecast?lat=" + String.valueOf(lat) + "&lon=" + String.valueOf(lon) + "&appid=c5e1f2658752e350a9e5702e334e689d";
        task.execute(urlStr);


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
                JSONArray list = jsonObject.getJSONArray("list");
                ArrayList<String> weatherType = new ArrayList<String>();
                ArrayList<String> high = new ArrayList<String>();
                ArrayList<String> low = new ArrayList<String>();
                ArrayList<String> wind = new ArrayList<String>();
                ArrayList<String> humidity = new ArrayList<String>();
                int count = 0;
                int i = 0;

                while (count < 27) {

                    JSONObject x = list.getJSONObject(i);
                    String dt_txt = x.getString("dt_txt");
                    JSONObject main = x.getJSONObject("main");
                    double temp = (main.getDouble("temp_max"));
                    apiText = Double.toString(temp);
                    high.add(apiText);
                    temp = (main.getDouble("temp_min"));
                    apiText = Double.toString(temp);
                    low.add(apiText);
                    temp = (main.getDouble("humidity"));
                    apiText = Double.toString(temp);
                    humidity.add(apiText);
                    count += 3;
                    i++;


                }


                count = 0;
                i = 0;


                while (count <27) {

                    JSONObject x = list.getJSONObject(i);
                    JSONObject main = x.getJSONObject("wind");
                    double wind1 = (main.getDouble("speed"));
                    apiText = Double.toString(wind1);
                    wind.add(apiText);
                    count = count + 3;
                    i++;

                }


                count = 0;
                i = 0;
                while (count < 27) {

                    JSONObject x = list.getJSONObject(i);
                    JSONArray windList = x.getJSONArray("weather");
                    JSONObject y = windList.getJSONObject(0);
                    String dt_txt = x.getString("dt_txt");
                    String description = y.getString("description");
                    apiText = description;
                    weatherType.add(apiText);
                    count = count + 3;
                    i++;
                }


                String temphigh = high.get(0) + "\t" + high.get(1) + "\t" +
                        high.get(2) + "\t" + high.get(3) + "\t" + high.get(4) + "\t" + high.get(5) + "\t" + high.get(6) + "\t" +
                        high.get(7) + "\t" +
                        high.get(8);
                String templow = low.get(0) + "\t" + low.get(1) + "\t" +
                        low.get(2) + "\t" + low.get(3) + "\t" + low.get(4) + "\t" + low.get(5) + "\t" + low.get(6) + "\t" +
                        low.get(7) + "\t" +
                        low.get(8);
                String humidityString = humidity.get(0) + "\t" + humidity.get(1) + "\t" +
                        humidity.get(2) + "\t" + humidity.get(3) + "\t" + humidity.get(4) + "\t" + humidity.get(5) + "\t" + humidity.get(6) + "\t" +
                        humidity.get(7) + "\t" +
                        humidity.get(8);
                String windString = wind.get(0) + "\t" + wind.get(1) + "\t" +
                        wind.get(2) + "\t" + wind.get(3) + "\t" + wind.get(4) + "\t" + wind.get(5) + "\t" + wind.get(6) + "\t" +
                        wind.get(7) + "\t" +
                        wind.get(8);
                String descriptionString = weatherType.get(0) + "\t" + weatherType.get(1) + "\t" +
                        weatherType.get(2) + "\t" + weatherType.get(3) + "\t" + weatherType.get(4) + "\t" + weatherType.get(5) + "\t" + weatherType.get(6) + "\t" +
                        weatherType.get(7) + "\t" +
                        weatherType.get(8);


                hightemperatureTextView.setText(temphigh);
                lowtemperatureTextView.setText(templow);
                humidityTextView.setText(humidityString);
                weathertypeTextView.setText(descriptionString);
                windTextView.setText(windString);


            } catch (Exception e) {
                Log.e("", e.toString());
            }

            super.onPostExecute(result);
        }
    }

}
