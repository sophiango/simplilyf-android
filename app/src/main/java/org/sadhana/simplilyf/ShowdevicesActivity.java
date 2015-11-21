package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;


public class ShowdevicesActivity extends ActionBarActivity {

    ListView list;
    String[] devicename ={
            "Thermostat",
            "Light"

    };

    Integer[] imgid={
            R.mipmap.thermometer,
            R.mipmap.bulb

    };
    private ImageButton mNestBtn;
    private ImageButton mPhilipsBtn;
    private ImageButton mMicBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdevices);
        String jsonMyObject=null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("devicesList");
        }
        DeviceList deviceObj = new Gson().fromJson(jsonMyObject, DeviceList.class);
        System.out.println("device object  "+deviceObj.getLights()+"get userName  "+deviceObj.getUsername()+"get userEmail "+deviceObj.getEmail());
        list=(ListView)findViewById(R.id.deviceList);
        DevicesCustomAdapter adapter=new DevicesCustomAdapter(ShowdevicesActivity.this,devicename,imgid);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Toast.makeText(ShowdevicesActivity.this, "Row " + position + " clicked", Toast.LENGTH_SHORT).show();
                // new PhilipsDetailAsync().execute(position+1);
                //// ListView Clicked item value
                String itemValue = (String) list.getItemAtPosition(position);
                System.out.println("value " + itemValue);
                System.out.println("position " + position);
                if(position==0){
                    Intent i = new Intent(ShowdevicesActivity.this, NestdevicesActivity.class);
                    startActivity(i);
                }
                if(position==1){
                    Intent i = new Intent(ShowdevicesActivity.this, PhilipsdevicesActivity.class);
                    startActivity(i);
                }
                //  String pos = Integer.toString(position + 1);
                // Intent i = new Intent(PhilipsdevicesActivity.this, PhilipsDetailsActivity.class);

            }
        });
        mMicBtn=(ImageButton)findViewById(R.id.micBtn);
        mMicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowdevicesActivity.this, VoicemoduleActivity.class);
                i.putExtra("MICPH_ID",R.id.micBtn);
                startActivity(i);
            }
        });

//        mNestBtn=(ImageButton)findViewById(R.id.nestBtn);
//        mNestBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ShowdevicesActivity.this, NestdevicesActivity.class);
//                startActivity(i);
//            }
//        });
//
//        mPhilipsBtn = (ImageButton) findViewById(R.id.philipsBtn);
//        mPhilipsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // new AsyncHttpTask().execute();
//                Intent i=new Intent(ShowdevicesActivity.this,PhilipsdevicesActivity.class);
//                startActivity(i);
//            }
//        });
        System.out.println("after loading adapter"+ list);
        list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showdevices, menu);
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
