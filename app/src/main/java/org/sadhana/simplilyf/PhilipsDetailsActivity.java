package org.sadhana.simplilyf;

import android.app.DialogFragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class PhilipsDetailsActivity extends AppCompatActivity implements ColorCustomDialog.EditDialogListener{


    final String SERVER = new Config().getPHILIPS_IP_ADDRESS() + "/light";
    private EditText mlightStatus;
  //  private Button mOnButton;
   // private Button mOffButton;
    private String position;
    private String lampName;
    private String lampColor;
    private String lampStatus;
    private Button mChangeColor;
    private ImageView mSwitch;
    private ImageView mLight;

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philips_details);
      //  mlightStatus=(EditText)findViewById(R.id.lightStatus_value);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setIcon(R.drawable.bulbon);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSwitch=(ImageView)findViewById(R.id.image_switch);
        mLight=(ImageView)findViewById(R.id.image_lamp);
        Intent intent=getIntent();
       position=  intent.getStringExtra("position Value");
        lampColor=intent.getStringExtra("lampColor");
        lampName=intent.getStringExtra("lampName");
        lampStatus=intent.getStringExtra("lampStatus");
        System.out.println("value from intent  " + position);
        System.out.println("color "+lampColor);
        System.out.println("name " + lampName);
        System.out.println("status " + lampStatus);
      //  new PhilipsDetailAsync().execute(position);
        displayDetails();
      //  mOnButton=(Button)findViewById(R.id.turnon);
        //mOffButton=(Button)findViewById(R.id.turnoff);
        mChangeColor=(Button)findViewById(R.id.chngebtn);
        mChangeColor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               // if(tagValue==1 && tagValue==2)
                if(lampStatus.equals("OFF")){
                    Toast.makeText(PhilipsDetailsActivity.this, "Please Turn ON Light First!", Toast.LENGTH_LONG).show();
                }
                if(lampStatus.equals("ON")) {
                    DialogFragment dialogFragment = ColorCustomDialog.newInstance();
                    dialogFragment.show(getFragmentManager(), "editMainDialog");
                }
            }
        });

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("get tag"+ mSwitch.getTag());
                int tagValue = (Integer)mSwitch.getTag();
                if(tagValue==1){
                    //button on
                    System.out.println("get tag"+ mSwitch.getTag()+"Button On");
                    lampStatus="ON";
                    new PhilipsLightONAsync().execute(position);
                }
                if(tagValue==2){
                    //button off
                    System.out.println("get tag"+ mSwitch.getTag()+"Button Off");
                    lampStatus="OFF";
                    new PhilipsLightOFFAsync().execute(position);
                }
            }
        });
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
        if(id== android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void displayDetails(){
        // checking lamp status
        if(lampStatus.equals("ON")){
            mSwitch.setImageResource(R.mipmap.button_off);
            playSound();
            mSwitch.setTag(Integer.valueOf(2));
            // set lamp color accordingly
            if(lampColor.equals("red")){
                System.out.println("in red " );
                mLight.setImageResource(R.drawable.red);
            }

            if(lampColor.equals("blue")){
                mLight.setImageResource(R.drawable.blue);
            }
            if(lampColor.equals("green")){
                mLight.setImageResource(R.drawable.green);
            }
            if(lampColor.equals("purple")){
                mLight.setImageResource(R.drawable.purple);
            }
            if(lampColor.equals("yellow")){
                mLight.setImageResource(R.drawable.bulbon);
            }

        }
        else if(lampStatus.equals("OFF")){
            mLight.setImageResource(R.drawable.bulboff);
            mSwitch.setImageResource(R.mipmap.button_on);
            mSwitch.setTag(Integer.valueOf(1));
            playSound();
        }
    }

    public class PhilipsLightOFFAsync extends AsyncTask<String, Void, PhilipsData> {

        @Override
        protected PhilipsData doInBackground(String... params) {
            InputStream inputStream = null;
            //int deviceNum=params[0];
            String deviceNum= SERVER+"/off/"+params[0];
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            PhilipsData msg=new PhilipsData();
            try {
                System.out.println("endpoint value "+deviceNum);
                System.out.println("Off Philips endpoint");
                /* forming th java.net.URL object */
                URL url = new URL(deviceNum);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("POST");
                //  List<NameValuePairs>
                int statusCode = urlConnection.getResponseCode();
                System.out.println("status code in off: " + statusCode);
                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    msg = new Gson().fromJson(response, PhilipsData.class);
                    //   parseResult(response);
                    System.out.println("Philips response....in off " +msg.getName()+" "+ msg.getState().getOn());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return msg;
        }

        @Override
        protected void onPostExecute(PhilipsData result) {
            System.out.println("RESULT: " + result);
            System.out.println("value of light status out of loop  "+result.getState().getOn());
            if(result.getState().getOn()==false){
                System.out.println("value of light status now  "+result.getState().getOn());
                mLight.setImageResource(R.drawable.bulboff);
                mSwitch.setImageResource(R.mipmap.button_on);
                playSound();
                mSwitch.setTag(Integer.valueOf(1));
            }

        }
    }

    public class PhilipsLightONAsync extends AsyncTask<String, Void, PhilipsData> {

        @Override
        protected PhilipsData doInBackground(String... params) {
            InputStream inputStream = null;
            //int deviceNum=params[0];
            String endpoint= SERVER+"/on/"+params[0];
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            PhilipsData msg=new PhilipsData();
            try {
                System.out.println("endpoint value "+endpoint);
                System.out.println("On Philips endpoint");
                /* forming th java.net.URL object */
                URL url = new URL(endpoint);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("POST");
                //  List<NameValuePairs>
                int statusCode = urlConnection.getResponseCode();
                System.out.println("status code in on: " + statusCode);
                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    msg = new Gson().fromJson(response, PhilipsData.class);
                    //   parseResult(response);
                    System.out.println("Philips response....in on " +msg.getName()+" "+ msg.getState().getOn());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return msg;
        }

        @Override
        protected void onPostExecute(PhilipsData result) {
            System.out.println("RESULT: " + result);
            // System.out.println("value of light status"+result.getState().getOn());
            if(result.getState().getOn()==true){
                System.out.println("value of light status now  "+result.getState().getOn());
                mLight.setImageResource(R.drawable.bulbon);
                mSwitch.setImageResource(R.mipmap.button_off);
                playSound();
                mSwitch.setTag(Integer.valueOf(2));
            }


        }
    }

    public class PhilipsColorChange extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            InputStream inputStream = null;
            //int deviceNum=params[0];
            String endpoint= SERVER+"/color/"+params[0]+"/"+params[1];
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            PhilipsData msg=new PhilipsData();
            try {
                System.out.println("endpoint value "+endpoint);
                System.out.println("color change Philips endpoint");
                /* forming th java.net.URL object */
                URL url = new URL(endpoint);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("POST");
                //  List<NameValuePairs>
                int statusCode = urlConnection.getResponseCode();
                System.out.println("status code in change: " + statusCode);
                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
//                    msg = new Gson().fromJson(response, PhilipsData.class);
//                    //   parseResult(response);
//                    System.out.println("Philips response....in off " +msg.getName()+" "+ msg.getState().getOn());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("RESULT: " + result);

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

    private void playSound() {
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.lightswitch);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
            }
        });
    }

    public void updateResult(String inputText) {
        result = inputText;
        System.out.println("fnal result " + result);
        if(result.equals("red")){
            System.out.println("in red " );
            new PhilipsColorChange().execute(position,result);
            mLight.setImageResource(R.drawable.red);
        }

        if(result.equals("blue")){
            new PhilipsColorChange().execute(position,result);
            mLight.setImageResource(R.drawable.blue);
        }
        if(result.equals("green")){
            new PhilipsColorChange().execute(position,result);
            mLight.setImageResource(R.drawable.green);
        }
        if(result.equals("purple")){
            new PhilipsColorChange().execute(position,result);
            mLight.setImageResource(R.drawable.purple);
        }
        if(result.equals("yellow")){
            new PhilipsColorChange().execute(position,result);
            mLight.setImageResource(R.drawable.bulbon);
        }



    }
}
