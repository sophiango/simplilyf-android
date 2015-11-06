package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddNewDevice extends AppCompatActivity {

    final String SERVER = "http://10.189.114.99:3000";
    EditText inputFullname, inputEmail, inputPW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);
        inputFullname = (EditText) findViewById(R.id.add_usrname);
        inputEmail = (EditText) findViewById(R.id.add_email);
        inputPW = (EditText) findViewById(R.id.input_password);
        Button addNewBtn = (Button) findViewById(R.id.btnAdd);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFullnameText = inputFullname.getText().toString();
                String inputEmailText = inputEmail.getText().toString();
                String inputPwText = inputPW.getText().toString();
                new NestLoginAsync().execute(inputFullnameText,inputEmailText,inputPwText);
            }
        });
    }

    private class NestLoginAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            try {
                System.out.println("ADD NEW THERMO ENDPOINT");
                /* forming th java.net.URL object */
                String register_endpoint = SERVER + "/thermo/new";
                URL url = new URL(register_endpoint);
                urlConnection = (HttpURLConnection) url.openConnection();
                 /* optional request header */
                //urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true); // accept request body
                //urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");

                /* for Get request */
                //  List<NameValuePairs>
                //you need to encode ONLY the values of the parameters
//                String param="username" + URLEncoder.encode(params[0],"UTF-8")+
//                "&password="+URLEncoder.encode(params[1],"UTF-8")+
//                "&email="+URLEncoder.encode(params[2],"UTF-8")+
//                "&fullname="+URLEncoder.encode(params[3],"UTF-8");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("fullname", params[0]);
                jsonParam.put("username", params[1]);
                jsonParam.put("password", params[2]);
                System.out.println("before post " + jsonParam.toString());

                // Set request header
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setUseCaches(false);


                //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //urlConnection.setRequestProperty("Content-Length",Integer.toString(param.getBytes().length));

                // write body
                OutputStream wr= urlConnection.getOutputStream();
                wr.write(jsonParam.toString().getBytes("UTF-8"));
                int statusCode = urlConnection.getResponseCode();
                wr.close();
                System.out.println("status code " + statusCode);

                InputStream in = urlConnection.getInputStream();
                //StringBuffer sb = new StringBuffer();
                int chr;
                while ((chr = in.read()) != -1) {
                    reply.append((char) chr);
                }
                String [] perThermoData = reply.toString().split("/");
                System.out.println("size: " + perThermoData.length);
                List<NestData> allThermoData = new ArrayList<NestData>();
                for (int i = 0; i < perThermoData.length; i++){
                    NestData nestData = new Gson().fromJson(perThermoData[i], NestData.class);
                    allThermoData.add(nestData);
                    System.out.println("nest data: " + nestData + " size: " + allThermoData.size());
                }

                System.out.println("Value of response...." + allThermoData.get(0).getName() + "," + allThermoData.get(1).getName());
                /* 200 represents HTTP OK */

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return reply.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("RESULT: " + result);
            if (result.equals(null) || result.equals("")) {
                Toast.makeText(AddNewDevice.this, "Unable to register the user", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddNewDevice.this, "Successful login", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddNewDevice.this, NestLivingrmActivity.class);
                startActivity(i);
            }
        }

    }


}