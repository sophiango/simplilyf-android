package org.sadhana.simplilyf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class UserRegistrationActivity extends ActionBarActivity {

    private EditText mFullName;
    private EditText mEmail;
    private EditText mUsrName;
    private EditText mPwd;
    private Button mRegister;

    private String inputFullName = null;
    private String inputEmail = null;
    private String inputUsername = null;
    private String inputPW = null;

    final String SERVER = "https://10.189.48.129:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mFullName = (EditText) findViewById(R.id.reg_fullname);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mUsrName = (EditText) findViewById(R.id.user_name);
        mPwd = (EditText) findViewById(R.id.reg_password);
        mRegister = (Button) findViewById(R.id.btnRegister);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputFullName = mFullName.getText().toString();
                inputEmail = mEmail.getText().toString();
                inputUsername = mUsrName.getText().toString();
                inputPW = mPwd.getText().toString();
                //System.out.println("Clicked register!!!!");
                //System.out.println("input: " + inputUsername + inputPW + inputEmail + inputFullName);
                new PostUserInfoAsync().execute(inputUsername, inputPW, inputEmail, inputFullName);
            }
        });
    }

    private class PostUserInfoAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            try {
                System.out.println("REGISTER ENDPOINT");
                /* forming th java.net.URL object */
                URL url = new URL(SERVER + "/signup");
                urlConnection = (HttpURLConnection) url.openConnection();
                 /* optional request header */
                //urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true); // accept request body
                //urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");

                /* for Get request */
                //  List<NameValuePairs>
                //you need to encode ONLY the values of the parameters
//                String param="username" + URLEncoder.encode(params[0],"UTF-8")+
//                "&password="+URLEncoder.encode(params[1],"UTF-8")+
//                "&email="+URLEncoder.encode(params[2],"UTF-8")+
//                "&fullname="+URLEncoder.encode(params[3],"UTF-8");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", params[0]);
                jsonParam.put("password", params[1]);
                jsonParam.put("email", params[2]);
                jsonParam.put("fullname", params[3]);
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

                /* 200 represents HTTP OK */
//                if (statusCode == 200) {
//                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                    String response = convertInputStreamToString(inputStream);
//                    //   parseResult(response);
//
//                    System.out.println("Value of response...." + response);
//                    result = 1; // Successful
//                } else {
//                    result = 0; //"Failed to fetch data!";
//                }
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("Value of content in onPostExecute()....");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_registration, menu);
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
