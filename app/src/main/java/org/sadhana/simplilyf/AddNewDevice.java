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
import org.sadhana.simplilyf.LightList.QuickLightData;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddNewDevice extends AppCompatActivity {

    final String SERVER = new Config().getIP_ADDRESS();
    EditText inputUsername, inputEmail, inputPW;
    List<NestData> allThermoData = new ArrayList<NestData>();
    Spinner vendorSelected;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);
        inputUsername = (EditText) findViewById(R.id.add_usrname);
        inputEmail = (EditText) findViewById(R.id.add_email);
        inputPW = (EditText) findViewById(R.id.input_password);
        vendorSelected = (Spinner) findViewById(R.id.spinner1);
        userEmail = getIntent().getExtras().getString("userEmail");
        Button addNewBtn = (Button) findViewById(R.id.btnAdd);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsernameText = inputUsername.getText().toString();
                String inputEmailText = inputEmail.getText().toString();
                String inputPwText = inputPW.getText().toString();
                String vendor = vendorSelected.getSelectedItem().toString();
//                new NestLoginAsync().execute("qwerty", "sophia2901@gmail.com", "Cmpe@295","nest");
                if (vendor.equals("nest") || vendor.equals("Nest")){
                    new NestLoginAsync().execute(userEmail,inputUsernameText, inputEmailText, inputPwText);
                } else {
                    new PhilipsLoginAsync().execute(inputUsernameText, inputEmailText, inputPwText);
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
                jsonParam.put("email", params[0]);
                jsonParam.put("fullname", params[1]);
                jsonParam.put("username", params[2]);
                jsonParam.put("password", params[3]);
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
                Toast.makeText(AddNewDevice.this, "Unable to access Nest account to add new device", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddNewDevice.this, "Nest authorization successfully", Toast.LENGTH_LONG).show();
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
                String register_endpoint = SERVER + "/light/new";
                URL url = new URL(register_endpoint);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", params[0]);
                jsonParam.put("email", params[1]);
                jsonParam.put("password", params[2]);
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
                System.out.println("json array: " + reply.toString());
                JSONArray lightArray = new JSONArray(reply.toString());
                for(int i=0;i<lightArray.length();i++) {
                    JSONObject jsonObject = lightArray.getJSONObject(i);
                    String light_name = jsonObject.getString("name");
                    Boolean is_on = jsonObject.getBoolean("status");
                    String light_status;
                    if (is_on == true){
                        light_status="ON";
                    } else {
                        light_status="OFF";
                    }
                    System.out.print("light status: " + light_status);
                    String light_hue = jsonObject.getString("hue");

                 String color = convertHueToColor(light_hue);
               //     convertHueToColor(light_hue);
                    QuickLightData lightData = new LightList().new QuickLightData(light_name,light_status,color);
                    allLightData.add(lightData);
                    System.out.println("");

                }
                System.out.println("length: " + allLightData.size());
                lightList = new LightList(allLightData);

                System.out.println("Value of response...." + allLightData.get(0).getName() + "," + allLightData.get(1).getName());
                /* 200 represents HTTP OK */

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return lightList;
        }

        public String convertHueToColor(String hue){
            String color = null;
            int hue_Num=Integer.parseInt(hue);
            switch (hue_Num) {
                case 0:
                    color = "red";
                    break;
                case 17000:
                    color="yellow";
                    break;
                case 25500:
                    color = "green";
                    break;

                case 46920:
                    color = "blue";
                    break;
                case 48000:
                    color = "purple";
                    break;

                default:
                    color = "white";
            }
            return color;
        }

        @Override
        protected void onPostExecute(LightList result) {
            if (result==null) {
                Toast.makeText(AddNewDevice.this, "Unable to access Philips account to add new device", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddNewDevice.this, "Philips authorization successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddNewDevice.this, PhilipsdevicesActivity.class);
                i.putExtra("lights", result);
                startActivity(i);
            }
        }

    }

}