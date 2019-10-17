package com.example.android3dprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android3dprint.robot.ArcData;
import com.example.android3dprint.robot.WeldData;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, WeldParameterActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
        String message="Hello Michael";
        ArcData arcData=new ArcData();
        WeldData weldData=new WeldData();

        arcData.setCurrent(200);
        weldData.setWeldSpeed(7.1);
        intent.putExtra(EXTRA_MESSAGE, weldData);
        startActivity(intent);

    }

}
