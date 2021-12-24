package com.example.android3dprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;
import com.example.android3dprint.robot.ArcData;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageData;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements SocketAsyncTask.OnSocketListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
//    private static String HOST = "10.0.2.2";
    private static String HOST = "192.168.2.52";
    private static int PORT = 3003;
    private static final String TAG = "MainActivity";
    private MutableLiveData<Integer> mNumberLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberLiveData = new MutableLiveData<>();

        mNumberLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "onChanged: " + integer);
            }
        });

    }

    public void retrofitTest() {
//        final DigestAuthenticator authenticator =
//                new DigestAuthenticator(new Credentials("Default User", "robotics"));
//
//        final Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();
//        final OkHttpClient client = new OkHttpClient.Builder()
//                .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
//                .addInterceptor(new AuthenticationCacheInterceptor(authCache))
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        retrofit2.Call<List<User>> repos = service.listRepos("octocat");
    }

    public void okHttpTest(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    final DigestAuthenticator authenticator =
                            new DigestAuthenticator(new Credentials("Default User", "robotics"));

                    final Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();
                    final OkHttpClient client = new OkHttpClient.Builder()
                            .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                            .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                            .build();

                    String url = "http://10.0.2.2:8610/rw/system";
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {

                    }
                    Log.d(TAG, "response code " + response);
                    Log.d(TAG, "code:" + response.code());

                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    Log.d(TAG, response.body().string());
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }

            }
        }.start();

//        new Thread() {
//
//            @Override
//            public void run() {
//                Request request = new Request.Builder()
//                        .url("http://10.0.2.2:8610/rw/system")
//                        .build();
//
//                try (Response response = client.newCall(request).execute()) {
//                    if (!response.isSuccessful()) {
//
//                    }
//                    Log.d(TAG,"response code " + response);
//                    Log.d(TAG,"code:" + response.code());
//
//                    Headers responseHeaders = response.headers();
//                    for (int i = 0; i < responseHeaders.size(); i++) {
//                        Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//
//                    Log.d(TAG, response.body().string());
//                }catch (Exception e){
//                    Log.d(TAG,e.getMessage());
//                }
//            }
//        }.start();

//        asynchronousGetRequests();
    }

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }



    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public void asynchronousGetRequests() {
        String url = "http://10.0.2.2:8610/rw/system";
//        String url = "https://www.baidu.com";

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "asynchronousGetRequests onFailure: " + e.getMessage());
//                runOnUiThread(() -> tvContent.setText("asynchronousGetRequests onFailure: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "asynchronousGetRequests onResponse: " + result);
//                runOnUiThread(() -> tvContent.setText("asynchronousGetRequests onResponse: " + result));

            }
        });
    }

    public void checkMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextInput);
        WeldData weldData = new WeldData();
        weldData.parse(editText.getText().toString());
        editText = (EditText) findViewById(R.id.editTextOutput);
        editText.setText(weldData.toString());
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
                    + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Book "
                    + " ADD COLUMN pub_year INTEGER");
        }
    };

    public void socketTest(View view) {
//        MyDBOpenHelper myDBHelper = new MyDBOpenHelper(MainActivity.this, "my.db", null, 1);
//        myDBHelper.getWritableDatabase();

//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "test1.db").addMigrations(MIGRATION_1_2).build();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "test1.db").allowMainThreadQueries().build();
        UserDao userDao = db.getUserDao();

        User user = new User();
        user.uid = 1;
        user.firstName = "Michael";
        user.lastName = "Zhu";
        userDao.insertAll(user);
        db.close();
//        for (int index = 3; index < 10003; index++) {
//            User user=new User();
//            user.uid=index;
//            user.firstName="Michael";
//            user.lastName="Zhu";
//            userDao.insertAll(user);
//        }
    }

    public void AsyncTask(View v) throws IOException {
//        SocketMessageData[] socketMessageData=new SocketMessageData[30];
//        int i=-1;
//        socketMessageData[i]=new SocketMessageData(SocketMessageType.GetOperatingMode);


//        SocketMessageType[] socketMessageTypes=new SocketMessageType[30];
//
//        socketMessageTypes[++i]=SocketMessageType.GetOperatingMode;
//        socketMessageTypes[++i]=SocketMessageType.GetRunMode;
//        socketMessageTypes[++i]=SocketMessageType.GetRobotStatus;
//
//        socketMessageTypes[++i]=SocketMessageType.GetSignalDo;
//        socketMessageTypes[i].setSignalName("sdoTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalGo;
//        socketMessageTypes[i].setSignalName("sgoTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalAo;
//        socketMessageTypes[i].setSignalName("saoTest1");
//
//        socketMessageTypes[++i]=SocketMessageType.GetSignalDi;
//        socketMessageTypes[i].setSignalName("sdiTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalGi;
//        socketMessageTypes[i].setSignalName("sgiTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalAi;
//        socketMessageTypes[i].setSignalName("saiTest1");
//
//        int j=0;
//        socketMessageTypes[++i]=SocketMessageType.SetSignalDo;
//        socketMessageTypes[i].setSignalName("sdoTest1");
//        socketMessageTypes[i].setSignalValue(j);
//        socketMessageTypes[++i]=SocketMessageType.SetSignalGo;
//        socketMessageTypes[i].setSignalName("sgoTest1");
//        socketMessageTypes[i].setSignalValue(j+1);
//        socketMessageTypes[++i]=SocketMessageType.SetSignalAo;
//        socketMessageTypes[i].setSignalName("saoTest1");
//        socketMessageTypes[i].setSignalValue(j+2);
//
//        socketMessageTypes[++i]=SocketMessageType.GetNumData;
//        socketMessageTypes[i].setSymbolName("reg1");
//
//        socketMessageTypes[++i]=SocketMessageType.SetNumData;
//        socketMessageTypes[i].setSymbolName("reg2");
//        socketMessageTypes[i].setSymbolValue(j+3);
//
//        socketMessageTypes[++i]=SocketMessageType.GetWeldData;
//        socketMessageTypes[i].setSymbolName("weld01");
//
//        socketMessageTypes[++i]=SocketMessageType.SetWeldData;
//        socketMessageTypes[i].setSymbolName("weld01");
//        WeldData weldData=new WeldData();
//        weldData.parse("[11,12,[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]");
//        weldData.setWeldSpeed(15);
//        socketMessageTypes[i].setSymbolValue(weldData);
//
//        socketMessageTypes[++i]=SocketMessageType.GetSeamData;
//        socketMessageTypes[i].setSymbolName("seam1");
//
//        socketMessageTypes[++i]=SocketMessageType.SetSeamData;
//        socketMessageTypes[i].setSymbolName("seam1");
//        SeamData seamData=new SeamData();
//        seamData.parse("[0,1.5,[71,0,0,0,0,0,0,0,0],0,0,0,0,0,[72,0,0,0,0,0,0,0,0],0,0,[73,0,0,0,0,0,0,0,0],0,0,[74,0,0,0,0,0,0,0,0],2.3]");
//        seamData.setPreflowTime(2.5);
//        socketMessageTypes[i].setSymbolValue(seamData);
//
//        socketMessageTypes[++i]=SocketMessageType.GetWeaveData;
//        socketMessageTypes[i].setSymbolName("weave01");
//
//        socketMessageTypes[++i]=SocketMessageType.SetWeaveData;
//        socketMessageTypes[i].setSymbolName("weave01");
//        WeaveData weaveData=new WeaveData();
//        weaveData.parse("[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]");
//        weaveData.setWeaveShape(3);
//        socketMessageTypes[i].setSymbolValue(weaveData);
//
//        socketMessageTypes[++i]=SocketMessageType.CloseConnection;
//
//        SocketAsyncTask socketAsyncTask=new SocketAsyncTask(HOST,PORT,this);
//        socketAsyncTask.execute(socketMessageTypes);
    }

    public void openScrolling(View view) {
        // Do something in response to button
//        Intent intent = new Intent(this, WeldParameterV2Activity.class);
        Intent intent = new Intent(this, WeldParameterRecyclerActivity.class);
        startActivity(intent);
    }

    public void openViewModel(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, WeldParameterV3Activity.class);
//        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }

    @Override
    public void refreshUI(SocketMessageData[] socketMessageDatas) {
//        EditText editText;
//        for(SocketMessageType socketMessageType : socketMessageTypes){
//            if(socketMessageType==null){
//                continue;
//            }
//            switch (socketMessageType){
//                case  GetNumData:
//                    editText = (EditText) findViewById(R.id.editTextOutput);
//                    editText.setText(socketMessageType.getSymbolValue().toString());
//            }
//        }
    }
}
