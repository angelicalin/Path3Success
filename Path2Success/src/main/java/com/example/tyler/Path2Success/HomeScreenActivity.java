package com.example.tyler.Path2Success;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

//Import statements for writing to local memory
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeScreenActivity extends AppCompatActivity {

    public static final String FILENAME = "goal_file";
    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tyler.myfirstapp.MESSAGE2";
    private ListView listLayout;
  //  private EditText taskContent;
  //  private EditText dueDate;
    private Button addButton;
    private LayoutTransition mTransition;
    public static final int RESULT_CODE = 9;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();
    private GoalDataAdapter adapter;
    private JSONArray goalList;
    private JSONObject currentGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        listLayout = (ListView) findViewById(R.id.checkboxes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Path 2 Success");
        adapter = new GoalDataAdapter(this, goalArrayList);
        listLayout.setAdapter(adapter);
        goalList = new JSONArray();

            //Code used from http://chrisrisner.com/31-Days-of-Android--Day-23-Writing-and-Reading-Files/
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            bis.close();
            fis.close();

            goalList = new JSONArray(b.toString());

            for (int i = 0; i < goalList.length(); i++) {
                currentGoal = goalList.getJSONObject(i);
                if (!currentGoal.getBoolean("isChecked")) {
                    String task = currentGoal.getString("title");
                    String date = currentGoal.getString("date");
                    IndividualGoal newGoal = new IndividualGoal(task, date);
                    goalArrayList.add(newGoal);
                    adapter.notifyDataSetChanged();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** Called when the user clicks the Send button */
    public void goToInputScreen(View view) {
        Intent intent = new Intent(this, InputNewGoal.class);
        startActivityForResult(intent, RESULT_CODE);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == RESULT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                //what does tContent mean?? what is it's purpose in the code?
                String tContent = data.getStringExtra(InputNewGoal.EXTRA_MESSAGE);
                String tDate = data.getStringExtra((InputNewGoal.EXTRA_MESSAGE2));
                if(!tContent.isEmpty()) {
                    IndividualGoal newGoal = new IndividualGoal(tContent, tDate);
                    goalArrayList.add(newGoal);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
