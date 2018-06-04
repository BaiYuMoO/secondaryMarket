package com.xw.h2market.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xw.h2market.MainActivity;
import com.xw.h2market.R;

/**
 * Created by Shinelon on 2018/5/6.
 */

public class Fragmentimage03 extends Fragment {
    private Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmentimage03, null);
        button = (Button) view.findViewById(R.id.id_btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SharedPreferences.Editor sp = getActivity().getSharedPreferences("insert", getActivity().MODE_PRIVATE).edit();
                sp.putInt("key", 1);
                sp.commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
