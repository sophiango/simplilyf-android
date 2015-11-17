package org.sadhana.simplilyf;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/* Created by Prajakta Naik */

public class VoicemoduleActivity extends Activity implements OnClickListener, OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private static final int REQ_CODE_SPEECH_INPUT_LIGHT_SEND =106 ;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    ArrayList<String> list = new ArrayList<String>();
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int REQ_CODE_SPEECH_INPUT_SECOND = 101;
    private final int REQ_CODE_SPEECH_INPUT_THIRD = 102;
    private final int REQ_CODE_SPEECH_INPUT_SEND_TEMP=103;
    private final int REQ_CODE_SPEECH_INPUT_CHECK_DEVICE=104;
    private final int REQ_CODE_SPEECH_INPUT_CONFIRM_DEVICE=106;
    private final int REQ_CODE_SPEECH_INPUT_ASK_ROOM=105;
    private final int REQ_CODE_SPEECH_INPUT_CONFIRM_ROOM=107;
    private final int REQ_CODE_SPEECH_INPUT_SET_AWAY=108;
    //variable for checking TTS engine data on user device
    private final int MY_DATA_CHECK_CODE = 0;
    private int mic_flag=0;
    private String user_speech="";
    private String confirm_device="";
    private String confirm_room="";
    private String temp_thermostat="";
    private TextToSpeech repeatTTS;
    final String SERVER = "http://10.189.146.45:3000";
    private String current_device="";


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

    private void call_new_intent(String input) {
            Intent intent_new = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent_new.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent_new.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent_new.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.interative_prompt)); //say yes or no
            mic_flag = 2;// first time mic opened set flag to 1
            try {
                if(input.equalsIgnoreCase("done")){
                    startActivityForResult(intent_new, REQ_CODE_SPEECH_INPUT_SECOND);
                }
                if(input.equalsIgnoreCase("confirm_temp")){
                    startActivityForResult(intent_new, REQ_CODE_SPEECH_INPUT_SEND_TEMP);
                }
                if(input.equalsIgnoreCase("confirm_device")){
                    startActivityForResult(intent_new, REQ_CODE_SPEECH_INPUT_CONFIRM_DEVICE);
                }
                if(input.equalsIgnoreCase("confirm_room")){
                    startActivityForResult(intent_new, REQ_CODE_SPEECH_INPUT_CONFIRM_ROOM);
                }
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
    }

    private void call_final_intent(String input) {
        Intent intent_final = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_final.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent_final.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                mic_flag = 3;// third time mic opened set flag to 3
        try {
            if(input.equalsIgnoreCase("ask_temp")) {
                startActivityForResult(intent_final, REQ_CODE_SPEECH_INPUT_THIRD);
            }
            if(input.equalsIgnoreCase("ask_room")) {
                startActivityForResult(intent_final, REQ_CODE_SPEECH_INPUT_ASK_ROOM);
            }
            if (input.equalsIgnoreCase("ask_device")){
                startActivityForResult(intent_final, REQ_CODE_SPEECH_INPUT_CHECK_DEVICE);
            }
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
                    repeatTTS.speak("Did you say " + user_speech, TextToSpeech.QUEUE_FLUSH, myHash);
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
                        repeatTTS.speak("Ok wait a moment", TextToSpeech.QUEUE_FLUSH, null);
                        String recieved_key =check_in_database(user_speech);
                        HashMap<String, String> myHash = new HashMap<String, String>();
                        if(recieved_key.equalsIgnoreCase("set_away")){
                            //call the api of thermostat and light to turn off
                            repeatTTS.speak("Okay set all the devices to away mode",TextToSpeech.QUEUE_FLUSH,myHash);

                        }
                        if(recieved_key.equalsIgnoreCase("set_home")){
                            //call the api of thermostat and light to turn on
                            repeatTTS.speak("Okay set all the devices to home mode",TextToSpeech.QUEUE_FLUSH,myHash);
                        }
                        if(recieved_key.equalsIgnoreCase("turn_on")){
                            myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_device");
                            repeatTTS.speak("Do you want to turn on the thermostat or the light",TextToSpeech.QUEUE_FLUSH,myHash);
                        }
                     if(recieved_key.equalsIgnoreCase("increase_temp")||recieved_key.equalsIgnoreCase("decrease_temp")){
                         current_device="thermostat";
                         myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_room");
                         repeatTTS.speak("Which room do you want to set the thermostat to", TextToSpeech.QUEUE_FLUSH, myHash);

                     }else if(recieved_key.equalsIgnoreCase("on_lights")){
                            repeatTTS.speak("Turning on the lights",TextToSpeech.QUEUE_FLUSH,myHash);
                         //call the light api to turn on lights
                        }else if(recieved_key.equalsIgnoreCase("off_lights")){
                            repeatTTS.speak("Turning off the lights",TextToSpeech.QUEUE_FLUSH,myHash);
                         //call the light api to turn off the lights
                        }else{
                        repeatTTS.speak("Sorry, did not get you.Please say again", TextToSpeech.QUEUE_FLUSH, null);
                        }

                    }else if(recieved_key.equalsIgnoreCase("change_color")){
                        current_device="light";
                        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_room");
                        repeatTTS.speak("Which room do you want to change the light color in", TextToSpeech.QUEUE_FLUSH, myHash);
                    }
                    else if("no".equalsIgnoreCase(text_output)){
                        repeatTTS.speak("Ok please say again what you want", TextToSpeech.QUEUE_FLUSH, null);
                    }else{
                        repeatTTS.speak("Sorry, did not get you.Please say again", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                break;
            }

            case REQ_CODE_SPEECH_INPUT_CHECK_DEVICE:{ //ask device
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    confirm_device=result.get(0);
                    HashMap<String, String> myHash = new HashMap<String, String>();
                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "confirm_device");
                    repeatTTS.speak("Did you say " + confirm_device, TextToSpeech.QUEUE_FLUSH, myHash);
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_CONFIRM_DEVICE:{ //confirm device
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String text_output=result.get(0);
                    HashMap<String, String> myHash = new HashMap<String, String>();

                    if("yes".equalsIgnoreCase(text_output)||"yeah".equalsIgnoreCase(text_output)){
                        //send the text_output to the thermostat api
                        if(confirm_device.equalsIgnoreCase("thermostat")||confirm_device.equalsIgnoreCase("thermo")||confirm_device.equalsIgnoreCase("the thermostat")){
                            current_device="thermostat";
                            myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_room");
                            repeatTTS.speak("Turning on the thermostat, which room do you want to set",TextToSpeech.QUEUE_FLUSH,myHash);
                        }else if(confirm_device.equalsIgnoreCase("light")||confirm_device.equalsIgnoreCase("lights")||confirm_device.equalsIgnoreCase("the lights")||confirm_device.equalsIgnoreCase("the light")){
                            current_device="light";
                            repeatTTS.speak("Turning on the lights",TextToSpeech.QUEUE_FLUSH,myHash);
                            //call the light api to switch on
                        }else{
                            myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_device");
                            repeatTTS.speak("Sorry, did not get you.Please say again", TextToSpeech.QUEUE_FLUSH, myHash);
                        }
                    }else if("no".equalsIgnoreCase(text_output)||"nope".equalsIgnoreCase(text_output)){
                        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_device");
                        repeatTTS.speak("Ok,please tell us again what to switch on thermostat or light bulb",TextToSpeech.QUEUE_ADD,myHash);
                    }else{
                        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_device");
                        repeatTTS.speak("Sorry, did not get you.Please say again", TextToSpeech.QUEUE_FLUSH, myHash);
                    }
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_ASK_ROOM:{ //ask room
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    confirm_room=result.get(0);
                    HashMap<String, String> myHash = new HashMap<String, String>();
                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "confirm_room");
                    repeatTTS.speak("Did you say " + confirm_room, TextToSpeech.QUEUE_FLUSH, myHash);
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_CONFIRM_ROOM:{ //confirm room
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String text_output=result.get(0);
                    HashMap<String, String> myHash = new HashMap<String, String>();

                    if("yes".equalsIgnoreCase(text_output)||"yeah".equalsIgnoreCase(text_output)){
                        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_temp");
                        //check the database for keyword everything
                        String recieved_key =check_in_database(user_speech);
                        if(recieved_key.equalsIgnoreCase("set_all")){
                            //call the all api to set temperature everything
                        }
                        repeatTTS.speak("Turning on the thermostat,curent temperatur is"+/*add the current temperature*/"what temperature you want to set",TextToSpeech.QUEUE_FLUSH,myHash);

                    }else if("no".equalsIgnoreCase(text_output)||"nope".equalsIgnoreCase(text_output)){
                        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_room");
                        repeatTTS.speak("Ok,please tell us again which room to set the thermostat temperature",TextToSpeech.QUEUE_FLUSH,myHash);
                    }else{
                        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_room");
                        repeatTTS.speak("Sorry, did not get you.Please say again", TextToSpeech.QUEUE_FLUSH, myHash);
                    }
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_THIRD: { //ask temperature
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    temp_thermostat=result.get(0);
                    HashMap<String, String> myHash = new HashMap<String, String>();
                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "confirm_temp");
                    repeatTTS.speak("Did you say " + temp_thermostat, TextToSpeech.QUEUE_FLUSH, myHash);
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_SEND_TEMP:{ //confirm temperature
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String text_output=result.get(0);
                    HashMap<String, String> myHash = new HashMap<String, String>();

                   if("yes".equalsIgnoreCase(text_output)||"yeah".equalsIgnoreCase(text_output)){
                       //send the text_output to the thermostat api
                       new ChangeTempAsync().execute(confirm_room,temp_thermostat);
                       repeatTTS.speak("Thankyou,set the temperature to"+temp_thermostat,TextToSpeech.QUEUE_FLUSH,null);

                   }else if("no".equalsIgnoreCase(text_output)||"nope".equalsIgnoreCase(text_output)){
                       myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ask_temp");
                    repeatTTS.speak("Ok,Please tell the temperature you want to set again",TextToSpeech.QUEUE_ADD,myHash);
                   }else{
                       myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "confirm_temp");
                       repeatTTS.speak("Sorry, did not get you.Please say again", TextToSpeech.QUEUE_FLUSH, myHash);
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

    public class ChangeTempAsync extends AsyncTask<String,Void, String>{

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            ThermoList thermoList = null;
            try {
                System.out.println("ADD NEW THERMO ENDPOINT");
                /* forming th java.net.URL object */
                String changeTempUrl = SERVER + "/thermo";
                URL url = new URL(changeTempUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                 /* optional request header */
                //urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true); // accept request body
                //urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("PUT");
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("thermo_name", params[0]);
                jsonParam.put("updated_temp", params[1]);
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
                int chr;
                while ((chr = in.read()) != -1) {
                    reply.append((char) chr);
                }
                System.out.println("received: " + reply.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("on post execute: " + s);
        }
    }

    public class VoiceAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection =null;
            Integer result =0;
            InputStream inputStream = null;
            StringBuilder reply = new StringBuilder();
            try{
                System.out.println("HELLO ENDPOINT");

                //String voice_url= "http://10.189.145.4:3000/voice/search";

                String voice_url= SERVER + "/voice/search";
                URL url= new URL(voice_url);
                urlConnection=(HttpURLConnection)url.openConnection();

            /*optional request headers*/
                urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestMethod("POST");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("command",params[0]);
                System.out.println("before post " + jsonParam.toString());
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
                return reply.toString();

            }catch(Exception e){
                Log.d("error", e.toString());
                return null; //if error then return null
            }finally {
                urlConnection.disconnect();
            }


        }
    }

        private String check_in_database(String text_output) {
            String recieved_key = null;
            try {
                recieved_key= new VoiceAsyncTask().execute(text_output).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return recieved_key;
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
            call_new_intent("done");
        }
        if(utteranceId.equalsIgnoreCase("ask_temp")){
            call_final_intent("ask_temp");
        }
        if(utteranceId.equalsIgnoreCase("ask_device")){
            call_final_intent("ask_device");
        }
        if(utteranceId.equalsIgnoreCase("ask_room")){
            call_final_intent("ask_room");
        }
        if(utteranceId.equalsIgnoreCase("confirm_temp")){
            System.out.print("ask the temperature");
            call_new_intent("confirm_temp");
        }
        if(utteranceId.equalsIgnoreCase("confirm_room")){
            System.out.print("room confirmed");
            call_new_intent("confirm_room");
        }
        if(utteranceId.equalsIgnoreCase("confirm_device")){
            System.out.print("ask the device thermostat or light bulb");
            call_new_intent("confirm_device");
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

}