package com.example.android3dprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android3dprint.robot.ArcData;
import com.example.android3dprint.robot.WeldData;

import java.text.DecimalFormat;

public class WeldParameterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weld_parameter);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        WeldData weldData=(WeldData) getIntent().getSerializableExtra(MainActivity.EXTRA_MESSAGE);

//        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat df = new DecimalFormat();

        EditText editText = (EditText) findViewById(R.id.editTextIndex);
        editText.setText(Integer.toString(weldData.getIndex()));

        editText = (EditText) findViewById(R.id.editTextWeldSpeed);
        editText.setText(df.format(weldData.getWeldSpeed()));

        editText = (EditText) findViewById(R.id.editTextMode);
        editText.setText(Integer.toString(weldData.getMainArc().getMode()));

        editText = (EditText) findViewById(R.id.editTextCurrent);
        editText.setText(new DecimalFormat("0").format( weldData.getMainArc().getCurrent()));

        editText = (EditText) findViewById(R.id.editTextVoltage);
        editText.setText(df.format(weldData.getMainArc().getVoltage()));
    }
}
