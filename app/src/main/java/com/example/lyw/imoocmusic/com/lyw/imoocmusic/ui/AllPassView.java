package com.example.lyw.imoocmusic.com.lyw.imoocmusic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.lyw.imoocmusic.R;

/**
 * Created by LYW on 2016/4/25.
 */
public class AllPassView extends Activity {
    private FrameLayout mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passview);
        mView = (FrameLayout) findViewById(R.id.id_title_framlay);
        mView.setVisibility(View.GONE);

    }
}
