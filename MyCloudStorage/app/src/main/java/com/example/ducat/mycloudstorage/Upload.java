package com.example.ducat.mycloudstorage;

/**
 * Created by Ducat on 11/29/2017.
 */

public class Upload
{


    public String name;
    public String url;

    public Upload()
    {

    }
    public Upload(String name, String url) {
        this.name = name;
        this.url = url;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
