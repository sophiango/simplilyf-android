package org.sadhana.simplilyf;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class NestLivingrmActivity extends ActionBarActivity implements TempUpdateDialog.OnCompleteListener {


   final String SERVER = "http://192.168.1.8:3000";
    private EditText mTempValue;
    private Button mUpdate;
    private Double res_temp;
    private String devicename;
    private EditText mUpdateTempValue;
    private Button mAwayBtn;
    private Button mCoolBtn;
    public static NestData nestData;
    double target = 65;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_livingrm);
//        mTempValue=(EditText)findViewById(R.id.temp_value);
//        mUpdateTempValue=(EditText)findViewById(R.id.updateTempValue);
        Intent i=getIntent();
         res_temp=i.getDoubleExtra("TEMP", 0.0);
         devicename=i.getStringExtra("DEVICENAME");
        System.out.println("res received" + res_temp);
//        mTempValue.setText(res_temp.toString());
        mAwayBtn=(Button)findViewById(R.id.awayBtn);
        mCoolBtn=(Button)findViewById(R.id.coolBtn);



        mAwayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AwayBtnAsync().execute("away");

            }
        });

        mCoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        final CircleView currentCircle = (CircleView) findViewById(R.id.main_screen_current);
        currentCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = TempUpdateDialog.newInstance(devicename,res_temp);
                dialogFragment.show(getFragmentManager(), "editMainDialog");
            }
        });
        currentCircle.setTitleText(Double.toString(res_temp));

        final CircleView targetCircle = (CircleView) findViewById(R.id.main_screen_target);
        targetCircle.setTitleText(Double.toString(res_temp));

        android.support.design.widget.FloatingActionButton floatingActionButton = (android.support.design.widget.FloatingActionButton) findViewById(R.id.temp_update);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = TempUpdateDialog.newInstance(devicename,res_temp);
                dialogFragment.show(getFragmentManager(), "tempSettingDialog");
            }
        });

        nestData = new NestData(devicename,res_temp);

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

    @Override
    public void onComplete(double targetTemperature, Boolean setPermanently) {
        System.out.println("ON COMPLETE");
        CircleView targetCircle = (CircleView) findViewById(R.id.main_screen_current);
//        targetTemperature = target;
        targetCircle.setTitleText(targetTemperature + "");
    }

    // Away
    public class AwayBtnAsync extends AsyncTask<String,Void, String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            //   String SERVER = "http://10.189.115.48:3000";
            ThermoList thermoList = null;
            try {
                System.out.println("AWAY THERMO ENDPOINT");
                /* forming th java.net.URL object */
                String changeTempUrl = SERVER+"/thermo/mode";
                URL url = new URL(changeTempUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true); // accept request body
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("PUT");
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("thermo_mode", params[0]);
             //   jsonParam.put("updated_temp", params[1]);
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
                System.out.println("received: " + reply.toString());
                if(statusCode==200){
                    System.out.println("mode changed");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            System.out.println("on post execute: " + s);

        }
    }

    // Cool


}



//        String retrievedStr;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                retrievedStr= null;
//                System.out.println("VALUE OF RETRIEVED STRING1"+retrievedStr);
//            } else {
//                retrievedStr= (String)extras.get("TEMP");
//                System.out.println("VALUE OF RETRIEVED STRING2"+retrievedStr);
//                mTempValue.setText(retrievedStr);
//            }
//        } else {
//            retrievedStr= (String) savedInstanceState.getSerializable("VALUE_SENT");
//        }
//        System.out.println("VALUE OF RETRIEVED STRING"+retrievedStr);
