package com.example.remotecontrolpc;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.remotecontrolpc.connect.ConnectFragment;
import com.example.remotecontrolpc.filedownload.FileDownloadFragment;
import com.example.remotecontrolpc.filetransfer.FileTransferFragment;
import com.example.remotecontrolpc.help.HelpFragment;
import com.example.remotecontrolpc.imageviewer.ImageViewerFragment;
import com.example.remotecontrolpc.keyboard.KeyboardFragment;
import com.example.remotecontrolpc.mediaplayer.MediaPlayerFragment;
import com.example.remotecontrolpc.poweroff.PowerOffFragment;
import com.example.remotecontrolpc.presentation.PresentationFragment;
import com.example.remotecontrolpc.touchpad.TouchpadFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
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
	public static Socket clientSocket;
	public static ObjectInputStream objectInputStream = null;
	public static PrintWriter outToServer;
	public static BufferedReader inFromServer;
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
			mTitle = getString(R.string.help);
			//not a navigation drawer item, so need to explicitly call it
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
		case 10: fragment = new HelpFragment();
		    break;
		}
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
    
	protected void onDestroy() {
		super.onDestroy();
		if (MainActivity.clientSocket != null) {
			try {
				MainActivity.clientSocket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	//this method is called from fragments to send message to server (Desktop)
	public static void sendMessageToServer(String message) {
		if (MainActivity.clientSocket != null) {
			try {
				MainActivity.outToServer.println(message);
				if (MainActivity.outToServer.checkError()) {
					Toast.makeText(thisActivity, "Connection Closed By server", Toast.LENGTH_LONG).show();
					try {
						MainActivity.clientSocket.close();
					} catch(Exception e2) {
						e2.printStackTrace();
					} finally {
						MainActivity.clientSocket = null;
					}
				}
			} catch (Exception e) {
				Toast.makeText(thisActivity, "Unable to send message", Toast.LENGTH_LONG).show();
				e.printStackTrace();
				if (MainActivity.clientSocket != null) {
					try {
						MainActivity.clientSocket.close();
					} catch(Exception e2) {
						e2.printStackTrace();
					}
				}
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
}
