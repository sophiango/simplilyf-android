package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PhilipsdevicesActivity extends AppCompatActivity {

    final String SERVER = new Config().getPHILIPS_IP_ADDRESS() + "/light";
    private ListView myList;
    private LightList receivedLightList = null,loginReceivedList=null;
    private ImageView onAllLights,offAllLights;
    private List<LightList.QuickLightData> lightList = new ArrayList<LightList.QuickLightData>();
    private List<LightList.QuickLightData> loginList = new ArrayList<LightList.QuickLightData>();
    private LinearLayout mLayout;
    public String userEmail;
    public DeviceList philipsList;
    final ArrayList list = new ArrayList<>();
    final ArrayList statelist = new ArrayList<>();
    final ArrayList colorlist = new ArrayList<>();
    private DeviceList deviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philipsdevices);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        mLayout=(LinearLayout)findViewById(R.id.layout);
        onAllLights=(ImageView)findViewById(R.id.onAllBtn);
        offAllLights=(ImageView)findViewById(R.id.offAllBtn);

        Intent i = getIntent();
        Bundle receivedBundle = i.getExtras();
        System.out.println("Bundle: " + receivedBundle);
        // register
        if (receivedBundle != null){
            System.out.println("Received bundle?");
            receivedLightList = (LightList) receivedBundle.getSerializable("lights");
            if(receivedLightList!=null) {
                lightList = receivedLightList.getLightList();
//                for (int j = 0; j < lightList.size(); j++) {
//                    System.out.println("light: " + lightList.get(j).getName());
//
//                }
                System.out.println("Inside light on create " + lightList.get(0).getName());
                List<LightList.QuickLightData> unsortedList = new ArrayList<LightList.QuickLightData>();
                for (int a = 0; a < lightList.size(); a++) {
                    LightList.QuickLightData light = new LightList.QuickLightData();
                    light.setName(lightList.get(a).getName());
                    light.setOn(lightList.get(a).getOn());
                    light.setHue(lightList.get(a).getHue());
                    unsortedList.add(light);
//                    list.add(lightList.get(a).getName());
//                    statelist.add(lightList.get(a).getOn());
//                    colorlist.add(lightList.get(a).getHue());

                }
                Collections.sort(unsortedList, new Comparator<LightList.QuickLightData>() {
                    @Override
                    public int compare(LightList.QuickLightData lhs, LightList.QuickLightData rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

                for (LightList.QuickLightData q : unsortedList){
                    System.out.println("sort?: " + q.getName());
                    list.add(q.getName());
                    statelist.add(q.getOn());
                    colorlist.add(q.getHue());
                }

//                for (int b = 0; b < lightList.size(); b++) {
////            statelist.add("ON");
//                    statelist.add(lightList.get(b).getOn());
//                }
//
//                for (int c = 0; c < lightList.size(); c++) {
////            colorlist.add("Red");
//                    colorlist.add(lightList.get(c).getHue());
//                }

            }
            mLayout.setVisibility(View.VISIBLE);
//            onAllLights.setVisibility(View.VISIBLE);
//           mLayout.setVisibility(View.VISIBLE);
//           offAllLights.setVisibility(View.VISIBLE);
        }
//        Bundle b = getIntent().getExtras();
//        if(b!=null) {
//            userEmail = b.getString("userEmail");
//           philips
//        }


        deviceList=(DeviceList)getIntent().getSerializableExtra("deviceObject");
        String email=getIntent().getStringExtra("userEmail");
        System.out.println("device email  "+email);
        if(deviceList!=null){
            System.out.println("device list  "+deviceList.getEmail());
            new PhilipsLoginAsync().execute(deviceList.getEmail());
        }

        myList = (ListView) findViewById(R.id.list);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PhilipsdevicesActivity.this, "Row " + position + " clicked", Toast.LENGTH_SHORT).show();
               // new PhilipsDetailAsync().execute(position+1);
                //// ListView Clicked item value
               // String  itemValue    = (String) listView.getItemAtPosition(position);
                String pos=Integer.toString(position+1);
                Intent i = new Intent(PhilipsdevicesActivity.this, PhilipsDetailsActivity.class);

                i.putExtra("position Value",pos);
                if(receivedLightList!=null && receivedLightList.getLightList()!=null) {
                    i.putExtra("lampName", receivedLightList.getlightList().get(position).getName());
                    i.putExtra("lampColor", receivedLightList.getlightList().get(position).getHue());
                    i.putExtra("lampStatus", receivedLightList.getlightList().get(position).getOn());
                }
                if(loginReceivedList!=null && loginReceivedList.getLightList()!=null){
                    i.putExtra("lampName", loginReceivedList.getlightList().get(position).getName());
                    i.putExtra("lampColor", loginReceivedList.getlightList().get(position).getHue());
                    i.putExtra("lampStatus", loginReceivedList.getlightList().get(position).getOn());
                }

//                System.out.println("lamp name   " + receivedLightList.getlightList().get(position).getName());
//                System.out.println("lamp color   " + receivedLightList.getlightList().get(position).getOn());
//                System.out.println("lamp status   "+receivedLightList.getlightList().get(position).getHue());
                System.out.print("Starting Intent");
                startActivity(i);
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

      final PhilipsCustomAdapter adapter = new PhilipsCustomAdapter(PhilipsdevicesActivity.this, list,statelist,colorlist);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // list.add("New Item");
                //adapter.notifyDataSetChanged();
                Intent i = new Intent(PhilipsdevicesActivity.this, AddNewDevice.class);
                System.out.print("Starting Intent");
                startActivity(i);
            }
        });

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
        // a view to represent an item in that data set.
      myList.setAdapter(adapter);

        onAllLights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new onAllAsync().execute(userEmail);
            }
        });

        offAllLights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new offAllAsync().execute(userEmail);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_philips, menu);
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
        if(id== android.R.id.home){
        NavUtils.navigateUpFromSameTask(this);
        return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class offAllAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            StringBuilder reply = new StringBuilder();
            try {
                System.out.println("REGISTER ENDPOINT");
                /* forming th java.net.URL object */
                String register_endpoint = SERVER + "/all/off";
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


                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user_id", params[0]);

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
                System.out.println("Value of response...." + reply.toString());
                /* 200 represents HTTP OK */

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //System.out.println("RESULT: " + result);


        }

    }

    private class onAllAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            StringBuilder reply = new StringBuilder();
            try {
                System.out.println("REGISTER ENDPOINT");
                /* forming th java.net.URL object */
                String register_endpoint = SERVER + "/all/on";
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


                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user_id", params[0]);

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
                System.out.println("Value of response...." + reply.toString());
                /* 200 represents HTTP OK */

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //System.out.println("RESULT: " + result);


        }

    }

    //get all
    private class PhilipsLoginAsync extends AsyncTask<String, Void, LightList> {
        @Override
        protected LightList doInBackground(String... params) {
//            InputStream inputStream = null;
//            HttpURLConnection urlConnection = null;
//            StringBuilder reply = new StringBuilder();

            LightList lightList = null;
            List<LightList.QuickLightData> allLightData = new ArrayList<LightList.QuickLightData>();
//
//            try {
//                System.out.println("ADD NEW THERMO ENDPOINT");
//                /* forming th java.net.URL object */
//                String register_endpoint = SERVER + "/getall";
//                URL url = new URL(register_endpoint);
//                urlConnection = (HttpURLConnection) url.openConnection();
//              //  urlConnection.setDoOutput(true);
//                urlConnection.setChunkedStreamingMode(0);
//                urlConnection.setRequestMethod("GET");
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            ThermoList thermoList = null;
            try {
                System.out.println("GET ALL LIGHT ENDPOINT");
                /* forming th java.net.URL object */
                String register_endpoint = SERVER + "/getall";
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
                    LightList.QuickLightData lightData = new LightList.QuickLightData(light_name,light_status,color);
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



        @Override
        protected void onPostExecute(LightList result) {
            if(result!=null){
                //result.getLightList();
                loginList = result.getLightList();
                loginReceivedList=result;
                System.out.println("Inside light on post execute");
                List<LightList.QuickLightData> unsortedList = new ArrayList<LightList.QuickLightData>();
                for (int a = 0; a < loginList.size(); a++) {
                    LightList.QuickLightData light = new LightList.QuickLightData();
                    light.setName(loginList.get(a).getName());
                    light.setOn(loginList.get(a).getOn());
                    light.setHue(loginList.get(a).getHue());
                    unsortedList.add(light);
//                    list.add(lightList.get(a).getName());
//                    statelist.add(lightList.get(a).getOn());
//                    colorlist.add(lightList.get(a).getHue());

                }
                Collections.sort(unsortedList, new Comparator<LightList.QuickLightData>() {
                    @Override
                    public int compare(LightList.QuickLightData lhs, LightList.QuickLightData rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

                for (LightList.QuickLightData q : unsortedList){
                    System.out.println("sort2?: " + q.getName());
                    list.add(q.getName());
                    statelist.add(q.getOn());
                    colorlist.add(q.getHue());
                }




//                for (int b = 0; b < loginList.size(); b++) {
//
//                    statelist.add(loginList.get(b).getOn());
//                    System.out.println(loginList.get(b).getOn());
//                }
//
//                for (int c = 0; c < loginList.size(); c++) {
//
//                    colorlist.add(loginList.get(c).getHue());
//                    System.out.println(loginList.get(c).getHue());
//                }

                final PhilipsCustomAdapter adapter = new PhilipsCustomAdapter(PhilipsdevicesActivity.this, list,statelist,colorlist);
                myList.setAdapter(adapter);
            }
        }

    }
    public String convertHueToColor(String hue){
        String color = null;
        int hue_Num=Integer.parseInt(hue);
//        switch (hue_Num) {
//            case isBe:
//                color = "red";
//                break;
//            case 17000:
//                color="yellow";
//                break;
//            case 25500:
//                color = "green";
//                break;
//
//            case 46920:
//                color = "blue";
//                break;
//            case 48000:
//                color = "purple";
//                break;
//
//            default:
//                color = "white";
//        }

        if(isBetween(hue_Num,0,11000) || isBetween(hue_Num,65000,67000)){
            color="red";
        }
        else if(isBetween(hue_Num,11000,25000)){
            color="yellow";
        }
        else if(isBetween(hue_Num,25100,31000)){
            color="green";
        }
        else if(isBetween(hue_Num,45000,47000)){
            color="blue";
        }
        else if(isBetween(hue_Num,48000,50000)){
            color="purple";
        }
        else{
            color="white";
        }
        return color;
    }

    public static boolean isBetween(int x,int lower,int upper){
        return lower<=x && x<=upper;
    }

}
