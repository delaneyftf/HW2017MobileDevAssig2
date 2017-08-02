package com.example.dramalho.itunessearch;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.dramalho.itunessearch.MainActivity.KEY;


public class SearchListFragment extends Fragment {

    private ItunesItemAdapter mSongAdapter;
    private MediaPlayer mMediaPlayer;
    private String mCurrentlyPlayingUrl;
    private ListView mListView;
    private TextView mTextField;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_list, container, false);

        mTextField = (TextView)v.findViewById(R.id.textone);
        mListView = v.findViewById(R.id.list_view);

        mSongAdapter = new ItunesItemAdapter(getActivity());
        mListView.setAdapter(mSongAdapter);
        String search_term = getArguments().getString(KEY);
        mTextField.append(search_term);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


        // I honestly don't even know.
        SongSource.get(getContext()).getSongs(new SongSource.SongListener(), search_term){
            @Override
            public void onSongResponse(List<Song> songList) {
                mSongAdapter.setItems(songList);
            }
        });


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle b){
        super.onSaveInstanceState(b);
        b.putString("text", mTextField.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    private void clickedAudioURL(String url) {
        if (mMediaPlayer.isPlaying()) {
            if (mCurrentlyPlayingUrl.equals(url)) {
                mMediaPlayer.stop();
                mSongAdapter.notifyDataSetChanged();
                return;
            }
        }

        mCurrentlyPlayingUrl = url;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mSongAdapter.notifyDataSetChanged();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mSongAdapter.notifyDataSetChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ItunesItemAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Song> mDataSource;

        public ItunesItemAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<Song> articleList) {
            mDataSource.clear();
            mDataSource.addAll(articleList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Song song = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.fragment_song_list, parent, false);

            TextView songNameField = (TextView)rowView.findViewById(R.id.textone);

            NetworkImageView thumbnailField = (NetworkImageView)rowView.findViewById(R.id.thumbnail);

            songNameField.setText(song.getTrackName());
            ImageLoader loader = SongSource.get(getContext()).getImageLoader();
            thumbnailField.setImageUrl(song.getArtworkUrl60(), loader);
            String audioSnippet = song.getPreviewUrl();

            final Button playButton = (Button) rowView.findViewById(R.id.play_button);
            final boolean isPlaying = mMediaPlayer.isPlaying() && mCurrentlyPlayingUrl.equals(song.getPreviewUrl());
            // Here, add code to set the play/pause button icon based on isPlaying

            return rowView;
        }
    }
}