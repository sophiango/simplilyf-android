package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PhilipsdevicesActivity extends ActionBarActivity {

    final String SERVER = "http://192.168.1.8:3000/light/";
    private ListView myList;
    private LightList receivedLightList = null;
    private ImageView onAllLights,offAllLights;
    private List<LightList.QuickLightData> lightList = new ArrayList<LightList.QuickLightData>();
    public String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philipsdevices);
        final ArrayList list = new ArrayList<>();
        final ArrayList statelist = new ArrayList<>();
        final ArrayList colorlist = new ArrayList<>();
        Intent i = getIntent();
        Bundle receivedBundle = i.getExtras();
        if (receivedBundle != null){
            receivedLightList = (LightList) receivedBundle.getSerializable("lights");
            if(receivedLightList!=null) {
                lightList = receivedLightList.getLightList();
                for (int j = 0; j < lightList.size(); j++) {
                    System.out.println("light: " + lightList.get(j).getName());
                }

                for (int a = 0; a < lightList.size(); a++) {
//               list.add("Hue Light " + i);
                    list.add(lightList.get(a).getName());
                }

                for (int b = 0; b < lightList.size(); b++) {
//            statelist.add("ON");
                    statelist.add(lightList.get(b).getOn());
                }

                for (int c = 0; c < lightList.size(); c++) {
//            colorlist.add("Red");
                    colorlist.add(lightList.get(c).getHue());
                }
            }
        }
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            userEmail = b.getString("userEmail");
        }
        myList = (ListView) findViewById(R.id.list);
        onAllLights=(ImageView)findViewById(R.id.onAllBtn);
        offAllLights=(ImageView)findViewById(R.id.offAllBtn);
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
}
