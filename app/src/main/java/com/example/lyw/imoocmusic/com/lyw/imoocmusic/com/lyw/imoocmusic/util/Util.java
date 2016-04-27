package com.example.lyw.imoocmusic.com.lyw.imoocmusic.com.lyw.imoocmusic.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lyw.imoocmusic.R;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.data.Const;
import com.example.lyw.imoocmusic.com.lyw.imoocmusic.model.IAlertDialogListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LYW on 2016/4/19.
 */
public class Util {
    private static LayoutInflater inflater;
    private static  AlertDialog mAlertDialog;
    public static View getView(Context context,int layoutId){
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutId,null);
        return v;
    }

    /**
     * 页面跳转
     * @param context
     * @param des
     */
    public static void startActivity(Context context,Class des){
        Intent intent = new Intent();
        intent.setClass(context, des);
        context.startActivity(intent);
           //关闭当前Activity
        ((Activity) context).finish();
    }

    /**
     * 显示自定义对话框
     */
    public   static void showDialog(Context context,String message,
                            final IAlertDialogListener listener){
      View dailogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dailogView = getView(context, R.layout.tipdialog_xml);
        ImageButton cancleBut = (ImageButton) dailogView.
                findViewById(R.id.id_alertdialog_nativebut);
        ImageButton okBut = (ImageButton) dailogView.findViewById(R.id
                .id_alertdialog_okbut);
        TextView messageTvt = (TextView) dailogView.findViewById(R.id.
                id_alertdialog_message);
         messageTvt.setText(message);
        cancleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlertDialog !=null){
                    mAlertDialog.cancel();
                }
            }
        });
        okBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (mAlertDialog !=null){
                    mAlertDialog.cancel();
                }
                if (listener != null){
                    listener.onClick();
                }
            }
        });
        //为dialog设置view
        builder.setView(dailogView);
        mAlertDialog = builder.create();
        //显示对话框
        mAlertDialog.show();
    }

    /**
     * 保存游戏数据
     * @param context
     * @param stageIndex
     * @param coins
     */
    public static void saveData(Context context,int stageIndex,int coins){
        FileOutputStream fileOutputStream  =null;
        try {
            fileOutputStream = context.openFileOutput(Const.FILE_NAME_SAVE_DATA,
                    context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fileOutputStream);
            dos.writeInt(stageIndex);
            dos.writeInt(coins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static int[] loadData(Context context){
        FileInputStream fis = null;
        int[] data = {-1,Const.CONST_CION_NUM};
        try {
            fis = context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dis = new DataInputStream(fis);
            data[Const.INDEX_LOAD_DATA_STAGE] = dis.readInt();
            data[Const.INDEX_LOAD_DATA_COINS] = dis.readInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
