package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UserRegistrationActivity extends ActionBarActivity {

    private EditText mFullName;
    private EditText mEmail;
    private EditText mUsrName;
    private EditText mPwd,mConfirmPwd;
    private Button mRegister;


    private String inputFullName = null;
    private String inputEmail = null;
    private String inputUsername = null;
    private String inputPW = null;

    final String SERVER = "http://192.168.1.8:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mFullName = (EditText) findViewById(R.id.reg_fullname);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mUsrName = (EditText) findViewById(R.id.user_name);
        mPwd = (EditText) findViewById(R.id.reg_password);
        mConfirmPwd=(EditText)findViewById(R.id.confirm_password);
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
                if(validate()) {
                    new PostUserInfoAsync().execute(inputUsername, inputPW, inputEmail, inputFullName);
                }
            }
        });
    }

    private class PostUserInfoAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            StringBuilder reply = new StringBuilder();
            try {
                System.out.println("REGISTER ENDPOINT");
                /* forming th java.net.URL object */
                String register_endpoint = SERVER + "/signup";
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
            return reply.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("RESULT: " + result);
            if (result!=null && result.equals("successful")) {
                Intent i = new Intent(UserRegistrationActivity.this, ShowdevicesActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(UserRegistrationActivity.this,"Unable to register the user",Toast.LENGTH_LONG).show();
            }
        }

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

    public boolean validate() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        String password = mPwd.getText().toString();
        String confirmPwd=mConfirmPwd.getText().toString();
        String userName=mUsrName.getText().toString();
        String fullName=mFullName.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("enter a valid email address");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            mPwd.setError("between 3 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPwd.setError(null);
        }
        if (confirmPwd.isEmpty() || !password.equals(confirmPwd)) {
            mConfirmPwd.setError("Re-check your password");
            valid = false;
        } else {
            mConfirmPwd.setError(null);
        }
        if (userName.isEmpty() ) {
            mUsrName.setError("Username shouldn't be empty");
            valid = false;
        } else {
            mUsrName.setError(null);
        }

        if (fullName.isEmpty() ) {
            mFullName.setError("FullName shouldn't be empty");
            valid = false;
        } else {
            mFullName.setError(null);
        }
        return valid;
    }
}
