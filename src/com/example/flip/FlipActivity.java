package com.example.flip;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FlipActivity extends Activity {

	private ImageView img;
	private FrameLayout layout;
	private Deck deck;
	private TrumpView trumpView1;
	private TrumpView trumpView2;
	private FrameLayout.LayoutParams params;
	private boolean flag = true;
	private boolean anim = true;
	
	private int count=0;

	private float centerX;
	private float centerY;

	private long time = 250;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flip);

		layout = (FrameLayout) findViewById(R.id.mainLayout);

		Resources r = getResources();
		Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.front);
		img = new ImageView(this.getApplicationContext());
		img.setImageBitmap(bmp);

		deck = new Deck(this.getApplicationContext());
		trumpView1 = new TrumpView(this.getApplicationContext());
		trumpView2 = new TrumpView(this.getApplicationContext());

		trumpView1 = (TrumpView) trumpView1.addTrumpView(deck, count, this.getApplicationContext());
		trumpView2 = (TrumpView) trumpView2.addTrumpView(deck, count+1, this.getApplicationContext());

		params =
				new FrameLayout.LayoutParams(trumpView1.getTrumpWidth(), trumpView1.getTrumpHeight(), Gravity.CENTER);

		centerX = trumpView1.getTrumpWidth() / 2;
		centerY = trumpView1.getTrumpHeight();
		layout.addView(trumpView1, params);
		layout.addView(trumpView2, params);
		trumpView2.setVisibility(View.INVISIBLE);
		count++;

		//		layout.addView(img, params);
		//		img.setVisibility(View.INVISIBLE);

		trumpView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (anim) {
										
					layout.removeView(trumpView2);
					trumpView2 = (TrumpView) trumpView2.addTrumpView(deck, count, FlipActivity.this);
					layout.addView(trumpView2, params);
					trumpView2.setVisibility(View.INVISIBLE);

					anim = false;
					Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90, centerX, centerY, 0f, true);
					rotation.setDuration(time);
					trumpView1.startAnimation(rotation);
					rotation.setAnimationListener(frontlistener);

				}
			}
		});

		trumpView2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (anim) {
					
					layout.removeView(trumpView1);
					trumpView1 = (TrumpView) trumpView1.addTrumpView(deck, count, FlipActivity.this);
					layout.addView(trumpView1, params);
					trumpView1.setVisibility(View.INVISIBLE);
					
					anim = false;
					Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90, centerX, centerY, 0f, true);
					rotation.setDuration(time);
					trumpView2.startAnimation(rotation);
					rotation.setAnimationListener(backlistener);

				}
			}
		});

		img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (anim) {
					anim = false;
					Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90, centerX, centerY, 0f, true);
					rotation.setDuration(time);
					img.startAnimation(rotation);
					rotation.setAnimationListener(backlistener);

				}
			}
		});

	}

	private AnimationListener frontlistener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {

			//			trumpView1.setVisibility(View.INVISIBLE);
			//			Rotate3dAnimation rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 0f, false);
			//			rotation.setDuration(time);
			//			img.startAnimation(rotation);
			//			rotation.setAnimationListener(listener);

			trumpView1.setVisibility(View.INVISIBLE);
			Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 0f, false);
			rotation.setDuration(time);
			trumpView2.startAnimation(rotation);
			rotation.setAnimationListener(listener);

		}
	};

	private AnimationListener backlistener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {

			//			img.setVisibility(View.INVISIBLE);
			//			Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 0f, false);
			//			rotation.setDuration(time);
			//			trumpView1.startAnimation(rotation);
			//			rotation.setAnimationListener(listener);

			trumpView2.setVisibility(View.INVISIBLE);
			Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 0f, false);
			rotation.setDuration(time);
			trumpView1.startAnimation(rotation);
			rotation.setAnimationListener(listener);

		}
	};
	private AnimationListener listener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {

			if (flag) {
				//				img.setVisibility(View.VISIBLE);
				trumpView2.setVisibility(View.VISIBLE);

				flag = false;

			} else {
				trumpView1.setVisibility(View.VISIBLE);
				flag = true;

			}
			if(count==51){
				count=0;
			}else{
			count++;}
			anim = true;

		}
	};

}