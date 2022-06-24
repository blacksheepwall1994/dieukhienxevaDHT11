package com.droiduino.droiduinorccontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnUp,btnDown,btnLeft,btnRight;
    private static TextView nhietdotxv, doamtxv;
    private String deviceName = null;
    private String deviceAddress;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khai bao giao dien nguoi dung
        final Button buttonConnect = findViewById(R.id.buttonConnect);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Phiên bản " + BuildConfig.VERSION_NAME);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnUp = findViewById(R.id.imageButtonUp);
        btnDown = findViewById(R.id.imageButtonDown);
        btnLeft = findViewById(R.id.imageButtonLeft);
        btnRight = findViewById(R.id.imageButtonRight);
        nhietdotxv = findViewById(R.id.nhietdo);
        doamtxv = findViewById(R.id.doam);
        btnUp.setEnabled(false);
        btnDown.setEnabled(false);
        btnLeft.setEnabled(false);
        btnRight.setEnabled(false);

        // Neu da chon thiet bi bluetooth
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null){
            // Lay dia chi thiet bi de ket noi
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Hien thi trang thai ket noi
            toolbar.setSubtitle("Đang kết nối đến " + deviceName + "...");
            progressBar.setVisibility(View.VISIBLE);
            buttonConnect.setEnabled(false);

            /*
            Khi deviceName da tim thay, code se goi mot thread moi de tao lap ket
            noi bluetooth den thiet bi da chon
             */
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter,deviceAddress);
            createConnectThread.start();
            //Log.e("blt", connectedThread.toString());
        }

        //ket noi toi thiet bi
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                toolbar.setSubtitle("Đã kết nối đến " + deviceName);
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                btnUp.setEnabled(true);
                                btnDown.setEnabled(true);
                                btnLeft.setEnabled(true);
                                btnRight.setEnabled(true);
                                break;
                            case -1:
                                toolbar.setSubtitle("Thiết bi kết nối thất bại");
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        break;
                }
            }
        };

        // Chuyen sang 1 intent ket noi bluetooth
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
                startActivity(intent);
            }
        });
        //connectedThread.read();
        // Nut di chuyen
        // .... Di thang
        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    String textCommand = "A";
                    Log.e("RC Command", textCommand);
                    connectedThread.write(textCommand);
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        String textCommand = "X";
                        Log.e("RC Command", textCommand);
                        connectedThread.write(textCommand);
                    }
                }
                return true;
            }
        });

        // .... Di lui ....
        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    String textCommand = "B";
                    Log.e("RC Command", textCommand);
                    connectedThread.write(textCommand);
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        String textCommand = "X";
                        Log.e("RC Command", textCommand);
                        connectedThread.write(textCommand);
                    }
                }
                return true;
            }
        });

        // .... Sang trai...
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    String textCommand = "D";
                    Log.e("RC Command", textCommand);
                    connectedThread.write(textCommand);
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        String textCommand = "X";
                        Log.e("RC Command", textCommand);
                        connectedThread.write(textCommand);
                    }
                }
                return true;
            }
        });

        // .... Sang phai...
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    String textCommand = "C";
                    Log.e("RC Command", textCommand);
                    connectedThread.write(textCommand);
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        String textCommand = "X";
                        Log.e("RC Command", textCommand);
                        connectedThread.write(textCommand);
                    }
                }
                return true;
            }
        });
    }

    /* ============================ Thread to Create Connection =================================== */
    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

                //TODO : Check why the methods below doesnt work, is it because I use Samsung ?
//                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//                tmp = device.createRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private  boolean createString = false;
        private String readMessage;


        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {


            String abc = "";
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    buffer[bytes] = (byte) mmInStream.read();
                    String readByte = new String(buffer, StandardCharsets.UTF_8).substring(0,1);
                    //String tempMsg=new String(buffer,0,);
                    Log.e("doam", readByte);
                    if(readByte.compareTo(";") != 0){
                        abc +=readByte;
                    }else{
                        final String[] part = abc.split(",");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                //Context context = getApplicationContext();
                                try{
                                    if(Float.parseFloat(part[0])>60){
                                        Log.e("quac", "khong duoc be oi");
                                        doamtxv.setText("Độ ẩm: BÁO ĐỘNG!!! (" + part[0] + ")");
                                    }else{
                                        doamtxv.setText("Độ ẩm: " + part[0]);
                                    }
                                    //do stuff like remove view etc
                                    nhietdotxv.setText("Nhiệt độ: " + part[1]);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });


//                        Log.e("doam", part[0]);
//                        Log.e("nhietdo", part[1]);
                        abc = "";

                    }


                    //
                    switch (readByte){
                        case "<":
                            createString = true;
                            readMessage = "";
                            break;
                        case ">":
                            createString = false;
                            break;
                    }

                    //
                    if (createString){
                        readMessage = readMessage + readByte;
                    } else {
//                        Log.e("UID Length", readMessage.length() + " characters");
//                        String readUID = readMessage.substring(1).trim();
//                        handler.obtainMessage(MESSAGE_READ,readUID).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }
        public void read() throws IOException {
            Integer abc = mmInStream.read();
            Log.e("read", abc.toString());
        }
        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        if (createConnectThread != null){
            createConnectThread.cancel();

        }
        finish();
    }

}
