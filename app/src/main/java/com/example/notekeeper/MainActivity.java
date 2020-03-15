package com.example.notekeeper;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    public static final String NOTE_INFO = "com.example.notekeeper.NOTE_INFO";
    public static final String NOTE_POSITION = "com.example.notekeeper.NOTE_POSTION";
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.notekeeper.ORIGINAL_NOTE_TEXT";
    private NoteInfo mNote = null;
    private boolean mIsNewNote = false;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalCourseId);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalText);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalTitle);
    }

    private static int postion;
    private Spinner mspinnerCourses;
    private EditText mtextNoteText;
    private EditText mtextNoteTitle;
    private int mNotePosition;
    private String mOriginalCourseId;
    private String mOriginalText;
    private String mOriginalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Entered OnCreate Method");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mspinnerCourses = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinnerCourses.setAdapter(adapterCourses);

        Button btn_Save = findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
//                Snackbar.make(v, "Note Saved Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        });

        Button btn_Cancel = findViewById(R.id.btnCancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNote();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        });

        readDisplayStateValues();
        if(savedInstanceState == null)
        {
            storeOriginalValues();
        }
        else
        {
            restoreOriginalNoteValue(savedInstanceState);
        }

        mtextNoteTitle = findViewById(R.id.txt_NoteTitle);
        mtextNoteText = findViewById(R.id.txt_NoteDescription);

        if(!mIsNewNote)
            displayNote(mspinnerCourses, mtextNoteTitle, mtextNoteText);

        Log.d(TAG, "exiting OnCreate Method");
    }

    private void cancelNote() {
        if(mIsNewNote == true)
        {
            DataManager.getInstance().removeNote(mNotePosition);
        }
    }

    private void restoreOriginalNoteValue(Bundle savedInstanceState) {
        mOriginalTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
        mOriginalCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
    }

    private void storeOriginalValues() {
        if(mIsNewNote)
            return;

        mOriginalCourseId = mNote.getCourse().getCourseId();
        mOriginalText = mNote.getText();
        mOriginalTitle = mNote.getTitle();
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mNotePosition = intent.getIntExtra(NOTE_POSITION, -1);
//        mNote = intent.getParcelableExtra(NOTE_INFO);
        if(mNotePosition == -1)
        {
            mIsNewNote = true;
            createNewNote();
        }

        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
//        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_send_mail){
            SendEmail();
            return true;
        }
        if(id == R.id.action_next)
        {
            moveNext();
        }
        if(id == R.id.action_previous)
        {
            movePrevious();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem nextMenuItem = menu.findItem(R.id.action_next);
        MenuItem previousMenuItem = menu.findItem(R.id.action_previous);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;
        nextMenuItem.setEnabled(mNotePosition < lastNoteIndex);
        previousMenuItem.setEnabled(mNotePosition > 0);
        return super.onPrepareOptionsMenu(menu);
    }

    private void movePrevious() {
        saveNote();

        mNotePosition--;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);

        storeOriginalValues();
        displayNote(mspinnerCourses, mtextNoteTitle, mtextNoteText);
        invalidateOptionsMenu();
    }

    private void moveNext() {
        saveNote();

        mNotePosition++;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);

        storeOriginalValues();
        displayNote(mspinnerCourses, mtextNoteTitle,mtextNoteText);
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "entering OnPause Method");

        if(mIsNewNote == false)
        {
            storePreviousValues();
        }

        Log.d(TAG, "exiting OnPause Method");
    }

    private void storePreviousValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalCourseId);
        mNote.setCourse(course);
        mNote.setText(mOriginalText);
        mNote.setTitle(mOriginalTitle);
    }

    private void saveNote() {
        Log.d(TAG, "entering saveNote Method");

        mNote.setCourse((CourseInfo) mspinnerCourses.getSelectedItem());
        mNote.setTitle(mtextNoteTitle.getText().toString());
        mNote.setText(mtextNoteText.getText().toString());
        mIsNewNote = true;
        Log.i(TAG, "user is saving note with course title: " + mNote.getCourse().getTitle() + " | CourseId(" + mNote.getCourse().getCourseId() + ")");

        Log.d(TAG, "exiting saveNote Method");
    }

    private void SendEmail() {
        CourseInfo course = (CourseInfo) mspinnerCourses.getSelectedItem();
        String subject = mtextNoteTitle.getText().toString();
        String body = "Checkout What I Learned in the Plural Sight Course \"" + course.getTitle() + "\"\n" + mtextNoteText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");      //Standard MIME type for Email Messages
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(intent);
    }
}
