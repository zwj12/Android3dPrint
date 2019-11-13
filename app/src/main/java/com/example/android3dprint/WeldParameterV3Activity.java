package com.example.android3dprint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;
import com.example.android3dprint.ui.weldparameterv3.WeldParameterV3Fragment;
import com.example.android3dprint.ui.weldparameterv3.WeldParameterV3ViewModel;

public class WeldParameterV3Activity extends AppCompatActivity {
    private static final String TAG = "WeldParameterV3Activity";

    private WeldParameterV3ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weld_parameter_v3_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Weld Parameter");
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WeldParameterV3Fragment.newInstance())
                    .commitNow();
        }

        viewModel = ViewModelProviders.of(this).get(WeldParameterV3ViewModel.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welding_parameter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                Log.d(TAG,"action_refresh" + viewModel.getWeldData().getValue().toString());
                Log.d(TAG,"action_refresh" + viewModel.getRemark().getValue());
                return true;
            case R.id.action_refresh:
                WeldData weldData=viewModel.getWeldData().getValue();
                weldData.setWeldSpeed(2);
                viewModel.setWeldData(weldData);
                viewModel.getWeldData().getValue().getMainArc().setCurrent(200);
                viewModel.getSeamData().getValue().setPreflowTime(1);
                viewModel.setSeamData(viewModel.getSeamData().getValue());
                WeaveData weaveData=viewModel.getWeaveData().getValue();
                weaveData.setWeaveShape(weaveData.getWeaveShape()+1);
                viewModel.setWeaveData(weaveData);
                Log.d(TAG,"action_refresh");
                return true;

            case R.id.action_toast:
                Toast toast = Toast.makeText(this, "Hello Michael!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(Color.YELLOW);
                toast.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
