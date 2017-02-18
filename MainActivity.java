package com.example.lenovo.mobil_proje;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.AbsSeekBar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.net.URI;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import android.os.Bundle;





public class MainActivity extends Activity implements Runnable {



    Button muzikcal,butonileri,butongeri;
    SeekBar sesayar;
    ProgressBar psure;
    TextView txtsure,gecensure;
    ListView mylist;

    AudioManager manager;
    MediaPlayer media;

    private  int saniye;

private android.os.Handler mhandler=new android.os.Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_main);



      //play music
        muzikcal = (Button) findViewById(R.id.btnmuzik);
		
		//button seek
        butonileri = (Button) findViewById(R.id.btnileri);
		//button back
        butongeri = (Button) findViewById(R.id.btngeri);
		//voice control
        sesayar = (SeekBar) findViewById(R.id.seekbarses);
		//show time 
        psure = (ProgressBar) findViewById(R.id.prgbsure);
		//show time remaining
        txtsure = (TextView) findViewById(R.id.txtsure);
		
		//time elapsed
        gecensure = (TextView) findViewById(R.id.txtgecensure);
		
		//music list
        mylist=(ListView)findViewById(R.id.listViewMyliste);

		//for voice control with seekbar
        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        sesayar.setMax(manager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sesayar.setProgress(manager
                .getStreamVolume(AudioManager.STREAM_MUSIC));

				// searc and find music in raw file
        final Field[] dosyalar = R.raw.class.getFields();
        String[] listem = new String[dosyalar.length];
        for (int count = 0; count < dosyalar.length; count++)
            listem[count] = dosyalar[count].getName();


        muzikcal.setText("Cal");

//music list
        ArrayAdapter<String> myadapter= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,listem);
        mylist.setAdapter(myadapter);







        muzikcal.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
				//music play
                if (muzikcal.getText() == "Cal") {
					//if no chose music
                    if(media==null)
                    {
					
                        Toast.makeText(getApplicationContext(),"Şarkı seçimi yapmadınız",Toast.LENGTH_SHORT).show();
                    }
                    else {


                        media.start();

                        Toast.makeText(getApplicationContext(), "çalıyor", Toast.LENGTH_SHORT).show();
                        new Thread(MainActivity.this).start();
                        mhandler.post(calıs) ;

                        muzikcal.setText("Durdur");


                    }

                } 
				//music stop
				else if (muzikcal.getText() == "Durdur") {
                    media.pause();

                    psure.setProgress(media.getCurrentPosition());

                    Toast.makeText(getApplicationContext(), "Durduruldu", Toast.LENGTH_SHORT).show();
                    muzikcal.setText("Cal");

                }


            }
        });

		//buton seek
        butonileri.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                media.seekTo(media.getCurrentPosition()+5000);

            }
        });




            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    txtsure.setText("");
                    String secilen;


                    secilen = "android.resource://" + getPackageName() + "/raw/" + ((TextView) view).getText();


                    if (media != null && media.isPlaying()) {
                        media.stop();
                        muzikcal.setText("Cal");
                    }


                    media = MediaPlayer.create(getApplicationContext(), Uri.parse(secilen));
                    psure.setMax(media.getDuration());

                    int son=0;
                    son = media.getDuration();
             Toast.makeText(getApplicationContext(),  "   " + ((TextView) view).getText().toString(),Toast.LENGTH_SHORT).show();
                     txtsure.setText(String.format("%d dk,%d sn", TimeUnit.MILLISECONDS.toMinutes((long) son), TimeUnit.MILLISECONDS.toSeconds((long) son) -
                             TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) son))));


                }
            });










        butongeri.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int sure = media.getCurrentPosition();

                if (sure > 5000) {
                    sure = sure - 5000;
                    media.seekTo(sure);
                } else if (sure <= 5000) {
                    media.seekTo(0);
                }
            }
        });

        sesayar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





    }

    private Runnable calıs = new Runnable() {

        public void run() {
            int dakika=0;

            dakika=((int)media.getCurrentPosition()/1000)/60;
            saniye=((int)media.getCurrentPosition()/1000)%60;

            gecensure.setText(String.format("%d dk,%d sn",dakika,saniye));
        }
    };




    public void run() {
        try {
            while (media.getCurrentPosition()<media.getDuration()) {
                Thread.sleep(1000);
                runOnUiThread(new Runnable() {
                    public void run() {
                        psure.setProgress(media.getCurrentPosition());
                        mhandler.postDelayed(calıs, 1000);

                    }
                });
            }
        } catch (InterruptedException e) {
        }
    }








    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }




}

