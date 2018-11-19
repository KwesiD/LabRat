package com.example.kwesi.labrat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RightingReflex extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private MouseDatabase mouseDB;
    private List<Mouse> mice;
    private Spinner descList;
    private Spinner colorList;
    private Spinner dobList;
    private Spinner trialList;
    private Spinner dayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_righting_reflex);
        Button recordButton = findViewById(R.id.rightingCameraButton);
        recordButton.setOnClickListener(createRecordListener());

        mouseDB = MouseDatabase.getAppDatabase(this);
        mice = mouseDB.userDao().getAll();

        if(mice.size() == 0){
            Toast.makeText(this,"You need to add a mouse in order to run a test!!",Toast.LENGTH_LONG).show();
            finish();
        }

        ArrayList<String> descs = new ArrayList<>();
        ArrayList<String> colors = new ArrayList<>();
        ArrayList<String> dobs = new ArrayList<>();
        String[] days = {"0","1","2","3","4","5","6","7","8","9","10"};
        String[] trials = {"1","2","3","4"};
        for(int m = 0;m < mice.size();m++){
            if(!descs.contains(mice.get(m).getParentDesc())){
                descs.add(mice.get(m).getParentDesc());
            }
            if(!colors.contains(mice.get(m).getColor())){
                colors.add(mice.get(m).getColor());
            }
            if(!dobs.contains(mice.get(m).getPairedDate().toString())){
                dobs.add(mice.get(m).getPairedDate().toString());
            }
        }

        descList = findViewById(R.id.descList);
        descList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, descs));
        colorList = findViewById(R.id.colorList);
        colorList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors));
        dobList = findViewById(R.id.dobList);
        dobList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dobs));
        dayList = findViewById(R.id.dayList);
        dayList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days));
        trialList = findViewById(R.id.trialList);
        trialList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trials));

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //Uri videoUri = intent.getData();
        }
    }

    private View.OnClickListener createRecordListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currDesc = descList.getSelectedItem().toString();
                String currColor = colorList.getSelectedItem().toString();
                String currDob = dobList.getSelectedItem().toString();
                String currTrial = trialList.getSelectedItem().toString();
                String currDay = dayList.getSelectedItem().toString();
                String currMid = currDesc + currColor + currDob;
                Mouse currMouse = null;
                for(int i = 0;i < mice.size();i++){
                    currMouse = mice.get(i);
                    if(currMouse.getMid() == currMid){
                        break;
                    }
                }

                createTakeVideoIntent(currMouse.mid+"--"+"Day"+currDay+"--"+"Trial"+currTrial);
            }
        };
        return listener;
    }

    private void createTakeVideoIntent(String videoName){

        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);

/*
        try {
            File file = createVideo(videoName);
            Uri outputFileUri = FileProvider.getUriForFile(this, "com.example.kwesi.labrat.fileprovider", file);
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
        catch (IOException io){
            io.printStackTrace();
        }*/
    }

    private File createVideo(String videoName) throws IOException{
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File image = File.createTempFile(videoName+"--",".mp4",storageDir); //Create temp file appends a random string of numbers to the end of the file. The dashes separate those from the file name
        return image;

    }

}
