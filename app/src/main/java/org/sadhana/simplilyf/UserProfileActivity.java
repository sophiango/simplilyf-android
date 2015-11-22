package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView profileInfo;
    private Button btnMydevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        String retrievedToken;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                retrievedToken= null;
                System.out.println("VALUE OF RETRIEVED STRING1"+retrievedToken);
            } else {
                retrievedToken= (String)extras.get("TOKEN_VALUE");
                System.out.println("VALUE OF RETRIEVED STRING2"+retrievedToken);
            }
        } else {
            retrievedToken= (String) savedInstanceState.getSerializable("VALUE_SENT");
        }
        System.out.println("VALUE OF RETRIEVED STRING" + retrievedToken);
        profileInfo=(TextView)findViewById(R.id.profile_info_textview);
             profileInfo.setText("Sahi Anil");

        btnMydevices=(Button)findViewById(R.id.myDevices);
        btnMydevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ShowdevicesActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
