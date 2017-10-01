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


public class ExtendedWeather extends AppCompatActivity {

    String apiTextF, apiTextC;
    String result;


    static TextView firstTemp;
    static TextView secondTemp;
    static TextView thirdTemp;
    static TextView fourthTemp;
    static TextView fifthTemp;
    static TextView sixthTemp;
    static TextView seventhTemp;
    static TextView eightTemp;

    static TextView time1;
    static TextView time2;
    static TextView time3;
    static TextView time4;
    static TextView time5;
    static TextView time6;
    static TextView time7;
    static TextView time8;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_weather);


        firstTemp = (TextView) findViewById(R.id.temp1);
        secondTemp = (TextView) findViewById(R.id.temp2);
        thirdTemp = (TextView) findViewById(R.id.temp3);
        fourthTemp = (TextView) findViewById(R.id.temp4);
        fifthTemp = (TextView) findViewById(R.id.temp5);
        sixthTemp = (TextView) findViewById(R.id.temp6);
        seventhTemp = (TextView) findViewById(R.id.temp7);
        eightTemp = (TextView) findViewById(R.id.temp8);

        time1 = (TextView) findViewById(R.id.time1);
        time2 = (TextView) findViewById(R.id.time2);
        time3 = (TextView) findViewById(R.id.time3);
        time4 = (TextView) findViewById(R.id.time4);
        time5 = (TextView) findViewById(R.id.time5);
        time6 = (TextView) findViewById(R.id.time6);
        time7 = (TextView) findViewById(R.id.time7);
        time8 = (TextView) findViewById(R.id.time8);



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
                ArrayList<String> temperatureF = new ArrayList<String>();
                ArrayList<String> temperatureC = new ArrayList<String>();
                ArrayList<String> time = new ArrayList<String >();
                int count = 3;
                int i = 0;

                while (count < 27) {

                    JSONObject x = list.getJSONObject(i);
                    String dt_txt = x.getString("dt_txt");
                    time.add(dt_txt);
                    JSONObject main = x.getJSONObject("main");
                    int tempF = (int)Math.round((main.getDouble("temp")-273.15)*1.8 +32);
                    int tempC =(int)Math.round((main.getDouble("temp")-273.15));
                    apiTextF = Integer.toString(tempF);
                    apiTextC = Integer.toString(tempC);
                    temperatureF.add(apiTextF);
                    temperatureC.add(apiTextC);


                    count += 3;
                    i++;


                }

                if (MainActivity.currTempUnit.equals("°F")) {
                    firstTemp.setText(temperatureF.get(0) + MainActivity.currTempUnit);
                    secondTemp.setText(temperatureF.get(1) + MainActivity.currTempUnit);
                    thirdTemp.setText(temperatureF.get(2) + MainActivity.currTempUnit);
                    fourthTemp.setText(temperatureF.get(3) + MainActivity.currTempUnit);
                    fifthTemp.setText(temperatureF.get(4) + MainActivity.currTempUnit);
                    sixthTemp.setText(temperatureF.get(5) + MainActivity.currTempUnit);
                    seventhTemp.setText(temperatureF.get(6) + MainActivity.currTempUnit);
                    eightTemp.setText(temperatureF.get(7) + MainActivity.currTempUnit);
                }

                if (MainActivity.currTempUnit.equals("°C")) {
                    firstTemp.setText(temperatureC.get(0) + MainActivity.currTempUnit);
                    secondTemp.setText(temperatureC.get(1) + MainActivity.currTempUnit);
                    thirdTemp.setText(temperatureC.get(2) + MainActivity.currTempUnit);
                    fourthTemp.setText(temperatureC.get(3) + MainActivity.currTempUnit);
                    fifthTemp.setText(temperatureC.get(4) + MainActivity.currTempUnit);
                    sixthTemp.setText(temperatureC.get(5) + MainActivity.currTempUnit);
                    seventhTemp.setText(temperatureC.get(6) + MainActivity.currTempUnit);
                    eightTemp.setText(temperatureC.get(7) + MainActivity.currTempUnit);
                }
                time1.setText(time.get(0));
                time2.setText(time.get(1));
                time3.setText(time.get(2));
                time4.setText(time.get(3));
                time5.setText(time.get(4));
                time6.setText(time.get(5));
                time7.setText(time.get(6));
                time8.setText(time.get(7));





            } catch (Exception e) {
                Log.e("", e.toString());
            }

            super.onPostExecute(result);
        }
    }

}

