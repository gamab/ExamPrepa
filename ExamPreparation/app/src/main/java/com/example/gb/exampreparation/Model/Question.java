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
    //the id of the question
    private int id;

    public Question(String q, String ans) {
        this.q = q;
        this.ans = ans;
        this.nbrCorrect = 0;
        this.id=0;
    }

    public Question(String q, String ans,int nbrCorrect, int id) {
        this.q = q;
        this.ans = ans;
        this.nbrCorrect = nbrCorrect;
        this.id=id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
       return "[" + q + ";" + ans + ";" + nbrCorrect + "]";
    }
}
