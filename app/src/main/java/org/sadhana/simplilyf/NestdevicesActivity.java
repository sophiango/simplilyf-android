package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NestdevicesActivity extends AppCompatActivity {

    final String SERVER = new Config().getIP_ADDRESS();
    private ListView myList;
    List<NestData> listThermo = new ArrayList<NestData>();
    private List<NestData> loginList = new ArrayList<NestData>();
    ThermoList receivedThermoList = null, loginReceivedList=null;;
    private ImageView mAwayBtn;
    private ImageView mHomeBtn;
    private DeviceList deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestdevices);
        //new AsyncHttpTask().execute();
        System.out.println("here");


        //relate the listView from java to the one created in xml
        //String receivedJsonObject = "";
        Intent i = getIntent();
        Bundle receivedBundle = i.getExtras();
//        if (receivedBundle!=null){
//            receivedJsonObject = receivedBundle.getString("thermo");
//            NestData receivedNestData = new Gson().fromJson(receivedJsonObject,NestData.class);
//            System.out.println("Received data: " + receivedNestData.getName());
//        }
        // add new
        if (receivedBundle != null){
            receivedThermoList = (ThermoList) receivedBundle.getSerializable("thermo");
            if (receivedThermoList!=null) {
                listThermo = receivedThermoList.getThermoList();
                for (int j = 0; j < listThermo.size(); j++) {
                    System.out.println("thermo: " + listThermo.get(j).getName());
                }
            }
        }
        // login
        deviceList=(DeviceList)getIntent().getSerializableExtra("deviceObject");
//        System.out.println("user email"+ deviceList.getEmail());
        if (deviceList!=null) {
            new NestGetDetailsAsync().execute(deviceList.getEmail()); // GET /thermo/all
        }
//        if(deviceList!=null && deviceList.getThermos()!=null && deviceList.getThermos().size()>0){
//            System.out.println("user email"+ deviceList.getEmail());
//            new NestGetDetailsAsync().execute(deviceList.getEmail()); // GET /thermo/all
//        }

        myList = (ListView) findViewById(R.id.list);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(NestdevicesActivity.this, "Row " + position + " clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NestdevicesActivity.this, NestLivingrmActivity.class);
                System.out.println("on login size  "+loginList.size() +" "+ listThermo.size());
                if(listThermo.size()>0 && listThermo.get(position)!=null) {
                    i.putExtra("TEMP", listThermo.get(position).getTarget_temperature());
                    i.putExtra("DEVICENAME", listThermo.get(position).getName());
                    i.putExtra("HIGH_TEMP", listThermo.get(position).getTarget_temperature_high());
                    i.putExtra("LOW_TEMP", listThermo.get(position).getTarget_temperature_low());
                    i.putExtra("TEMP_MODE", listThermo.get(position).getTarget_temperature_mode());
                    i.putExtra("THERMO_MODE", listThermo.get(position).getMode());
                }
                if(loginList.size()>0  && loginList.get(position)!=null){
                    i.putExtra("TEMP", loginList.get(position).getTarget_temperature());
                    i.putExtra("DEVICENAME", loginList.get(position).getName());
                    i.putExtra("HIGH_TEMP", loginList.get(position).getTarget_temperature_high());
                    i.putExtra("LOW_TEMP", loginList.get(position).getTarget_temperature_low());
                    i.putExtra("TEMP_MODE", loginList.get(position).getTarget_temperature_mode());
                    i.putExtra("THERMO_MODE", loginList.get(position).getMode());
                }
             //   System.out.print("Starting Intent" + receivedThermoList.getThermoList().get(position).getCurrentTemperature());
                startActivity(i);
            }
        });



        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final ArrayList list = new ArrayList<>();
        final ArrayList list2 = new ArrayList<>();
        for (int k = 0; k < listThermo.size(); k++) {
            list.add(listThermo.get(k).getName());
            list2.add(listThermo.get(k).getTarget_temperature());
        }
        final MyCustomAdapter adapter = new MyCustomAdapter(NestdevicesActivity.this, list,list2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // list.add("New Item");
                //adapter.notifyDataSetChanged();
                Intent i = new Intent(NestdevicesActivity.this, AddNewDevice.class);
                if (deviceList.getEmail()!=null){
                    i.putExtra("userEmail",deviceList.getEmail());
                }
                System.out.print("Starting Intent");
                startActivity(i);
            }
        });

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
        // a view to represent an item in that data set.
        myList.setAdapter(adapter);

        mAwayBtn=(ImageView)findViewById(R.id.offAllBtn);
        mHomeBtn=(ImageView)findViewById(R.id.onAllBtn);



        mAwayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AwayBtnAsync().execute("away");

            }
        });

        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AwayBtnAsync().execute("home");

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nest, menu);
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

    private class NestGetDetailsAsync extends AsyncTask<String, Void, ThermoList> {
        @Override
        protected ThermoList doInBackground(String... params) {
//            InputStream inputStream = null;
//            HttpURLConnection urlConnection = null;
//            StringBuilder reply = new StringBuilder();
            List<NestData> allThermoData = new ArrayList<>();
//            ThermoList thermoList = null;
//            try {
//                System.out.println("GET ALL NEST ENDPOINT");
//                /* forming th java.net.URL object */
//                String register_endpoint = SERVER + "/thermo/all";
//                URL url = new URL(register_endpoint);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setDoOutput(true);
//                urlConnection.setChunkedStreamingMode(0);
//                urlConnection.setRequestMethod("POST");
//                // Set request header
//                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
//                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//                urlConnection.setUseCaches(false);
//                JSONObject jsonParam = new JSONObject();
//                jsonParam.put("email", params[0]);
//                System.out.println("before post " + jsonParam.toString());
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            ThermoList thermoList = null;
            try {
                System.out.println("ADD NEW THERMO ENDPOINT");
                /* forming th java.net.URL object */
                String register_endpoint = SERVER + "/thermo/all";
                URL url = new URL(register_endpoint);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[0]);
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
            if(result!=null){
                loginReceivedList = result;
                loginList=result.getThermoList();
                final ArrayList list = new ArrayList<>();
                final ArrayList list2 = new ArrayList<>();

                for (int k = 0; k < loginList.size(); k++) {
                    list.add(loginList.get(k).getName());
                    list2.add(loginList.get(k).getTarget_temperature());
                }
                System.out.println("on post size  "+list.size() + " "+list2.size());
                System.out.println("on login size  "+loginList.size() +" "+ result.getThermoList().size());

                final MyCustomAdapter adapter = new MyCustomAdapter(NestdevicesActivity.this, list,list2);
                myList.setAdapter(adapter);
            }
        }

    }

}




//Serializable bundle = i.getSerializableExtra("ThermoList");
//List<NestData> listThermo = (List<NestData>) i.getSerializableExtra("ThermoList");
//System.out.println("BUNDLE RECEIVE: " + bundle);
//        if (listThermo!=null) {
//            //List<NestData> listThermo = (List<NestData>) bundle.get("ThermoList");
////            if (listThermo.isEmpty()) {
////                System.out.println("no thermo list");
////            } else {
//                for (int j = 0; j < listThermo.size(); j++) {
//                    System.out.println("thermo: " + listThermo.get(j));
//                }
////            }
//        }
