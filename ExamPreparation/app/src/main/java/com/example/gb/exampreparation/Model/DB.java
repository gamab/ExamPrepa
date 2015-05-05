package com.example.gb.exampreparation.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gb on 01/05/15.
 */
public class DB extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "DB_QUESTIONNER.db";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "Questions";
    private static String TABLE_ID = "_id";
    private static String TABLE_QUESTION = "question";
    private static String TABLE_ANSWER = "answer";
    private static String TABLE_NBCORRECT = "nbcorrect";

    public DB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(DATABASE_NAME,"Table created");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TABLE_QUESTION + " TEXT,"
                + TABLE_ANSWER + " TEXT,"
                + TABLE_NBCORRECT + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // CREATE NEW INSTANCE OF SCHEMA
        onCreate(db);
    }

    /**
     * Allow getting rid of the data
     */
    public void deleteEverything() {
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,1,2);
    }

    public void printDB() {

        SQLiteDatabase sd = getWritableDatabase();

        String query = "SELECT " + TABLE_NAME + "." + TABLE_ID + ", "
                +TABLE_NAME + "." + TABLE_QUESTION + ", "
                + TABLE_NAME + "." + TABLE_ANSWER + ", "
                + TABLE_NAME + "." + TABLE_NBCORRECT + " ";

        query += "FROM " + TABLE_NAME + " ";

        //Log.d(DATABASE_NAME, "Query is : " + query);

        Cursor cursor = sd.rawQuery(query,null);

        Log.d(DATABASE_NAME,TABLE_ID + " | " + TABLE_QUESTION + " | " + TABLE_ANSWER + " | " + TABLE_NBCORRECT );
        while (cursor.moveToNext()) {
            Log.d(DATABASE_NAME,cursor.getString(0) + " | " + cursor.getString(1) + " | " + cursor.getString(2) + " | " + cursor.getString(3));
        }
        cursor.close();
    }

    public void printNFSDB() {

        SQLiteDatabase sd = getWritableDatabase();

        String query = "SELECT " + TABLE_NAME + "." + TABLE_ID + ", "
                +TABLE_NAME + "." + TABLE_QUESTION + ", "
                + TABLE_NAME + "." + TABLE_ANSWER + ", "
                + TABLE_NAME + "." + TABLE_NBCORRECT + " ";

        query += "FROM " + TABLE_NAME + " ";

        query += "WHERE " + TABLE_NAME + "." + TABLE_NBCORRECT + " < " + ConstanteQuestion.MAX_ASK + " ;";

        //Log.d(DATABASE_NAME, "Query is : " + query);

        Cursor cursor = sd.rawQuery(query,null);

        Log.d(DATABASE_NAME,TABLE_ID + " | " + TABLE_QUESTION + " | " + TABLE_ANSWER + " | " + TABLE_NBCORRECT );
        while (cursor.moveToNext()) {
            Log.d(DATABASE_NAME,cursor.getString(0) + " | " + cursor.getString(1) + " | " + cursor.getString(2) + " | " + cursor.getString(3));
        }
        cursor.close();
    }

    public boolean addQuestion(Question q) {
        //Log.d(DATABASE_NAME,"add a Question to the database");
        ContentValues cv = new ContentValues();
        cv.put(TABLE_QUESTION,q.getQ());
        cv.put(TABLE_ANSWER,q.getAns());
        cv.put(TABLE_NBCORRECT,q.getNbrCorrect());
        SQLiteDatabase sd = getWritableDatabase();
        long result = sd.insert(TABLE_NAME, TABLE_ID, cv);

        return (result >= 0);
    }

    public int getTotalNumberOfQuestion() {
        int totalNbrQ = 0;

        SQLiteDatabase sd = getWritableDatabase();

        String query = "SELECT COUNT(" + TABLE_NAME + "." + TABLE_ID + ") ";

        query += "FROM " + TABLE_NAME + ";";

        //Log.d(DATABASE_NAME, "Query is : " + query);

        Cursor cursor = sd.rawQuery(query,null);

        if (cursor.moveToNext()) {
            totalNbrQ = cursor.getInt(0);
        }
        cursor.close();

        //Log.d(DATABASE_NAME, "Total number of question is : " + totalNbrQ);

        return totalNbrQ;
    }

    /**
     * Get the number of questions that don't have five stars already (NFS = Non Five Stars)
     * @return the number of NFS questions
     */
    public int getNumberOfNFSQuestion() {
        int totalNbrQ = 0;

        SQLiteDatabase sd = getWritableDatabase();

        String query = "SELECT COUNT(" + TABLE_NAME + "." + TABLE_ID + ") ";

        query += "FROM " + TABLE_NAME + " ";

        query += "WHERE " + TABLE_NAME + "." + TABLE_NBCORRECT + " < " + ConstanteQuestion.MAX_ASK + " ;";

        //Log.d(DATABASE_NAME, "Query is : " + query);

        Cursor cursor = sd.rawQuery(query,null);

        if (cursor.moveToNext()) {
            totalNbrQ = cursor.getInt(0);
        }
        cursor.close();

        //Log.d(DATABASE_NAME, "Total number of non five stars question is : " + totalNbrQ);

        return totalNbrQ;
    }

    /**
     * Get question From Non Five Stars allows to retrieve the question at the row i
     * @param i
     * @return
     */
    public Question getQuestionFNFS(int i) {
        Question q = null;
        String question = null;
        String answer = null;
        int nbCorrect;
        int realId = convertNFSIdToRealId(i);

        SQLiteDatabase sd = getWritableDatabase();

        String query = "SELECT " + TABLE_NAME + "." + TABLE_QUESTION + ", "
                + TABLE_NAME + "." + TABLE_ANSWER + ", "
                + TABLE_NAME + "." + TABLE_NBCORRECT + " ";

        query += "FROM " + TABLE_NAME + " ";

        query += "WHERE " + TABLE_NAME + "." + TABLE_NBCORRECT + " < " + ConstanteQuestion.MAX_ASK + " AND "
                + TABLE_ID + " = " + realId + ";";

        //Log.d(DATABASE_NAME, "Query is : " + query);

        Cursor cursor = sd.rawQuery(query,null);

        if (cursor.moveToNext()) {
            question = cursor.getString(0);
            answer = cursor.getString(1);
            nbCorrect = cursor.getInt(2);
            q = new Question(question, answer);
            q.setNbrCorrect(nbCorrect);
            //Log.d(DATABASE_NAME, "%%%%%> Found question for " + i + " in NFS questions");
        }
        //else {
        //    Log.d(DATABASE_NAME, "%%%%%> COULD NOT find question for " + i + " in NFS questions");
        //}
        cursor.close();

        //if (q==null) {
        //    Log.d(DATABASE_NAME, "Did not Find question");
        //}
        return q;
    }

    public boolean setQuestionNbCorrect(int id, int nbCorrect) {
        //Log.d(DATABASE_NAME,"Change number of correct in the db");
        SQLiteDatabase sd = getWritableDatabase();
        String strFilter = TABLE_ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(TABLE_NBCORRECT, nbCorrect);
        int result = sd.update(TABLE_NAME,cv,strFilter,null);

        return (result >= 0);
    }

    public boolean resetAllQuestionsNbCorrect() {
        //Log.d(DATABASE_NAME,"Change number of correct in the db");
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_NBCORRECT, 0);
        int result = sd.update(TABLE_NAME,cv,null,null);

        return (result >= 0);
    }

    public boolean setQuFNFSNbCorrect(int id, int nbrCorrect) {
        boolean couldSetNbCorrect = false;
        int realId;

        realId = convertNFSIdToRealId(id);
        if (realId != -1) {
            couldSetNbCorrect = setQuestionNbCorrect(realId,nbrCorrect);
        }

        return couldSetNbCorrect;
    }

    /**
     * Converts an id of a non five star question into a real id in the DB
     * The user of the database wants to change a field the second question from the questions that are not with five stars
     * hence we have to get the real id of this question, it might be the 4th question for instance
     * @param nfsId the number of the question that doesn't have five stars already
     * @return the real id of this question in the database
     */
    private int convertNFSIdToRealId (int nfsId) {
        int id = -1;

        SQLiteDatabase sd = getWritableDatabase();

        String query = "SELECT " + TABLE_NAME + "." + TABLE_ID + " ";

        query += "FROM " + TABLE_NAME + " ";

        query += "WHERE " + TABLE_NAME + "." + TABLE_NBCORRECT + " < " + ConstanteQuestion.MAX_ASK + " ";

        query += "LIMIT 1 OFFSET " + (nfsId - 1) + " ;";

        //Log.d(DATABASE_NAME, "=====>Query is : " + query);

        Cursor cursor = sd.rawQuery(query,null);

        if (cursor.moveToNext()) {
            id = cursor.getInt(0);
            //Log.d(DATABASE_NAME, "=====>Real id for " + nfsId +" in NFS is : " + id);
        }
        cursor.close();
        return id;
    }
}
