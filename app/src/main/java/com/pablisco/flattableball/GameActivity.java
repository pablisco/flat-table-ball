package com.pablisco.flattableball;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by pablisco on 26/01/2015.
 *
 * This is where the magic happens. Here we'll start the physics engine and build the game.
 */
public class GameActivity extends Activity {

	/**
	 * Use to attach a team to the extras used to launch this activity
	 */
	public static final String EXTRA_TEAM = "com.pablisco.flattableball;EXTRA_TEAM";

	/**
	 * This enum describes the two possible teams that the user can choose
	 */
	public enum Team {
		BLUE, RED
	}

	/**
	 * Internal reference of the team provided in the references
	 */
	private Team team;

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
		// let's go fullscreen
		requestWindowFeature(FEATURE_NO_TITLE);
		getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
		final Bundle extras = getIntent().getExtras();
		// make sure we have a team
		if (!extras.containsKey(EXTRA_TEAM)) {
			Toast.makeText(this, "No team", LENGTH_SHORT).show();
			finish();
		} else {
			team = (Team) extras.getSerializable(EXTRA_TEAM);
		}
	}
}
