package com.example.dramalho.itunessearch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dramalho on 8/1/17.
 * Data model class
 */

public class Song {
    protected String mKind;
    protected String mTrackName;
    protected String mArtistName;
    protected String mCollectionName;
    protected String mPreviewUrl;
    protected String mArtworkUrl60;
    protected String mTrackViewUrl;


    public Song(JSONObject songObj) {
        try {
            // We expect that these keys will be in the response.
            mKind = songObj.getString("kind");
            mTrackName = songObj.getString("trackName");
            mArtistName = songObj.getString("artistName");
            mCollectionName = songObj.getString("collectionName");
            mPreviewUrl = songObj.getString("previewUrl");
            mArtworkUrl60 = songObj.getString("artworkUrl60");
            mTrackViewUrl = songObj.getString("trackViewUrl");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getKind() {
        return mKind;
    }

    public String getTrackName() {
        return mTrackName;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public String getCollectionName() {
        return mCollectionName;
    }

    public String getPreviewUrl() {
        return mPreviewUrl;
    }

    public String getArtworkUrl60() {
        return mArtworkUrl60;
    }

    public String getTrackViewUrl() {
        return mTrackViewUrl;
    }

}
