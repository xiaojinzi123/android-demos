package com.example.cxj.fresco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private CommonNineView niv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        niv = (CommonNineView) findViewById(R.id.niv);
    }

    public void qwe(View view) {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.mipmap.ic_launcher);
        niv.addClildView(iv);
    }
}
