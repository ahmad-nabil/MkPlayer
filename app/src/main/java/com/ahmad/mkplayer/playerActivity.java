package com.ahmad.mkplayer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmad.mkplayer.databinding.ActivityPlayerBinding;

import java.io.File;
import java.util.ArrayList;

public class playerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    ArrayList<File> files;
    MediaPlayer mediaPlayer;
    int position;
    String sname;
    Thread UpdatesSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        files = (ArrayList) bundle.getParcelableArrayList("song");
        position = bundle.getInt("position");
        binding.titlemusic.setSelected(true);
        //pase files for media player
        Uri uri = Uri.parse(files.get(position).toString());
        binding.titlemusic.setText(files.get(position).getName());
        //initialize media player
        mediaPlayer = MediaPlayer.create(playerActivity.this, uri);
        mediaPlayer.start();
        //method for update seekbar
       Updateseekbar();

//get posion seek bar when user changing it
        binding.seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

//listener btns
        binding.playbtn.setOnClickListener(this::playMusic);
        binding.nextbtn.setOnClickListener(this::nextMusic);
        binding.previousbtn.setOnClickListener(this::previousMusic);
        mediaPlayer.setOnCompletionListener(mp -> {
            binding.nextbtn.performClick();
        });

        //forward 10 sec
        binding.ffbtn.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
            }
        });
        //replay last 10
        binding.frbtn.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
            }
        });
        //initialize visualizer and connected it with media player
        int audiosessionID = mediaPlayer.getAudioSessionId();
        if (audiosessionID != -1) {
            binding.blobVisualizer.setAudioSessionId(audiosessionID);
        }
    }

  public void Updateseekbar(){
      //update seek bar using threads
      UpdatesSeekBar = new Thread() {
          @Override
          public void run() {
              int total_duration = mediaPlayer.getDuration();
              int current_position = 0;
              while (current_position < total_duration) {
                  try {
                      sleep(500);
                      current_position = mediaPlayer.getCurrentPosition();
                      binding.seek.setProgress(current_position);
                  } catch (InterruptedException | IllegalStateException e) {
                      e.printStackTrace();
                  }
              }
          }
      };
      //set progress seekbar same duration
      binding.seek.setMax(mediaPlayer.getDuration());
      UpdatesSeekBar.start();
      String endTime = createtimming(mediaPlayer.getDuration());
      binding.endtxt.setText(endTime);
      Handler handler = new Handler();
//update playback every second
      handler.postDelayed(new Runnable() {
          @Override
          public void run() {
              String curenttime = createtimming(mediaPlayer.getCurrentPosition());
              binding.starttxt.setText(curenttime);
              handler.postDelayed(this, 1000);
          }
      }, 1000);
  }

    private void previousMusic(View previousMusic) {
        mediaPlayer.stop();
        mediaPlayer.release();
        position = ((position - 1) < 0) ? (files.size() - 1) : position - 1;
        sname = files.get(position).getName();
        Uri u = Uri.parse(files.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        binding.titlemusic.setText(sname);
        mediaPlayer.start();
        binding.playbtn.setBackgroundResource(R.drawable.baseline_pause_circle_outline_24);
        startAnimation(binding.imgsong);
        binding.seek.setProgress(0);
        Updateseekbar();
    }

    private void nextMusic(View nextMusic) {
        mediaPlayer.stop();
        mediaPlayer.release();
        position = ((position + 1) % files.size());
        Uri u = Uri.parse(files.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        sname = files.get(position).getName();
        binding.titlemusic.setText(sname);
        mediaPlayer.start();
        binding.playbtn.setBackgroundResource(R.drawable.baseline_pause_circle_outline_24);
        startAnimation(binding.imgsong);
        binding.seek.setProgress(0);
        Updateseekbar();
    }

    private void playMusic(View Play) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.playbtn.setBackgroundResource(R.drawable.baseline_play_circle_outline_24);
        } else {
            binding.playbtn.setBackgroundResource(R.drawable.baseline_pause_circle_outline_24);
            mediaPlayer.start();
        }

    }

    public void startAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(binding.imgsong.getId(), "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createtimming(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int Sec = duration / 1000 % 60;
        time += min + ":";
        if (Sec < 10) {
            time += "0";
        }
        time += Sec;
        return time;
    }

    @Override
    protected void onDestroy() {
        if (binding.blobVisualizer != null) {
            binding.blobVisualizer.release();
        }
        super.onDestroy();

    }
}