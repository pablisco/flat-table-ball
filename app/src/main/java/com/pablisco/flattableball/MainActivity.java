package com.pablisco.flattableball;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Main Activity. This is the first screen that the user will see
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public enum Team {
		BLUE, RED;
	}

	/**
	 * The user has selected the blue team
	 */
	public void chooseBlue(View view) {
		chooseTeam(Team.BLUE);
	}

	/**
	 * The user has selected the red team
	 */
	public void chooseRed(View view) {
		chooseTeam(Team.RED);
	}

	/**
	 * The user has made a decision and this method will tke him to the game screen
	 *
	 * @param team The team the user has choosen
	 */
	private void chooseTeam(Team team) {
		// TODO: forward to the game activity
	}

}
