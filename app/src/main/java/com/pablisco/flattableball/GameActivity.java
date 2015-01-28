package com.pablisco.flattableball;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.pablisco.physics.PhysicsEngine;
import com.pablisco.physics.ui.PhysicsView;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
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
	private PhysicsEngine physicsEngine;

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

	private int ballCount = 10;

	private int redGoals = 0;
	private int blueGoals = 0;

	private TextView scoreView;

	private TextView ballsView;

	private PhysicsView physicsView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// let's go fullscreen
		requestWindowFeature(FEATURE_NO_TITLE);
		getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game);
		// gather views
		scoreView = (TextView) findViewById(R.id.score_view);
		ballsView = (TextView) findViewById(R.id.balls_view);
		physicsView = (PhysicsView) findViewById(R.id.physics_view);
		physicsEngine = physicsView.getPhysicsEngine();

		// process extras
		final Bundle extras = getIntent().getExtras();
		// make sure we have a team
		if (extras == null || !extras.containsKey(EXTRA_TEAM)) {
			Toast.makeText(this, "No team", LENGTH_SHORT).show();
			finish();
		} else {
			team = (Team) extras.getSerializable(EXTRA_TEAM);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateLabels();
		physicsEngine.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		physicsEngine.stop();
	}

	/**
	 * Utility method to color the string representation of an object
	 * @param item object to convert to string
	 * @param color used to color the result
	 * @return
	 */
	private Spannable colorObject(Object item, int color) {
		Spannable result = new SpannableString(item.toString());
		result.setSpan(new ForegroundColorSpan(color), 0, result.length(), 0);
		return result;
	}

	/**
	 * This method is in charge of updating the status labels in the game
	 */
	private void updateLabels() {
		// paint score
		CharSequence score = TextUtils.concat(getString(R.string.score),
			colorObject(blueGoals, BLUE), " - ", colorObject(redGoals, RED));
		scoreView.setText(score);
		// paint balls
		StringBuilder ballsBuilder = new StringBuilder(getString(R.string.balls_left));
		for(int i = 0; i < ballCount;i++) {
			ballsBuilder.append('\u26BD');
		}
		ballsView.setText(ballsBuilder);
	}

}
