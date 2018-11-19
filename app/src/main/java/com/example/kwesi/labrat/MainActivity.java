package com.example.kwesi.labrat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView addMouseButton = findViewById(R.id.toAddMouse);
        addMouseButton.setOnClickListener(createAddMouseListener());

        TextView rightingButton = findViewById(R.id.toRighting);
        rightingButton.setOnClickListener(createRightingListener());
    }


    private View.OnClickListener createAddMouseListener(){
        final Intent intent = new Intent(this, AddMouse.class);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        };
        return listener;
    }

    private View.OnClickListener createRightingListener(){
        final Intent intent = new Intent(this, RightingReflex.class);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        };
        return listener;
    }
}
