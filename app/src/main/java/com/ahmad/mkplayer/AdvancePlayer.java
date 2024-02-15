package com.ahmad.mkplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmad.mkplayer.databinding.ActivityAdvancePlayerBinding;

import java.io.File;
import java.util.ArrayList;

public class AdvancePlayer extends AppCompatActivity {
    ActivityAdvancePlayerBinding activityAdvancePlayerBinding;
    String items[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdvancePlayerBinding = ActivityAdvancePlayerBinding.inflate(getLayoutInflater());
        setContentView(activityAdvancePlayerBinding.getRoot());

        displaysong();
    }

    //Recursive method that searches for audio files in a specified directory and its subdirectorie also retun array list
    public ArrayList<File> findSongFile(File file) {
        ArrayList<File> list = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File singleitem : files) {
                if (singleitem.isDirectory() && !singleitem.isHidden()) {
                    // Recursive to find Song File for subdirectories
                    list.addAll(findSongFile(singleitem));
                } else {
                    if (singleitem.getName().endsWith(".mp3") ||
                            singleitem.getName().endsWith(".wav") ||
                            singleitem.getName().endsWith(".amr")) {
                        list.add(singleitem);
                    }
                }
            }
        }
        return list;
    }

    //Populates the items array with the names of the songs and sets up the customAdabter for the  custom ListView. also handle intent to player
    public void displaysong() {
        final ArrayList<File> songs = findSongFile(Environment.getExternalStorageDirectory());
        if (songs != null) {
            items = new String[songs.size()];
            for (int i = 0; i < songs.size(); i++) {
                items[i] = songs.get(i).getName().toString().replace(".mp3", "");
            }
            customAdabter Adabter = new customAdabter(items, this);
            activityAdvancePlayerBinding.listSongs.setAdapter(Adabter);
            activityAdvancePlayerBinding.listSongs.setOnItemClickListener((parent, view, position, id) -> {
                String itemsname = (String) activityAdvancePlayerBinding.listSongs.getItemAtPosition(position);
                startActivity(new Intent(this, playerActivity.class)
                        .putExtra("song", songs)
                        .putExtra("name", itemsname)
                        .putExtra("position", position));
            });
        }
    }


}