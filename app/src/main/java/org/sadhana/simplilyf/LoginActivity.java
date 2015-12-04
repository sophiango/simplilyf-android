package org.sadhana.simplilyf;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    // endpoints
    final String SERVER = new Config().getIP_ADDRESS();

    public DeviceList   devicesList;
    private Button mLoginBtn;
    private EditText mUserName;
    private EditText mPassword;
    private TextView mRegisterlink;

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout,linlaHeaderProgress;
    private LinearLayout emailLayout;
    private LinearLayout pwdLayout;
    private LinearLayout btnLayout;

    private Button btnMydevices;
    private Button cancelBtn;

    private static String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName.setText(null);
                mPassword.setText(null);
            }
        });

        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userEmail = mUserName.getText().toString();
               // if(validate()) {
                    new PostUserInfoAsync().execute(userEmail, mPassword.getText().toString());
                //}
            }
        });

        mRegisterlink = (TextView) findViewById(R.id.link_register);
        mRegisterlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, UserRegistrationActivity.class);
                startActivity(i);
            }
        });
        mUserName = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private class PostUserInfoAsync extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            String output = null;
            User msg = new User();
            StringBuilder reply = new StringBuilder();
            User user = null;
            try {
                System.out.println("LOGIN ENDPOINT");
                /* forming th java.net.URL object */
                String endpoint = SERVER + "/signin";
                URL url = new URL(endpoint);
                urlConnection = (HttpURLConnection) url.openConnection();
                 /* optional request header */
                //urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true); // accept request body
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", params[0]);
                jsonParam.put("password", params[1]);
                // Set request header
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setUseCaches(false);

                // write body
                OutputStream wr = urlConnection.getOutputStream();
                wr.write(jsonParam.toString().getBytes("UTF-8"));
                int statusCode = urlConnection.getResponseCode();
                wr.close();
                System.out.println("status code " + statusCode);
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    msg = new Gson().fromJson(response, User.class);
                    System.out.println("Response: " + response + msg.getEmail());
                    //   parseResult(response);
                    System.out.println("Philips response...." + msg.getLights() + " ");
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
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(User result) {
            System.out.println("RESULT:in post execute in LoginActivity " + result);
            if (result!=null) {
                Intent i = new Intent(LoginActivity.this, ShowdevicesActivity.class);
                System.out.println("User: " + result + ' ' + result.getEmail());
                devicesList=new DeviceList(result.getEmail(),result.getUserId(),result.getUsername(),result.getLights(),result.getThermos());
               i.putExtra("devicesList", new Gson().toJson(devicesList));
                linlaHeaderProgress.setVisibility(View.GONE);
                       startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this,"Invalid login",Toast.LENGTH_LONG).show();
                linlaHeaderProgress.setVisibility(View.GONE);
            }
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

    public boolean validate() {
        boolean valid = true;


        String password = mPassword.getText().toString();


        if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            mUserName.setError("enter a valid email address");
            valid = false;
        } else {
            mUserName.setError(null);
        }

        if (password.isEmpty() ) {
            mPassword.setError("Please enter a password");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

}





// App code
             /*   System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("USER FIREST NAME"+Profile.getCurrentProfile().getFirstName());
                System.out.println("USER  NAME" + Profile.getCurrentProfile().getName());
               // System.out.println("USER email"+Profile.getCurrentProfile().);
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
                Intent i=new Intent(LoginActivity.this,UserProfileActivity.class);
                String token=loginResult.getAccessToken().getToken();
                i.putExtra("TOKEN_VALUE",token);
                startActivity(i);


                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                if (BuildConfig.DEBUG) {
                                    FacebookSdk.setIsDebugEnabled(true);
                                    FacebookSdk
                                            .addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

                                    System.out
                                            .println("AccessToken.getCurrentAccessToken()"
                                                    + AccessToken
                                                    .getCurrentAccessToken()
                                                    .toString());
                                    System.out.println("value of profile"+Profile.getCurrentProfile());
                                    if(Profile.getCurrentProfile()!=null) {
                                        Profile.getCurrentProfile().getId();
                                        Profile.getCurrentProfile().getFirstName();
                                        Profile.getCurrentProfile().getLastName();
                                        Profile.getCurrentProfile().getProfilePictureUri(50, 50);
                                    }
                                    //String email=UserManager.asMap().get(“email”).toString();
                                }
                            }
                        });
                request.executeAsync();
                */

