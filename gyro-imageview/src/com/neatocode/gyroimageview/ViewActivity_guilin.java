package com.neatocode.gyroimageview;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.polites.android.GestureImageView;
import com.polites.android.MoveAnimation;
import com.polites.android.MoveAnimationListener;

/**
 * View an image, scrolling it with head movements.
 *
 */
public class ViewActivity_guilin extends Activity implements FilteredOrientationTracker.Listener {
	
	private static final int ANIMATION_DURATION_MS = 100;
	private static final float GYRO_TO_X_PIXEL_DELTA_MULTIPLIER = 50;
	
	private GestureImageView image;
	private MoveAnimation moveAnimation;
	private FilteredOrientationTracker tracker;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	getWindow().setFormat(PixelFormat.RGB_565);
    	ScreenOn.run(this);
    			
       	setContentView(R.layout.view_activity_guilin);
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
    }

	@Override
	public void onResume() {
		super.onResume();
		tracker.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
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
		
		moveAnimation.reset();
		moveAnimation.setTargetX(nextX);
		moveAnimation.setTargetY(nextY);
		image.animationStart(moveAnimation);
	}
}