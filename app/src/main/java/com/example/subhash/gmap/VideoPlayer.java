package com.example.subhash.gmap;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class VideoPlayer extends AppCompatActivity {
    VideoView videoView;
    Button button,play;
    EditText e_link;
    /*url = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"*/
    String link="";
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        videoView=findViewById(R.id.videoView);
        button=findViewById(R.id.bt_button);
        play=findViewById(R.id.bt_play);
        e_link=findViewById(R.id.et_vedio_link);
        mediaController=new MediaController(VideoPlayer.this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                finish();
            }



            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link=e_link.getText().toString();
                if (link.equals(""))
                {
                    e_link.setError("Video Link");
                }
                else
                {
                    try {
                        String uriString=link;
                        Uri uri=Uri.parse(uriString);
                        videoView.setVideoURI(uri);

                        play.setVisibility(View.INVISIBLE);
                        e_link.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(VideoPlayer.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);

            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                button.setVisibility(View.VISIBLE);
                play.setVisibility(View.VISIBLE);
                e_link.setVisibility(View.VISIBLE);
                e_link.setTextColor(Color.parseColor("#000000"));
                e_link.setHintTextColor(Color.parseColor("#000000"));
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            Uri video=data.getData();
            try {
                button.setVisibility(View.INVISIBLE);
                play.setVisibility(View.INVISIBLE);
                e_link.setVisibility(View.INVISIBLE);

                videoView.setVideoURI(video);

                videoView.start();

            }
            catch (Exception e)
            {
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
