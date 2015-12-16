package com.cwork.coursework;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button mapScrn;
    private Button refreshRSS;
    private Button drawBtn;
    private ListView feedRSS;
    private String rssURL;
    private FragmentManager fmAboutDialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Drawing
        drawBtn = (Button)findViewById(R.id.drawBtn);
        drawBtn.setOnClickListener(this);

        //About dialogue
        fmAboutDialogue = this.getFragmentManager();

        //Map screen button
        mapScrn = (Button) findViewById(R.id.mapBtn);
        mapScrn.setOnClickListener(this);

        //----------------------RSS FEED------------------------//
        listRSSView();

        refreshRSS = (Button) findViewById(R.id.refreshBtn);
        refreshRSS.setOnClickListener(this);
        //----------------------RSS FEED------------------------//
    }

    //Handles everything rss feed related
    private void listRSSView(){

        ArrayList<mcRSSDataItem> items = null;                                  //ArrayList holding feedRSS ListView items
        //rssURL = "http://feeds.bbci.co.uk/news/world/europe/rss.xml";           //The rss feed's URL
        rssURL = "http://feeds.bbci.co.uk/news/rss.xml";
        mcAsyncRSSParser rssAsyncParser = new mcAsyncRSSParser(this, rssURL);   //Instantiate the parser on the UI thread
        try {
            items = rssAsyncParser.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        feedRSS = (ListView)findViewById(R.id.feedRSSList);                     //Find the ListView
        CusAdapter adapter = new CusAdapter(this,items);                        //Instantiate the custom adapter
        feedRSS.setAdapter(adapter);                                            //Set the adapter

        //Remove the first item which is just a title - due to nature of RSS feed.
        items.remove(0);                                                        //Remove the first item from the ArrayList
        adapter.notifyDataSetChanged();                                         //Update the ListView by notifying the adapter of a change

    }


    public void onClick(View v){

        //Re-runs the rss feed method effectively refreshing the feed
        //as the ArrayAdapter.notifyDataSetChanged() method is called
        if(v == refreshRSS){
            listRSSView();
        }
        //Creates a new intent and starts the map activity.
        if(v == mapScrn){
            Intent mcMap = new Intent(this, mcMapActivity.class);
            this.startActivity(mcMap);
        }
        if(v == drawBtn){
            Intent mcDraw = new Intent(this, mcFingerPaint.class);
            this.startActivity(mcDraw);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mc_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mAbout:
                DialogFragment mcAboutDlg = new mcAboutDialog();
                mcAboutDlg.show(fmAboutDialogue,"mc_about_dlg");
                return true;
            case R.id.mQuit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
