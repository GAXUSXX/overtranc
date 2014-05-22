package rui.app.transcalc;

import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class staover extends Service {
	String from,msg;
	private WindowManager wm;
	private ImageView chatHead;
	private int moveFlag = 0;
	private Display disp;
	private int openFlag = 0;
	private int DEBUG = 1;
	public static final String ACTION = "SimpleService Action";
	WindowManager.LayoutParams params;
	AlarmManager am2;
	PendingIntent pi;

	@Override public IBinder onBind(Intent intent) {
		// Not used
		from = intent.getStringExtra("From");
		msg = intent.getStringExtra("Msg");
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override public void onCreate() {
		super.onCreate();
		Log.v("overser","ステータスオーバーレイランチャー起動");

		//このおまじないのおかげでサービスが殺されにくくなっている？
		//Notification lNotification = new Notification(0, "ticker", System.currentTimeMillis());
		//startForeground(1, lNotification);

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		// ディスプレイのインスタンス生成
		disp = wm.getDefaultDisplay();
		disp.getWidth();

		//座標初期化
		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		int x = mSP.getInt("x", 0);
		int y = mSP.getInt("y", 0);
		/**Activityが開かれていたら非表示**/
		int openActivity = mSP.getInt("openActivity", 0);

		chatHead = new ImageView(this);
		chatHead.setImageResource(R.drawable.ic_launcher2);
		if(openActivity == 1){
			chatHead.setImageResource(R.drawable.blank);
		}

		params = new WindowManager.LayoutParams(
				disp.getHeight()/9,
				disp.getHeight()/9,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.y=y;
		params.x=x;

		wm.addView(chatHead, params);

		chatHead.setOnTouchListener(new View.OnTouchListener() {
			private int currentX;
			private int currentY;
			private float lastTouchX;
			private float lastTouchY;
			private int notTouchFlag = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastTouchX = event.getRawX();
					lastTouchY = event.getRawY();
					//最後に触れた時間取得
					SharedPreferences mSP = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					int lastTime = mSP.getInt("lastTime",1);
					Log.v("downlastTime",String.valueOf(lastTime));
					int x0 = mSP.getInt("x", 0);
					int y0 = mSP.getInt("y", 0);
					notTouchFlag = 0;
					if(lastTime > 1){
						if(lastTouchX < x0+disp.getWidth()/11 || lastTouchX > x0+disp.getWidth()/8) {
							//触ったら復元
							chatHead.setImageResource(R.drawable.ic_launcher2);
						}
						else{
							notTouchFlag = 1;
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					if(notTouchFlag == 0){
						mSP = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
						Editor edit = mSP.edit();
						//最後に触った時間を記録
						edit.putInt("lastTime",1);

						edit.commit();

						// インテント作成
						Intent intent1 = new Intent(getBaseContext(), staover.class);

						pi = PendingIntent.getService(getBaseContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

						// AlarmManager取得
						am2 = (AlarmManager)getBaseContext().getSystemService(getBaseContext().ALARM_SERVICE);

						// 現在時刻取得
						long currentTimeMillis = System.currentTimeMillis();

						// リピートアラーム設定
						long interval = 2 * 1000; // 1秒ごと

						//初回は現在の1秒後に実行　それ以降1秒ごとに実行
						am2.setRepeating(am2.RTC, currentTimeMillis + 3000, interval, pi);

						Log.v("UP座標は",Integer.toString((int)currentY));
						if(openFlag == 1){
							//終了フラグを送りactivity終了
							// intentを作成する。（SimpleService.ACTIONのブロードキャストとして配信させる）
							Intent i = new Intent(ACTION);
							// 日付を送信する
							i.putExtra("message", (new Date()).toString());
							// ブロードキャストへ配信させる
							sendBroadcast(i);

							int x = mSP.getInt("x", 0);
							int y = mSP.getInt("y", 0);

							if(x >= disp.getWidth()/2){
								x = disp.getWidth()-disp.getHeight()/10;
							}
							else{
								x= 0;
							}

							params.gravity = Gravity.TOP | Gravity.LEFT;
							params.y=y;
							params.x=x;

							wm.updateViewLayout(chatHead, params);

							moveFlag = 1;
							openFlag = 0;
						}
						else if(moveFlag == 0){
							Log.d("起動","起動");

							/* FLAG_ACTIVITY_NEW_TASK付きでActivityを呼び出す例 */
							Intent intent = new Intent();
							intent.setClassName("rui.app.transcalc", "rui.app.transcalc.DecimalActivity");
							intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT| Intent.FLAG_ACTIVITY_NEW_TASK);
							getApplicationContext().startActivity(intent);

							currentX = (int) event.getRawX();
							currentY = (int) event.getRawY();

							edit.putInt("x",currentX-disp.getWidth()/10);
							edit.putInt("y",currentY-disp.getWidth()/10);
							edit.putInt("openActivity", 1);
							//最後に触った時間を記録
							edit.putInt("lastTime",40);
							edit.commit();

							params.gravity = Gravity.TOP | Gravity.LEFT;
							params.y=(int)disp.getHeight();
							params.x=(int) disp.getWidth()/2-disp.getWidth()/12;
							wm.updateViewLayout(chatHead, params);
							openFlag = 1;
						}
						else{

							params.gravity = Gravity.TOP | Gravity.LEFT;
							params.y=currentY-disp.getWidth()/8;
							params.x=currentX-disp.getWidth()/8;

							if(params.x >= disp.getWidth()/2){
								params.x = disp.getWidth()-disp.getHeight()/10;
							}
							else{
								params.x= 0;
							}

							edit.putInt("x",currentX-disp.getWidth()/10);
							edit.putInt("y",(int) (currentY-disp.getWidth()/7.89));
							edit.commit();

							wm.updateViewLayout(chatHead, params);
						}
						moveFlag = 0;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					currentX = (int) event.getRawX();
					currentY = (int) event.getRawY();
					if(lastTouchX-currentX > 100 || lastTouchX-currentX < -100 || lastTouchY-currentY > 100 || lastTouchY-currentY < -100){
						moveFlag = 1;
					}
					params.x = currentX-disp.getWidth()/8;
					params.y = currentY-disp.getWidth()/6;
					if(notTouchFlag == 0){
						wm.updateViewLayout(chatHead, params);
					}
				}
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		from = intent.getStringExtra("From");
		msg = intent.getStringExtra("Msg");
		if(DEBUG == 1){
			Log.d("In Start", "In start");
		}

		// インテント作成
		Intent intent1 = new Intent(getBaseContext(), staover.class);

		pi = PendingIntent.getService(getBaseContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

		// AlarmManager取得
		am2 = (AlarmManager)getBaseContext().getSystemService(getBaseContext().ALARM_SERVICE);

		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		int x = mSP.getInt("x", 0);
		int y = mSP.getInt("y", 0);

		if(x >= disp.getWidth()/2){
			x = disp.getWidth()-disp.getHeight()/10;
		}
		else{
			x= 0;
		}

		int size= 0;
		//最後に触れた時間取得
		int lastTime = mSP.getInt("lastTime",1);
		Log.v("lastTime",String.valueOf(lastTime));

		if(lastTime == 2){
			chatHead.setImageResource(R.drawable.icpop1);
			if(x==0){
				chatHead.setImageResource(R.drawable.icpop4);
			}
		}
		if(lastTime == 3){
			chatHead.setImageResource(R.drawable.icpop2);
			if(x==0){
				chatHead.setImageResource(R.drawable.icpop5);
			}
		}
		if(lastTime == 4){
			if(am2 != null){
				am2.cancel(pi);
			}
			lastTime = 10;
		}
		if(lastTime == 10){
			chatHead.setImageResource(R.drawable.icpop3);
			if(x==0){
				chatHead.setImageResource(R.drawable.icpop6);
			}
		}

		//経過秒数に応じて引っ込める
		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.y=y;
		params.x=x;

		Editor edit = mSP.edit();
		//最後に触った時間を記録
		if(lastTime != 10){
			lastTime++;
		}

		if(lastTime >= 40){
			Log.v("blank","ok");
			chatHead.setImageResource(R.drawable.blank);
			if(am2 != null){
				am2.cancel(pi);
			}
			lastTime = 0;
		}

		if(lastTime >= 20){
			chatHead.setImageResource(R.drawable.ic_launcher2);
			if(am2 != null){
				am2.cancel(pi);
			}
			lastTime = 0;
		}
		if(lastTime == 19){
			chatHead.setImageResource(R.drawable.ic_launcher2);
			if(am2 != null){
				//am2.cancel(pi);
			}
			lastTime = 0;
		}

		edit.putInt("lastTime",lastTime);

		edit.commit();

		openFlag = 0;
		moveFlag = 0;

		wm.updateViewLayout(chatHead, params);

		return START_STICKY;

	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("service","Destroy");
		if (chatHead != null) wm.removeView(chatHead);

		startService(new Intent(this, staover.class));

		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		int lastTime = mSP.getInt("lastTime",1);
		if(lastTime == 20){
			lastTime = 18;
			// インテント作成
			Intent intent1 = new Intent(getBaseContext(), staover.class);

			pi = PendingIntent.getService(getBaseContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

			// AlarmManager取得
			am2 = (AlarmManager)getBaseContext().getSystemService(getBaseContext().ALARM_SERVICE);

			// 現在時刻取得
			long currentTimeMillis = System.currentTimeMillis();

			// リピートアラーム設定
			long interval = 2 * 1000; // 1秒ごと

			//初回は現在の1秒後に実行　それ以降1秒ごとに実行
			am2.setRepeating(am2.RTC, currentTimeMillis + 5000, interval, pi);
		}
		else if(lastTime == 18){

		}
		else if(lastTime == 40){

		}
		else{
			lastTime = 1;
		}
		Editor edit = mSP.edit();
		//最後に触った時間を記録
		edit.putInt("lastTime",lastTime);

		edit.commit();

	}

	/**アニメーション**/
	public Handler mHandler; // Your handler

	class LooperThread extends Thread {


		public void run() {
			Looper.prepare();

			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					// process incoming messages here
				}
			};

			Looper.loop();
		}
	}
}