package watsalacanoa.todolisttest.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import watsalacanoa.todolisttest.ChecklistActivity;
import watsalacanoa.todolisttest.R;


public class ItemsArrayAdapter extends ArrayAdapter<String> {

    public static final String TAG = ItemsArrayAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<String> mArrayList;
    boolean wantsSlash;
    ChecklistActivity mChecklistActivity;
    EditText mNewItemText;

    final int INVALID_ID = -1;

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public ItemsArrayAdapter
            (ChecklistActivity context, ArrayList<String> arrayList, boolean slashes){
        super(context, R.layout.item_row,arrayList);
        mContext = context;
        mArrayList = arrayList;
        mChecklistActivity = context;
        wantsSlash = slashes;
        update();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_row, parent, false);
            holder = new ViewHolder();

            holder.itemName = (TextView)convertView.findViewById(R.id.itemText);

            for (int i = 0; i < mArrayList.size(); ++i) {
                mIdMap.put(mArrayList.get(i), i);
            }
            Log.d("Log", "Crea Delete");
            holder.delete = (ImageView)convertView.findViewById(R.id.delete_item);
            holder.edit = (ImageView)convertView.findViewById(R.id.edit_item);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemName.setText(mArrayList.get(position));
        if(wantsSlash){
            holder.itemName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChecklistActivity.deleteItem(position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChecklistActivity.editItem(position);
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        TextView itemName;
        ImageView edit;
        ImageView delete;
    }

    @Override
    public long getItemId(int position) {
        Long idToReturn = (long) INVALID_ID;
        if (position >= 0 && position <= mIdMap.size()-1) {
            try {
                String item = getItem(position);
                idToReturn = (long) mIdMap.get(item);
            }
            catch(Exception e){}
        }

        return idToReturn;
    }


    public void update(){
        for (int i = 0; i < mArrayList.size(); ++i) {
            mIdMap.put(mArrayList.get(i), i);
        }
    }

    @Override
    public boolean hasStableIds() {
        return android.os.Build.VERSION.SDK_INT < 20;
    }

}

