package com.example.roboticarmcontrol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class controller extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice[] btArray;
    int REQUEST_ENABLE_BT=1;
    int requestCodeEnable;
    Button connect;
    ListView listView;
    TextView status,s1,s2,s3,s4;
    boolean connection_check=false;
    FloatingActionButton fab1,fab2,fab3,fab4;
    SeekBar sb1,sb2,sb3,sb4;
   // char data[]=new char[4];
    char[] data={'L','L','L','L','L','L','L','L'};



    static final int STATE_CONNECTED=1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTION_FAILED=3;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    SendData sendData;



    public void ShowPairedDevices(View view)
    {

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        String deviceName[]=new String[pairedDevices.size()];
        btArray=new BluetoothDevice[pairedDevices.size()];
        //jump to activity1




        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            int i=0;
            for (BluetoothDevice device : pairedDevices)
            {
                btArray[i]=device;
                deviceName[i] = device.getName();
                i++;
                //String deviceHardwareAddress = device.getAddress(); // MAC address
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceName);
            ListView listView = (ListView) findViewById(R.id.pairedList);
            listView.setAdapter(arrayAdapter);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientClass clientClass=new ClientClass(btArray[position]);
                clientClass.start();
                status.setText("Connecting");
                listView.setAdapter(null);

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeEnable) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth is Enabled", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Bluetooth enabling failed", Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        setContentView(R.layout.activity_controller);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        if (!bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            requestCodeEnable=1;
        }
        findViewByIds();
        sendingdata();
      //  TextView d =findViewById(R.id.datacheck);
        //d.setText(String.valueOf(data,0,4));


    }
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch(msg.what)
            {
                case STATE_CONNECTED:
                    status.setText("Connected"); connection_check=true;
                    break;
                case STATE_CONNECTING:
                    status.setText("Conecting");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
            }
            return false;
        }
    });
    private void findViewByIds()
    {
        connect=(Button) findViewById(R.id.button);
        status=(TextView) findViewById(R.id.status);
        listView=(ListView) findViewById(R.id.pairedList);
        fab1=findViewById(R.id.floatingActionButton1);
        fab2=findViewById(R.id.floatingActionButton2);
        fab3=findViewById(R.id.floatingActionButton3);
        fab4=findViewById(R.id.floatingActionButton4);
        sb1=findViewById(R.id.skb1);
        sb2=findViewById(R.id.skb2);
        sb3=findViewById(R.id.skb3);
        sb4=findViewById(R.id.skb4);
        s1=findViewById(R.id.status1);
        s2=findViewById(R.id.status2);
        s3=findViewById(R.id.status3);
        s4=findViewById(R.id.status4);



    }
    void sendingdata()
    {

            fab1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    TextView d;
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:data[0]='H';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();
                        break;

                        case MotionEvent.ACTION_UP:
                            data[0]='L';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                            break;

                    }


                    return false;

                }

            });
            fab2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    TextView d;
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN: data[1]='H';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                            break;
                        case MotionEvent.ACTION_UP:data[1]='L';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                            break;

                    }

                    return false;
                }
            });
            fab3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    TextView d;
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN: data[2]='H';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                            break;
                        case MotionEvent.ACTION_UP: data[2]='L';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                            break;
                       // default:data[2]='L';
                    }


                    return false;
                }
            });
            fab4.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    TextView d;

                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN: data[3]='H';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                            break;
                        case MotionEvent.ACTION_UP:data[3]='L';
                            if(connection_check)
                            {
                                d =findViewById(R.id.datacheck);
                                String str=String.valueOf(data)+"\n";
                                d.setText(str);
                                sendData.write(str.getBytes());
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                            break;
                     //   default:data[3]='L';
                    }









                    return false;
                }
            });

            sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    s1.setText(""+progress);
                    data[4]=(char)progress;
                    if(connection_check)
                    {
                        TextView d =findViewById(R.id.datacheck);
                        String str=String.valueOf(data)+"\n";
                        d.setText(str);
                        sendData.write(str.getBytes());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

            });
            sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    s2.setText(""+progress);
                    data[5]=(char)progress;
                    if(connection_check)
                    {
                        TextView d =findViewById(R.id.datacheck);
                        String str=String.valueOf(data)+"\n";
                        d.setText(str);
                        sendData.write(str.getBytes());

                    }
                    else
                        Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    s3.setText(""+progress);
                    data[6]=(char)progress;
                    if(connection_check)
                    {
                        TextView d =findViewById(R.id.datacheck);
                        String str=String.valueOf(data)+"\n";
                        d.setText(str);
                        sendData.write(str.getBytes());

                    }
                    else
                        Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            sb4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    s4.setText(""+progress);
                    data[7]=(char)progress;
                    if(connection_check)
                    {
                        TextView d =findViewById(R.id.datacheck);
                        String str=String.valueOf(data)+"\n";
                        d.setText(str);
                        sendData.write(str.getBytes());

                    }
                    else
                        Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });




    }
    class ClientClass extends Thread
    {
        BluetoothDevice device;
        private BluetoothSocket socket;


        public ClientClass(BluetoothDevice device1)
        {
            device=device1;

            try {
                socket=device.createInsecureRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try
            {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);
                sendData=new SendData(socket);
                sendData.start();
                connection_check=true;

            }
            catch (IOException e)
            {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

            }
        }

    }
    private class SendData extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final OutputStream outputStream;

        public SendData(BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            OutputStream tempOut=null;

            try {
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream=tempOut;

        }
        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Bluetooth not connected",Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }
    }



    public void fullscreen()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

    }
}
