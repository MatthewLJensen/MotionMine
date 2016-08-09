package matthew.motionmine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MineActivity extends AppCompatActivity implements AccelerometerListener {


    public MediaPlayer mpexplosion;
    public boolean isarmed = false;
    public boolean flashenabled;
    public boolean arminginprocess = false;
    public String TAG = "MineActivity.class";
    static public TapTestCounter CountDownTimer;
    private final long startTime = 3100;
    private final long interval = 1000;
    public PowerManager powerManager;
    public PowerManager.WakeLock wakeLock;
    public PowerManager.WakeLock wuwakelock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        CountDownTimer = new TapTestCounter(startTime, interval);
        final TextView status = (TextView) findViewById(R.id.status);
        final Button armbtn = (Button) findViewById(R.id.armmine);
        final TextView timetoarmed = (TextView) findViewById(R.id.timetoarmed);
        status.setTextColor(Color.RED);
        mpexplosion = MediaPlayer.create(MineActivity.this, R.raw.explosionsound);
        flashenabled = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);


        armbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isarmed && !arminginprocess) {
                    Log.i(TAG, "Arming Mine");
                    armbtn.setText("Disarm Mine");
                    timetoarmed.setVisibility(View.VISIBLE);
                    CountDownTimer.start();
                    arminginprocess = true;
                } else {
                    Log.i(TAG, "Disarming Mine");
                    status.setText("Disarmed");
                    status.setTextColor(Color.RED);
                    armbtn.setText("Arm Mine");
                    isarmed = false;
                    CountDownTimer.cancel();
                    arminginprocess = false;
                    timetoarmed.setVisibility(View.INVISIBLE);

                    try {
                        if (wakeLock != null) {
                            wakeLock.release();

                        } else {
                            Log.i(TAG, "Wakelock not released");
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "Wakelock probably released");
                    }


                }

            }
        });

    }


    public void flash() {
        final Camera cam = Camera.open();
        final Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();


        final Handler handlerlight = new Handler();
        handlerlight.postDelayed(new Runnable() {
            @Override
            public void run() {

                cam.stopPreview();
                cam.release();
            }
        }, 1500);

    }

    public void vibrate() {

        //code for explosion vibration
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1500);
    }

    public void explode() {
        Button armbtn = (Button) findViewById(R.id.armmine);
        TextView status = (TextView) findViewById(R.id.status);
        isarmed = false;
        status.setText("Disarmed");
        status.setTextColor(Color.RED);
        armbtn.setText("Arm Mine");



        Log.v("ProximityActivity", "ON!");
        wuwakelock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        wuwakelock.acquire();
        wuwakelock.release();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                vibrate();

            }
        }, 250);


        try {
            if (wakeLock != null) {
                wakeLock.release();

            } else {
                Log.i(TAG, "Wakelock not released");
            }
        } catch (Exception e) {
            Log.i(TAG, "Wakelock probably released");
        }


        //code for mp3 file
        mpexplosion.start();


        if (flashenabled) {
            //code for flash

            flash();

        }


        isarmed = false;
    }

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        if (isarmed) {
            explode();
        }
        // Called when Motion Detected
        Log.i("toast replacement", "Motion Detected");
        //Toast.makeText(getBaseContext(), "Motion detected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("toast replacement", "onResume Accelerometer Started");
        //Toast.makeText(getBaseContext(), "onResume Accelerometer Started", Toast.LENGTH_SHORT).show();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);

        }else {


            AlertDialog alertDialog = new AlertDialog.Builder(MineActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("We have found your device to be incompatible with this app. We are sorry for your inconvenience.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            // AccelerometerManager.stopListening();
            Log.i("toast replacement", "onStop Accelerometer Stopped");
            //Toast.makeText(getBaseContext(), "onStop Accelerometer Stopped",Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
            Log.i("toast replacement", "onDestroy Accelerometer Stopped");




            try {
                if (wakeLock != null) {
                    wakeLock.release();

                } else {
                    Log.i(TAG, "Wakelock not released");
                }
            } catch (Exception e) {
                Log.i(TAG, "Wakelock probably released");
            }


            //Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",Toast.LENGTH_SHORT).show();
        }

    }


    public class TapTestCounter extends CountDownTimer {

        TextView timetoarmed = (TextView) findViewById(R.id.timetoarmed);

        public TapTestCounter(long startTime, long interval) {
            super(startTime, interval);
        }


        @Override
        public void onFinish() {
            TextView status = (TextView) findViewById(R.id.status);
            Button armbtn = (Button) findViewById(R.id.armmine);
            status.setText("Device Armed");
            status.setTextColor(Color.GREEN);
            armbtn.setText("Disarm Mine");
            timetoarmed.setVisibility(View.INVISIBLE);
            isarmed = true;
            arminginprocess = false;


            powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
            wakeLock.acquire();


        }

        @Override
        public void onTick(long millisUntilFinished) {
            timetoarmed.setText(millisUntilFinished / 1000 + "");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        } */


        if (id == R.id.action_info) {
            Intent intent = new Intent(this, InfoActivity.class);
            MineActivity.this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
