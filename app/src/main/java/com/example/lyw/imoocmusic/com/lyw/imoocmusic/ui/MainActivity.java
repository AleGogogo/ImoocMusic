package com.example.lyw.imoocmusic.com.lyw.imoocmusic.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyw.imoocmusic.R;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.com.lyw.imoocmusic.util
        .PlayMedia;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.com.lyw.imoocmusic.util
        .Util;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.data.Const;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.model.IAlertDialogListener;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.model.Song;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.model.WordButtonListener;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.model.WordsButton;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements WordButtonListener {

    //答案是正确 错误 不完整
    public static final int STATE_ANSWER_RIGHT = 1;
    public static final int STATE_ANSWER_WRONG = 2;
    public static final int STATE_ANSER_UNCOM = 3;

    //设置文字闪烁的间断时间
    public static final int SPASH_TIMES = 6;

    //设置显示对话框状态的常量
    public static final int ID_DAILOG_DELET_WORD = 1;
    public static final int ID_DAILOG_OK_WORD = 2;
    public static final int ID_DAILOG_LACK_WORD = 3;

    private Animation mPanAnim;
    private LinearInterpolator mLinearPanIn;

    private Animation mBarInAnim;
    private LinearInterpolator mLinearInBar;

    private Animation mBaroutAnim;
    private LinearInterpolator mLinearOutBar;

    private ArrayList<WordsButton> mAllWordList;
    private ArrayList<WordsButton> mAllWordSelect;
    private MyGridView myGridView;
    private LinearLayout mLayoutContainer;
    //通关界面
    private View mPassLayout;
    //当前的播放的歌曲
    private Song mCurrentSong;
    //当前索引
    private int mCurrentStageIndex = -1;


    //当前过关索引
    private TextView mCurrentStagePassView ;
    private TextView mCurrentStageView;

    //当前过关歌曲
    private TextView mCurrentPassViewTxt;
    //当前金币数量
    private int mCurrentCoins = Const.CONST_CION_NUM;
    private TextView mViewCurrentCoins;


    private ImageButton mPlayButton;
    private ImageView mViewPan;
    private ImageView mViewBar;
    private boolean misRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniView();
        //读取数据
        int[] data = Util.loadData(MainActivity.this);
        mCurrentStageIndex = data[Const.INDEX_LOAD_DATA_STAGE];
        mCurrentCoins = data[Const.INDEX_LOAD_DATA_COINS];

        initCurrentStateData();
        //处理删除按钮事件
        handledeletWord();
        //处理提示按钮事件
        handleTipButton();
    }

    private void iniView() {
        mViewPan = (ImageView) findViewById(R.id.id_pan_imageview1);
        mViewBar = (ImageView) findViewById(R.id.id_pan_bar);
        myGridView = (MyGridView) findViewById(R.id.id_mygridview);
        mViewCurrentCoins = (TextView) findViewById(R.id.id_title_tv);
        mViewCurrentCoins.setText(mCurrentCoins + "");
        //为啥这里用了个监听？
        myGridView.rigsteonWordButton(this);

        mLayoutContainer = (LinearLayout) findViewById(R.id
                .id_wordselect_layout);
        mPlayButton = (ImageButton) findViewById(R.id.id_pan_but_start);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerBar();
            }
        });
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mLinearPanIn = new LinearInterpolator();
        mPanAnim.setInterpolator(mLinearPanIn);
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewBar.setAnimation(mBaroutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mLinearInBar = new LinearInterpolator();
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mLinearInBar);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPan.setAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBaroutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_0);
        mLinearOutBar = new LinearInterpolator();
        mBaroutAnim.setFillAfter(true);
        mBaroutAnim.setInterpolator(mLinearOutBar);
        mBaroutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                misRunning = false;
                mPlayButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private void handlerBar() {
        if (mViewBar != null) {
            if (!misRunning) {
                misRunning = true;

                //开始拨杆进入动画
                mViewBar.setAnimation(mBarInAnim);

                //GONE 与 INVISIBLE 差太多！！！
                mPlayButton.setVisibility(View.GONE);

                //开始播放音乐
                PlayMedia.playSong(MainActivity.this,mCurrentSong.getSongFileName());
            }
        }
    }

    @Override
    protected void onPause() {
        mViewPan.clearAnimation();
        //保存游戏
        Util.saveData(MainActivity.this,mCurrentStageIndex-1,mCurrentCoins);
        //停止播放音乐
        PlayMedia.stopPlay(MainActivity.this);
        super.onPause();

    }

    private Song loadSongInfo(int index) {
        Song s = new Song();
        String[] stage = Const.SONG_INFO[index];
        s.setSongFileName(stage[Const.INDEXT_FILE_NAME]);
        s.setSongName(stage[Const.INDEXT_nAME_NAME]);
        Log.d("TAG", "歌名是" + s.getSongName());
        return s;
    }

    private void initCurrentStateData() {

        //初始化歌曲文字
        mCurrentSong = loadSongInfo(++mCurrentStageIndex);
        //初始化已选择文字
        mAllWordSelect = initAllSelect();
        //每次到新一关的时候清除原来的答案
        mLayoutContainer.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
        for (int i = 0; i < mAllWordSelect.size(); i++) {
            mLayoutContainer.addView(
                    mAllWordSelect.get(i).mViewButton,
                    lp);
        }
        mCurrentStageView = (TextView) findViewById(R.id.id_framelayout_tvt);
        mCurrentStageView.setText((mCurrentStageIndex+1)+"");
        //获取数据
        mAllWordList = initAllWords();
        //更新数据---myGridView
        myGridView.updataData(mAllWordList);
    }

    private void stateComput(int state) {
        Log.d("TAG","state is "+state);
        if (state == STATE_ANSWER_WRONG) {
            sparkWords();
        } else if (state == STATE_ANSWER_RIGHT) {
            setPassLayout();
        } else if (state == STATE_ANSER_UNCOM) {
            for (int i = 0; i < mAllWordSelect.size(); i++) {
                mAllWordSelect.get(i).mViewButton.
                        setTextColor(Color.WHITE);
            }
        }

    }

    /**
     * 处理过关逻辑
     */
    private void setPassLayout() {
        mPassLayout = (LinearLayout) this.findViewById(R.id.id_passgame);
        mPassLayout.setVisibility(View.VISIBLE);
        //停止未完成的动画
        mViewPan.clearAnimation();

        //停止播放音乐
        PlayMedia.stopPlay(MainActivity.this);

        //开始播放音效
        //PlayMedia.
        mCurrentStagePassView = (TextView) findViewById(R.id.id_id_passtvt);
        if (mCurrentStagePassView!=null){
            mCurrentStagePassView.setText(""+mCurrentStageIndex);
        }
        //显示歌曲名字
        mCurrentPassViewTxt = (TextView) findViewById(R.id.id_passviewname);
        if (mCurrentPassViewTxt!=null){
            mCurrentPassViewTxt.setText(mCurrentSong.getSongName());
        }
        //下一关界面处理
        ImageButton butPass = (ImageButton) findViewById(R.id.id_next);
        butPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (judeAppPass()){
                 Util.startActivity(MainActivity.this,AllPassView.class);
                }else
                //开始新一关
                    mPassLayout.setVisibility(View.GONE);
                    //重新加载数据
                initCurrentStateData();
            }
        });

    }

    /**
     * 判断是否到最后一关
     */
    private boolean judeAppPass(){
        return( mCurrentStageIndex == Const.SONG_INFO.length-1);
    }

    private void sparkWords() {
        TimerTask task = new TimerTask() {
            boolean isChange = false;
            int sparktime = 0;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (++sparktime > SPASH_TIMES) {
                            return;
                        }
                        for (int i = 0; i < mAllWordSelect.size(); i++) {
                            mAllWordSelect.get(i).mViewButton.
                                    setTextColor(isChange ? Color.RED : Color
                                            .WHITE);
                        }
                        isChange = !isChange;
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1, 150);
    }


    private int checkAnswer(ArrayList<WordsButton> mAllWordSelect) {
        int a = 0;
        for (int i = 0; i < mAllWordSelect.size(); i++) {
            if (mAllWordSelect.get(i).mWords.length() == 0) {
                return STATE_ANSER_UNCOM;
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(mAllWordSelect.get(i).mWords);
                a = (sb.equals(mCurrentSong.toString()) ?
                        STATE_ANSWER_RIGHT : STATE_ANSWER_WRONG);
            }
        }
        return a;
    }

    /**
     * 初始化已选择文字??
     */
    private ArrayList<WordsButton> initAllWords() {
        ArrayList<WordsButton> data = new ArrayList<WordsButton>();
        //获取待选文字
        String[] words = generateWords();

        for (int i = 0; i < myGridView.COUNTS_WORDS; i++) {
            WordsButton wordsButton = new WordsButton();
            wordsButton.mWords = words[i];
            data.add(wordsButton);
            Log.d("TAG", "data 的第??个字??" + data.get(0).toString());
        }
        return data;
    }

    /**
     * 初始化已选文字框
     */
    private ArrayList<WordsButton> initAllSelect() {
        ArrayList<WordsButton> data = new ArrayList<WordsButton>();
        for (int i = 0; i < mCurrentSong.getSongNameLenth(); i++) {
            Log.d("TAG", mCurrentSong.getSongNameLenth() + "");
            View v = Util.getView(MainActivity.this, R.layout
                    .select_button_layout);
            final WordsButton holder = new WordsButton();
            holder.mViewButton = (Button) v.findViewById(R.id.id_wordButton);
            holder.mViewButton.setText("");
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.misVisible = false;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearTheAnswer(holder);
                }
            });
            data.add(holder);
        }
        return data;
    }

    /**
     * 生成24个文字
     */
    private String[] generateWords() {
        Random random = new Random();
        String[] words = new String[myGridView.COUNTS_WORDS];
        for (int i = 0; i < mCurrentSong.getSongNameLenth(); i++) {
            words[i] = mCurrentSong.getNameCharacters()[i] + "";
        }
        for (int i = mCurrentSong.getSongNameLenth(); i < myGridView
                .COUNTS_WORDS; i++) {
            words[i] = getRandomChar() + "";
        }

        //打乱文字
        for (int i = myGridView.COUNTS_WORDS - 1; i >= 0; i--) {
            int index = random.nextInt(i + 1);
            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;

        }
        return words;
    }

    /**
     * 生成随机汉字
     */
    private char getRandomChar() {
        String str = "";
        int highpos;
        int lowos;
        Random random = new Random();
        highpos = (176 + Math.abs(random.nextInt(39)));
        lowos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highpos)).byteValue();
        b[1] = (Integer.valueOf(lowos)).byteValue();
        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }

    @Override
    public void onWordButtonClick(WordsButton wordsButton) {
        int state = 0;
        showSeleWords(wordsButton);
        state = checkAnswer(mAllWordSelect);
        stateComput(state);
    }

    private void showSeleWords(WordsButton wordsButton) {
        for (int i = 0; i < mAllWordSelect.size(); i++) {
            if (mAllWordSelect.get(i).mWords.length() == 0) {
                mAllWordSelect.get(i).mViewButton.setText(wordsButton.mWords);
                mAllWordSelect.get(i).misVisible = true;
                mAllWordSelect.get(i).mIndex = wordsButton.mIndex;
                mAllWordSelect.get(i).mWords = wordsButton.mWords;
                setButtonVisible(wordsButton, View.GONE);
                break;
            }
        }
    }

    //设置24个文字中被选定的文字的可见性
    private void setButtonVisible(WordsButton button, int visible) {
        mAllWordList.get(button.mIndex).mViewButton.setVisibility(visible);
        mAllWordList.get(button.mIndex).misVisible = (visible == View
                .VISIBLE) ? true : false;
    }

    //清除已选文字
    private void clearTheAnswer(WordsButton button) {
        button.mWords = "";
        button.mViewButton.setText("");
        button.misVisible = false;
        setButtonVisible(button, View.VISIBLE);
    }

    /**
     * 判断金币是加还是减
     * true 增加或者减少成功
     */
    private boolean handleCoins(int data) {
        //判断当前总的金币数量是否可被减少
        if (mCurrentCoins + data > 0) {
            mCurrentCoins += data;
            mViewCurrentCoins.setText("" + mCurrentCoins);
            return true;
        } else {
            //金币不足
            return false;
        }


    }

    /**
     * 从配置文件中读取删除金币数
     */
    private int getDeletConins() {
        return this.getResources().getInteger(R.integer.pay_delet_word);
    }

    /**
     * 从配置文件中读取提示金币数
     *
     * @return
     */
    private int getTipCoins() {
        return this.getResources().getInteger(R.integer.pay_tip_anwser);
    }

    /**
     * 处理删除待选文字事件
     */
    private void handledeletWord() {
        ImageButton button = (ImageButton) findViewById(R.id.id_floatlyaout_linerlay_fram_imgbut1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showConfromDailog(ID_DAILOG_DELET_WORD);
            }
        });

    }
    /**
     * 处理提示事件
     */
   private void handleTipButton(){
      ImageButton button = (ImageButton) findViewById(R.id.id_floatlyaout_linerlay_fram_imgbut2);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              showConfromDailog(ID_DAILOG_OK_WORD);
           }
       });
  }
    /**
    * 自定义AlertDailog事件响应
    */
    //删除错误答案
    private IAlertDialogListener mButDeletListener = new IAlertDialogListener() {


        @Override
        public void onClick() {
            deletOneWord();
        }
    };

    //答案提示
    private IAlertDialogListener mButOkListener = new IAlertDialogListener() {
        @Override
        public void onClick() {
            tipAnswer();
        }
    } ;

    //金币不足
    private IAlertDialogListener mButLackListener = new IAlertDialogListener() {
        @Override
        public void onClick() {

        }
    };

    /**
     *判断弹出的对话框属于哪种状态,并显示
     */
    private void showConfromDailog(int data){
        switch (data){
            case ID_DAILOG_DELET_WORD:
                Util.showDialog(this,"确认花掉"+getDeletConins()+"个金币" +
                        "去掉一个错误答案",mButDeletListener);
                break;
            case ID_DAILOG_OK_WORD:
                Util.showDialog(this,"确认花掉"+getTipCoins()+"个金币获得一个文字提示",
                        mButOkListener);
                break;
            case ID_DAILOG_LACK_WORD:
                Util.showDialog(this,"金币不足去商店购买？",mButLackListener);
                break;
        }
    }

    /**
     * 自动选择一个答案
     */
    private void tipAnswer() {
        boolean tipword = false;

        for (int i = 0; i < mAllWordSelect.size(); i++) {
            if (mAllWordSelect.get(i).mWords.length() == 0) {
                onWordButtonClick(findIsAnswer(i));
                tipword = true;

                if (handleCoins(-getTipCoins())) {
                    //显示对话框 金币不足
                    return;
                }
                break;
            }

        }
        if (!tipword) {
            //没有找到可以填充的答案
            sparkWords();
        }
    }

    /**
     * 选择一个正确答案
     */
    private WordsButton findIsAnswer(int index) {
        WordsButton buf = null;

        for (int i = 0 ;i<myGridView.COUNTS_WORDS;i++) {
            buf = mAllWordList.get(i);
            if (buf.mWords.equals("" + mCurrentSong.getNameCharacters()[index])) {

                return buf;
            }
        }
        return  null;

    }

    /**
     * 删除一个非正确答案的文字事件
     */
    private void deletOneWord() {
//        Random random = new Random();


//            }
//        }
        //减少金币数
        if (!handleCoins(-getDeletConins())) {
            //金币不足，显示对话框
            return;
        }
        setButtonVisible(findNotAnswerButton(), View.INVISIBLE);
    }

    /**
     * 隐藏不是正确答案的文字
     *
     * @return
     */
    private WordsButton findNotAnswerButton() {
        Random random = new Random();
        WordsButton buf = null;
        while (true) {
            int index = random.nextInt(myGridView.COUNTS_WORDS);
            buf = mAllWordList.get(index);
            if (buf.misVisible && isTheAnswerWord(buf)) {
                return buf;
            }
        }
    }

    /**
     * 判断随机取得文字是否为答案
     */
    private boolean isTheAnswerWord(WordsButton button) {
        for (int i = 0; i < mCurrentSong.getSongNameLenth(); i++) {
            if (button.mWords.equals("" + mCurrentSong.
                    getNameCharacters()[i]))
                return true;
        }
        return false;
    }

}



