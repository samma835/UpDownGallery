package com.socogame.udgallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.socogame.udgallery.anim.Rotate3dAnimation;

public class UDGalleryActivity extends Activity implements OnClickListener {

	private float deepin = 0.0f;
	private ImageView[] ivs;
	private int[] iv_ids = new int[] { R.id.iv_p0, R.id.iv_p1, R.id.iv_p2,
			R.id.iv_p3, R.id.iv_p4, R.id.iv_p5 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_udgallery);

		ivs = new ImageView[6];

		for (int i = 0; i < 6; i++) {
			ivs[i] = (ImageView) findViewById(iv_ids[i]);
			ivs[i].setOnClickListener(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_udgallery, menu);
		return true;
	}

	/**
	 * Setup a new 3D rotation on the container view.
	 *
	 * @param position the item that was clicked to show a picture, or -1 to show the list
	 * @param start the start angle at which the rotation must begin
	 * @param end the end angle of the rotation
	 */
	private void applyRotation(View view, View nextView, int position,
			float start, float end) {
		// Find the center of the container
		final float centerX = view.getWidth() / 2.0f;
		final float centerY = view.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, deepin, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(view, nextView));

		view.startAnimation(rotation);
	}

	/**
	 * This class listens for the end of the first half of the animation.
	 * It then posts a new action that effectively swaps the views when the container
	 * is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final View mView, mNextView;

		private DisplayNextView(View view, View nextView) {
			mView = view;
			mNextView = nextView;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			mView.post(new SwapViews(mNextView));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable {
		private final View mView;

		public SwapViews(View nextView) {
			mView = nextView;
		}

		public void run() {
			final float centerX = mView.getWidth() / 2.0f;
			final float centerY = mView.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			rotation = new Rotate3dAnimation(-90, 0, centerX, centerY, deepin,
					false);

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mView.startAnimation(rotation);
		}
	}

	@Override
	public void onClick(View v) {
		applyRotation(v, v, 1, 0, 90);
	}
}
