package org.sadhana.simplilyf;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Sadhana on 11/15/15.
 */
public class ColorCustomDialog extends DialogFragment {

    private String btnName;
    Button setBtn,cancelBtn,blueBtn,redBtn,purpleBtn,greenBtn,yellowBtn;

    static ColorCustomDialog newInstance() {
        return new ColorCustomDialog();
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         getDialog().setTitle("Choose a color");
        final View v = inflater.inflate(R.layout.colorchange_dialog, null);


         setBtn = (Button) v.findViewById(R.id.setColor);
        setBtn.setOnClickListener(setListener);

         cancelBtn = (Button) v.findViewById(R.id.cancelColor);
        cancelBtn.setOnClickListener(cancelListener);

         blueBtn = (Button)v.findViewById(R.id.btn_blue);
        blueBtn.setOnClickListener(blueBtnListener);

        redBtn = (Button)v.findViewById(R.id.btn_red);
        redBtn.setOnClickListener(redBtnListener);

        purpleBtn = (Button)v.findViewById(R.id.btn_purple);
        purpleBtn.setOnClickListener(purpleBtnListener);

        greenBtn = (Button)v.findViewById(R.id.btn_green);
        greenBtn.setOnClickListener(greenBtnListener);

        yellowBtn = (Button)v.findViewById(R.id.btn_yellow);
        yellowBtn.setOnClickListener(yellowBtnListener);


        return v;
    }

    private Button.OnClickListener blueBtnListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            btnName= blueBtn.getText().toString();
            System.out.println("btn text " + blueBtn.getText().toString() + "" + btnName);
         //   blueBtn.setTag(Integer.valueOf(20));
        }
    };


    private Button.OnClickListener redBtnListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            btnName= redBtn.getText().toString();
            System.out.println("btn text " + redBtn.getText().toString()+" "+btnName);
            //redBtn.setTag(Integer.valueOf(21));
        }
    };

    private Button.OnClickListener purpleBtnListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            btnName= purpleBtn.getText().toString();
            System.out.println("btn text " + purpleBtn.getText().toString()+" "+btnName);
           // purpleBtn.setTag(Integer.valueOf(22));
        }
    };

    private Button.OnClickListener greenBtnListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            btnName= greenBtn.getText().toString();
            System.out.println("btn text " + greenBtn.getText().toString()+" "+btnName);
           // greenBtn.setTag(Integer.valueOf(23));
        }
    };
    private Button.OnClickListener yellowBtnListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            btnName= yellowBtn.getText().toString();
            System.out.println("btn text " + yellowBtn.getText().toString()+" "+btnName);
            //yellowBtn.setTag(Integer.valueOf(24));
        }
    };


    private Button.OnClickListener setListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            EditDialogListener activity = (EditDialogListener) getActivity();

            activity.updateResult(btnName);
            dismiss();

        }
    };

    private Button.OnClickListener cancelListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            dismiss();
        }
    };

    public interface EditDialogListener {
        void updateResult(String inputText);
    }

}














//    @Override
//    public Dialog onCreateDialog(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        Button button1 = (Button) findViewById(R.id.btn_blue);
//        Button button2 = (Button) findViewById(R.id.btn_red);
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.colorchange_dialog, null))
//                // Add action buttons
//                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                        System.out.println("dialog item"+id+"  clicked");
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
//        return builder.create();
//    }
