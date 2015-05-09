package com.example.gb.exampreparation.Model;

import android.content.Context;

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
    private int mNTAId;
    private int mTotalNbrQ;

    /**
     * Create a new model
     * @param ctxt
     */
    public Model(Context ctxt) {
        mDb = new DB(ctxt);

        mR = new Random();
    }

    /**
     * Recreate the model as it was
     * @param ctxt
     * @param ntaId the Not Totally Answered Question Id
     * @param questionId the real question Id
     */
    public Model(Context ctxt,int ntaId, int questionId) {
        mDb = new DB(ctxt);

        mR = new Random();

        mNTAId = ntaId;

        mCurQ = mDb.getQuestion(questionId);
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
    public int getQuestionId() {
        if (mCurQ != null) {
            return mCurQ.getId();
        }
        else {
            return -1;
        }
    }

    @Override
    public int getNTAQuestionId() {
        return mNTAId;
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
                    mDb.setQuestionNbCorrect(mCurQ.getId(), mCurQ.getNbrCorrect());
                    mDb.printDB();
                }
            }
        }
    }

    @Override
    public void nextQuestion() {
        //Log.d(TAG, "\n==============================================================\n");
        int nextQId = mNTAId;
        mTotalNbrQ = mDb.getNumberOfNTAQuestion();
        //Log.d(TAG,"Total question available " + mTotalNbrQ);
        if (mTotalNbrQ == 0) {
            mNTAId = 0;
            mCurQ = null;
        }
        else {
            if (mTotalNbrQ == 1) {
                mNTAId = 1;
            }
            else {
                while (nextQId == mNTAId) {
                    nextQId = mR.nextInt(mTotalNbrQ) + 1;
                }
                mNTAId = nextQId;
            }
            mCurQ = mDb.getQuestionFNTA(mNTAId);
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
        if (mCurQ != null) {
            mCurQ.setNbrCorrect(0);
        }
        mDb.resetAllQuestionsNbCorrect();
    }

    @Override
    public int getTotalNumberOfQuestions() {
        return mDb.getTotalNumberOfQuestion();
    }

    @Override
    public int getNumberOfNonTotallyAnsweredQuestions() {
        return mDb.getNumberOfNTAQuestion();
    }
}
