package com.example.rgbcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
//TODO: define
    private Context context;
    private String btData;
    private TextView textViewBT;
    private BluetoothAdapter btAdapter;
    private BTChatService myChatService;
    private String macAddress;
    private final String ALL_ON = "1";
    private final String ALL_OFF = "2";
    private final String Breath = "3";
    private final String RLED = "4";
    private final String G = "5";
    private final String B = "6";
    private final String purple = "7";
    private final String yellow = "8";
    private final String BG = "9";
    private final String rainbow = "a";
    private final String flash = "b";
    private final String chase = "c";
    private SeekBar seekbarR,seekbarG,seekbarB;
    private TextView textViewR,textViewG,textViewB;
    private int level_R,level_G,level_B;
       private ImageButton imageButtonR,imageButtonG,imageButtonB,imageButtonBG,imageButtonP,imageButtonY,imageButtonRainbow,imageButtonFlash,imageButtonChase,imageButtonBreath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        context = this;
        setTitle("Home control");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定成直立的畫面
        //返回鍵
        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        btData = intent.getStringExtra("btdata");
        Log.d("home","btData = "+btData);

        textViewBT = (TextView) findViewById(R.id.textView_homeBT);
        textViewBT.setText(btData);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myChatService = new BTChatService(context,myHandler);//藍芽的物件

        //先確定data是不是空的
        if(btData != null){
            macAddress = btData.substring(btData.length()-17);
            Log.d("home","macAddress = "+macAddress);
            BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
            myChatService.connect(device);
        }
        //TODO: imageButton
        imageButtonR = (ImageButton) findViewById(R.id.imageButton_R);
        imageButtonG = (ImageButton) findViewById(R.id.imageButton_G);
        imageButtonB = (ImageButton) findViewById(R.id.imageButton_B);
        imageButtonBG = (ImageButton) findViewById(R.id.imageButton_BG);
        imageButtonP = (ImageButton) findViewById(R.id.imageButton_P);
        imageButtonY = (ImageButton) findViewById(R.id.imageButton_Y);
        imageButtonRainbow = (ImageButton) findViewById(R.id.imageButton_Rainbow);
        imageButtonFlash = (ImageButton) findViewById(R.id.imageButton_Flash);
        imageButtonChase = (ImageButton) findViewById(R.id.imageButton_Chase);
        imageButtonBreath = (ImageButton) findViewById(R.id.imageButton_Breath);

        imageButtonR.setOnClickListener(new MyImagebutton());
        imageButtonG.setOnClickListener(new MyImagebutton());
        imageButtonB.setOnClickListener(new MyImagebutton());
        imageButtonBG.setOnClickListener(new MyImagebutton());
        imageButtonP.setOnClickListener(new MyImagebutton());
        imageButtonY.setOnClickListener(new MyImagebutton());
        imageButtonRainbow.setOnClickListener(new MyImagebutton());
        imageButtonFlash.setOnClickListener(new MyImagebutton());
        imageButtonChase.setOnClickListener(new MyImagebutton());
        imageButtonBreath.setOnClickListener(new MyImagebutton());





        textViewG = (TextView) findViewById(R.id.textView_G);
        textViewG.setText("");
        seekbarG = (SeekBar) findViewById(R.id.seekBar_G);
        seekbarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewG.setText("當前值:"+ progress +"/255");
                level_G = progress;
                Log.d("home","G = "+level_G);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
            }
        });
        textViewB = (TextView) findViewById(R.id.textView_B);
        textViewB.setText("");
        seekbarB = (SeekBar) findViewById(R.id.seekBar_B);
        seekbarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewB.setText("當前值:"+ progress +"/255");
                level_B = progress;
                Log.d("home","G = "+level_B);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
            }
        });


    }
    //返回鍵的設定
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private final Handler myHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MESSAGE_DEVICE_NAME:
                    String data = msg.getData().getString(Constants.DEVICE_NAME);//回傳已連上的訊息
                    Toast.makeText(context,"Connected to "+data,Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST://連線錯誤的訊息
                    String error = msg.getData().getString(Constants.TOAST);
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };

    //傳送指令
    private void sendCMD(String message){
        int mState = myChatService.getState();
        if(mState == BTChatService.STATE_CONNECTED){
            if(message.length()>0){
                byte[] sendData = message.getBytes();
                myChatService.BTWrite((sendData));
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myChatService != null){
            myChatService.stop();
            myChatService=null;
        }
    }
//    int flag1 = 0;
//    int flag2 = 0;
//    private class ColorButton implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//
//            switch (v.getId()){
//                case R.id.button_Allon :
//
//                    switch (flag1){
//                        case 0:
//                            sendCMD(ALL_ON);
//                            flag1 = 1;
//                            break;
//                        case 1:
//                            sendCMD(ALL_OFF);
//                            flag1 = 0;
//                            break;
//                    }
//                    break;
//                case R.id.button_R:
//                    switch (flag2){
//                        case 0:
//                            sendCMD(RLED);
//                            flag2 = 1;
//                            break;
//                        case 1:
//                            sendCMD(ALL_OFF);
//                            flag2 = 0;
//                            break;
//                    }
//
//            }
//        }
//    }
    //TODO: imageButton onClick
    int flagR = 0;
    int flagG = 0;
    int flagB = 0;
    int flagBG = 0;
    int flagP = 0;
    int flagY = 0;
    int flagRainbow = 0;
    int flagFlash = 0;
    int flagChase = 0;
    int flagBreath = 0;


    private class MyImagebutton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageButton_R:
                    switch (flagR){
                        case 0:
                            imageButtonR.setImageResource(R.drawable.r_off);
                            sendCMD(RLED);
                            flagR = 1;
                            break;
                        case 1:
                            imageButtonR.setImageResource(R.drawable.r_on);
                            sendCMD(ALL_OFF);
                            flagR = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_B:
                    switch (flagB){
                        case 0:
                            imageButtonB.setImageResource(R.drawable.b_off);
                            sendCMD(B);
                            flagB = 1;
                            break;
                        case 1:
                            imageButtonB.setImageResource(R.drawable.b_on);
                            sendCMD(ALL_OFF);
                            flagB = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_G:
                    switch (flagG){
                        case 0:
                            imageButtonG.setImageResource(R.drawable.g_off);
                            sendCMD(G);
                            flagG = 1;
                            break;
                        case 1:
                            imageButtonG.setImageResource(R.drawable.g_on);
                            sendCMD(ALL_OFF);
                            flagG = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_BG:
                    switch (flagBG){
                        case 0:
                            imageButtonBG.setImageResource(R.drawable.bg_off);
                            sendCMD(BG);
                            flagBG = 1;
                            break;
                        case 1:
                            imageButtonBG.setImageResource(R.drawable.bg_on);
                            sendCMD(ALL_OFF);
                            flagBG = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_P:
                    switch (flagP){
                        case 0:
                            imageButtonP.setImageResource(R.drawable.p_off);
                            sendCMD(purple);
                            flagP = 1;
                            break;
                        case 1:
                            imageButtonP.setImageResource(R.drawable.p_on);
                            sendCMD(ALL_OFF);
                            flagP = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Y:
                    switch (flagY){
                        case 0:
                            imageButtonY.setImageResource(R.drawable.y_off);
                            sendCMD(yellow);
                            flagY = 1;
                            break;
                        case 1:
                            imageButtonY.setImageResource(R.drawable.y_on);
                            sendCMD(ALL_OFF);
                            flagY = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Rainbow:
                    switch (flagRainbow){
                        case 0:
                            imageButtonRainbow.setImageResource(R.drawable.all_off);
                            sendCMD(rainbow);
                            flagRainbow = 1;
                            break;
                        case 1:
                            imageButtonRainbow.setImageResource(R.drawable.rainbow_on);
                            sendCMD(ALL_OFF);
                            flagRainbow = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Flash:
                    switch (flagFlash){
                        case 0:
                            imageButtonFlash.setImageResource(R.drawable.all_off);
                            sendCMD(flash);
                            flagFlash = 1;
                            break;
                        case 1:
                            imageButtonFlash.setImageResource(R.drawable.flash_on);
                            sendCMD(ALL_OFF);
                            flagFlash = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Chase:
                    switch (flagChase){
                        case 0:
                            imageButtonChase.setImageResource(R.drawable.all_off);
                            sendCMD(chase);
                            flagChase = 1;
                            break;
                        case 1:
                            imageButtonChase.setImageResource(R.drawable.chase_on);
                            sendCMD(ALL_OFF);
                            flagChase = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Breath:
                    switch (flagBreath){
                        case 0:
                            imageButtonBreath.setImageResource(R.drawable.all_off);
                            sendCMD(Breath);
                            flagBreath = 1;
                            break;
                        case 1:
                            imageButtonBreath.setImageResource(R.drawable.breath_on);
                            sendCMD(ALL_OFF);
                            flagBreath = 0;
                            break;
                    }
                    break;
            }
        }
    }
}