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
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NestdevicesActivity extends AppCompatActivity {

    private ListView myList;
    List<NestData> listThermo = new ArrayList<NestData>();
    ThermoList receivedThermoList = null;

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
        if (receivedBundle != null){
            receivedThermoList = (ThermoList) receivedBundle.getSerializable("thermo");
            listThermo = receivedThermoList.getThermoList();
            for (int j = 0; j < listThermo.size(); j++) {
                System.out.println("thermo: " + listThermo.get(j).getName());
            }
        }


        myList = (ListView) findViewById(R.id.list);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(NestdevicesActivity.this, "Row " + position + " clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NestdevicesActivity.this, NestLivingrmActivity.class);
                i.putExtra("TEMP", listThermo.get(position).getCurrentTemperature());
             //   System.out.print("Starting Intent" + receivedThermoList.getThermoList().get(position).getCurrentTemperature());
                startActivity(i);
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final ArrayList list = new ArrayList<>();
        for (int k = 0; k < listThermo.size(); k++) {
            list.add(listThermo.get(k).getName());
        }
        final MyCustomAdapter adapter = new MyCustomAdapter(NestdevicesActivity.this, list);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // list.add("New Item");
                //adapter.notifyDataSetChanged();
                Intent i = new Intent(NestdevicesActivity.this, AddNewDevice.class);
                System.out.print("Starting Intent");
                startActivity(i);
            }
        });

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
        // a view to represent an item in that data set.
        myList.setAdapter(adapter);

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


    public class AsyncHttpTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            try {
                System.out.println("HELLO ENDPOINT");
                /* forming th java.net.URL object */
                URL url = new URL("http://10.189.48.129:3000/hello");
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
                    //   parseResult(response);

                    System.out.println("Value of response...." + response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return null;
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
