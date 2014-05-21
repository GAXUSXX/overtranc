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
		chatHead.setImageResource(R.drawable.ic_launcher);
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

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastTouchX = event.getRawX();
					lastTouchY = event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					Log.v("UP座標は",Integer.toString((int)currentY));
					if(openFlag == 1){
						//終了フラグを送りactivity終了
						// intentを作成する。（SimpleService.ACTIONのブロードキャストとして配信させる）
						Intent i = new Intent(ACTION);
						// 日付を送信する
						i.putExtra("message", (new Date()).toString());
						// ブロードキャストへ配信させる
						sendBroadcast(i);

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
						SharedPreferences mSP = PreferenceManager
								.getDefaultSharedPreferences(getBaseContext());

						Editor edit = mSP.edit();
						edit.putInt("x",currentX-disp.getWidth()/10);
						edit.putInt("y",currentY-disp.getWidth()/10);
						edit.putInt("openActivity", 1);
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

						wm.updateViewLayout(chatHead, params);
					}
					moveFlag = 0;
					break;
				case MotionEvent.ACTION_MOVE:
					currentX = (int) event.getRawX();
					currentY = (int) event.getRawY();
					if(lastTouchX-currentX > 100 || lastTouchX-currentX < -100 || lastTouchY-currentY > 100 || lastTouchY-currentY < -100){
						moveFlag = 1;
					}
					params.x = currentX-disp.getWidth()/8;
					params.y = currentY-disp.getWidth()/6;
					wm.updateViewLayout(chatHead, params);
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

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.y=y;
		params.x=x;
		openFlag = 0;
		moveFlag = 0;

		wm.updateViewLayout(chatHead, params);

		return startId;

	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null) wm.removeView(chatHead);

		startService(new Intent(this, staover.class));

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