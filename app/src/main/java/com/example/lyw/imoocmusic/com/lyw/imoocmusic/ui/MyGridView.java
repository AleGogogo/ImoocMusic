package com.example.lyw.imoocmusic.com.lyw.imoocmusic.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.lyw.imoocmusic.R;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.com.lyw.imoocmusic.util
        .Util;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.model.WordButtonListener;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.model.WordsButton;

import java.util.ArrayList;

/**
 * Created by LYW on 2016/4/19.
 */
public class MyGridView extends GridView {
    public final static int COUNTS_WORDS = 24;
    private ArrayList<WordsButton> wordsbutList = new ArrayList<WordsButton>();
    private MyAdapter mAdapter;
    private Context mContext;
    private Animation mScaleAnim;
    private WordButtonListener wordButtonListener;
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAdapter = new MyAdapter();
        this.setAdapter(mAdapter);
    }
     public void updataData(ArrayList<WordsButton> list){
        wordsbutList = list;


         //重新加载数据
         setAdapter(mAdapter);
     }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return wordsbutList.size();
        }

        @Override
        public Object getItem(int position) {
            return wordsbutList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           final WordsButton  holder;
            if ( convertView == null ) {
               convertView = Util.getView(mContext, R.layout
                       .select_button_layout);
                //加载动画
                mScaleAnim = AnimationUtils.loadAnimation(mContext,R.anim.scale);
                mScaleAnim.setStartOffset(position*100);
                holder = wordsbutList.get(position);
                holder.mIndex = position;
                holder.mViewButton = (Button) convertView.findViewById(R.id.id_wordButton);
                holder.mViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordButtonListener.onWordButtonClick(holder);
                    }
                });
                convertView.setTag(holder);
            }else {
                holder = (WordsButton) convertView.getTag();
            }
            holder.mViewButton.setText(holder.mWords);
            //播放动画
            convertView.startAnimation(mScaleAnim);
            return convertView;
        }
    }

    /**
     * 注册监听接口
     * @param listener
     */
    public void  rigsteonWordButton(WordButtonListener listener){
        wordButtonListener = listener;
    }
}
