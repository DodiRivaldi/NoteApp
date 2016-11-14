package androidkejar.noteapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidkejar.noteapp.R;
import androidkejar.noteapp.database.RealmDB;
import androidkejar.noteapp.model.Note;
import androidkejar.noteapp.util.TimeUtil;

public class SaveActivity extends AppCompatActivity {

    private EditText noteText;
    private int id;

    public static void start(Context context, int id){
        Intent intent = new Intent(context, SaveActivity.class);
        intent.putExtra(SaveActivity.class.getSimpleName(), id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        noteText = (EditText) findViewById(R.id.noteText);

        id = getIntent().getExtras().getInt(SaveActivity.class.getSimpleName());

        if(id != 0){
            // get by id
            Note note = new RealmDB(this).getById(Note.class, id);
            noteText.setText(String.valueOf(note.getNote()));
        }
    }

    // add note
    public void addNote(String noteText) {
        Note note = new Note();
        note.setId((int) (System.currentTimeMillis()) / 1000);
        note.setNote(noteText);
        note.setDateModified(String.valueOf(TimeUtil.getUnix()));

        new RealmDB(this).add(note);
    }

    // update note
    public void updateNote(int id, String noteText){
        Note note = new Note();
        note.setId(id);
        note.setNote(noteText);
        note.setDateModified(String.valueOf(TimeUtil.getUnix()));

        new RealmDB(this).update(note);
    }

    // delete note
    public void deleteNote(int id) {
        new RealmDB(this).delete(Note.class, id);
    }

    private void createOrUpdate(){
        if(!TextUtils.isEmpty(noteText.getText().toString())) {
            if (id == 0) {
                addNote(noteText.getText().toString());
            } else {
                updateNote(id, noteText.getText().toString());
            }
        }else{
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                createOrUpdate();
                finish();
                return true;
            case R.id.icon_save:
                createOrUpdate();
                finish();
                return true;
            case R.id.icon_delete:
                if(id != 0) deleteNote(id);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
