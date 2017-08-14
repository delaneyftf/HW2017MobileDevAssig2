package com.dramalho.itunessearch;

/**
 * Created by Guest Account on 8/14/2017.
 */

import android.app.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class Music {

    protected String mSongName;
    protected String mArtistName;
    protected String mAlbumName;
    protected String mCoverArtURL;
    protected String mWebpageUrl;
    protected String mAudioSnippetUrl;

    public Music(JSONObject jObj){
        try{
            mSongName = jObj.getString("trackName");
            mArtistName = jObj.getString("artistName");
            mAlbumName = jObj.getString("collectionName");
            mCoverArtURL = jObj.getString("artworkUrl60");
            mWebpageUrl = jObj.getString("trackViewUrl");
            mAudioSnippetUrl = jObj.getString("previewUrl");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getSongName() {
        return mSongName;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public String getCoverArt() {
        return mCoverArtURL;
    }

    public String getWebpage() {
        return mWebpageUrl;
    }

    public String getAudioSnippet() {return mAudioSnippetUrl;}


}