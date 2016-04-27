package com.example.lyw.imoocmusic.com.lyw.imoocmusic.model;

/**
 * Created by LYW on 2016/4/20.
 */
public class Song {

    private String songName;
    private String songFileName;
    private int songNameLenth;

    public char[] getNameCharacters(){
        return songName.toCharArray();
    }

    public void setSongNameLenth(int songNameLenth) {
        this.songNameLenth = songNameLenth;
    }

    public void setSongFileName(String songFileName) {
        this.songFileName = songFileName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
        this.songNameLenth = songName.length();
    }

    public String getSongName() {

        return songName;
    }

    public String getSongFileName() {
        return songFileName;
    }

    public int getSongNameLenth() {
        return songNameLenth;
    }
}
