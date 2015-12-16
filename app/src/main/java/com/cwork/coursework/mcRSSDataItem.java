package com.cwork.coursework;


import java.io.Serializable;

//Self-explanatory - objects that hold rss feed data
public class mcRSSDataItem implements Serializable {

    private String itemTitle;
    private String itemDesc;
    private String itemLink;

    public String getItemTitle() { return this.itemTitle; }
    public void setItemTitle(String itemTitle) { this.itemTitle = itemTitle; }

    public String getItemDesc() { return this.itemDesc; }
    public void setItemDesc(String itemDesc) { this.itemDesc = itemDesc; }

    public String getItemLink() { return this.itemLink; }
    public void setItemLink(String itemLink) { this.itemLink = itemLink; }

    public mcRSSDataItem() {
        this.itemTitle = "";
        this.itemDesc = "";
        this.itemLink = "";
    }

    @Override
    public String toString() {
        String returnData;
        returnData = "mcRSSDataItem[itemTitle=" + itemTitle;
        returnData = ", itemDesc=" + itemDesc;
        returnData = ", itemLink=" + itemLink + "]";
        return returnData;
    }

}
