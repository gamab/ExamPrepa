package com.example.gb.exampreparation.FileLoader;

import android.util.Log;

import com.example.gb.exampreparation.Model.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gb on 01/05/15.
 */
public class QuestionQoSLoader {

    private static final String TAG = "QuestionReader";
    public static final char endlReplacer = '|';

    //FilePath within the sd card ex : /sdcard/DCIM/App/questionQoS.csv
    public static ArrayList<Question> retrieveQuestionsFromFile(String filePath) {
        ArrayList<Question> qs;


        //Get the text file
        File f = new File(filePath);
        Log.d(TAG,"====>file :" + f.getAbsolutePath());

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append(endlReplacer); //append a end line replacer
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            Log.e(TAG,"Could not read the file.");
            Log.e(TAG,"Because of " + e.getMessage());
        }

        if (text.length() != 0) {
            //Parse the file
            qs = parseCsvToQuestions(text);
        }
        else {
            qs = new ArrayList<>();
        }

        return qs;
    }

    private static ArrayList<Question> parseCsvToQuestions(StringBuilder text) {
        ArrayList<Question> qs = new ArrayList<>();

        String q, a;

        Pattern pattern = Pattern.compile("(([\"][^\"]*[\"])*[^,]*)[,](([\"][^\"]*[\"])*[^|]*)[|]");
        Matcher match = pattern.matcher(text);
        Log.d(TAG,"=====>Let s see matches");

        //get rid of the first three lines
        for (int i = 0; i < 3; i++) {
            match.find();
        }
        while (match.find()) {
            //System.out.println(" found $1 : " + match.group(0));
            q = replaceEndlReplacer(match.group(1));
            a = replaceEndlReplacer(match.group(3));
            qs.add(new Question(q,a));
            Log.d(TAG, "=====>found question " + qs.get(qs.size() - 1));
        }

        return qs;
    }

    public static String replaceEndlReplacer(String toReplace) {
        char[] replacer = toReplace.toCharArray();
        for (int i = 0; i < replacer.length; i++) {
            if (replacer[i]==endlReplacer) {
                replacer[i] = '\n';
                Log.d(TAG, "=====>found endl to replace.");
            }
        }
        return new String(replacer);
    }
}
