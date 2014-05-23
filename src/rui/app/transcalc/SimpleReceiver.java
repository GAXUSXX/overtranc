package rui.app.transcalc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SimpleReceiver extends BroadcastReceiver {

	/*
	 * ブロードキャストに配信されてきたものを受信した時に呼び出される
	 *
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */

	/**serviceから呼び出しActivityを終了**/
	@Override
	public void onReceive(Context context, Intent intent) {
		String msg = intent.getStringExtra("message");
		//Toast.makeText(context, "現在時刻：" + msg, Toast.LENGTH_SHORT).show();

		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(context);

		Editor edit = mSP.edit();
		edit.putInt("openActivity", 0);
		edit.commit();
		
		// レシーバーを受信したActivityを利用する場合
		TestActivity activity = (TestActivity) context;
		activity.moveTaskToBack (true);

	}
}