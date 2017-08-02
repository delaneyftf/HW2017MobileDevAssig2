package com.example.dramalho.itunessearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public final static String KEY = "search";
    String search_term = "panicatthedisco";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private Fragment createCustomFragment(Fragment f, String search_term){
        Bundle bundle = new Bundle();
        bundle.putString(KEY, search_term);
        f.setArguments(bundle);
        return f;
    }
}
