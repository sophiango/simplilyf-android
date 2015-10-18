package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NestdevicesActivity extends ActionBarActivity {

    private Button mViewDevice;
    private ImageButton mBedrm;
    private ImageButton mKitchen;
    public String content=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestdevices);

        mViewDevice=(Button)findViewById(R.id.viewDevices);
        mViewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NestdevicesActivity.this,NestLivingrmActivity.class);
                String roomName="livingroom";
                i.putExtra("VALUE_SENT", roomName);
                //obtainTemp();
               new AsyncHttpTask().execute();
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

//    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... params) {
//            System.out.println("DoInBackground method");
//            try {
//                final String url = "http://10.189.114.192:3000/hello";
//                //RestTemplate restTemplate = new RestTemplate();
//                //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//                //content = restTemplate.getForObject(url, String.class);
//                System.out.println("after hitting URL");
//                System.out.println("Value of greeting...."+content);
//                return content;
//            } catch (Exception e) {
//                Log.e("LoginActivity", e.getMessage(), e);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String greeting) {
//
//            //TextView greetingContentText = (TextView) findViewById(R.id.content_value);
//            System.out.println("Value of CONTENT...." + content);
//           // greetingContentText.setText(content);
//        }
//
//    }

    public class AsyncHttpTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            try {
                /* forming th java.net.URL object */
                URL url = new URL("http://10.189.114.192:3000/hello");
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    //   parseResult(response);

                    System.out.println("Value of response...." +response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                System.out.print("error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Value of content in onPostExecute()...." +result);
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        System.out.println("result value"+result );
        return result;
    }

//    private void parseResult(String result) {
//        try{
//            JSONObject response = new JSONObject(result);
//            JSONArray posts = response.optJSONArray("posts");
//            blogTitles = new String[posts.length()];
//
//            for(int i=0; i< posts.length();i++ ){
//                JSONObject post = posts.optJSONObject(i);
//                String title = post.optString("title");
//                blogTitles[i] = title;
//            }
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }


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
