package com.neatocode.gyroimageview;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;


/**
 * The main application service that manages the lifetime of the compass live card and the objects
 * that help out with orientation tracking and landmarks.
 */
public class ViewActivityService extends Service {

    private static final String LIVE_CARD_ID = "compass";

    /**
     * A binder that gives other components access to the speech capabilities provided by the
     * service.
     */    
    public class CompassBinder extends Binder {

    }

    private final CompassBinder mBinder = new CompassBinder();

    //private OrientationManager mOrientationManager;
    //private Landmarks mLandmarks;
    private TextToSpeech mSpeech;

    private TimelineManager mTimelineManager;
    private LiveCard mLiveCard;
    
    
    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("ViewActivityService: onCreate()");
        
        mTimelineManager = TimelineManager.from(this);

        // Even though the text-to-speech engine is only used in response to a menu action, we
        // initialize it when the application starts so that we avoid delays that could occur
        // if we waited until it was needed to start it up.
        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // Do nothing.
            }
        });

        
    
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
    	System.out.println("ViewActivityService: onStartCommand()");
    	    	
    	if (mLiveCard == null) {
            mLiveCard = mTimelineManager.createLiveCard(LIVE_CARD_ID);
            
            //mRenderer = new CompassRenderer(this, mOrientationManager, mLandmarks);
            //mLiveCard.enableDirectRendering(true).getSurfaceHolder().addCallback(mRenderer);
            //mLiveCard.setNonSilent(true);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, ViewActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));

            mLiveCard.publish(LiveCard.PublishMode.REVEAL);
        }

        return START_STICKY;
    }
    

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            //mLiveCard.getSurfaceHolder().removeCallback(mRenderer);
            mLiveCard = null;
        }

        mSpeech.shutdown();
        mSpeech = null;
        super.onDestroy();
    }
    
}
