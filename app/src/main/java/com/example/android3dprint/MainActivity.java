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
        WeldData weldData=new WeldData();
        EditText editText = (EditText) findViewById(R.id.editTextInput);
        weldData.parse(editText.getText().toString());
        intent.putExtra(EXTRA_MESSAGE, weldData);
        startActivity(intent);
    }

    public void checkMessage(View view){
        EditText editText = (EditText) findViewById(R.id.editTextInput);
        WeldData weldData=new WeldData();
        weldData.parse(editText.getText().toString());
        editText = (EditText) findViewById(R.id.editTextOutput);
        editText.setText(weldData.toString());
    }

    public void socketTest(View view){
        Intent intent = new Intent(this, SocketActivity.class);
        startActivity(intent);
    }
}
