package com.gautam.esangeet;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    private TextView textview;
    private ImageView play, next, prev;
    private ArrayList<File> songs;
    private MediaPlayer mediaPlayer;
    private String textContent;
    private int position;
    private SeekBar seekbar;
    private Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textview=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        seekbar=findViewById(R.id.seekBar);

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songList");
        textContent=intent.getStringExtra("currentSong");
        textview.setText(textContent);
        textview.setSelected(true);
        textview.setSelected(true);
        position=intent.getIntExtra("position",0);
//        songs=(ArrayList) intent.getParcelableArrayListExtra("songList");
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer= MediaPlayer.create(this,uri);
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause);
        seekbar.setMax(mediaPlayer.getDuration());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try{
                    while(currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position = position - 1;
                }
                else{
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekbar.setMax(mediaPlayer.getDuration());
//                textContent = songs.get(position).getName().toString();
                textContent = songs.get(position).getName().toString();
                textview.setText(textContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position = position + 1;
                }
                else{
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekbar.setMax(mediaPlayer.getDuration());
//                textContent = songs.get(position).getName().toString();
                textContent = songs.get(position).getName().toString();
                textview.setText(textContent);

            }
        });
    }
}
