package com.dramalho.itunessearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.dramalho.itunessearch.MainActivity.QUERY_KEY;

public class ItunesMusicListFragment extends Fragment {
    private ItunesItemAdapter mSongAdapter;
    private TextView mTextBox;
    private MediaPlayer mMediaPlayer;
    private String mPlayingUrl;
    private ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_itunes_music_list, container, false);

        mTextBox = (TextView)v.findViewById(R.id.results_view);
        mListView = v.findViewById(R.id.list_view);
        mSongAdapter = new ItunesItemAdapter(getActivity());
        mListView.setAdapter(mSongAdapter);
        String query = getArguments().getString(QUERY_KEY);
        mTextBox.append(query);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music song = (Music) parent.getAdapter().getItem(position);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType(getString(R.string.Type));
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
                shareIntent.putExtra(Intent.EXTRA_TEXT, song.getWebpage());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_message)));
            }
        });

        ItunesMusicSource.get(getContext()).getItunesResults(query, new ItunesMusicSource.ItunesResultsListener() {
            @Override
            public void onSongResponse(List<Music> songList) {
                mSongAdapter.setItems(songList);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle b){
        super.onSaveInstanceState(b);
        b.putString("text", mTextBox.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    private void clickedAudioURL(String url) {
        if (mMediaPlayer.isPlaying()) {
            if (mPlayingUrl.equals(url)) {
                mMediaPlayer.stop();
                mSongAdapter.notifyDataSetChanged();
                return;
            }
        }

        mPlayingUrl = url;
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
        private List<Music> mDataSource;

        public ItunesItemAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<Music> articleList) {
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

            final Music song = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.list_item, parent, false);

            TextView songNameField = (TextView)rowView.findViewById(R.id.song_name);
            TextView artistNameField = (TextView)rowView.findViewById(R.id.artist_name);
            TextView albumNameField = (TextView)rowView.findViewById(R.id.album_name);
            NetworkImageView thumbnailField = (NetworkImageView)rowView.findViewById(R.id.thumbnail);

            songNameField.setText(song.getSongName());
            artistNameField.setText(song.getArtistName());
            albumNameField.setText(song.getAlbumName());
            ImageLoader loader = ItunesMusicSource.get(getContext()).getImageLoader();
            thumbnailField.setImageUrl(song.getCoverArt(), loader);
            String audioSnippet = song.getAudioSnippet();

            final ImageButton playButton = (ImageButton) rowView.findViewById(R.id.play_button);
            final boolean isPlaying = mMediaPlayer.isPlaying() && mPlayingUrl.equals(song.getAudioSnippet());
            // Here, add code to set the play/pause button icon based on isPlaying

            playButton.setImageDrawable((isPlaying) ? getResources().getDrawable(R.drawable.pause_button):
                    getResources().getDrawable(R.drawable.play_button));

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedAudioURL(song.getAudioSnippet());
                }
            });

            return rowView;
        }
    }
}
