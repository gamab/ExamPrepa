package com.example.gb.exampreparation.Model;

/**
 * Created by gb on 01/05/15.
 */
public class Question {
    //the question
    private String q;
    //the answer
    private String ans;
    //the number of correct answers
    private int nbrCorrect;

    public Question(String q, String ans) {
        this.q = q;
        this.ans = ans;
        this.nbrCorrect = 0;
    }

    public String getQ() {
        return q;
    }

    public String getAns() {
        return ans;
    }

    public int getNbrCorrect() {
        return nbrCorrect;
    }

    /**
     * Set NbrCorrect to nbrCorrect if nbrCorrect € [0-ConstanteQuestion.MAX_ASK]
     * else set to 0 if nbrCorrect < 0 or MAX_ASK if nbrCorrect > MAX_ASK
     * @param nbrCorrect
     */
    public void setNbrCorrect(int nbrCorrect) {
        if (nbrCorrect < 0)
            this.nbrCorrect = 0;
        else if (nbrCorrect <= ConstanteQuestion.MAX_ASK)
            this.nbrCorrect = nbrCorrect;
        else
            this.nbrCorrect = ConstanteQuestion.MAX_ASK;
    }

    @Override
    public String toString() {
       return "[" + q + ";" + ans + ";" + nbrCorrect + "]";
    }
}
