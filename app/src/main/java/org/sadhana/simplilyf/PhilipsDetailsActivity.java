package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhilipsDetailsActivity extends AppCompatActivity {

    final String SERVER = "http://10.189.114.99:3000/light/";
    private EditText mlightStatus;
    private Button mOnButton;
    private Button mOffButton;
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philips_details);
        mlightStatus=(EditText)findViewById(R.id.lightStatus_value);
        Intent intent=getIntent();
       position=  intent.getStringExtra("position Value");
        System.out.println("value from intent  "+position);
        new PhilipsDetailAsync().execute(position);
        mOnButton=(Button)findViewById(R.id.turnon);
        mOffButton=(Button)findViewById(R.id.turnoff);

        mOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button clicked");
                System.out.println("value of editetxt   "+mlightStatus.getText().toString());
                String lightStatus=mlightStatus.getText().toString();
                System.out.println("light status "+lightStatus);
                if(lightStatus.equals("true")) {
                    System.out.println("light is true");
                    //hit the endpoint
                    new PhilipsLightOFFAsync().execute(position);
                }

                else {
                    System.out.println("light is false");
                    Toast.makeText(PhilipsDetailsActivity.this, "Light is already OFF!", Toast.LENGTH_LONG).show();
                }

            }
        });

        mOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nest_livingrm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class PhilipsDetailAsync extends AsyncTask<String, Void, PhilipsData> {

        @Override
        protected PhilipsData doInBackground(String... params) {
            InputStream inputStream = null;
            //int deviceNum=params[0];
            String deviceNum= SERVER+"getlight/"+params[0];
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            PhilipsData msg=new PhilipsData();
            try {
                System.out.println("endpoint value "+deviceNum);
                System.out.println("Philips endpoint");
                /* forming th java.net.URL object */
                URL url = new URL(deviceNum);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");
                //  List<NameValuePairs>
                int statusCode = urlConnection.getResponseCode();
                System.out.println("status code: " + statusCode);
                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                     msg = new Gson().fromJson(response, PhilipsData.class);
                    //   parseResult(response);
                    System.out.println("Philips response...." +msg.getName()+" "+ msg.getState().getOn());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return msg;
        }

        @Override
        protected void onPostExecute(PhilipsData result) {
            System.out.println("RESULT: " + result);
           // System.out.println("value of light status"+result.getState().getOn());
            if(result.getState().getOn()==true){
                mlightStatus.setText("true");
            }
            else
                mlightStatus.setText("false");


        }
    }

    public class PhilipsLightOFFAsync extends AsyncTask<String, Void, PhilipsData> {

        @Override
        protected PhilipsData doInBackground(String... params) {
            InputStream inputStream = null;
            //int deviceNum=params[0];
            String deviceNum= SERVER+"off/"+params[0];
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            PhilipsData msg=new PhilipsData();
            try {
                System.out.println("endpoint value "+deviceNum);
                System.out.println("Off Philips endpoint");
                /* forming th java.net.URL object */
                URL url = new URL(deviceNum);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("POST");
                //  List<NameValuePairs>
                int statusCode = urlConnection.getResponseCode();
                System.out.println("status code in off: " + statusCode);
                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    msg = new Gson().fromJson(response, PhilipsData.class);
                    //   parseResult(response);
                    System.out.println("Philips response....in off " +msg.getName()+" "+ msg.getState().getOn());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return msg;
        }

        @Override
        protected void onPostExecute(PhilipsData result) {
            System.out.println("RESULT: " + result);
            // System.out.println("value of light status"+result.getState().getOn());
            if(result.getState().getOn()==true){
                mlightStatus.setText("true");
            }
            else
                mlightStatus.setText("false");


        }
    }

    public class PhilipsLightONAsync extends AsyncTask<String, Void, PhilipsData> {

        @Override
        protected PhilipsData doInBackground(String... params) {
            InputStream inputStream = null;
            //int deviceNum=params[0];
            String deviceNum= SERVER+"on/"+params[0];
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            PhilipsData msg=new PhilipsData();
            try {
                System.out.println("endpoint value "+deviceNum);
                System.out.println("On Philips endpoint");
                /* forming th java.net.URL object */
                URL url = new URL(deviceNum);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("POST");
                //  List<NameValuePairs>
                int statusCode = urlConnection.getResponseCode();
                System.out.println("status code in on: " + statusCode);
                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    msg = new Gson().fromJson(response, PhilipsData.class);
                    //   parseResult(response);
                    System.out.println("Philips response....in on " +msg.getName()+" "+ msg.getState().getOn());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return msg;
        }

        @Override
        protected void onPostExecute(PhilipsData result) {
            System.out.println("RESULT: " + result);
            // System.out.println("value of light status"+result.getState().getOn());
            if(result.getState().getOn()==true){
                mlightStatus.setText("true");
            }
            else
                mlightStatus.setText("false");


        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        System.out.println("result value" + result);
        return result;
    }
}
