package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class NestdevicesActivity extends ActionBarActivity {

    private ImageButton mLivingrm;
    private ImageButton mBedrm;
    private ImageButton mKitchen;
    public String content=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestdevices);

        mLivingrm=(ImageButton)findViewById(R.id.livingroombtn);
        mLivingrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NestdevicesActivity.this,NestLivingrmActivity.class);
                String roomName="livingroom";
                i.putExtra("VALUE_SENT", roomName);
                //obtainTemp();
               new HttpRequestTask().execute();
                System.out.print("Starting Intent");
                startActivity(i);
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

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            System.out.println("DoInBackground method");
            try {
                final String url = "http://10.189.146.141:3000/hello";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                content = restTemplate.getForObject(url, String.class);
                System.out.println("after hitting URL");
                System.out.println("Value of greeting...."+content);
                return content;
            } catch (Exception e) {
                Log.e("LoginActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String greeting) {

            //TextView greetingContentText = (TextView) findViewById(R.id.content_value);
            System.out.println("Value of CONTENT...." + content);
           // greetingContentText.setText(content);
        }

    }


//   private class HttpRequestTask extends AsyncTask<Void, Void, Void> {
//       @Override
//       protected void doInBackground(Void... params) {
//           System.out.println("DoInBackground method");
//           final String url = "http://10.189.114.192:3000/thermo/addNew";
//           try {
//               HttpHeaders requestHeaders = new HttpHeaders();
//               requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
//               HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
//               RestTemplate restTemplate = new RestTemplate();
//               restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//               ResponseEntity<ThermoDO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ThermoDO.class);
//               ThermoDO users = responseEntity.getBody();
//               System.out.println("Value of content in onPostExecute()...." + users.getTarget_temperature_f());
//              // return users;
//           }
//           catch (Exception e) {
//               System.out.println("Error: " + e);
//              // return null;
//           }
//
//       }
//
//       @Override
//       protected void onPostExecute(Void thermoDO) {
//
//           //  TextView greetingContentText = (TextView) findViewById(R.id.content_value);
//           System.out.println("Value of content in onPostExecute()...." );
//           //greetingContentText.setText(content);
//       }
//
//   }


}
