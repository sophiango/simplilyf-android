package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.sadhana.simplilyf.LightList.QuickLightData;

public class AddNewDevice extends AppCompatActivity {

    final String SERVER = "http://172.16.1.9:3000";
    EditText inputFullname, inputEmail, inputPW;
    List<NestData> allThermoData = new ArrayList<NestData>();
    Spinner vendorSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);
        inputFullname = (EditText) findViewById(R.id.add_usrname);
        inputEmail = (EditText) findViewById(R.id.add_email);
        inputPW = (EditText) findViewById(R.id.input_password);
        vendorSelected = (Spinner) findViewById(R.id.spinner1);
        Button addNewBtn = (Button) findViewById(R.id.btnAdd);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFullnameText = inputFullname.getText().toString();
                String inputEmailText = inputEmail.getText().toString();
                String inputPwText = inputPW.getText().toString();
                String vendor = vendorSelected.getSelectedItem().toString();
//                new NestLoginAsync().execute("qwerty", "sophia2901@gmail.com", "Cmpe@295","nest");
                if (vendor.equals("nest")){
                    new NestLoginAsync().execute(inputFullnameText, inputEmailText, inputPwText,vendor);
                } else {
                    new PhilipsLoginAsync().execute();
                }

            }
        });
    }

    private class NestLoginAsync extends AsyncTask<String, Void, ThermoList> {
        @Override
        protected ThermoList doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            ThermoList thermoList = null;
            try {
                System.out.println("ADD NEW THERMO ENDPOINT");
                /* forming th java.net.URL object */
                String register_endpoint = SERVER + "/thermo/new";
                URL url = new URL(register_endpoint);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("fullname", params[0]);
                jsonParam.put("username", params[1]);
                jsonParam.put("password", params[2]);
                jsonParam.put("vendor", params[3]);
                System.out.println("before post " + jsonParam.toString());

                // Set request header
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setUseCaches(false);
                // write body
                OutputStream wr= urlConnection.getOutputStream();
                wr.write(jsonParam.toString().getBytes("UTF-8"));
                int statusCode = urlConnection.getResponseCode();
                wr.close();
                System.out.println("status code " + statusCode);

                InputStream in = urlConnection.getInputStream();
                int chr;
                while ((chr = in.read()) != -1) {
                    reply.append((char) chr);
                }
                JSONArray thermoArray = new JSONArray(reply.toString());
                for(int i=0;i<thermoArray.length();i++) {
                    JSONObject jsonObject = thermoArray.getJSONObject(i);
                    String thermo_name = jsonObject.getString("thermo_name");
                    Double target_temperature = jsonObject.getDouble("target_temperature");
                    Double target_temperature_high = jsonObject.getDouble("target_temperature_high");
                    Double target_temperature_low = jsonObject.getDouble("target_temperature_low");
                    String target_temperature_mode = jsonObject.getString("target_temperature_mode");
                    String thermo_mode = jsonObject.getString("thermo_mode");

                    NestData nestData = new NestData(thermo_name, target_temperature, target_temperature_high, target_temperature_low, target_temperature_mode, thermo_mode);
                    allThermoData.add(nestData);
                }
                thermoList = new ThermoList(allThermoData);

                System.out.println("Value of response...." + allThermoData.get(0).getName() + "," + allThermoData.get(1).getName());
                /* 200 represents HTTP OK */

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return thermoList;
        }

        @Override
        protected void onPostExecute(ThermoList result) {
            System.out.println("RESULT: " + result);
            if (result==null) {
                Toast.makeText(AddNewDevice.this, "Unable to register the user", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddNewDevice.this, "Successful login", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddNewDevice.this, NestdevicesActivity.class);
                i.putExtra("thermo", result);
                startActivity(i);
            }
        }

    }

    private class PhilipsLoginAsync extends AsyncTask<String, Void, LightList> {
        @Override
        protected LightList doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            LightList lightList = null;
            List<LightList.QuickLightData> allLightData = new ArrayList<LightList.QuickLightData>();

            try {
                System.out.println("ADD NEW THERMO ENDPOINT");
                /* forming th java.net.URL object */
                String getAllLight = SERVER + "/light/getall";
                URL url = new URL(getAllLight);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);
                InputStream in = urlConnection.getInputStream();
                int chr;
                while ((chr = in.read()) != -1) {
                    reply.append((char) chr);
                }
                JSONArray lightArray = new JSONArray(reply.toString());
                for(int i=0;i<lightArray.length();i++) {
                    JSONObject jsonObject = lightArray.getJSONObject(i);
                    String light_name = jsonObject.getString("name");
                    String light_status = jsonObject.getString("status");
                    String light_hue = jsonObject.getString("hue");

                    QuickLightData lightData = new LightList().new QuickLightData(light_name,light_status,light_hue);
                    allLightData.add(lightData);

                }
                lightList = new LightList(allLightData);

                System.out.println("Value of response...." + allThermoData.get(0).getName() + "," + allThermoData.get(1).getName());
                /* 200 represents HTTP OK */

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return lightList;
        }

        @Override
        protected void onPostExecute(LightList result) {
            System.out.println("RESULT: " + result.getlightList().get(0).getOn());
//            if (result==null) {
//                Toast.makeText(AddNewDevice.this, "Unable to register the user", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(AddNewDevice.this, "Successful login", Toast.LENGTH_LONG).show();
//                Intent i = new Intent(AddNewDevice.this, NestdevicesActivity.class);
//                i.putExtra("thermo", result);
//                startActivity(i);
//            }
        }

    }

}