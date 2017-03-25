package watsalacanoa.todolisttest;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import watsalacanoa.todolisttest.adapters.CardViewAdapter;
import watsalacanoa.todolisttest.contentprovider.NoteContentProvider;
import watsalacanoa.todolisttest.db.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MAX_QTY_CHECKLISTS = 100;
    private boolean noneSelected = true;
    private int currentCol = -9;
    private int prevCol = -2;
    public int selectedChecklistPos = -17;
    private int prevSelectedChecklistPos = -13;
    public int selectedId = -1;
    private Drawable bgCardView;
    private RecyclerView recycleViewList;
    private FloatingActionButton addChecklistButton;
    private ActionBar mainActionBar;
    public CardViewAdapter mainCardViewAdapter;
    private List<List<String>> mNoteList;
    private List<List<String>> mTitleList;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up RecyleView (A more complex ListView)
        recycleViewList = (RecyclerView) findViewById(R.id.recyclerV_rV_main);
        LinearLayoutManager llmForRecycler = new LinearLayoutManager(this);
        llmForRecycler.setOrientation(LinearLayoutManager.VERTICAL);
        recycleViewList.setLayoutManager(llmForRecycler);

        // Set up add checklist button
        addChecklistButton = (FloatingActionButton) findViewById(R.id.fab_add_main);
        addChecklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChecklistActivity(v);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    public void deleteIcon(View v){

        deleteNote(selectedId);
    }

    public void backIcon(View v){

        clearFocus();
        actionBarOff(null);
        noneSelected = true;
    }

    public void deleteNote(int id) {

        Uri uri = Uri.parse(NoteContentProvider.CONTENT_URI + "/" + id);
        getContentResolver().delete(uri, null, null);
        getLoaderManager().initLoader(0, null, this);
        actionBarOff(null);
        noneSelected = true;
    }

    public void noteFocused(int col, View v){

        currentCol = col;
        final CardView card = (CardView) v;
        if(selectedChecklistPos == prevSelectedChecklistPos && currentCol == prevCol && !noneSelected) {

            clearFocus();
            card.setEnabled(false);
            card.postDelayed(new Runnable() {

                public void run() {
                    card.setEnabled(true);
                }
            }, 300);
            actionBarOff(null);
            noneSelected = true;
        }
        else {

            clearFocus();
            card.setEnabled(false);
            card.postDelayed(new Runnable() {

                public void run() {
                    card.setEnabled(true);
                }
            }, 300);
            actionBarOn();
            noneSelected = false;
        }

        prevSelectedChecklistPos = selectedChecklistPos;
        prevCol = col;
    }

    public void clearFocus() {

        for(int i = 0; i < recycleViewList.getChildCount(); i++) {

            LinearLayout linearLayout = (LinearLayout) recycleViewList.getChildAt(i);
            CardView one = (CardView) linearLayout.findViewById(R.id.card_view);
            CardView two = (CardView) linearLayout.findViewById(R.id.card_view2);
            if(bgCardView == null) {

                bgCardView = one.getBackground();
            }
        }
    }

    public void actionBarOn() {

        if(mainActionBar == null) {

            mainActionBar = getSupportActionBar();
        }

        View view = getLayoutInflater().inflate(R.layout.highlight_actionbar, null);
        if(mainActionBar.getCustomView() == null) {

            mainActionBar.setCustomView(view);
        }

        mainActionBar.setDisplayShowCustomEnabled(true);
    }

    public void actionBarOff(View view) {

        if(mainActionBar == null) {
            mainActionBar = getSupportActionBar();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                mainActionBar.setDisplayShowCustomEnabled(false);
            }
        }, 150);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. Add menuItems to the action bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void toChecklistActivity(View view) {

        if((recycleViewList.getChildCount() * 2) - 1 > MAX_QTY_CHECKLISTS) {

            Toast.makeText(this, "You cannot have more than " + MAX_QTY_CHECKLISTS + " to do lists", Toast.LENGTH_SHORT).show();
        }
        else {

            Intent intent = new Intent(this, ChecklistActivity.class);
            intent.putExtra("source", "newNote");
            startActivity(intent);
        }
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {Task.COLUMN_ID, Task.COLUMN_ITEMS, Task.COLUMN_SLASHED, Task.COLUMN_NOTE_TITLE};
        return new CursorLoader(this, NoteContentProvider.CONTENT_URI, projection, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mNoteList = new ArrayList<List<String>>();
        List<List<Integer>> mainColId = new ArrayList<>();
        List<List<String>> mainSlashes = new ArrayList<>();
        mTitleList = new ArrayList<>();

        List<String> oneTitles = new ArrayList<>();
        List<String> oneList = new ArrayList<>();
        List<Integer> oneListId = new ArrayList<>();
        List<String> oneListSlashes = new ArrayList<>();

        if(data != null) {

            data.moveToFirst();
            for(int c = 0; c < data.getCount(); c++) {

                try {

                    String sItems = data.getString(data.getColumnIndexOrThrow(Task.COLUMN_ITEMS));
                    String sColId = data.getString(data.getColumnIndexOrThrow(Task.COLUMN_ID));
                    String sSlashes = data.getString(data.getColumnIndexOrThrow(Task.COLUMN_SLASHED));
                    String sTitles = data.getString(data.getColumnIndexOrThrow(Task.COLUMN_NOTE_TITLE));

                    oneTitles.add(sTitles);
                    oneListSlashes.add(sSlashes);
                    oneListId.add(Integer.parseInt(sColId));
                    oneList.add(sItems);
                    data.moveToNext();
                }
                catch(Exception e) {

                    Log.v("MainActivity.java", e.getMessage());
                }
            }
        }

        int rows;

        if(oneList.size() % 2 == 0) {

            rows = oneList.size() / 2;
        }
        else {

            rows = (oneList.size() / 2) + 1;
        }

        for(int i = 0; i < rows; i++) {

            mTitleList.add(new ArrayList<String>());
            mNoteList.add(new ArrayList<String>());
            mainColId.add(new ArrayList<Integer>());
            mainSlashes.add(new ArrayList<String>());
        }

        for(int i = 0, b = 0; i < oneList.size(); i += 2, b++) {

            mNoteList.get(b).add(oneList.get(i));
            if (oneList.size() > i + 1) {

                mNoteList.get(b).add(oneList.get(i + 1));
            }

            mainColId.get(b).add(oneListId.get(i));
            if (oneListId.size() > i + 1) {

                mainColId.get(b).add(oneListId.get(i + 1));
            }

            mainSlashes.get(b).add(oneListSlashes.get(i));
            if (oneListSlashes.size() > i + 1) {

                mainSlashes.get(b).add(oneListSlashes.get(i + 1));
            }
            mTitleList.get(b).add(oneTitles.get(i));
            if (oneTitles.size() > i + 1) {

                mTitleList.get(b).add(oneTitles.get(i + 1));
            }
        }

        mainCardViewAdapter = new CardViewAdapter(mNoteList, this, mainColId, mainSlashes, mTitleList);
        recycleViewList.setAdapter(mainCardViewAdapter);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v("MainActivity.java", "onLoaderReset method called");
    }

}
