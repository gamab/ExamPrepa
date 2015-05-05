package com.example.gb.exampreparation.Model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by gb on 01/05/15.
 */
public class Model implements Activity2Model {
    private static final String TAG = "MODEL";

    private Random mR;
    private DB mDb;
    private Question mCurQ;
    private int mCurId;
    private int mTotalNbrQ;

    public Model(Context ctxt) {

        mDb = new DB(ctxt);

        mR = new Random();

        //Let's get the next question so that mCur and mCurQ are set
        nextQuestion();
    }


    @Override
    public String getQuestion() {
        if (mCurQ != null) {
            return mCurQ.getQ();
        }
        else {
            return null;
        }
    }

    @Override
    public int getNbCorrectAns() {
        if (mCurQ != null) {
            return mCurQ.getNbrCorrect();
        }
        else {
            return -1;
        }
    }

    @Override
    public String getAnswer() {
        if (mCurQ != null) {
            return mCurQ.getAns();
        }
        else {
            return null;
        }
    }

    @Override
    public void rememberUserWasCorrect(boolean correct) {
        if (mCurQ != null) {
            if (correct) {
                int nb = mCurQ.getNbrCorrect();
                nb++;
                mCurQ.setNbrCorrect(nb);
                if (mTotalNbrQ != 0) {
                    mDb.setQuFNFSNbCorrect(mCurId, mCurQ.getNbrCorrect());
                }
            }
        }
    }

    @Override
    public void nextQuestion() {
        //Log.d(TAG, "\n==============================================================\n");
        int nextQId = mCurId;
        mTotalNbrQ = mDb.getNumberOfNFSQuestion();
        //Log.d(TAG,"Total question available " + mTotalNbrQ);
        if (mTotalNbrQ == 0) {
            mCurId = 0;
            mCurQ = null;
        }
        else {
            if (mTotalNbrQ == 1) {
                mCurId = 1;
            }
            else {
                while (nextQId == mCurId) {
                    nextQId = mR.nextInt(mTotalNbrQ) + 1;
                }
                mCurId = nextQId;
            }
            mCurQ = mDb.getQuestionFNFS(mCurId);
            //if (mCurQ == null) {
            //    Log.d(TAG, "Aie no more questions apparently for " + mCurId);
            //}
        }

        //mDb.printDB();
        //Log.d(TAG, "\n---\n");
        //mDb.printNFSDB();
    }

    @Override
    public void reloadDBWithQuestion(ArrayList<Question> qs) {
        mDb.deleteEverything();
        ListIterator<Question> lit = qs.listIterator();
        while (lit.hasNext()) {
            mDb.addQuestion(lit.next());
        }
    }

    @Override
    public void resetAllQuestionsNbCorrect() {
        mDb.resetAllQuestionsNbCorrect();
    }
}
