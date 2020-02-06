package edu.fullsail.mgems.cse.treasurehunter.christopherwest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnCredits).setOnClickListener(this);
        findViewById(R.id.btnInventory).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
