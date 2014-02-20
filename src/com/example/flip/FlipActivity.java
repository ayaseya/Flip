package com.example.flip;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class FlipActivity extends Activity {

	private FrameLayout layout;// レイアウト
	private Deck deck;// トランプ1組
	private TrumpView trumpView1;// トランプ画像1
	private TrumpView trumpView2;// トランプ画像2
	private FrameLayout.LayoutParams params;// レイアウトのパラメーター
	private boolean flag = true;// 表と裏のどちらが表示されているか判断
	private boolean anim = true;// アニメーション中はfalseとなり画像をクリックできない

	private int count = 0;//0～51まで増加、トランプの枚数を管理

	private float centerX;// Y軸回転の中心点(X座標)を設定
	private float centerY;// Y軸回転の中心点(Y座標)を設定

	private long time = 250;// Y軸回転のアニメーションスピード

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバーを非表示にする
		setContentView(R.layout.activity_flip);

		// メインレイアウトのインスタンスを取得
		layout = (FrameLayout) findViewById(R.id.mainLayout);

		// トランプ1組(52枚)を取得
		deck = new Deck(this.getApplicationContext());
		
		// トランプの画像を表示させるカスタムビューのインスタンス取得
		trumpView1 = new TrumpView(this.getApplicationContext());
		trumpView2 = new TrumpView(this.getApplicationContext());

		// トランプの画像を生成
		trumpView1 = (TrumpView) trumpView1.addTrumpView(deck, count, this.getApplicationContext());
		trumpView2 = (TrumpView) trumpView2.addTrumpView(deck, count + 1, this.getApplicationContext());

		// レイアウトのパラメーターを設定
		params =
				new FrameLayout.LayoutParams(trumpView1.getTrumpWidth(), trumpView1.getTrumpHeight(), Gravity.CENTER);

		// Y軸回転の中心点を設定
		centerX = trumpView1.getTrumpWidth() / 2;
		centerY = trumpView1.getTrumpHeight();
		
		// レイアウトにトランプ画像1と2を追加する
		layout.addView(trumpView1, params);
		layout.addView(trumpView2, params);
		// トランプ画像2は一時的に非表示
		trumpView2.setVisibility(View.INVISIBLE);
		
		// カウントを加算する
		count++;

		// トランプ画像をクリックしたと時の処理1
		trumpView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (anim) {
					// アニメーション中にクリックできないようfalseに変更する
					anim = false;
					
					// トランプ画像2をレイアウトから削除する
					layout.removeView(trumpView2);
					// トランプ画像2に次に表示されるトランプを格納する
					trumpView2 = (TrumpView) trumpView2.addTrumpView(deck, count, FlipActivity.this);
					layout.addView(trumpView2, params);
					trumpView2.setVisibility(View.INVISIBLE);

					
					// Y軸回転(前半)
					Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90, centerX, centerY, 0f, true);
					rotation.setDuration(time);
					trumpView1.startAnimation(rotation);
					rotation.setAnimationListener(frontlistener);

				}
			}
		});

		// トランプ画像をクリックしたと時の処理1
		trumpView2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (anim) {
					// アニメーション中にクリックできないようfalseに変更する
					anim = false;
					
					// トランプ画像1をレイアウトから削除する
					layout.removeView(trumpView1);
					// トランプ画像1に次に表示されるトランプを格納する
					trumpView1 = (TrumpView) trumpView1.addTrumpView(deck, count, FlipActivity.this);
					layout.addView(trumpView1, params);
					trumpView1.setVisibility(View.INVISIBLE);

					// Y軸回転(前半)
					Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90, centerX, centerY, 0f, true);
					rotation.setDuration(time);
					trumpView2.startAnimation(rotation);
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

			// Y軸回転(後半)
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
			// Y軸回転(後半)
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
				// Y軸回転終了後に画像を表示
				trumpView2.setVisibility(View.VISIBLE);
				flag = false;

			} else {
				// Y軸回転終了後に画像を表示
				trumpView1.setVisibility(View.VISIBLE);
				flag = true;

			}
			// カウントの加算処理
			if (count == 51) {
				count = 0;
			} else {
				count++;
			}
			// アニメーションの終了
			anim = true;

		}
	};

}