package com.example.rgbcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageButton;
import top.defaults.colorpicker.ColorPickerPopup;

public class HomeActivity extends AppCompatActivity {
    //TODO: define
    private Context context;
    private String btData;
    private TextView textViewBT;
    private BluetoothAdapter btAdapter;
    private BTChatService myChatService;
    private String macAddress;


    private String ALL_ON = "1";
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
    private final String breathOff = "m";
    private final String flashOff = "n";
    private final String wcOff = "o";

//    //TODO:seekbar Srting R
//    private final String R1 =">AL<";
//    private final String R2 =">BL<";
//    private final String R3 =">CL<";
//    private final String R4 =">DL<";
//    private final String R5 =">EL<";
//    private final String R6 =">FL<";
//    private final String R7 =">GL<";
//    private final String R8 =">HL<";
//    private final String R9 =">IL<";
//    private final String R10 =">JL<";
//    private final String R11 =">KL<";
//    //TODO:seekbar Srting G
//    private final String G1 =">L%<";
//    private final String G2 =">M%<";
//    private final String G3 =">N%<";
//    private final String G4 =">O%<";
//    private final String G5 =">P%<";
//    private final String G6 =">Q%<";
//    private final String G7 =">R%<";
//    private final String G8 =">S%<";
//    private final String G9 =">T%<";
//    private final String G10 =">U%<";
//    private final String G11 =">V%<";
//    //TODO:seekbar Srting B
//    private final String B1 =">W&<";
//    private final String B2 =">X&<";
//    private final String B3 =">Y&<";
//    private final String B4 =">Z&<";
//    private final String B5 =">d&<";
//    private final String B6 =">e&<";
//    private final String B7 =">f&<";
//    private final String B8 =">g&<";
//    private final String B9 =">h&<";
//    private final String B10 =">i&<";
    private final String B11 =">j&<";
//    TODO:seekbar String W
    private final String W1 =">Lw<";
    private final String W2 =">Mw<";
    private final String W3 =">Nw<";
    private final String W4 =">Ow<";
    private final String W5 =">Pw<";
    private final String W6 =">Qw<";
    private final String W7 =">Rw<";
    private final String W8 =">Sw<";
    private final String W9 =">Tw<";
    private final String W10 =">Uw<";
    private final String W11 =">Vw<";
//    TODO:seekbar String C
    private final String C1 =">Lx<";
    private final String C2 =">Mx<";
    private final String C3 =">Nx<";
    private final String C4 =">Ox<";
    private final String C5 =">Px<";
    private final String C6 =">Qx<";
    private final String C7 =">Rx<";
    private final String C8 =">Sx<";
    private final String C9 =">Tx<";
    private final String C10 =">Ux<";
    private final String C11 =">Vx<";


    private SeekBar seekbarR,seekbarG,seekbarB,seekbarW,seekbarC;
    private TextView textViewR,textViewG,textViewB,textViewW,textViewC;
    private int level_R,level_G,level_B,level_W,level_C;
    private GifImageButton imageButtonR,imageButtonG,imageButtonB,imageButtonBG,imageButtonP,imageButtonY,imageButtonRainbow,imageButtonFlash,imageButtonChase,imageButtonBreath,imageButtonWC;
    private GifImageButton imageButtonAllOn,mPickColorButton;
    private String rgbValue;
    private int mDefaultColor;
    private int colorFlag;
    private String wvalue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        context = this;
        setTitle("RGB control");

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
        imageButtonAllOn = (GifImageButton)findViewById(R.id.imageButton_all);
        imageButtonR = (GifImageButton) findViewById(R.id.imageButton_R);
        imageButtonG = (GifImageButton) findViewById(R.id.imageButton_G);
        imageButtonB = (GifImageButton) findViewById(R.id.imageButton_B);
        imageButtonBG = (GifImageButton) findViewById(R.id.imageButton_BG);
        imageButtonP = (GifImageButton) findViewById(R.id.imageButton_P);
        imageButtonY = (GifImageButton) findViewById(R.id.imageButton_Y);
        imageButtonRainbow = (GifImageButton) findViewById(R.id.imageButton_Rainbow);
        imageButtonFlash = (GifImageButton) findViewById(R.id.imageButton_Flash);
//        imageButtonChase = (GifImageButton) findViewById(R.id.imageButton_Chase);
        imageButtonBreath = (GifImageButton) findViewById(R.id.imageButton_Breath);
        imageButtonWC = (GifImageButton) findViewById(R.id.imageButton_wc);

        imageButtonAllOn.setOnClickListener(new MyImagebutton());
        imageButtonR.setOnClickListener(new MyImagebutton());
        imageButtonG.setOnClickListener(new MyImagebutton());
        imageButtonB.setOnClickListener(new MyImagebutton());
        imageButtonBG.setOnClickListener(new MyImagebutton());
        imageButtonP.setOnClickListener(new MyImagebutton());
        imageButtonY.setOnClickListener(new MyImagebutton());
        imageButtonRainbow.setOnClickListener(new MyImagebutton());
        imageButtonFlash.setOnClickListener(new MyImagebutton());
//        imageButtonChase.setOnClickListener(new MyImagebutton());
        imageButtonBreath.setOnClickListener(new MyImagebutton());
        imageButtonWC.setOnClickListener(new MyImagebutton());

//TODO:seekbar
        //TODO: R
//        textViewR = (TextView) findViewById(R.id.textView_R);
//        textViewR.setText("");
//        seekbarR = (SeekBar) findViewById(R.id.seekBar_R);
//        seekbarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                textViewR.setText("當前值:"+ progress +"/255");
//                level_R = progress;
//                Log.d("home","R = "+level_R);
//
//
//                int Rvalue = level_R / 10;
//                switch (Rvalue){
//                    case 10:
//                        sendCMD(R1);
//                        break;
//                    case 9:
//                        sendCMD(R2);
//                        break;
//                    case 8:
//                        sendCMD(R3);
//                        break;
//                    case 7:
//                        sendCMD(R4);
//                        break;
//                    case 6:
//                        sendCMD(R5);
//                        break;
//                    case 5:
//                        sendCMD(R6);
//                        break;
//                    case 4:
//                        sendCMD(R7);
//                        break;
//                    case 3:
//                        sendCMD(R8);
//                        break;
//                    case 2:
//                        sendCMD(R9);
//                        break;
//                    case 1:
//                        sendCMD(R10);
//                        break;
//                    case 0:
//                        sendCMD(R11);
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
//            }
//        });
        //TODO: G
//        textViewG = (TextView) findViewById(R.id.textView_G);
//        textViewG.setText("");
//        seekbarG = (SeekBar) findViewById(R.id.seekBar_G);
//        seekbarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                textViewG.setText("當前值:"+ progress +"/255");
//                level_G = progress;
//                Log.d("home","G = "+level_G);
//
//                int Gvalue = level_G / 10;
//                switch (Gvalue){
//                    case 10:
//                        sendCMD(G1);
//                        break;
//                    case 9:
//                        sendCMD(G2);
//                        break;
//                    case 8:
//                        sendCMD(G3);
//                        break;
//                    case 7:
//                        sendCMD(G4);
//                        break;
//                    case 6:
//                        sendCMD(G5);
//                        break;
//                    case 5:
//                        sendCMD(G6);
//                        break;
//                    case 4:
//                        sendCMD(G7);
//                        break;
//                    case 3:
//                        sendCMD(G8);
//                        break;
//                    case 2:
//                        sendCMD(G9);
//                        break;
//                    case 1:
//                        sendCMD(G10);
//                        break;
//                    case 0:
//                        sendCMD(G11);
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
//            }
//        });
// TODO: B

//        textViewB = (TextView) findViewById(R.id.textView_B);
//        textViewB.setText("");
//        seekbarB = (SeekBar) findViewById(R.id.seekBar_B);
//        seekbarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                textViewB.setText("當前值:"+ progress +"/255");
//                level_B = progress;
//                Log.d("home","B = "+level_B);
//
//                int Bvalue = level_B / 10;
//                switch (Bvalue){
//                    case 10:
//                        sendCMD(B1);
//                        break;
//                    case 9:
//                        sendCMD(B2);
//                        break;
//                    case 8:
//                        sendCMD(B3);
//                        break;
//                    case 7:
//                        sendCMD(B4);
//                        break;
//                    case 6:
//                        sendCMD(B5);
//                        break;
//                    case 5:
//                        sendCMD(B6);
//                        break;
//                    case 4:
//                        sendCMD(B7);
//                        break;
//                    case 3:
//                        sendCMD(B8);
//                        break;
//                    case 2:
//                        sendCMD(B9);
//                        break;
//                    case 1:
//                        sendCMD(B10);
//                        break;
//                    case 0:
//                        sendCMD(B11);
//                        break;
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
//            }
//        });
//TODO: W
        textViewW = (TextView) findViewById(R.id.textView_w);
        textViewW.setText("");
        seekbarW = (SeekBar) findViewById(R.id.seekBar_w);
        seekbarW.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewW.setText("當前值:"+ progress +"/100");
                imageButtonWC.setBackgroundResource(R.drawable.wc_offgif);
                level_W = progress;
                Log.d("home","W = "+level_W);
                wvalue = Integer.toHexString(level_W);
                Log.d("home","W HEX = "+wvalue);
//                sendCMD(">"+wvalue+"<");

                int Wvalue = level_W / 10;
                switch (Wvalue){
                    case 10:
                        sendCMD(W1);
                        break;
                    case 9:
                        sendCMD(W2);
                        break;
                    case 8:
                        sendCMD(W3);
                        break;
                    case 7:
                        sendCMD(W4);
                        break;
                    case 6:
                        sendCMD(W5);
                        break;
                    case 5:
                        sendCMD(W6);
                        break;
                    case 4:
                        sendCMD(W7);
                        break;
                    case 3:
                        sendCMD(W8);
                        break;
                    case 2:
                        sendCMD(W9);
                        break;
                    case 1:
                        sendCMD(W10);
                        break;
                    case 0:
                        sendCMD(W11);
                        break;

                }
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
//TODO: C
        textViewC = (TextView) findViewById(R.id.textView_c);
        textViewC.setText("");
        seekbarC = (SeekBar) findViewById(R.id.seekBar_c);
        seekbarC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewC.setText("當前值:"+ progress +"/100");
                imageButtonWC.setBackgroundResource(R.drawable.wc_offgif);
                level_C = progress;
                Log.d("home","C = "+level_C);

                int Cvalue = level_C / 10;
                switch (Cvalue){
                    case 10:
                        sendCMD(C1);
                        break;
                    case 9:
                        sendCMD(C2);
                        break;
                    case 8:
                        sendCMD(C3);
                        break;
                    case 7:
                        sendCMD(C4);
                        break;
                    case 6:
                        sendCMD(C5);
                        break;
                    case 5:
                        sendCMD(C6);
                        break;
                    case 4:
                        sendCMD(C7);
                        break;
                    case 3:
                        sendCMD(C8);
                        break;
                    case 2:
                        sendCMD(C9);
                        break;
                    case 1:
                        sendCMD(C10);
                        break;
                    case 0:
                        sendCMD(C11);
                        break;

                }
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
//TODO: colorPick

        colorFlag = 0;
        mPickColorButton = (GifImageButton) findViewById(R.id.pick_color_button);

        mDefaultColor = 0;

        mPickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(colorFlag == 0){
                    new ColorPickerPopup.Builder(context).initialColor(Color.RED).enableBrightness(true).cancelTitle("Cancel").showIndicator(true).showValue(true).build().show(v, new ColorPickerPopup.ColorPickerObserver() {
                        @Override
                        public void onColorPicked(int color) {
                            mDefaultColor = color;
                            rgbValue = Integer.toHexString(mDefaultColor);
                            sendCMD(">"+rgbValue+"<k");
                            mPickColorButton.setBackgroundResource(R.drawable.color_off);
                            colorFlag = 1;
                        }
                    });
                } else {
                    sendCMD(ALL_OFF);
                    mPickColorButton.setBackgroundResource(R.drawable.color_on);
                    colorFlag = 0;
                }

                Log.d("home","mDefaultColor = "+rgbValue);
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
    int flagAllOn = 0;
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
                case R.id.imageButton_all:
                    switch (flagAllOn) {
                        case 0:
                            imageButtonAllOn.setBackgroundResource(R.drawable.all_off);
                            imageButtonAllOn.setScaleType(ImageButton.ScaleType.CENTER);
                            sendCMD(ALL_ON);
                            flagAllOn = 1;
                            break;
                        case 1:
                            imageButtonAllOn.setBackgroundResource(R.drawable.all_on);
                            sendCMD(ALL_OFF);
                            flagAllOn = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_R:
                    switch (flagR){
                        case 0:
                            imageButtonR.setBackgroundResource(R.drawable.r_off);
                            sendCMD(RLED);
                            flagR = 1;
                            break;
                        case 1:
                            imageButtonR.setBackgroundResource(R.drawable.r_on);
                            sendCMD(ALL_OFF);
                            flagR = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_B:
                    switch (flagB){
                        case 0:
                            imageButtonB.setBackgroundResource(R.drawable.b_off);
                            sendCMD(B);
                            flagB = 1;
                            break;
                        case 1:
                            imageButtonB.setBackgroundResource(R.drawable.b_on);
                            sendCMD(ALL_OFF);
                            flagB = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_G:
                    switch (flagG){
                        case 0:
                            imageButtonG.setBackgroundResource(R.drawable.g_off);
                            sendCMD(G);
                            flagG = 1;
                            break;
                        case 1:
                            imageButtonG.setBackgroundResource(R.drawable.g_on);
                            sendCMD(ALL_OFF);
                            flagG = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_BG:
                    switch (flagBG){
                        case 0:
                            imageButtonBG.setBackgroundResource(R.drawable.bg_off);
                            sendCMD(BG);
                            flagBG = 1;
                            break;
                        case 1:
                            imageButtonBG.setBackgroundResource(R.drawable.bg_on);
                            sendCMD(ALL_OFF);
                            flagBG = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_P:
                    switch (flagP){
                        case 0:
                            imageButtonP.setBackgroundResource(R.drawable.p_off);
                            sendCMD(purple);
                            flagP = 1;
                            break;
                        case 1:
                            imageButtonP.setBackgroundResource(R.drawable.p_on);
                            sendCMD(ALL_OFF);
                            flagP = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Y:
                    switch (flagY){
                        case 0:
                            imageButtonY.setBackgroundResource(R.drawable.y_off);
                            sendCMD(yellow);
                            flagY = 1;
                            break;
                        case 1:
                            imageButtonY.setBackgroundResource(R.drawable.y_on);
                            sendCMD(ALL_OFF);
                            flagY = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Rainbow:
                    switch (flagRainbow){
                        case 0:
                            imageButtonRainbow.setBackgroundResource(R.drawable.all_off);
                            sendCMD(rainbow);
                            flagRainbow = 1;
                            break;
                        case 1:
                            imageButtonRainbow.setBackgroundResource(R.drawable.rainbow_on);
                            sendCMD(ALL_OFF);
                            flagRainbow = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_Flash:
                    switch (flagFlash){
                        case 0:
                            imageButtonFlash.setBackgroundResource(R.drawable.all_off);
                            sendCMD(flash);
                            flagFlash = 1;
                            break;
                        case 1:
                            imageButtonFlash.setBackgroundResource(R.drawable.flash_on);
                            sendCMD(flashOff);
                            flagFlash = 0;
                            break;
                    }
                    break;
//                case R.id.imageButton_Chase:
//                    switch (flagChase){
//                        case 0:
//                            imageButtonChase.setBackgroundResource(R.drawable.all_off);
//                            sendCMD(chase);
//                            flagChase = 1;
//                            break;
//                        case 1:
//                            imageButtonChase.setBackgroundResource(R.drawable.chase_on);
//                            sendCMD(ALL_OFF);
//                            flagChase = 0;
//                            break;
//                    }
//                    break;
                case R.id.imageButton_Breath:
                    switch (flagBreath){
                        case 0:
                            imageButtonBreath.setBackgroundResource(R.drawable.all_off);
                            sendCMD(Breath);
                            flagBreath = 1;
                            break;
                        case 1:
                            imageButtonBreath.setBackgroundResource(R.drawable.breath_on);
                            sendCMD(breathOff);
                            flagBreath = 0;
                            break;
                    }
                    break;
                case R.id.imageButton_wc:
                    imageButtonWC.setBackgroundResource(R.drawable.wc_off);
                    sendCMD(wcOff);

            }
        }
    }

}