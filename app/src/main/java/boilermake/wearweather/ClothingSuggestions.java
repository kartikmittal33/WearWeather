package boilermake.wearweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ClothingSuggestions extends AppCompatActivity {

    String apiText;
    String result;

    static TextView Descript1;
    ImageView coatImgView;
    ImageView shirtImgView;
    ImageView pantImgView;
    ImageView shoeImgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_suggestions);
        Descript1 = (TextView) findViewById(R.id.Descript);
        coatImgView = (ImageView) findViewById(R.id.coatImageView);
        shirtImgView = (ImageView) findViewById(R.id.shirtImageView);
        pantImgView = (ImageView) findViewById(R.id.pantImageView);
        shoeImgView = (ImageView) findViewById(R.id.shoeImageView);

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
        double lat = 40.4525;


//        double lon = location.getLongitude();
        double lon = 74.4767;
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
                ArrayList<String> descriptionList = new ArrayList<String>();
                int count = 3;
                int i = 0;

                while (count < 27) {

                    JSONObject x = list.getJSONObject(i);
                    JSONArray windList = x.getJSONArray("weather");
                    JSONObject y = windList.getJSONObject(0);
                    String dt_txt = x.getString("dt_txt");
                    String description = y.getString("description");
                    apiText = description;
                    descriptionList.add(apiText);
                    count = count + 3;
                    i++;


                }


                int count2=0;
                int count3=0;
                int count4=0;
                int count5=0;
                int count6=0;
                int count7=0;
                int count8=0;
                int count9=0;
                int count10=0;
                for(int j=0; j<descriptionList.size(); j++){
                    if(descriptionList.get(j).equals("clear sky")){
                        count2++;
                    }
                    else if(descriptionList.get(j).equals("few clouds")){
                        count3++;
                    }
                    else if(descriptionList.get(j).equals("scattered clouds")){
                        count4++;
                    }
                    else if(descriptionList.get(j).equals("broken clouds")){
                        count5++;
                    }
                    else if(descriptionList.get(j).equals("shower rain")){
                        count6++;
                    }
                    else if(descriptionList.get(j).equals("rain")){
                        count7++;
                    }
                    else if(descriptionList.get(j).equals("thunderstorm")){
                        count8++;
                    }
                    else if(descriptionList.get(j).equals("snow")){
                        count9++;
                    }
                    else if(descriptionList.get(j).equals("mist")){
                        count10++;
                    }
                }

                if(count2>5){
                    Descript1.setText("Wear a cotton shirt and jeans. The day is nice. You should be good to go.");
                    coatImgView.setImageAlpha(0);
                    shirtImgView.setImageResource(R.drawable.purpletee);
                    pantImgView.setImageResource(R.drawable.jeans);
                    shoeImgView.setImageResource(R.drawable.trainers);

                }
                else if(count3>3 || count4>3){
                    Descript1.setText("The weather is cloudy. You may want to wear a jacket.");
                    coatImgView.setImageResource(R.drawable.browncoat);
                    shirtImgView.setImageResource(R.drawable.bluebuttondown);
                    pantImgView.setImageResource(R.drawable.pants);
                    shoeImgView.setImageResource(R.drawable.greendressshoes);
                }
                else if(count5>3){
                    Descript1.setText("It is most likely going to rain. Wear a jacket and take your umbrella");
                    coatImgView.setImageResource(R.drawable.orangejacket);
                    shirtImgView.setImageResource(R.drawable.purpletee);
                    pantImgView.setImageResource(R.drawable.jeans);
                    shoeImgView.setImageResource(R.drawable.trainers);
                }
                else if(count6>2){
                    Descript1.setText("It is going to rain slightly. Wear a jacket and take your umbrella");
                    coatImgView.setImageResource(R.drawable.orangejacket);
                    shirtImgView.setImageResource(R.drawable.purpletee);
                    pantImgView.setImageResource(R.drawable.jeans);
                    shoeImgView.setImageResource(R.drawable.trainers);
                }
                else if(count7>2){
                    Descript1.setText("It is going to rain. Wear your jacket, boots and take your umbrella");
                    coatImgView.setImageResource(R.drawable.browncoat);
                    shirtImgView.setImageResource(R.drawable.bluebuttondown);
                    pantImgView.setImageResource(R.drawable.jeans);
                    shoeImgView.setImageResource(R.drawable.yellowgreyboot);
                }
                else if(count8>2){
                    Descript1.setText("There is a thunderstorm. Wear your jacket, boots and take your umbrella. Try to stay indoors");
                    coatImgView.setImageResource(R.drawable.browncoat);
                    shirtImgView.setImageResource(R.drawable.bluebuttondown);
                    pantImgView.setImageResource(R.drawable.jeans);
                    shoeImgView.setImageResource(R.drawable.yellowgreyboot);
                }
                else if(count9>2){
                    Descript1.setText("It will snow today. Wear your jacket, coat, boots and take your umbrella in case. Try to stay indoors");
                    coatImgView.setImageResource(R.drawable.orangejacket);
                    shirtImgView.setImageResource(R.drawable.bluebuttondown);
                    pantImgView.setImageResource(R.drawable.jeans);
                    shoeImgView.setImageResource(R.drawable.yellowgreyboot);
                }
                else if(count10>2){
                    Descript1.setText("There is mist today. Be careful while you drive. Make sure you wear a jacket for warmth");
                    coatImgView.setImageResource(R.drawable.orangejacket);
                    shirtImgView.setImageResource(R.drawable.purpletee);
                    pantImgView.setImageResource(R.drawable.jeans);
                    shoeImgView.setImageResource(R.drawable.yellowgreyboot);
                }





            } catch (Exception e) {
                Log.e("", e.toString());
            }

            super.onPostExecute(result);
        }
    }

}
