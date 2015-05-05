package com.example.gb.exampreparation.Model;

import java.util.ArrayList;

/**
 * Created by gb on 01/05/15.
 */
public interface Activity2Model {

    public String getQuestion();

    public int getNbCorrectAns();

    public String getAnswer();

    public void rememberUserWasCorrect(boolean correct);

    public void nextQuestion();

    public void reloadDBWithQuestion(ArrayList<Question> qs);

    public void resetAllQuestionsNbCorrect();
}
