package watsalacanoa.todolisttest.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import watsalacanoa.todolisttest.ChecklistActivity;
import watsalacanoa.todolisttest.R;

public class FinishedItemsArrayAdapter extends ArrayAdapter<String> {

    public static final String TAG = FinishedItemsArrayAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<String> mArrayList;
    boolean wantsSlash;
    ChecklistActivity mChecklistActivity;

    public FinishedItemsArrayAdapter
            (ChecklistActivity context, ArrayList<String> arrayList, boolean slashes){
        super(context, R.layout.item_row,arrayList);
        mContext = context;
        mArrayList = arrayList;
        mChecklistActivity = context;
        wantsSlash = slashes;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_row, parent, false);
            holder = new ViewHolder();

            holder.itemName = (TextView)convertView.findViewById(R.id.itemText);

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
                mChecklistActivity.deleteFinishedItem(position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChecklistActivity.editFinishedItem(position);
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        TextView itemName;
        ImageView edit;
        ImageView delete;
    }
}

