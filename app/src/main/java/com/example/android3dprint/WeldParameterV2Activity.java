package com.example.android3dprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

import java.io.IOException;

public class WeldParameterV2Activity extends AppCompatActivity
        implements WeldParameterFragment.OnFragmentInteractionListener , SocketAsyncTask.OnSocketListener {
    private static final String TAG = "WeldParameterV2Activity";

    private WeldParameterFragment fragment;
    private SeamData seamData = new SeamData();
    private WeldData weldData = new WeldData();
    private WeaveData weaveData = new WeaveData();
    private static String HOST = "10.0.2.2";
    private static int PORT = 3003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weld_parameter_v2);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Weld Parameter");
        setSupportActionBar(myToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        seamData.setPreflowTime(0.5);
        weldData.setWeldSpeed(1.5);
        weaveData.setWeaveShape(1);
        fragment = WeldParameterFragment.newInstance(seamData, weldData, weaveData);
        fragmentTransaction.add(R.id.frame_WeldingParameter, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welding_parameter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SocketAsyncTask socketAsyncTask = new SocketAsyncTask(HOST, PORT, this);
        SocketMessageType[] socketMessageTypes = new SocketMessageType[4];
        int i;
        switch (item.getItemId()) {
            case R.id.action_save:
                this.fragment.save();
                i = -1;

                socketMessageTypes[++i]=SocketMessageType.SetWeldData;
                socketMessageTypes[i].setSymbolName("weld01");
                socketMessageTypes[i].setSymbolValue(weldData);

                socketMessageTypes[++i]=SocketMessageType.SetSeamData;
                socketMessageTypes[i].setSymbolName("seam01");
                socketMessageTypes[i].setSymbolValue(seamData);

                socketMessageTypes[++i]=SocketMessageType.SetWeaveData;
                socketMessageTypes[i].setSymbolName("weave01");
                socketMessageTypes[i].setSymbolValue(weaveData);

                socketMessageTypes[++i] = SocketMessageType.CloseConnection;

                socketAsyncTask.execute(socketMessageTypes);
                Log.d(TAG,"action_refresh");
                return true;
            case R.id.action_refresh:
                i = -1;

                socketMessageTypes[++i] = SocketMessageType.GetWeldData;
                socketMessageTypes[i].setSymbolName("weld01");
                weldData.setIndex(1);
                socketMessageTypes[i].setSymbolValue(weldData);

                socketMessageTypes[++i] = SocketMessageType.GetSeamData;
                socketMessageTypes[i].setSymbolName("seam01");
                seamData.setIndex(1);
                socketMessageTypes[i].setSymbolValue(seamData);

                socketMessageTypes[++i] = SocketMessageType.GetWeaveData;
                socketMessageTypes[i].setSymbolName("weave01");
                weaveData.setIndex(1);
                socketMessageTypes[i].setSymbolValue(weaveData);

                socketMessageTypes[++i] = SocketMessageType.CloseConnection;

                socketAsyncTask.execute(socketMessageTypes);
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

    @Override
    public void refreshUI(SocketMessageType[] socketMessageTypes) {
        Log.d(TAG,"refreshUI");
        this.fragment.refreshUI(socketMessageTypes);
    }
}
