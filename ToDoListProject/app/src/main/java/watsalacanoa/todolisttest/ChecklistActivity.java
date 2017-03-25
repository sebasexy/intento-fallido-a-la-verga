package watsalacanoa.todolisttest;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import watsalacanoa.todolisttest.adapters.FinishedItemsArrayAdapter;
import watsalacanoa.todolisttest.adapters.ItemsArrayAdapter;
import watsalacanoa.todolisttest.contentprovider.NoteContentProvider;
import watsalacanoa.todolisttest.db.Task;

public class ChecklistActivity extends AppCompatActivity {

    public final static int SLASHED = 1;
    public final static int UNSLASHED = 0;
    private EditText mNewItemText;
    private EditText mNoteTitle;
    private ActionBar mActionBar;
    private ListView mFinishedItemsListView;
    private ArrayList<String> slashes;
    private ArrayList<String> mItems;
    private ArrayList<String> mFinishedItems;
    private Uri checklistUri;
    private ItemsArrayAdapter mItemsArrayAdapter;
    private FinishedItemsArrayAdapter mFinishedItemsArrayAdapter;
    private DynamicListView mItemsListView;

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_checklist);

        slashes = new ArrayList<>();
        mFinishedItems = new ArrayList<>();
        mFinishedItemsArrayAdapter = new FinishedItemsArrayAdapter(this, mFinishedItems, true);
        mFinishedItemsListView = (ListView) findViewById(R.id.lv_finished_checklist);
        mFinishedItemsListView.setAdapter(mFinishedItemsArrayAdapter);
        mFinishedItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView item = (TextView) view.findViewById(R.id.itemText);
                String text = item.getText().toString();
                mFinishedItems.remove(text);
                mItems.add(text);
                mFinishedItemsArrayAdapter.notifyDataSetChanged();
                mItemsArrayAdapter.notifyDataSetChanged();
            }
        });

        mItems = new ArrayList<String>();
        mItemsArrayAdapter = new ItemsArrayAdapter(this, mItems, false);
        mItemsListView = (DynamicListView) findViewById(R.id.itemsListView);
        mItemsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mItemsListView.setAdapter(mItemsArrayAdapter);
        mItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView item = (TextView) view.findViewById(R.id.itemText);
                String text = item.getText().toString();
                mItems.remove(text);
                mFinishedItems.add(text);
                mFinishedItemsArrayAdapter.notifyDataSetChanged();
                mItemsArrayAdapter.notifyDataSetChanged();
            }
        });

        Bundle extras = getIntent().getExtras();
        // check from the saved Instance
        checklistUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(NoteContentProvider.CONTENT_ITEM_TYPE);

        mActionBar = getSupportActionBar();
        View view = getLayoutInflater().inflate(R.layout.note_actionbar, null);

        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setCustomView(view);
        //ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        mNoteTitle = (EditText) mActionBar.getCustomView().findViewById(R.id.noteName);

        // Or passed from the other activity
        if (extras != null) {
            if(extras.getParcelable(NoteContentProvider.CONTENT_ITEM_TYPE) != null) {
                checklistUri = extras
                        .getParcelable(NoteContentProvider.CONTENT_ITEM_TYPE);
            }
            fillData(checklistUri);

        }

        mItemsListView.setCheeseList(mItems);
    }

    private void fillData(Uri uri) {

        String[] projection = {Task.COLUMN_ITEMS, Task.COLUMN_SLASHED, Task.COLUMN_NOTE_TITLE};
        Cursor cursor = null;

        try {

            cursor = getContentResolver().query(uri, projection, null, null, null);
        } catch (NullPointerException e) {
            Log.e("ChecklistActivity.java", "NullPointerException caught: ", e);
        }
        if(cursor != null) {

            cursor.moveToFirst();
            String sItems = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_ITEMS));
            String sSlashes = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_SLASHED));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NOTE_TITLE));
            try {

                JSONArray jsonArray = new JSONArray(sItems);
                mNoteTitle.setText(title);
                JSONArray slashesJsonArray = new JSONArray(sSlashes);
                for(int i = 0; i < slashesJsonArray.length(); i++) {

                    slashes.add("" + slashesJsonArray.get(i));
                    if(slashesJsonArray.get(i).equals(ChecklistActivity.UNSLASHED)) {

                        mItems.add((String) jsonArray.get(i));
                    }
                    else {

                        mFinishedItems.add((String) jsonArray.get(i));
                    }
                }

                mFinishedItemsArrayAdapter.notifyDataSetChanged();
                mItemsArrayAdapter.notifyDataSetChanged();

            } catch (JSONException ignored) {}

            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(NoteContentProvider.CONTENT_ITEM_TYPE, checklistUri);
    }

    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        mItems.addAll(mFinishedItems);
        String note = new JSONArray(mItems).toString();
        ArrayList<Integer> slashes = new ArrayList<>();
        for (int i = 0; i < mItemsListView.getChildCount(); i++) {

            slashes.add(ChecklistActivity.UNSLASHED);
        }
        for (int i = 0; i < mFinishedItemsListView.getChildCount(); i++) {

            slashes.add(ChecklistActivity.SLASHED);
        }

        String sSlashes = new JSONArray(slashes).toString();

        if (mItems.isEmpty()) {

            return;
        }

        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_ITEMS, note);
        values.put(Task.COLUMN_SLASHED, sSlashes);
        String noteTitle = mNoteTitle.getText().toString();
        if(noteTitle.isEmpty()) {
            noteTitle = "Untitled";
        }

        values.put(Task.COLUMN_NOTE_TITLE, noteTitle);
        if (checklistUri == null) {
            checklistUri = getContentResolver().insert(NoteContentProvider.CONTENT_URI, values);
            String firstPart = NoteContentProvider.CONTENT_URI.toString();
            Long id = ContentUris.parseId(checklistUri);
            String correctURIs = firstPart + "/" + id;
            Uri correctedUri = Uri.parse(correctURIs);
            try{
                getContentResolver().update(checklistUri, values, null, null);
            }
            catch (Exception e){
                checklistUri = correctedUri;
            }

        } else {
            getContentResolver().update(checklistUri, values, null, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void addItem(View v) {

        if (mItems.size() < 100) {

            AlertDialog alertToShow = getDialog().create();
            alertToShow.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alertToShow.show();
        }
        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Max Items")
                    .setMessage("You have reached the maximum " +
                            "number of items (100) one note can hold.")
                    .setPositiveButton("OK", null);
            builder.show();
        }
    }

    public AlertDialog.Builder getDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        LinearLayout newNoteBaseLayout = (LinearLayout) li.inflate(R.layout.new_item_dialog, null);
        mNewItemText = (EditText) newNoteBaseLayout.getChildAt(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                closeKeyboard();
                String text = mNewItemText.getText().toString();
                if (!mItems.contains(text) && !text.isEmpty()) {

                    mItems.add(text);
                    mItemsArrayAdapter.update();
                    mItemsArrayAdapter.notifyDataSetChanged();
                }
                else if(mItems.contains(text)) {

                    Toast.makeText(ChecklistActivity.this, text + " is already added.", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                closeKeyboard();
            }
        }).setTitle("New Item");
        builder.setView(newNoteBaseLayout);
        return builder;
    }

    private void closeKeyboard() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNewItemText.getWindowToken(), 0);
    }

    public void deleteItem(int position) {
        mItems.remove(position);
        mItemsArrayAdapter.update();
        mItemsArrayAdapter.notifyDataSetChanged();
    }

    public void editItem(final int position) {
        AlertDialog.Builder builder = getDialog();
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
        AlertDialog alertToShow = builder.create();
        alertToShow.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertToShow.show();
        mNewItemText.setSelection(mNewItemText.getText().length());
    }
/*
    public void uncheckAll(View view) {
        mItems.addAll(mFinishedItems);
        mFinishedItems.clear();
        mItemsArrayAdapter.notifyDataSetChanged();
        mFinishedItemsArrayAdapter.notifyDataSetChanged();
    }
*/
    public void deleteFinishedItem(int position) {
        mFinishedItems.remove(position);
        mFinishedItemsArrayAdapter.notifyDataSetChanged();
    }

    public void editFinishedItem(final int position) {

        AlertDialog.Builder builder = getDialog();
        mNewItemText.setText(mFinishedItems.get(position));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFinishedItems.set(position, mNewItemText.getText().toString());
                mFinishedItemsArrayAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }
}
