package com.example.codflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

public class AddNoteActivity extends AppCompatActivity {
    private TextInputEditText et_title, et_description;
    int receivedNoteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);


        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            setTitle("Edit Note");
            et_title.setText(intent.getStringExtra("title"));
            et_description.setText(intent.getStringExtra("description"));
            receivedNoteId = intent.getIntExtra("id", -1);

        } else {
            setTitle("add Note");
        }


    }

    private void saveNote() {
        String title = et_title.getText().toString();
        String description = et_description.getText().toString();
        NoteViewModel noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "please write Title and Description ", Toast.LENGTH_SHORT).show();
            return;
        }
        Note note = new Note(title, description);


        if (receivedNoteId != -1) {
            noteViewModel.delete(note);
            noteViewModel.insert(note);
            note.setId(receivedNoteId);

        }


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
