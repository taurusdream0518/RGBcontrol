package com.example.rgbcontrol;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CarActivity extends AppCompatActivity {

    private Context context;

    private Button  buttonBack;
    private TextView textViewDatetime,textViewTemp,textViewHum;
    private String webAddress="http://192.168.63.25:8080/11-14_api/";
    private String readData = "read_data.php";
    private StringBuilder myAddress;
    private Button buttobUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("環境溫溼度監測");
        textViewDatetime = (TextView) findViewById(R.id.textView_Datetime);
        textViewTemp = (TextView) findViewById(R.id.textView_temp);
        textViewHum = (TextView) findViewById(R.id.textView_hum);
        buttobUpdate = (Button)findViewById(R.id.button_update);

        buttobUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PHPReadData().start();
            }
        });




        Intent intent = getIntent();


//        btData = intent.getStringExtra("btdata");
//        Log.d("car","btData = "+btData);
//
//        textViewBT = (TextView) findViewById(R.id.textView_carBT);
//        textViewBT.setText(btData);
//
//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//        myChatService = new BTChatService(context,myHandler);
//
//        if (btData != null){
//            macAddress = btData.substring(btData.length()-17);
//            Log.d("car","macAddress = "+macAddress);
//            BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
//            myChatService.connect(device);
//
//        }
//
//        spinnerSong = (Spinner) findViewById(R.id.spinner_carSong);
//        spinnerSong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                switch (position){
//                    case 0:
//                        songCMD = "0";
//                        break;
//                    case 1:
//                        songCMD = "1";
//                        break;
//                    case 2:
//                        songCMD = "2";
//                        break;
//                    case 3:
//                        songCMD = "3";
//                        break;
//                    case 4:
//                        songCMD = "4";
//                        break;
//                    case 5:
//                        songCMD = "5";
//                        break;
//                }
//                Log.d("car","songCMD = "+songCMD );
//                sendCMD(songCMD);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        spinnerAdpater = ArrayAdapter.createFromResource(context,R.array.song_name,R.layout.simple_spinner_item);
//        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSong.setAdapter(spinnerAdpater);
//
//
//        imagebuttonForward = (ImageButton) findViewById(R.id.imageButton_forward);
//        imagebuttonLeft = (ImageButton) findViewById(R.id.imageButton_left);
//        imagebuttonRight = (ImageButton) findViewById(R.id.imageButton_right);
//        imagebuttonBack = (ImageButton) findViewById(R.id.imageButton_back);
//        imagebuttonStop = (ImageButton)findViewById(R.id.imageButton_stop);
//
//        imagebuttonForward.setOnClickListener(new MyButton());
//        imagebuttonLeft.setOnClickListener(new MyButton());
//        imagebuttonRight.setOnClickListener(new MyButton());
//        imagebuttonBack.setOnClickListener(new MyButton());
//        imagebuttonStop.setOnClickListener(new MyButton());
//
//        switch_adc = (Switch) findViewById(R.id.switch_adc);
//        switch_adc.setChecked(false);
//        switch_adc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    sendCMD("v");
//                }else {
//                    sendCMD("z");
//                }
//            }
//        });
//
//        buttonLink = (Button) findViewById(R.id.button_carLink);
//        buttonLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(btData != null){
//                    BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
//                    myChatService.connect(device);
//                }else {
//                    Toast.makeText(context,"no paired BT device.",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private class PHPReadData extends Thread{
        private URL url;
        private HttpURLConnection conn;
        private int code;
        private InputStream inputStream = null;
        private String dataString;
        private JSONArray jsonArray = null;
        private StringBuffer userDataTime;
        private StringBuffer userDataTemp;
        private StringBuffer userDataHumi;

        @Override
        public void run() {
            super.run();
            myAddress = new StringBuilder();
            myAddress.append(webAddress);
            myAddress.append(readData);

            try {
                url = new URL(myAddress.toString());
                Log.d("php", "url = " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            try {
                code = conn.getResponseCode();
                Log.d("php", "code = " + code);
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (code == HttpURLConnection.HTTP_OK) {
                Log.d("php", "ok");
                try {
                    inputStream = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader stringReader = new BufferedReader(reader);
                try {
                    dataString = stringReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("php", "dataString = " + dataString);

                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dataString.length() == 0) {
                            return;
                        }

                        try {
                            jsonArray = new JSONArray(dataString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int count = jsonArray.length();
                        Log.d("php", "count = " + count);
                        userDataTime = new StringBuffer();
                        userDataTemp = new StringBuffer();
                        userDataHumi = new StringBuffer();
                        for (int i = 0; i < count; i++) {
                            JSONObject jsonData = null;
                            try {
                                jsonData = jsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                String datetime = jsonData.getString("datetime");
                                Log.d("php", "datetime = " + datetime);
                                userDataTime.append(datetime);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                String temperature = jsonData.getString("temperature");
                                Log.d("php", "temperature = " + temperature);
                                userDataTemp.append(temperature + "℃");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                String humidity = jsonData.getString("humidity");
                                Log.d("php", "humidity = " + humidity);
                                userDataHumi.append(humidity + "％RH");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        textViewDatetime.append(userDataTime.toString());
                        textViewTemp.append(userDataTemp.toString());
                        textViewHum.append(userDataHumi.toString());


                    }
                });


            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.car_menu,menu);
//        return  true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.car_sensor:
//
//                break;
//            case R.id.mode_home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }
//
//    private final Handler myHandler = new Handler(Looper.myLooper()){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case Constants.MESSAGE_DEVICE_NAME:
//                    String data = msg.getData().getString(Constants.DEVICE_NAME);//回傳已連上的訊息
//                    Toast.makeText(context,"Connected to "+data,Toast.LENGTH_SHORT).show();
//                    break;
//                case Constants.MESSAGE_TOAST://連線錯誤的訊息
//                    String error = msg.getData().getString(Constants.TOAST);
//                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
//                    break;
//
//            }
//
//        }
//    };
//    //傳送指令
//    private void sendCMD(String message){
//        int mState = myChatService.getState();
//        if(mState == BTChatService.STATE_CONNECTED){
//            if(message.length()>0){
//                byte[] sendData = message.getBytes();
//                myChatService.BTWrite((sendData));
//            }
//
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(myChatService != null){
//            myChatService.stop();
//            myChatService=null;
//        }
//    }
//
//    private class MyButton implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.imageButton_forward:
//                    sendCMD(car_foward);
//                    break;
//                case R.id.imageButton_left:
//                    sendCMD(car_left);
//                    break;
//                case R.id.imageButton_right:
//                    sendCMD(car_right);
//                    break;
//                case R.id.imageButton_back:
//                    sendCMD(car_back);
//                    break;
//                case R.id.imageButton_stop:
//                    sendCMD(car_stop);
//                    break;
//            }


//        }
//    }
}