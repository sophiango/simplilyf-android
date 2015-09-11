package org.sadhana.simplilyf;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class ShowdevicesActivity extends ActionBarActivity {

    private ImageButton mNestBtn;
    private ImageButton mPhilipsBtn;
    private ImageButton mMicBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdevices);

        mMicBtn=(ImageButton)findViewById(R.id.micBtn);
        mMicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowdevicesActivity.this, VoicemoduleActivity.class);
                startActivity(i);
            }
        });

        mNestBtn=(ImageButton)findViewById(R.id.nestBtn);
        mNestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ShowdevicesActivity.this,NestdevicesActivity.class);
                startActivity(i);
            }
        });

        mPhilipsBtn=(ImageButton)findViewById(R.id.philipsBtn);
        mPhilipsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ShowdevicesActivity.this,PhilipsdevicesActivity.class);
                startActivity(i);
            }
        });
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
