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
import com.example.gb.exampreparation.FileLoader.QuestionQoSLoader;

import java.util.ArrayList;


public class QuestionnerActivity extends Activity implements View2Activity {

    private static final String TAG = "QuestionnerActivity";
    private static final String STATE_QUESTION_ID = "question_id";
    private static final String STATE_NTA_QUESTION_ID = "nta_question_id";

    private Activity2Model mMod;

    private Button mBtnTrue;
    private Button mBtnFalse;
    private Button mBtnShow;
    private TextView mTxtQuestion;
    private TextView mTxtAnswer;
    private TextView mTxtNbNTAQuestion;
    private TextView mTxtNbTotQuestion;
    private RatingBar mNbCorrectAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questionner);

        ConstanteQuestion.MAX_ASK = this.getResources().getInteger(R.integer.numberOfStars);

        restoreModel(savedInstanceState);

        Intent i = this.getIntent();
        byte param = i.getByteExtra(QuestionnerLauncherActivity.EXTRA,QuestionnerLauncherActivity.UNKNOWN);

        if (savedInstanceState == null) {
            if (param == QuestionnerLauncherActivity.RELOAD) {
                ArrayList<Question> qs = QuestionQoSLoader.retrieveQuestionsFromFile("/sdcard/DCIM/App/questionQoS.csv");
                if (qs.size() == 0) {
                    Toast.makeText(this, this.getString(R.string.errorLoadingFile), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, this.getString(R.string.loadFile), Toast.LENGTH_SHORT).show();
                    mMod.reloadDBWithQuestion(qs);
                }
            } else if (param == QuestionnerLauncherActivity.RESET) {
                Toast.makeText(this, this.getString(R.string.resetQuestions), Toast.LENGTH_SHORT).show();
                mMod.resetAllQuestionsNbCorrect();
            } else if (param == QuestionnerLauncherActivity.CONTINUE) {
                Toast.makeText(this, this.getString(R.string.continuingLastTry), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, this.getString(R.string.unknownChoice), Toast.LENGTH_SHORT).show();
            }
        }


        mBtnFalse = (Button) findViewById(R.id.btnFalse);
        mBtnTrue = (Button) findViewById(R.id.btnTrue);
        mBtnShow = (Button) findViewById(R.id.btnShow);

        mTxtQuestion = (TextView) findViewById(R.id.txtQuestion);
        mTxtAnswer = (TextView) findViewById(R.id.txtAnswer);
        mTxtNbNTAQuestion = (TextView) findViewById(R.id.txtNbNTAQuestion);
        mTxtNbTotQuestion = (TextView) findViewById(R.id.txtNbTotQuestion);

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

        mNbCorrectAns.setIsIndicator(true);

        setQuestion();
        setNbCorrectAns();
        setTotNbQuestions();
        setNbNTAQuestions();
    }

    /**
     * Restore the model from the saved instance.
     * If there is no saved instance then create a new model and set the next question
     * @param savedInstanceState
     */
    private void restoreModel(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int qId = savedInstanceState.getInt(STATE_QUESTION_ID,-1);
            int qNtaId = savedInstanceState.getInt(STATE_NTA_QUESTION_ID,-1);
            if (qId != -1 && qNtaId != -1) {
                mMod = new Model(this,qNtaId,qId);
            }
            else {
                Toast.makeText(this,this.getString(R.string.errorRetreivingState),Toast.LENGTH_SHORT).show();
                mMod = new Model(this);
                mMod.nextQuestion();
            }
        }
        else {
            mMod = new Model(this);
            mMod.nextQuestion();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //remember id we were at
        outState.putInt(STATE_QUESTION_ID,mMod.getQuestionId());
        outState.putInt(STATE_NTA_QUESTION_ID,mMod.getNTAQuestionId());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void processShowAnswer() {
        setAnswer(mMod.getAnswer());
    }

    @Override
    public void processUserWasRight() {
        processUserCorrectness(true);
        setNbNTAQuestions();
    }

    @Override
    public void processUserWasWrong() {
        processUserCorrectness(false);
    }

    private void processUserCorrectness(boolean correct) {
        mMod.rememberUserWasCorrect(correct);
        mMod.nextQuestion();
        setNbCorrectAns();
        setQuestion();
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


    private void setNbNTAQuestions() {
        int nbNTAQ= mMod.getNumberOfNonTotallyAnsweredQuestions();
        mTxtNbNTAQuestion.setText(String.valueOf(nbNTAQ));
    }

    private void setTotNbQuestions() {
        int nbQ= mMod.getTotalNumberOfQuestions();
        mTxtNbTotQuestion.setText(String.valueOf(nbQ));
    }
}
