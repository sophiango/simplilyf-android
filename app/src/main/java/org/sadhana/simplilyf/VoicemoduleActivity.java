package org.sadhana.simplilyf;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* Created by Prajakta Naik */

public class VoicemoduleActivity extends Activity implements OnClickListener, OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    ArrayList<String> list = new ArrayList<String>();
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int REQ_CODE_SPEECH_INPUT_SECOND = 101;
    //variable for checking TTS engine data on user device
    private final int MY_DATA_CHECK_CODE = 0;
    private int mic_flag=0;
    private String user_speech="";
    private TextToSpeech repeatTTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicemodule);
        list.add("Turn off the thermostat");
        list.add("Turn on the thermostat");
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        //find out whether speech recognition is supported
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (intActivities.size() != 0) {
            //speech recognition is supported - detect user button clicks
            btnSpeak.setOnClickListener(this);
            //prepare the TTS to repeat chosen words
            Intent checkTTSIntent = new Intent();
//check TTS data
            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//start the checking Intent - will retrieve result in onActivityResult
            startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        }
        else
        {
            //speech recognition not supported, disable button and output message
            btnSpeak.setEnabled(false);
            Toast.makeText(this, "Oops - Speech recognition not supported!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Called when the user presses the speak button
     */
    public void onClick(View v) {
        if (v.getId() == R.id.btnSpeak) {
            //listen for result
            promptSpeechInput();
        }
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        mic_flag=1;// first time mic opened set flag to 1
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void call_new_intent() {
            Intent intent_new = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent_new.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent_new.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent_new.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.interative_prompt)); //say yes or no
            mic_flag = 2;// first time mic opened set flag to 1
            try {
                startActivityForResult(intent_new, REQ_CODE_SPEECH_INPUT_SECOND);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
    }
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    user_speech=result.get(0);
                    HashMap<String, String> myHash = new HashMap<String, String>();
                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "done");
                    repeatTTS.speak("Did you say " + user_speech, TextToSpeech.QUEUE_ADD, myHash);
                  /*  boolean tts_isspeaking=repeatTTS.isSpeaking();
                    do{
                        tts_isspeaking=repeatTTS.isSpeaking();
                    }while(tts_isspeaking); //be in the loop till the speaking*/


                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_SECOND: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String text_output=result.get(0);
                    if("yes".equalsIgnoreCase(text_output)){
                        repeatTTS.speak("Ok wait a moment let me check", TextToSpeech.QUEUE_FLUSH, null);
                        boolean ifexist=check_in_database(user_speech);
                        if(ifexist){
                            repeatTTS.speak("Ok, Turning on the thermostat", TextToSpeech.QUEUE_ADD, null);
                        }else{
                            repeatTTS.speak("Sorry, did not get you.Please say again", TextToSpeech.QUEUE_ADD, null);
                        }
                    }else if("no".equalsIgnoreCase(text_output)){
                        repeatTTS.speak("Ok please say again what you want", TextToSpeech.QUEUE_ADD, null);
                    }else{
                        repeatTTS.speak("Ok please say again what you want", TextToSpeech.QUEUE_ADD, null);
                    }
                }
                break;
            }

            case MY_DATA_CHECK_CODE: {
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
                    repeatTTS = new TextToSpeech(this, this);
                    //data not installed, prompt the user to install it
                else {
                    //intent will take user to TTS download page in Google Play
                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
                break;
            }


        }
    }



    private boolean check_in_database(String text_output) {
        for(String s: list){
            if(text_output.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voicemodule, menu);
        return true;
    }

    @Override
    public void onInit(int status) {
        repeatTTS.setOnUtteranceCompletedListener(this);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        if (utteranceId.equalsIgnoreCase("done")) {
            System.out.print("done speaking");
            call_new_intent();
        }

    }
}