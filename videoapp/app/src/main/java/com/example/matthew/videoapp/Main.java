package com.example.matthew.videoapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

import io.vov.vitamio.Vitamio;
//import android.hardware.camera2;


public class Main extends ActionBarActivity {
    private static String logtag = "CameraApp";
    private static int TAKE_PICTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private Uri videoUri;
    private Uri imageUri;
    SurfaceView sur_view;
    MediaController media_Controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //VideoView video_player_view;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        // your code

        //Button cameraButton = (Button)findViewById(R.id.button_camera);
        //cameraButton.setOnClickListener(cameraListener);

        //-----------------Video Session Code---------------------------
        Button videoButton = (Button)findViewById(R.id.button_video);
        Button replayButton = (Button)findViewById(R.id.replay_video);
        videoButton.setOnClickListener(replayListener);
        replayButton.setOnClickListener(videoListener);
       // Vitamio.MediaPlayer mMediaPlayer = new MediaPlayer();
        float speed = .5f;



        //mMediaPlayer.setPlaybackSpeed(speed);
        //for listing files
        ListView lv;
        ArrayList<String> FilesInFolder = GetFiles("/sdcard/Pictures/Screenshots");
        lv = (ListView)findViewById(R.id.filelist);

        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, FilesInFolder));




    }
    private View.OnClickListener videoListener = new View.OnClickListener(){
        public void onClick(View v){
            takeVideo();
        }
    };
    private View.OnClickListener replayListener = new View.OnClickListener(){
        public void onClick(View v){
            replayVideo();
        }
    };

    //--------------------NOT CURRENTLY USED-------------------------
    private void playVideo(String resourceName) {
        Intent videoPlaybackActivity = new Intent(this, VideoPlayer.class);
        int res=this.getResources().getIdentifier(resourceName, "raw", getPackageName());
        videoPlaybackActivity.putExtra("fileRes", res);
        startActivity(videoPlaybackActivity);
    }

    private void replayVideo(){
        playVideo("video");
    }

    //--------------------------------------------------------------
    private void takeVideo(){
        dispatchTakeVideoIntent();
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File video = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"video.mp4");
        videoUri = Uri.fromFile(video);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    //for displaying files
    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++)
                MyFiles.add(files[i].getName());
        }

        return MyFiles;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            setupPlayback();
        }
    }

    //THE VIDEO PLAYING BEHIND LISTVIEW
    private void setupPlayback(){


        VideoView video_player_view = (VideoView) findViewById(R.id.video_camera);
        media_Controller = new MediaController(this);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.win);
        video_player_view.setVideoURI(video);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        video_player_view.setMinimumWidth(width);
        video_player_view.setMinimumHeight(height);
        video_player_view.setMediaController(media_Controller);
       // video_player_view.findViewById(R.raw.win);
        video_player_view.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

