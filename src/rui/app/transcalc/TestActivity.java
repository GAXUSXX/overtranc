package rui.app.transcalc;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TestActivity extends FragmentActivity implements OnTouchListener {

	private BroadcastReceiver m_HomeButtonReceive;
	private WebView web;
	private WindowManager wm;
	private Display disp;
	private int width;
	private double Xsize;
	private int height;
	private double Ysize;
	private float lastTouchX;
	private float lastTouchY;
	private float currentX;
	private float currentY;
	private float DefPos;
	private int topMargin;
	private int topMargin2;
	private int PosMoveFlag;
	private int PButtonFlag;
	private int PosMoveFlag3;
	private float SetPos;
	private float Poss;
	public static final String ACTION = "SimpleService Action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.miniappback);

		stopService(new Intent(this, staover.class));
		//startService(new Intent(this, staover.class));
		// アニメーションの設定
		overridePendingTransition(R.anim.in_down, R.anim.out_up);
		// レシーバーを用意する
		SimpleReceiver mReceiver = new SimpleReceiver();
		// Intentを識別するためのフィルターを生成する
		IntentFilter filter = new IntentFilter();
		// フィルターを登録する（SimpleService.ACTIONが配信された場合のみ実行されるようになる）
		filter.addAction(ACTION);
		// レシーバーの登録
		registerReceiver(mReceiver, filter);

		web= (WebView) findViewById(R.id.webView1);
		//対象のWebivewを取得して設定を有効化
		web.setVerticalScrollbarOverlay(true); //スクロールバーをオーバーレイして余白を消す
		web.getSettings().setJavaScriptEnabled(true); //JavaScriptの有効化
		web.getSettings().setBuiltInZoomControls(true); // will give pinch zoom
		web.getSettings().setDisplayZoomControls(false); // but won't display the zoom buttons
		getWindow().setFlags(//HWアクセラレータ有効
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
				,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
				);
		web.setVerticalScrollbarOverlay(true); // 右端の余白を削除
		web.getSettings().setUseWideViewPort(true); // ワイドビューポート
		web.getSettings().setLoadWithOverviewMode(true); // ズームアウト
		//キャッシュの設定
		web.getSettings().setAppCacheEnabled(true);
		web.getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//キャッシュがある場合はキャッシュから読み込み

		/**WebViewの各設定(ページ遷移時の設定メイン)**/
		web.setWebViewClient(new WebViewClient() {
			@Override
			/**歳とロード中の処理**/
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				//setProgressBarIndeterminateVisibility(true);
			}

			/**歳とロード完了の処理**/
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				ImageView reloadimg = (ImageView) findViewById(R.id.reloadimg);
				reloadimg.setVisibility(View.INVISIBLE);
			}
		});
		//URLロード
		web.loadUrl("http://pppnexus.ddo.jp/news.cgi");

		//HOMEキーが押されたときのレシーバ設定
		m_HomeButtonReceive = new HomeButtonReceive();
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		this.registerReceiver(m_HomeButtonReceive, iFilter);
	}


	//HOMEボタンで終了する。
	public class HomeButtonReceive extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1){
			Log.v("onHomeDestroy","再起動します。");
			//unregisterReceiver(m_HomeButtonReceive);
			//m_HomeButtonReceive = null;
			moveTaskToBack (true);
			//finish();

		}
	}

	public void close(View view){
		//unregisterReceiver(m_HomeButtonReceive);
		moveTaskToBack (true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// レシーバーの解除
		if(m_HomeButtonReceive != null){
			unregisterReceiver(m_HomeButtonReceive);
			m_HomeButtonReceive = null;
		}
		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		Editor edit = mSP.edit();
		edit.putInt("openActivity", 0);
		edit.commit();
	}
	@Override
	public void onPause(){
		super.onPause();
		stopService(new Intent(this, staover.class));
		startService(new Intent(this, staover.class));
		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		Editor edit = mSP.edit();
		edit.putInt("openActivity", 0);
		edit.commit();
	}

	@Override
	public void onResume(){
		super.onResume();
		// アニメーションの設定
		overridePendingTransition(R.anim.in_down, R.anim.out_up);
		stopService(new Intent(this, staover.class));
	}
	@Override
	/*戻るボタンを有効化するメソッド*/
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		/*閲覧履歴がもしあればもどれるので、trueを返す*/
		if ( event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack() == true ) {
			try {
				WebView.class.getMethod("onStop").invoke(web);
			} catch (IllegalArgumentException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			web.goBack();
			return true;
		}
		return super.onKeyDown( keyCode, event );
	}


	/**リロードシステム実装**/
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		// Toast1("タッチ");

		return onTouch(web, ev);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		// ウィンドウマネージャのインスタンス取得
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		// ディスプレイのインスタンス生成
		disp = wm.getDefaultDisplay();
		width = disp.getWidth();
		height = disp.getHeight();
		Xsize = width / 2.9;
		Ysize = height / 4.3;
		float Poss;
		float Poss2;
		float Poss3;

		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		Editor editor = mSP.edit();

		//Toast1("タッチ");

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			lastTouchX = event.getX();
			lastTouchY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			currentY = event.getY();

			reloadView();

			break;

		case MotionEvent.ACTION_UP:
			currentX = event.getX();
			currentY = event.getY();
			mSP = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			editor = mSP.edit();
			editor.putFloat("DefPButtos", 0);
			editor.putFloat("DefPosX", 0);
			editor.putFloat("DefPosX2", 0);
			editor.putFloat("DefPos", 0);

			DefPos = (float) 0.0;
			editor.commit();

			//利ローど
			if (topMargin > 148 && topMargin2 < 250) {

				// TextToSpeechオブジェクトの生成

				// speechText("");
				ImageView reloadimg = (ImageView) findViewById(R.id.reloadimg);

				// reloadimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
				reloadimg.setX(-30);
				reloadimg.setImageResource(R.drawable.reloadimg3);
				reloadimg.setVisibility(View.VISIBLE);
				reloadimg.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, 350));
				TranslateAnimation translate = new TranslateAnimation(0, 0, 0,
						3080); // (0,0)から(100,100)に移動
				translate.setDuration(1300); // 1300msかけてアニメーションする
				// translate.setFillAfter(true); //終了後を保持
				reloadimg.startAnimation(translate); // アニメーション適用
				web.reload();
			} else {
				ImageView reloadimg = (ImageView) findViewById(R.id.reloadimg);
				reloadimg.setVisibility(View.INVISIBLE);
			}

			FrameLayout layout = (FrameLayout) findViewById(R.id.webview);
			layout.setPadding(0, 0, 0, 0);

			PosMoveFlag = 0;
			PButtonFlag = 0;
			topMargin = 0;

			break;

		case MotionEvent.ACTION_CANCEL:
			currentX = event.getX();
			if (lastTouchX < currentX) {
				// 前に戻る動作
			}
			if (lastTouchX > currentX) {
				// 次に移動する動作
			}
			//return false;
			break;
		}
		return true;
	}

	//リロードシステム
	public void reloadView(){
		//リロード(上)
		float ScrollY = web.getScrollY();
		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		Editor editor = mSP.edit();

		if (lastTouchY  < currentY && ScrollY <= 1 && PosMoveFlag3 != 1 && PButtonFlag!=1) {
			if (PosMoveFlag != 1) {
				DefPos = currentY;
				editor.putFloat("DefPos", DefPos);
				editor.commit();
				// Toast1(String.valueOf(DefPos));
			} else {
				if (SetPos < 1) {
					SetPos = (float) 0;
				}
				DefPos = mSP.getFloat("DefPos", (float) 0.0);
				// DefPos = (float) 0.0;
				Poss = currentY - DefPos;
				// Toast1(String.valueOf(DefPos));
				SetPos = SetPos + Poss / 100;

				float Set = (float) Poss / 2;
				topMargin = (int) Set;
				FrameLayout layout = (FrameLayout) findViewById(R.id.webview);
				ImageView reloadimg = (ImageView) findViewById(R.id.reloadimg);

				if (topMargin < 150) {
					layout.setPadding(0, topMargin, 0, 0);
					// reloadimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
					reloadimg.setX(-30);
					reloadimg.setImageResource(R.drawable.reloadimg);
					reloadimg.setVisibility(View.VISIBLE);
					reloadimg
					.setLayoutParams(new RelativeLayout.LayoutParams(
							LayoutParams.FILL_PARENT, topMargin));
				} else {
					reloadimg.setImageResource(R.drawable.reloadimg2);
				}
				if (topMargin < 10) {
					reloadimg.setVisibility(View.INVISIBLE);
				}
				web.scrollTo(0, 0);
			}
			PosMoveFlag = 1;
		}
	}
}
