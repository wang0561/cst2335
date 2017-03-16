package com.example.wang.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    String current, min, max, iconName;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.weatherProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery thread = new ForecastQuery();
        thread.execute();
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String ... args){
            String in="";
            try{
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inStream = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                int type;
                while((type = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.getEventType() == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equals("temperature") ){
                            max =xpp.getAttributeValue(null, "max");
                            publishProgress(25);
                            Thread.sleep(1000);
                            Log.i("XML maxTemp:" , max );

                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            Thread.sleep(1000);
                            Log.i("XML minTemp:", min);

                            current = xpp.getAttributeValue(null, "value");
                            publishProgress(75);
                            Thread.sleep(1000);
                            Log.i("XML currentTemp:" , current );
                        }
                        else if (xpp.getName().equals("weather")){
                            iconName = xpp.getAttributeValue(null, "icon");
                            publishProgress(100);
                            Thread.sleep(1000);
                            Log.i("XML iconName:" , iconName );

                            if (!fileExistance( iconName + ".png")) {
                                URL imageURL = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                Bitmap img = getImage(imageURL);
                                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                img.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(iconName + ".png");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(fis);
                            }else {
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(iconName + ".png");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(fis);
                                publishProgress(100);
                            }
                        }

                    }
                    xpp.next();
                }
            }catch(Exception me){
                Log.e("AsyncTask", "Malformed URL:" + me.getMessage());
            }

            return in;
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }


        public Bitmap getImage(URL url) {

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        public void onProgressUpdate(Integer ... values){

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.weatherProgressBar);
            progressBar.setProgress(values[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String result){

            TextView currentTempView = (TextView)findViewById(R.id.currentTemp);
            currentTempView.setText("Current Temperature:" + current+"°C");

            TextView minTempView = (TextView)findViewById(R.id.minTemp);
            minTempView.setText("Min Temperature: " + min+"°C");

            TextView maxTempView = (TextView)findViewById(R.id.maxTemp);
            maxTempView.setText("Max Temperature: " + max+"°C");

            ImageView imageView = (ImageView)findViewById(R.id.weatherImageView);
            imageView.setImageBitmap(image);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.weatherProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
