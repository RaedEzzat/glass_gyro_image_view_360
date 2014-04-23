package com.neatocode.gyroimageview;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/**
 * View an image, scrolling it with head movements.
 *
 */
public class MenuActivity extends Activity {
			
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	System.out.println("MenuActivity: onCreate()");
    	 
        	
    }
    
    
    @Override
    public void openOptionsMenu() {
            super.openOptionsMenu();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    	
    	System.out.println("MenuActivity: onCreateOptionsMenu()");    	
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
    }

    
	@Override
	public void onResume() {
		super.onResume();		 
		System.out.println("MenuActivity: onResume()");
	}
	
	@Override
	protected void onPause() {
		super.onPause();		
		System.out.println("MenuActivity: onPause()");
	}


}