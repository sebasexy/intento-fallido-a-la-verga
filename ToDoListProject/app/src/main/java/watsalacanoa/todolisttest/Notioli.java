package watsalacanoa.todolisttest;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;

public class Notioli extends AppCompatActivity {

    /*
    TextView title;
    EditText note1,note2;
    Button home;
    */

    // Dialog Builder
    private EditText mNewItemText;
    private String pastNotioliText;

    private TaskHelper mNotioliHelper;
    private ListView mListView;
    private FloatingActionButton addNotioliButton;
    private ArrayAdapter<String> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notioli);
        /*
        title = (TextView) findViewById(R.id.notioli_textView);
        note1 = (EditText) findViewById(R.id.notioli_editText);
        note2 = (EditText) findViewById(R.id.notioli_editText2);
        home = (Button) findViewById(R.id.notioli_button);

        title.setText("Notioli");
        home.setText("Home");
        note1.setText("Note 1");
        note2.setText("Note 2");
        */
        mNotioliHelper = new TaskHelper(this);
        mListView = (ListView) findViewById(R.id.notioli_listView);
        addNotioliButton = (FloatingActionButton) findViewById(R.id.fab_add_notioli);
        addNotioliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
        updateUI();


    }

    private void addNote() {
        final EditText notioliEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Notioli")
                .setMessage("Add a new Notioli")
                .setView(notioliEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = notioliEditText.getText().toString();
                        SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.NOTIOLI_COLUMN_TITLE,task);
                        db.insertWithOnConflict(Task.TABLE_NOTIOLI, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();
        dialog.show();
    }

    private void updateUI(){
        ArrayList<String> notioliList = new ArrayList<>();
        SQLiteDatabase db = mNotioliHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TABLE_NOTIOLI, new String[] {Task.NOTIOLI_COLUMN_ID, Task.NOTIOLI_COLUMN_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(Task.NOTIOLI_COLUMN_TITLE);
            notioliList.add(cursor.getString(index));
        }

        if(mAdapter == null){
            mAdapter = new ArrayAdapter<>(this, R.layout.notioli_item, R.id.notioli_title, notioliList);
            mListView.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(notioliList);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    public void deleteNotioli(View v){
        View parent = (View) v.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.notioli_title);
        String notioli = taskTextView.getText().toString();
        SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
        db.delete(Task.TABLE_NOTIOLI, Task.NOTIOLI_COLUMN_TITLE + " = ? ", new String[] {notioli});
        db.close();
        updateUI();
    }

    public AlertDialog.Builder getDialog() {
        final SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
        LayoutInflater li = LayoutInflater.from(this);
        LinearLayout newNoteBaseLayout = (LinearLayout) li.inflate(R.layout.new_item_dialog, null);
        mNewItemText = (EditText) newNoteBaseLayout.getChildAt(0);
        mNewItemText.setText(pastNotioliText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues values = new ContentValues();
                values.put(Task.NOTIOLI_COLUMN_TITLE, mNewItemText.getText().toString());
                db.update(Task.TABLE_NOTIOLI, values, Task.NOTIOLI_COLUMN_TITLE + " = '" + pastNotioliText + "'" , null);
                updateUI();
                closeKeyboard();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeKeyboard();
            }
        }).setTitle("Edit");
        builder.setView(newNoteBaseLayout);
        return builder;
    }
    public void editNotioli(View v) {
        View parent = (View) v.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.notioli_title);
        String notioli = taskTextView.getText().toString();
        pastNotioliText = notioli;
        AlertDialog.Builder builder = getDialog();
        AlertDialog alertToShow = builder.create();
        alertToShow.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertToShow.show();
    }
    /*
    public void editItem(View v) {
        android.app.AlertDialog.Builder builder = getDialog();
        mNewItemText.setText(mItems.get(position));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeKeyboard();
                mItems.set(position, mNewItemText.getText().toString());
                mItemsArrayAdapter.update();
                mItemsArrayAdapter.notifyDataSetChanged();
            }
        });
        android.app.AlertDialog alertToShow = builder.create();
        alertToShow.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertToShow.show();
        mNewItemText.setSelection(mNewItemText.getText().length());
    }
    */
    public void goHome(View v){
        Intent i = new Intent();
        i.putExtra("message","You visited Notioli");
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    private void closeKeyboard() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNewItemText.getWindowToken(), 0);
    }
}