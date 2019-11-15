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

import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageData;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;
import com.example.android3dprint.ui.weldparameterv3.WeldParameterV3Fragment;
import com.example.android3dprint.ui.weldparameterv3.WeldParameterV3ViewModel;

public class WeldParameterV3Activity extends AppCompatActivity
        implements SocketAsyncTask.OnSocketListener {
    private static final String TAG = "WeldParameterV3Activity";
    private WeldParameterV3ViewModel viewModel;
    private SocketAsyncTask socketAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weld_parameter_v3_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Weld Parameter");
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, WeldParameterV3Fragment.newInstance(), "WeldParameterV3Fragment")
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
        int i;
        SocketMessageData[] socketMessageDatas;
        switch (item.getItemId()) {
            case R.id.action_save:
                WeldParameterV3Fragment fragment = (WeldParameterV3Fragment) getSupportFragmentManager()
                        .findFragmentByTag("WeldParameterV3Fragment");
                fragment.SaveWeldParameter();

                socketAsyncTask = new SocketAsyncTask(viewModel.HOST, viewModel.PORT, this);
                socketMessageDatas = new SocketMessageData[4];
                i = -1;

                socketMessageDatas[++i] =new SocketMessageData(SocketMessageType.SetSeamData);
                socketMessageDatas[i].setSymbolName(String.format("seam%02d", viewModel.getIndex().getValue()));
                socketMessageDatas[i].setSymbolValue(viewModel.getSeamData().getValue());

                socketMessageDatas[++i] = new SocketMessageData(SocketMessageType.SetWeldData);
                socketMessageDatas[i].setSymbolName(String.format("weld%02d", viewModel.getIndex().getValue()));
                socketMessageDatas[i].setSymbolValue(viewModel.getWeldData().getValue());

                socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.SetWeaveData);
                socketMessageDatas[i].setSymbolName(String.format("weave%02d", viewModel.getIndex().getValue()));
                socketMessageDatas[i].setSymbolValue(viewModel.getWeaveData().getValue());

                socketMessageDatas[++i] = new SocketMessageData(SocketMessageType.CloseConnection);

                socketAsyncTask.execute(socketMessageDatas);
                Log.d(TAG, "action_save");
                return true;
            case R.id.action_refresh:

                socketAsyncTask = new SocketAsyncTask(viewModel.HOST, viewModel.PORT, this);
                socketMessageDatas = new SocketMessageData[4];
                i = -1;

                socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.GetSeamData);
                socketMessageDatas[i].setSymbolName(String.format("seam%02d", viewModel.getIndex().getValue()));
                socketMessageDatas[i].setSymbolValue(viewModel.getSeamData().getValue());

                socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.GetWeldData);
                socketMessageDatas[i].setSymbolName(String.format("weld%02d", viewModel.getIndex().getValue()));
                socketMessageDatas[i].setSymbolValue(viewModel.getWeldData().getValue());

                socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.GetWeaveData);
                socketMessageDatas[i].setSymbolName(String.format("weave%02d", viewModel.getIndex().getValue()));
                socketMessageDatas[i].setSymbolValue(viewModel.getWeaveData().getValue());

                socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.CloseConnection);

                socketAsyncTask.execute(socketMessageDatas);
                Log.d(TAG, "action_refresh");
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

    @Override
    public void refreshUI(SocketMessageData[] socketMessageDatas) {
        WeldParameterV3Fragment fragment = (WeldParameterV3Fragment) getSupportFragmentManager()
                .findFragmentByTag("WeldParameterV3Fragment");
        fragment.refreshUI(socketMessageDatas);
    }
}
