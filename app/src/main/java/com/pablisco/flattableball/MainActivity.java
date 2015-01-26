package com.pablisco.flattableball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.pablisco.flattableball.GameActivity.EXTRA_TEAM;
import static com.pablisco.flattableball.GameActivity.Team.BLUE;
import static com.pablisco.flattableball.GameActivity.Team.RED;

/**
 * Main Activity. This is the first screen that the user will see
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// let's go fullscreen
		requestWindowFeature(FEATURE_NO_TITLE);
		getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
	}

	/**
	 * The user has selected the blue team. <br/>
	 * Mapped in the layout {@link R.layout#activity_main}
	 */
	public void chooseBlue(View view) {
		chooseTeam(BLUE);
	}

	/**
	 * The user has selected the red team. <br/>
	 * Mapped in the layout {@link R.layout#activity_main}
	 */
	public void chooseRed(View view) {
		chooseTeam(RED);
	}

	/**
	 * The user has made a decision and this method will tke him to the game screen
	 *
	 * @param team The team the user has choosen
	 */
	private void chooseTeam(GameActivity.Team team) {
		Intent intent = new Intent(this, GameActivity.class);
		Bundle extras = new Bundle();
		extras.putSerializable(EXTRA_TEAM, team);
		startActivity(intent);
	}

}
