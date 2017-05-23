package com.example.remotecontrolpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.remotecontrolpc.connect.ConnectFragment;
import com.example.remotecontrolpc.filedownload.FileDownloadFragment;
import com.example.remotecontrolpc.filetransfer.FileTransferFragment;
import com.example.remotecontrolpc.help.HelpFragment;
import com.example.remotecontrolpc.imageviewer.ImageViewerFragment;
import com.example.remotecontrolpc.keyboard.KeyboardFragment;
import com.example.remotecontrolpc.livescreen.LiveScreenFragment;
import com.example.remotecontrolpc.mediaplayer.MediaPlayerFragment;
import com.example.remotecontrolpc.poweroff.PowerOffFragment;
import com.example.remotecontrolpc.presentation.PresentationFragment;
import com.example.remotecontrolpc.touchpad.TouchpadFragment;

import android.Manifest;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	public static Socket clientSocket = null;
	public static ObjectInputStream objectInputStream = null;
	public static ObjectOutputStream objectOutputStream = null;
	private static ActionBarActivity thisActivity;
	private boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        thisActivity = this;
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checkForPermission();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	private void checkForPermission() {
		if (thisActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
		    // Should we show an explanation?
		    if (thisActivity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
		    	Toast.makeText(thisActivity, "Read Permission is necessary to transfer", Toast.LENGTH_LONG).show();
		    } else {
		    	thisActivity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
		        //2 is integer constant for WRITE_EXTERNAL_STORAGE permission, uses in onRequestPermissionResult
		    }
        }
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
		        .beginTransaction()
		        .replace(R.id.container,
				        returnAppropriateFragment(position + 1)).commit();
		
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.connect);
			break;
		case 2:
			mTitle = getString(R.string.touchpad);
			break;
		case 3:
			mTitle = getString(R.string.keyboard);
			break;
		case 4:
			mTitle = getString(R.string.file_transfer);
			break;
		case 5:
			mTitle = getString(R.string.file_download);
			break;
		case 6:
			mTitle = getString(R.string.media_player);
			break;
		case 7:
			mTitle = getString(R.string.image_viewer);
			break;
		case 8:
			mTitle = getString(R.string.presentation);
			break;
		case 9:
			mTitle = getString(R.string.power_off);
			break;
		case 10:
			mTitle = "Live Screen";
            // not a navigation drawer item, so need to explicitly call it
            restoreActionBar();
			break;
		case 11:
			mTitle = getString(R.string.help);
			// not a navigation drawer item, so need to explicitly call it
			restoreActionBar();
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_help) {
			onNavigationDrawerItemSelected(10);
			return true;
		} else if (id == R.id.action_live_screen) {
            onNavigationDrawerItemSelected(9);
            return true;
        }
		return super.onOptionsItemSelected(item);
	}
	
	//will return appropriate fragment based on section_number
	private Fragment returnAppropriateFragment (int sectionNumber) {
		Fragment fragment = null;
		String ARG_SECTION_NUMBER = "section_number";
		switch (sectionNumber) {
		case 1: fragment = new ConnectFragment();
		    break;
		case 2: fragment = new TouchpadFragment();
	        break;
		case 3: fragment = new KeyboardFragment();
	        break;
		case 4: fragment = new FileTransferFragment();
            break;
		case 5: fragment = new FileDownloadFragment();
            break;
		case 6: fragment = new MediaPlayerFragment();
            break;
		case 7: fragment = new ImageViewerFragment();
            break;
		case 8: fragment = new PresentationFragment();
            break;
		case 9: fragment = new PowerOffFragment();
            break;
		case 10: fragment = new LiveScreenFragment();
			break;
		case 11: fragment = new HelpFragment();
		    break;
		}
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
    
	protected void onDestroy() {
		super.onDestroy();
		//Toast.makeText(thisActivity, "Destroyed", Toast.LENGTH_LONG).show();
		try {
			if (MainActivity.clientSocket != null) {
				MainActivity.clientSocket.close();
			}
			if (MainActivity.objectOutputStream != null) {
				MainActivity.objectOutputStream.close();
			}
			if (MainActivity.objectInputStream != null) {
				MainActivity.objectInputStream.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//this method is called from fragments to send message to server (Desktop)
	public static void sendMessageToServer(String message) {
		if (MainActivity.clientSocket != null) {
			try {
				MainActivity.objectOutputStream.writeObject(message);
				MainActivity.objectOutputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
				socketException();
			}	
		}
	}
	
	public static void sendMessageToServer(int message) {
		if (MainActivity.clientSocket != null) {
			try {
				MainActivity.objectOutputStream.writeObject(message);
				MainActivity.objectOutputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
				socketException();
			}	
		}
	}
	
	private static void socketException() {
		Toast.makeText(thisActivity, "Connection Closed", Toast.LENGTH_LONG).show();
		if (MainActivity.clientSocket != null) {
			try {
				MainActivity.clientSocket.close();
				MainActivity.objectOutputStream.close();
				MainActivity.clientSocket = null;
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void sendMessageToServer(float message) {
		if (MainActivity.clientSocket != null) {
			try {
				MainActivity.objectOutputStream.writeObject(message);
				MainActivity.objectOutputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
				socketException();
			}	
		}
	}
	
	public static void sendMessageToServer(long message) {
		if (MainActivity.clientSocket != null) {
			try {
				MainActivity.objectOutputStream.writeObject(message);
				MainActivity.objectOutputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
				socketException();
			}	
		}
	}
	
	@Override
	public void onBackPressed() {
	    if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        return;
	    }
	    doubleBackToExitPressedOnce = true;
	    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce = false;                       
	        }
	    }, 2000);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
	        String permissions[], int[] grantResults) {
	    switch (requestCode) {
	        case 2: {
	            // If request is cancelled, the result arrays are empty.
	            if (grantResults.length > 0
	                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	            	Toast.makeText(this, "Click again to download", Toast.LENGTH_SHORT).show();

	            } else {
	            	Toast.makeText(this, "Failed to download", Toast.LENGTH_SHORT).show();
	            }
	            return;
	        }
	        case 1: {
	        	// If request is cancelled, the result arrays are empty.
	            if (grantResults.length > 0
	                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	            	//Toast.makeText(this, "Click again to download", Toast.LENGTH_SHORT).show();

	            } else {
	            	Toast.makeText(this, "File Transfer will not work", Toast.LENGTH_SHORT).show();
	            }
	            return;
	        }
	    }
	}
	
}
