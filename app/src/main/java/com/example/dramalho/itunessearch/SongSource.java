package com.example.dramalho.itunessearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class SongSource {

    private static SongSource sItunesSongSourceInstance;

    public interface SongListener {
        void onSongResponse(List<Song> songList);
    }

    private final static int IMAGE_CACHE_COUNT = 100;



    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static SongSource get(Context context) {
        if (sItunesSongSourceInstance == null) {
            sItunesSongSourceInstance = new SongSource(context);
        }
        return sItunesSongSourceInstance;
    }

    private SongSource(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(IMAGE_CACHE_COUNT);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

    }

    public void getSongs(SongListener songListener, String search_term) {
        final SongListener songListenerInternal = songListener;

        String url = "https://itunes.apple.com/search?term=" + search_term + "&entity=musicTrack";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Song> songList = new ArrayList<Song>();
                            JSONArray songObjArr = response.getJSONArray("results");
                            for (int i = 0; i < songObjArr.length(); i++){
                                songList.add(new Song(songObjArr.getJSONObject(i)));
                            }
                            songListenerInternal.onSongResponse(songList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            songListenerInternal.onSongResponse(null);
                            Toast.makeText(mContext, "Could not get songs.", Toast.LENGTH_SHORT);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        songListenerInternal.onSongResponse(null);
                        Toast.makeText(mContext, "Could not get songs.", Toast.LENGTH_SHORT);
                    }
                });

        mRequestQueue.add(jsonObjRequest);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}

