package com.example.tyler.Path2Success;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditGoal extends AppCompatActivity {
    //final string to bring information to the main activity
    public final static String GOAL_TITLE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String DUE_DATE = "com.example.tyler.myfirstapp.MESSAGE2";
    public final static String GOAL_CATEGORY = "com.example.tyler.myfirstapp.MESSAGE3";
    //an integer to indicate the category of the goal
    private Integer category;
    //private DatePicker dueDate;
    private EditText taskContent;
    private Button addButton;
    private JSONArray goalList;
    private EditText dateInput;
    private EditText categoryInput;
    private Calendar myCalendar;
    private TextView repeatOptionView;
    private IndividualGoal goal;

    //Make this an array that is retrieved from internal storage every time.
    private CharSequence categories[] =new CharSequence[]{"Fitness","Academics", "Miscellaneous"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent homeIntent = getIntent();
        goal = (IndividualGoal) homeIntent.getSerializableExtra("IndividualGoal");
        myCalendar = Calendar.getInstance();
        setContentView(R.layout.activity_edit_goal);
        addButton = (Button) findViewById(R.id.add_and_back);
        //dueDate = (DatePicker) findViewById(R.id.datePicker);
        taskContent = (EditText) findViewById(R.id.taskContent);
        taskContent.setText(goal.getTitle());


        Toolbar myToolbar = (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Edit goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Handle date input
        dateInput = (EditText) findViewById(R.id.datePicker);
        dateInput.setText(goal.getDueDate());
        dateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (dateInput.hasFocus()) {
                    pickDate();
                    dateInput.clearFocus();
                }
            }
        });
        taskContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        dateInput.setKeyListener(null);




        //Handle category input
        categoryInput = (EditText)findViewById(R.id.categorySelector);
        categoryInput.setText(categories[goal.getCategory()]);
        categoryInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(categoryInput.hasFocus()){
                    pickCategory();
                    categoryInput.clearFocus();

                }
            }
        });
    }
// get info from putExtra and then setText to

    private void pickCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a category");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                category = which;
                categoryInput.setText(categories[category]);
            }
        });
        builder.show();

    }


    /**
     * Add a new item, and bring the information from this activity to the main activity
     * @param view
     */
    public void addNewItem(View view){
        LocalStorage storage = new LocalStorage(this.getApplicationContext());
        Intent intent = new Intent();
        String task = taskContent.getText().toString();
        String date = dateInput.getText().toString();

        String id = goal.getRandomID();
        goal.setTitle(task);
        goal.setDueDate(date);
        goal.setCategory(category);
        storage.updateGoal(id, goal);
        intent.putExtra(GOAL_TITLE, task);
        intent.putExtra(DUE_DATE, date);
        intent.putExtra(GOAL_CATEGORY, category);
        setResult(Activity.RESULT_OK, intent);
        finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Hide keyboard
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }

    };


    private void pickDate(){
        new DatePickerDialog(EditGoal.this, date,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * This method will update the text in the date field to the date that is selected
     */
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateInput.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }
}
