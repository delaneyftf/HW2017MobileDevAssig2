package com.dramalho.itunessearch;

/**
 * Created by Guest Account on 8/14/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import static com.dramalho.itunessearch.MainActivity.QUERY_KEY;

public class WaitingFragment extends Fragment {

    TextView mQueryField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wait_screen, container, false);
        mQueryField = (TextView)v.findViewById(R.id.query_field);
        Bundle bundle = getArguments();
        mQueryField.append(bundle.getString(QUERY_KEY));
        return v;
    }

}
