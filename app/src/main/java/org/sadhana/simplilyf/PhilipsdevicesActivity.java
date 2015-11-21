package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PhilipsdevicesActivity extends ActionBarActivity {


    private ListView myList;
    private LightList receivedLightList = null;
    private List<LightList.QuickLightData> lightList = new ArrayList<LightList.QuickLightData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philipsdevices);
        Intent i = getIntent();
        Bundle receivedBundle = i.getExtras();
        if (receivedBundle != null){
            receivedLightList = (LightList) receivedBundle.getSerializable("lights");
            lightList = receivedLightList.getLightList();
            for (int j = 0; j < lightList.size(); j++) {
                System.out.println("light: " + lightList.get(j).getName());
            }
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
                System.out.print("Starting Intent");
                startActivity(i);
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final ArrayList list = new ArrayList<>();
        for (int a = 0; a < lightList.size(); a++) {
//            list.add("Hue Light " + i);
            list.add(lightList.get(a).getName());
        }
        final ArrayList statelist = new ArrayList<>();
        for (int b = 0; b < lightList.size(); b++) {
//            statelist.add("ON");
            statelist.add(lightList.get(b).getOn());
        }
        final ArrayList colorlist = new ArrayList<>();
        for (int c = 0; c < lightList.size(); c++) {
//            colorlist.add("Red");
            colorlist.add(lightList.get(c).getHue());
        }
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



}
