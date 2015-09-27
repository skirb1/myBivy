package sarahkirby.knightnighta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;

/**
 * MainActivity file for the myBivy app, an app for PTSD survivors to
 * track sleep disruptions with the pebble watch.
 *
 * Uses PebbleKit Android SDK
 * Contains instantiation for widgets and objects.
 * Contains functions to connect and accept data from pebble watch accelerometer
 * Contains statistics functions to analyze data (commented out below)
 * Contains chronometer functions to keep track of total sleep time
 *
 */
public class MainActivity extends Activity  {
    private Chronometer chron;
    private TextView myText;
    private Button startB, stopB;

    private static final UUID APP_UUID = UUID.fromString("788ed624-e9fd-4e62-94bc-9aaceb349147");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private final StringBuilder mDisplayText = new StringBuilder();
    private PebbleKit.PebbleDataLogReceiver mDataLogReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        DATE_FORMAT.setTimeZone(TimeZone.getDefault());

        myText = (TextView)findViewById(R.id.main_text);
        chron = (Chronometer)findViewById(R.id.chronometer);
        startB = (Button)findViewById(R.id.start_b);
        stopB = (Button)findViewById(R.id.stop_b);

        //start chronometer on start button
        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chron.start();
                myText.setText("Waiting for data...");
            }
        });

        //stop chronometer on stop button
        stopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chron.stop();
                myText.setText("Sleep logged.");
            }
        });
    }

    //Unregister receiver when data connection is lost
    @Override
    protected void onPause() {
        super.onPause();
        if (mDataLogReceiver != null) {
            unregisterReceiver(mDataLogReceiver);
            mDataLogReceiver = null;
        }
    }

    //register data log receiver when data is ready to be received
    @Override
    protected void onResume() {
        super.onResume();

        mDataLogReceiver = new PebbleKit.PebbleDataLogReceiver(APP_UUID) {
            @Override
            public void receiveData(android.content.Context context, UUID logUuid, Long timestamp, Long tag,
                                    int data) {
                mDisplayText.append("\nreceived data");
                //mDisplayText.append(tag.intValue());
            }

            @Override
            public void onFinishSession(android.content.Context context, UUID logUuid, Long timestamp, Long tag) {
                super.onFinishSession(context, logUuid, timestamp, tag);
                myText.setText(mDisplayText.toString());
            }

        };
        PebbleKit.registerDataLogReceiver(this, mDataLogReceiver);
        PebbleKit.requestDataLogsForApp(this, APP_UUID);
    }
/*
    private String getTimestamp() {
        //return DATE_FORMAT.format(new Date(uint.longValue() * 1000L)).toString();
        return "time";
    }*/

}


/**
 package stackoverflow;

 import org.rosuda.JRI.REXP;
 import org.rosuda.JRI.Rengine;

 /**This function takes the data taken from
 * the pebble watch sensor as input, and outputs
 * the function in another class as java output.
 *
 * Credit to Stack Overflow for providing a very rough
 * package for doing so.
 * @author Tyler
 *
 */
/**
 public class Analytics_from_R {
 public static void main(String[] args) throws Exception {
 String[] Rargs = {"--vanilla"};
 Rengine rengine = new Rengine(  Rargs, false, null);
 rengine.eval("require(mosaic)"); //Requires mosaic package that allows for essential statistical calculations.
 rengine.eval("library('survival')"); //loads library allowing us to conduct survival analysis
 rengine.eval("vecData = main.get("android.content.Context context, UUID lopQuid, Long timestamp, Long tag, int data)";"
 rengine.eval("timeTrack = c(vecData[,1], (length(vecData*1)));
 //insert code line to find survival censoring. This would take a couple more days to research and code.
 rengine.eval("mean1 = mean(timeTrack[1:3]);
 REXP result1= rengine.eval("Mean 7-day Sleep Time: ");
 REXP result2= rengine.eval("Mean sleep time until disturbance: ");
 System.out.println("Greeting from R: "+result1.asDouble()+result2.asDouble()); //this should look like a nicely formatted table.
 }
 }
 **/
