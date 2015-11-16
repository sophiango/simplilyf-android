package org.sadhana.simplilyf;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class TempUpdateDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private double targetTemperature = 0.0;
    private boolean hasChanged = false;
    final String SERVER = "http://172.16.1.9:3000";
    private String roomName = "";
    private Button allThermos;

    public static TempUpdateDialog newInstance(String roomName, double targetTemperature) {
        TempUpdateDialog TempUpdateDialog = new TempUpdateDialog();
        Bundle args = new Bundle();
        args.putDouble("targetTemperature", targetTemperature);
        args.putString("roomName", roomName);
        TempUpdateDialog.setArguments(args);

        return TempUpdateDialog;
    }

    public static interface OnCompleteListener {

        public abstract void onComplete(double targetTemperature);
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        targetTemperature = getArguments().getDouble("targetTemperature");
        roomName = getArguments().getString("roomName");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        System.out.println("CLICK!");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // getDialog().setTitle("Temperature editing");
        final View v = inflater.inflate(R.layout.temp_setting, null);
        final CircleView editTarget = (CircleView) v.findViewById(R.id.edit_target_circleView);
        editTarget.setTitleText(targetTemperature + "");


        final VerticalSeekBar seekBar = (VerticalSeekBar) v.findViewById(R.id.vertical_Seekbar);
        seekBar.setMax(1000);

        seekBar.setProgress((int) targetTemperature*10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hasChanged = true;
                targetTemperature = (progress / 10.0) + 5;
//                try {
//                    MainActivity.thermostat.setManualTemperatureValue(targetTemperature);
//
//                    System.out.println(targetTemperature);
//                    MainActivity.thermostat.setManualMode(true);
//
//                } catch (Exception e) {
//
//                }
                editTarget.setTitleText(targetTemperature + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button editBtn = (Button) v.findViewById(R.id.edit_main_dialog_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double temp = (seekBar.getProgress() / 10.0);
                if (hasChanged) {
                    targetTemperature = (seekBar.getProgress() / 10.0) + 5;
                } else {
                    targetTemperature = temp;
                }
                try {

                    new ChangeTempAsync().execute(roomName, Double.toString(targetTemperature));
//                    NestLivingrmActivity.thermostat.setCurrentTemp(targetTemperature);
//                    System.out.println(targetTemperature);

                } catch (Exception e) {
                    System.out.println(e);
                }
                mListener.onComplete(targetTemperature);
                dismiss();
            }
        });

        Button cancelBtn = (Button) v.findViewById(R.id.cancel_main_dialog_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        allThermos=(Button)v.findViewById(R.id.allthermosBtn);
        allThermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Async task for all thermos
                System.out.println("All thermos");
            }
        });

        return v;
    }

    public static double roundToDecimals(double d, int c) {
        int temp = (int) ((d * Math.pow(10, c)));
        return (((double) temp) / Math.pow(10, c));
    }

    public class ChangeTempAsync extends AsyncTask<String,Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            StringBuilder reply = new StringBuilder();
            ThermoList thermoList = null;
            try {
                System.out.println("ADD NEW THERMO ENDPOINT");
                String changeTempUrl = SERVER + "/thermo";
                URL url = new URL(changeTempUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true); // accept request body
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
            return targetTemperature;
        }

        @Override
        protected void onPostExecute(Double temp) {
            NestLivingrmActivity.nestData.setCurrentTemp(temp);
            System.out.println("CURRENT: " + NestLivingrmActivity.nestData.getCurrentTemp());
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
