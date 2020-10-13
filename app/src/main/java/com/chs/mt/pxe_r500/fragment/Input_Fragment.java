package com.chs.mt.pxe_r500.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chs.mt.pxe_r500.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Input_Fragment extends Fragment {


    public Input_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chs_fragment_delay_frs, container, false);
        initData();
        initView(view);
        initClick();
        return view;
    }

    private void initData(){

    }
    private void initView(View view){

    }
    private void initClick(){

    }

    //刷新页面UI
    public void FlashPageUI(){
        FlashChannelUI();
    }
    //刷新通道UI
    public void FlashChannelUI(){

    }

}
