package com.neatocode.gyroimageview;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polites.android.GestureImageView;
import com.polites.android.MoveAnimation;
import com.polites.android.MoveAnimationListener;


/**
 * View an image, scrolling it with head movements.
 *
 */
public class ViewActivity extends Activity implements FilteredOrientationTracker.Listener {
	
	private static final int ANIMATION_DURATION_MS = 100;
	private static final float GYRO_TO_X_PIXEL_DELTA_MULTIPLIER = 50;
	private static final float GYRO_TO_Y_PIXEL_DELTA_MULTIPLIER = 10;
	
	private GestureImageView image;
	private MoveAnimation moveAnimation;
	private FilteredOrientationTracker tracker;
	
	private GestureDetector mGestureDetector;
	
	private boolean mResumed;
	private DrawView drawView;
	private int imageID = 1;
		
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	System.out.println("ViewActivity: onCreate()");
    	               
    	mGestureDetector = createGestureDetector(this);
    	  
    	// 360 app
    	launch360();
    	
    	
    }
    
    
    private void launchPosterImage() {
    	
    	
    }
    
    
    private void activate360(int layout) {
    	
    	getWindow().setFormat(PixelFormat.RGB_565);
    	ScreenOn.run(this);
    			
       	setContentView(layout);
    	image = (GestureImageView) findViewById(R.id.image);	
    	moveAnimation = new MoveAnimation();
		moveAnimation.setAnimationTimeMS(ANIMATION_DURATION_MS);
		moveAnimation.setMoveAnimationListener(new MoveAnimationListener() {
			@Override
			public void onMove(final float x, final float y) {
				image.setPosition(x, y);
				image.redraw();
			}
		});	
		
		tracker = new FilteredOrientationTracker(this, this);
				
        //drawView = new DrawView(this);
    }

    
    @Override
    public void openOptionsMenu() {
        if (mResumed) {
            super.openOptionsMenu();
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	System.out.println("ViewActivity: onCreateOptionsMenu()");
    	
        getMenuInflater().inflate(R.menu.view_activity, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	int iSelected = item.getItemId();
    	    	
    	System.out.println("onOptionsItemSelected: " + iSelected);
    	
        switch (iSelected) {
            case R.id.zoom:
                //mCompassService.readHeadingAloud();
                return true;
            case R.id.stop:
                //stopService(new Intent(this, CompassService.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
     
        // We must call finish() from this method to ensure that the activity ends either when an
        // item is selected from the menu or when the menu is dismissed by swiping down.
        finish();
    }

    
	@Override
	public void onResume() {
		super.onResume();
		 
		System.out.println("ViewActivity: onResume()");

/*		
		
*/	
		mResumed = true;
		
		if (tracker != null)
			tracker.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		System.out.println("ViewActivity: onPause()");
		
		mResumed = false;
		
		if (tracker != null)
			tracker.onPause();
	}

	// On gyro motion, start an animated scroll that direction.
	@Override
	public void onUpdate(float[] aGyro, float[] aGyroSum) {
		final float yGyro = aGyro[1];
		final float xGyro = aGyro[0];
		final float deltaY = GYRO_TO_X_PIXEL_DELTA_MULTIPLIER * xGyro;
		final float deltaX = GYRO_TO_X_PIXEL_DELTA_MULTIPLIER * yGyro;
		
		animateTo(deltaX, deltaY);		
	}

	// Animate to a given offset, stopping at the image edges.
	private void animateTo(final float animationOffsetX, final float animationOffsetY) {
		float nextX = image.getImageX() + animationOffsetX;
		float nextY = image.getImageY() + animationOffsetY;
		final int maxWidth = image.getScaledWidth();
		final int maxHeight = image.getScaledHeight();
		
		final int topBoundary = (maxHeight / 2);
		final int bottomBoundary = (-maxHeight / 2) + image.getDisplayHeight();
		final int leftBoundary = (-maxWidth / 2) + image.getDisplayWidth();
		final int rightBoundary = (maxWidth / 2);
		if ( nextX < leftBoundary ) {
			nextX = leftBoundary;
		} else if ( nextX > rightBoundary ) {
			nextX = rightBoundary;
		}
		if (nextY > topBoundary) {
			nextY = topBoundary;
		} else if (nextY < bottomBoundary) {
			nextY = bottomBoundary;
		}
		
		//System.out.println("(" + nextX + ":" + nextY + ")");
		
		moveAnimation.reset();
		moveAnimation.setTargetX(nextX);
		moveAnimation.setTargetY(nextY);
		image.animationStart(moveAnimation);
		
		//drawView.setXY(nextX, nextY, nextX+20, nextY+20);
        //setContentView(drawView);
	}
	
	private GestureDetector createGestureDetector(Context context) {
		
	    GestureDetector gestureDetector = new GestureDetector(context);
	    
	        //Create a base listener for generic gestures
	        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {	            
	        	@Override
	            public boolean onGesture(Gesture gesture) {
	                if (gesture == Gesture.TAP) {
	                	// do something on tap
	                	System.out.println("TAP!");	                	
	                	//processSingleTap();
	                    return true;
	                } else if (gesture == Gesture.TWO_TAP) {
	                    // do something on two finger tap
	                    return true;
	                } else if (gesture == Gesture.SWIPE_RIGHT) {
	                    // do something on right (forward) swipe       	
	                	processSwipeRight();
	                	
	                    return true;
	                } else if (gesture == Gesture.SWIPE_LEFT) {
	                    // do something on left (backwards) swipe                	
	                	processSwipeLeft();
	                	return true;
	                }
	                return false;
	            }
	        });
	        
	        return gestureDetector;
	}
	
	private void processSwipeRight() {
		imageID++;
		System.out.print("SWIPE RIGHT: ");
		launch360();		
	}
	
	private void processSwipeLeft() {
		imageID--;
    	System.out.print("SWIPE LEFT: ");
		launch360();		
	}

	private void launch360() {
		
		if (imageID < 1) 
			imageID = 4;
			
		if (imageID > 4)
			imageID = 1;
				
		System.out.println(imageID);
		
		switch (imageID) {
		
		case 1:
			activate360(R.layout.view_activity);
			break;
		case 2:
			activate360(R.layout.view_activity_guilin);
			break;
		case 3:
			//activate360(R.layout.view_activity_sony);
			break;
		case 4: 
			//activate360(R.layout.view_activity_grimm360_closeup);
			activate360(R.layout.view_activity_universal);
			break;
		default: 
			activate360(R.layout.view_activity);
			break;	
		}
	}
	
	
	private void processSingleTap() {		
		// Let's initiate close up view of Grimm if it is in view!
		activate360(R.layout.view_activity_grimm360_closeup);		
	}
	
    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }	
	
}