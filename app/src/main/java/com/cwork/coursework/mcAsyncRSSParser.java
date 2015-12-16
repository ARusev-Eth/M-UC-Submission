package com.cwork.coursework;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

// <Input type for doInBackground, input type for
// onProgressUpdate, return type for doInBackground & onPostExecute>
public class mcAsyncRSSParser extends AsyncTask<String, Integer, ArrayList<mcRSSDataItem>> {

    //Vars
    private Context appContext;
    private String urlRSS;

    //Constructor
    public mcAsyncRSSParser(Context currentAppContext, String urlRSSt){
        appContext = currentAppContext;
        urlRSS = urlRSSt;
    }

    //Executed on UI thread on task start
    protected void onPreExecution() {
        Toast.makeText(appContext, "Parsing Started!",
                Toast.LENGTH_SHORT).show();
    }

    //Creates instance of mcRSSDataItem and the Parser
    //Parses the data and returns the parsed data on call
    protected ArrayList<mcRSSDataItem> doInBackground(String... params){
        ArrayList<mcRSSDataItem> items = new ArrayList<mcRSSDataItem>();
        mcRSSParser rssParser = new mcRSSParser();

        try {
            items = rssParser.parseRSSData(urlRSS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return items;
    }

    //Indicates parsing ended
    protected void onPostExecute(mcRSSDataItem result) {
        Toast.makeText(appContext,"Parsing done!",
                Toast.LENGTH_SHORT).show();
    }
}
