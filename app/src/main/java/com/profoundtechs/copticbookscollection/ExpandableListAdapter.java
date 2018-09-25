package com.profoundtechs.copticbookscollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 3/17/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> bookTitles;
    private HashMap<String,List<String>> subTitles;

    public ExpandableListAdapter(Context context, List<String> bookTitles, HashMap<String, List<String>> subTitles) {
        this.context = context;
        this.bookTitles = bookTitles;
        this.subTitles = subTitles;
    }

    @Override
    public int getGroupCount() {
        return this.bookTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.subTitles.get(this.bookTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.bookTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.subTitles.get(this.bookTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String groupText=(String)getGroup(groupPosition);

        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.layout_drawer_group_listview,null);
        }
        TextView tvBookTitles=(TextView)convertView.findViewById(R.id.tvBookTitles);
        //tvBookTitles.setTypeface(null, Typeface.BOLD);
        tvBookTitles.setText(groupText);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText=(String)getChild(groupPosition,childPosition);

        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.layout_drawer_child_listview,null);
        }
        TextView tvSubtitles=(TextView)convertView.findViewById(R.id.tvSubtitles);
        tvSubtitles.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
