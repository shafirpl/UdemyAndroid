package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);

        // this is how we retrieve the information passed from a screen and use it in this screen
        Intent intent = getIntent();
        // since we are getting an int from the previous screen, we use Int Extra
        // we also have to put a default value if for some reason the intent didn't get passed through here
        // also notice the name of the passed info has to be exact match, from previous screen
        // we were passing "noteId", so it has to match that
        this.noteId = intent.getIntExtra("noteId",-1);

        /*
        * So whats happening here, if we don't get a correct noteId, the default value will be set meaning
        * that there was some issue (we are using -1 as array starts at 0, so if something went wrong, we would be showing the first
        * item if we were using 0, which we don't want)
        * But if thats not the case, we get the id of the array item from the previous screen when we click on the note
        * using that, we know the id of that array item and so we can access that and set the editor text to that to begin with
         */

        /*
        * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339536#questions
        * Watch from 24:00 to understand the else part, it is really important
        * So in the else part, we add an empty string to the notes arraylist, and then get the new noteId, which is the last one or size-1
        * so at first, the arraylist is ("Example notes"), then we click add new note, it becomes ("example notes", ""), so now the
        * new noteId is 1, or the second item, as this is the one we will be working with
         */
        if (this.noteId != -1){
            editText.setText(MainActivity.notes.get(noteId));
        }
        else{
            MainActivity.notes.add("");
            this.noteId = MainActivity.notes.size()-1;
        }

        // this will update the array item in the list to reflect our edits
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // this function will run if we add a single character to existing text
                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                // recall after we update the arraylist, we need to notify the arrayAdapter to reflect the change on the screen
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
