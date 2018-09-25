package com.profoundtechs.copticbookscollection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HP on 3/3/2018.
 */

public class ContentAdapter extends BaseAdapter {
    Context context;
    List<Content> contents;

    public ContentAdapter(Context context, List<Content> contents) {
        this.context = context;
        this.contents = contents;
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Object getItem(int position) {
        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contents.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context,R.layout.item_list_view,null);
        Bitmap bitmapt = null;
        Bitmap bitmapb = null;
        TextView tvContent=(TextView) view.findViewById(R.id.tvContent);
        TextView tvChapter=(TextView) view.findViewById(R.id.tvChapter);
        ImageView ivImageTop = (ImageView) view.findViewById(R.id.ivImageTop);
        ImageView ivImageBottom = (ImageView) view.findViewById(R.id.ivImageBottom);
        tvContent.setText(contents.get(position).getContent());
        tvChapter.setText(contents.get(position).getChapter());
        byte[] imaget = contents.get(position).getImaget();
        byte[] imageb = contents.get(position).getImageb();
        if (imaget!=null){
            bitmapt = BitmapFactory.decodeByteArray(imaget,0,imaget.length);
        }
        ivImageTop.setImageBitmap(bitmapt);
        if (imageb!=null){
            bitmapb = BitmapFactory.decodeByteArray(imageb,0,imageb.length);
        }
        ivImageBottom.setImageBitmap(bitmapb);
        return view;
    }
}
