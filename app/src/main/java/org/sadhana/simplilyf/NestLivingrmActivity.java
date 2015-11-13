package org.sadhana.simplilyf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class NestLivingrmActivity extends ActionBarActivity {

    private EditText mTempValue;
    //private Button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_livingrm);
        mTempValue=(EditText)findViewById(R.id.temp_value);
        Intent i=getIntent();
        String res=i.getStringExtra("TEMP");

        System.out.println("res receuved"+ res);

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


//        String retrievedStr;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                retrievedStr= null;
//                System.out.println("VALUE OF RETRIEVED STRING1"+retrievedStr);
//            } else {
//                retrievedStr= (String)extras.get("TEMP");
//                System.out.println("VALUE OF RETRIEVED STRING2"+retrievedStr);
//                mTempValue.setText(retrievedStr);
//            }
//        } else {
//            retrievedStr= (String) savedInstanceState.getSerializable("VALUE_SENT");
//        }
//        System.out.println("VALUE OF RETRIEVED STRING"+retrievedStr);
