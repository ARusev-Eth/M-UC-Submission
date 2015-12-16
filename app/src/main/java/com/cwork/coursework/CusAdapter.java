package com.cwork.coursework;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.util.ArrayList;

/**
 * Custom adapter. Overrides the getView method of the ArrayAdapter class
 * inflating a custom view for each row of the ListView it's set to.
 */
public class CusAdapter extends ArrayAdapter<mcRSSDataItem> implements View.OnClickListener {

    private Context context;
    private ArrayList<mcRSSDataItem> items;

    //Constructor
    public CusAdapter(Context context, ArrayList<mcRSSDataItem> items) {
        super(context, R.layout.row_view, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        //Instantiate the LayoutInflater and set a View
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_view, parent, false);

        //View setup
        TextView textView1 = (TextView) rowView.findViewById(R.id.line1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.line2);

        //Value setup
        textView1.setText(items.get(position).getItemTitle());
        textView2.setText(items.get(position).getItemDesc());

        Button linkBtn = (Button) rowView.findViewById(R.id.linkBtn);
        linkBtn.setOnClickListener(this);

        linkBtn.setTag(new Integer(position));

        return rowView;
    }

    @Override
    public void onClick(View v){
        //On click opens the link specified in items.get(1).getItemLink()
        //using the native device browser.
        Log.e("getView()", "I was clicked");
        Log.e("OnClick","" + v.getTag());
        int tempPos;
        tempPos = (Integer) v.getTag();

        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(items.get(tempPos).getItemLink()));
        context.startActivity(intent);

    }


}
