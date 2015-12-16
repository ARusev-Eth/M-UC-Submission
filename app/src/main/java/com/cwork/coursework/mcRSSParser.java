package com.cwork.coursework;



import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParser;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class mcRSSParser {

    private mcRSSDataItem RSSDataItem;

    public ArrayList<mcRSSDataItem> parseRSSDataItem(XmlPullParser theParser, int theEventType)
    {

        ArrayList<mcRSSDataItem> tempItems = new ArrayList<mcRSSDataItem>();    //Temporary ArrayList to hold parsed data and to be returned.
        RSSDataItem = new mcRSSDataItem();                          //Initial mcRSSDataItem object to hold initial parsed data

        try
        {
            while (theEventType != XmlPullParser.END_DOCUMENT)
            {
                //Unless the mcRSSDataItem object has a Link value keep using it to store parsed data
                //As Link is after title and description in the RSS feed, this ensures the object
                //has values for all three parameters, avoiding "empty" rows in the ListView.
                if(RSSDataItem.getItemLink() != ""){
                    tempItems.add(RSSDataItem);
                    RSSDataItem = new mcRSSDataItem();
                }

                // Found a start tag
                if(theEventType == XmlPullParser.START_TAG)
                {
                    // Check which Tag has been found
                    if (theParser.getName().equalsIgnoreCase("title"))
                    {
                        // Now just get the associated text
                        String temp = theParser.nextText();
                        // store data in class
                        RSSDataItem.setItemTitle(temp);
                    }
                    else
                        // Check which Tag we have
                        if (theParser.getName().equalsIgnoreCase("description"))
                        {
                            // Now just get the associated text
                            String temp = theParser.nextText();
                            // store data in class
                            RSSDataItem.setItemDesc(temp);

                        }
                        else
                            // Check which Tag we have
                            if (theParser.getName().equalsIgnoreCase("link"))
                            {
                                // Now just get the associated text
                                String temp = theParser.nextText();
                                // store data in class
                                RSSDataItem.setItemLink(temp);

                            }
                    else {

                            }
                }

                // Get the next event
                theEventType = theParser.next();

            } // End of while
        }
        catch (XmlPullParserException parserExp1)
        {
            Log.e("MyTag","Parsing error" + parserExp1.toString());
        }

        catch (IOException parserExp1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        for(int i = 0;i < tempItems.size(); i++) {

        }

        return tempItems;               //Return ArrayList containing data objects
    }

    public ArrayList<mcRSSDataItem> parseRSSData(String RSSItemsToParse) throws MalformedURLException {
        URL rssURL = new URL(RSSItemsToParse);
        InputStream rssInputStream;
        ArrayList<mcRSSDataItem> temp = new ArrayList<mcRSSDataItem>();
        try
        {
            XmlPullParserFactory parseRSSfactory = XmlPullParserFactory.newInstance();
            parseRSSfactory.setNamespaceAware(true);
            XmlPullParser RSSxmlPP = parseRSSfactory.newPullParser();
            String xmlRSS = getStringFromInputStream(getInputStream(rssURL), "UTF-8");
            RSSxmlPP.setInput(new StringReader(xmlRSS));
            int eventType = RSSxmlPP.getEventType();

            temp = parseRSSDataItem(RSSxmlPP,eventType);

        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        return temp;
    }

    public InputStream getInputStream(URL url) throws IOException
    {
        return url.openConnection().getInputStream();
    }

    public static String getStringFromInputStream(InputStream stream, String charsetName) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, charsetName);
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}