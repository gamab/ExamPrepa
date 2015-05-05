package com.example.gb.exampreparation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gb.exampreparation.Model.Activity2Model;
import com.example.gb.exampreparation.Model.ConstanteQuestion;
import com.example.gb.exampreparation.Model.Model;
import com.example.gb.exampreparation.Model.Question;
import com.example.gb.exampreparation.Revision.QuestionQoSLoader;

import java.util.ArrayList;


public class QuestionnerActivity extends Activity implements View2Activity {

    private static final String TAG = "QuestionnerActivity";

    private Activity2Model mMod;

    private Button mBtnTrue;
    private Button mBtnFalse;
    private Button mBtnShow;
    private TextView mTxtQuestion;
    private TextView mTxtAnswer;
    private RatingBar mNbCorrectAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questionner);

        ConstanteQuestion.MAX_ASK = this.getResources().getInteger(R.integer.numberOfStars);

        Intent i = this.getIntent();
        byte param = i.getByteExtra(QuestionnerLauncherActivity.EXTRA,QuestionnerLauncherActivity.UNKNOWN);

        mMod = new Model(this);

        if (param == QuestionnerLauncherActivity.RELOAD) {
            ArrayList<Question> qs = QuestionQoSLoader.retrieveQuestionsFromFile("/sdcard/DCIM/App/questionQoS.csv");
            if (qs.size() == 0) {
                Toast.makeText(this,this.getString(R.string.errorLoadingFile),Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,this.getString(R.string.loadFile),Toast.LENGTH_SHORT).show();
                mMod.reloadDBWithQuestion(qs);
            }
        }
        else if (param == QuestionnerLauncherActivity.RESET) {
            Toast.makeText(this,this.getString(R.string.resetQuestions),Toast.LENGTH_SHORT).show();
            mMod.resetAllQuestionsNbCorrect();
        }
        else if (param == QuestionnerLauncherActivity.CONTINUE) {
            Toast.makeText(this,this.getString(R.string.continuingLastTry),Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,this.getString(R.string.unknownChoice),Toast.LENGTH_SHORT).show();
        }


        mBtnFalse = (Button) findViewById(R.id.btnFalse);
        mBtnTrue = (Button) findViewById(R.id.btnTrue);
        mBtnShow = (Button) findViewById(R.id.btnShow);

        mTxtQuestion = (TextView) findViewById(R.id.txtQuestion);
        mTxtAnswer = (TextView) findViewById(R.id.txtAnswer);

        mNbCorrectAns = (RatingBar) findViewById(R.id.ratbNbFound);

        mBtnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUserWasWrong();
            }
        });

        mBtnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUserWasRight();
            }
        });

        mBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processShowAnswer();
            }
        });

        setQuestion();

        mNbCorrectAns.setIsIndicator(true);
        setNbCorrectAns();
    }

    @Override
    public void processShowAnswer() {
        setAnswer(mMod.getAnswer());
    }

    @Override
    public void processUserWasRight() {
        processUserCorrectness(true);
    }

    @Override
    public void processUserWasWrong() {
        processUserCorrectness(false);
    }

    private void processUserCorrectness(boolean correct) {
        mMod.rememberUserWasCorrect(correct);
        mMod.nextQuestion();
        setQuestion();
        setNbCorrectAns();
        setAnswer(" ");
    }

    private void setQuestion() {
        String q = mMod.getQuestion();
        if (q == null) {
            q = this.getString(R.string.noMoreQuestions);
        }
        mTxtQuestion.setText(q);
    }


    private void setAnswer(String ans) {
        if (ans == null) {
            ans = this.getString(R.string.noMoreAnswers);
        }
        mTxtAnswer.setText(ans);
    }


    private void setNbCorrectAns() {
        int nbCorrect = mMod.getNbCorrectAns();
        if (nbCorrect == -1) {
            nbCorrect = ConstanteQuestion.MAX_ASK;
        }
        mNbCorrectAns.setRating((float) nbCorrect);
    }
}
