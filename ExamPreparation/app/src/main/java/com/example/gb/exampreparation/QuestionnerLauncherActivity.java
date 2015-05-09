package com.example.gb.exampreparation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class QuestionnerLauncherActivity extends Activity {

    public static final String EXTRA = "ACTION";
    public static final byte CONTINUE = 1;
    public static final byte RESET = 2;
    public static final byte RELOAD = 3;
    public static final byte UNKNOWN = 4;
    //Action 1 is for Continue
    //Action 2 is for Reset of the number of stars for each question
    //Action 3 is for Reload of the question file

    private static final String TAG = "QuestionnerActivity";

    private Button mBtnReset;
    private Button mBtnReload;
    private Button mBtnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_questionner);



        mBtnContinue = (Button) findViewById(R.id.btnContinue);
        mBtnReset = (Button) findViewById(R.id.btnReset);
        mBtnReload = (Button) findViewById(R.id.btnReload);


        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuestionnerLauncherActivity.this, QuestionnerActivity.class);
                i.putExtra(EXTRA,CONTINUE);
                startActivity(i);
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuestionnerLauncherActivity.this, QuestionnerActivity.class);
                i.putExtra(EXTRA,RESET);
                startActivity(i);
            }
        });


        mBtnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuestionnerLauncherActivity.this, QuestionnerActivity.class);
                i.putExtra(EXTRA,RELOAD);
                startActivity(i);
            }
        });
    }

}
