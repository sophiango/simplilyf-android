package org.sadhana.simplilyf;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class NestLivingrmActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_livingrm);

        String retrievedStr;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                retrievedStr= null;
                System.out.println("VALUE OF RETRIEVED STRING1"+retrievedStr);
            } else {
                retrievedStr= (String)extras.get("VALUE_SENT");
                System.out.println("VALUE OF RETRIEVED STRING2"+retrievedStr);
            }
        } else {
            retrievedStr= (String) savedInstanceState.getSerializable("VALUE_SENT");
        }
        System.out.println("VALUE OF RETRIEVED STRING"+retrievedStr);


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

        return super.onOptionsItemSelected(item);
    }
}
