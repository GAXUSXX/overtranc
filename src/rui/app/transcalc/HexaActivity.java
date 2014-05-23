package rui.app.transcalc;

import rui.app.transcalc.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HexaActivity extends Activity{

	// �ｿｽi�ｿｽ�ｿｽ�ｿｽo�ｿｽ[�ｿｽf�ｿｽB�ｿｽv�ｿｽ�ｿｽ�ｿｽC
	EditText numDis;

	// Spinner
	Spinner spinner;

	String current;		//�ｿｽ@�ｿｽ謫ｾ�ｿｽp
	String added;		//�ｿｽ@�ｿｽ�ｿｽﾍ用
	String deleted;		// �ｿｽ尞懶ｿｽp

	// �ｿｽﾏ奇ｿｽ�ｿｽp
	String Decimal;
	String Hexa;

	TextView num_1;
	TextView num_2;
	TextView num_3;
	TextView num_4;
	TextView num_5;
	TextView num_6;
	TextView num_7;
	TextView num_8;
	TextView num_9;
	TextView num_0;
	TextView num_A;
	TextView num_B;
	TextView num_C;
	TextView num_D;
	TextView num_E;
	TextView num_F;
	TextView clear;
	TextView delete;
	TextView trans;

	// AlertDialog
	AlertDialog.Builder adb;
	AlertDialog alertDialog;

	private rui.app.transcalc.HexaActivity.HomeButtonReceive m_HomeButtonReceive;



	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hexa);

		getObject();
		setSpinnerListener();
		setTextViewListener();

		numDis.setRawInputType(InputType.TYPE_NULL);

		//HOME繧ｭ繝ｼ縺梧款縺輔ｌ縺溘→縺阪�ｮ繝ｬ繧ｷ繝ｼ繝占ｨｭ螳�
		m_HomeButtonReceive = new HomeButtonReceive();
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		this.registerReceiver(m_HomeButtonReceive, iFilter);

	}

	// �ｿｽI�ｿｽu�ｿｽW�ｿｽF�ｿｽN�ｿｽg�ｿｽ�ｿｽ�ｿｽ�ｿｽ
	public void getObject(){

		// TextView�ｿｽ謫ｾ
		num_1 = (TextView)findViewById(R.id.num_1);
		num_2 = (TextView)findViewById(R.id.num_2);
		num_3 = (TextView)findViewById(R.id.num_3);
		num_4 = (TextView)findViewById(R.id.num_4);
		num_5 = (TextView)findViewById(R.id.num_5);
		num_6 = (TextView)findViewById(R.id.num_6);
		num_7 = (TextView)findViewById(R.id.num_7);
		num_8 = (TextView)findViewById(R.id.num_8);
		num_9 = (TextView)findViewById(R.id.num_9);
		num_0 = (TextView)findViewById(R.id.num_0);
		num_A = (TextView)findViewById(R.id.num_A);
		num_B = (TextView)findViewById(R.id.num_B);
		num_C = (TextView)findViewById(R.id.num_C);
		num_D = (TextView)findViewById(R.id.num_D);
		num_E = (TextView)findViewById(R.id.num_E);
		num_F = (TextView)findViewById(R.id.num_F);
		clear = (TextView)findViewById(R.id.clear);
		delete = (TextView)findViewById(R.id.delete);
		trans = (TextView)findViewById(R.id.trans);

		// EditText�ｿｽ謫ｾ
		numDis = (EditText)findViewById(R.id.numDisplay);

		// Spinner�ｿｽ謫ｾ
		spinner = (Spinner) findViewById(R.id.spinner);
	}

	//�ｿｽ@setOnItemSelectedListener
	public void setSpinnerListener(){

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// �ｿｽA�ｿｽC�ｿｽe�ｿｽ�ｿｽ�ｿｽ�ｿｽﾇ会ｿｽ
		adapter.add(getString(R.string.hexa));
		adapter.add(getString(R.string.decimal));
		// �ｿｽA�ｿｽ_�ｿｽv�ｿｽ^�ｿｽ[�ｿｽ�ｿｽﾝ定し�ｿｽﾜゑｿｽ
		spinner.setAdapter(adapter);
		// �ｿｽX�ｿｽs�ｿｽi�ｿｽ[�ｿｽﾌア�ｿｽC�ｿｽe�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽI�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ黷ｽ�ｿｽ�ｿｽ�ｿｽﾉ呼び出�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽR�ｿｽ[�ｿｽ�ｿｽ�ｿｽo�ｿｽb�ｿｽN�ｿｽ�ｿｽ�ｿｽX�ｿｽi�ｿｽ[�ｿｽ�ｿｽo�ｿｽ^�ｿｽ�ｿｽ�ｿｽﾜゑｿｽ
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
				Spinner spinner = (Spinner)parent;
				String item = (String)spinner.getSelectedItem();
				if(item.equals(getString(R.string.decimal))){
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName( "rui.app.transcalc","rui.app.transcalc.DecimalActivity");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				Log.i("Hoge", item);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void setTextViewListener(){

		OnTouchListener touchListener = new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){

				case MotionEvent.ACTION_DOWN:

					switch(v.getId()){
					case R.id.delete:
						delete(v);
						break;
					case R.id.clear:
						clear(v);
					case R.id.trans:
						trans(v);
					default:
						add(v);
					}

					v.setBackgroundResource(R.drawable.down);
					break;
				case MotionEvent.ACTION_UP:
					v.setBackgroundResource(R.drawable.up);
					v.setPadding(0, 0, 0, 0);
					break;
				}
				return true;
			}

		};

		num_1.setOnTouchListener(touchListener);
		num_2.setOnTouchListener(touchListener);
		num_3.setOnTouchListener(touchListener);
		num_4.setOnTouchListener(touchListener);
		num_5.setOnTouchListener(touchListener);
		num_6.setOnTouchListener(touchListener);
		num_7.setOnTouchListener(touchListener);
		num_8.setOnTouchListener(touchListener);
		num_9.setOnTouchListener(touchListener);
		num_0.setOnTouchListener(touchListener);
		num_A.setOnTouchListener(touchListener);
		num_B.setOnTouchListener(touchListener);
		num_C.setOnTouchListener(touchListener);
		num_D.setOnTouchListener(touchListener);
		num_E.setOnTouchListener(touchListener);
		num_F.setOnTouchListener(touchListener);
		clear.setOnTouchListener(touchListener);
		delete.setOnTouchListener(touchListener);
		trans.setOnTouchListener(touchListener);

	}



	// �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ
	public void add(View v){

		// �ｿｽ�ｿｽﾍゑｿｽ�ｿｽ�ｿｽﾄゑｿｽ�ｿｽ髟ｶ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ謫ｾ
		current = numDis.getText().toString();

		if(current.length()==9){
			return;
		}

		switch(v.getId()){
		case R.id.num_1:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="1";
			}else{
				added = current + "1";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_2:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="2";
			}else{
				added = current + "2";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_3:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="3";
			}else{
				added = current + "3";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_4:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="4";
			}else{
				added = current + "4";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_5:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="5";
			}else{
				added = current + "5";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_6:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="6";
			}else{
				added = current + "6";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_7:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="7";
			}else{
				added = current + "7";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_8:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="8";
			}else{
				added = current + "8";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_9:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="9";
			}else{
				added = current + "9";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_0:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="0";
			}else{
				added = current + "0";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_A:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="A";
			}else{
				added = current + "A";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_B:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="B";
			}else{
				added = current + "B";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_C:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="C";
			}else{
				added = current + "C";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_D:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="D";
			}else{
				added = current + "D";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_E:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="E";
			}else{
				added = current + "E";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		case R.id.num_F:
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="F";
			}else{
				added = current + "F";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		}


	}

	// �ｿｽN�ｿｽ�ｿｽ�ｿｽA�ｿｽ{�ｿｽ^�ｿｽ�ｿｽ
	public void clear(View v){
		numDis.setText("");
	}
	// �ｿｽf�ｿｽ�ｿｽ�ｿｽ[�ｿｽg�ｿｽ{�ｿｽ^�ｿｽ�ｿｽ
	public void delete(View v){
		current = numDis.getText().toString();

		// �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽﾛゑｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽI�ｿｽ�ｿｽ�ｿｽI
		if(current.equals("")){
			return;
		}

		deleted = current.substring(0,current.length()-1);
		numDis.setText(deleted);
		numDis.setSelection(deleted.length());
	}

	// �ｿｽﾏ奇ｿｽ�ｿｽ{�ｿｽ^�ｿｽ�ｿｽ
	public void trans(View v){
		// �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ謫ｾ
		current = numDis.getText().toString();

		// �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽﾛゑｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽI�ｿｽ�ｿｽ�ｿｽI
		if(current.equals("")){
			return;
		}

		// 10�ｿｽi�ｿｽ�ｿｽ�ｿｽ�ｿｽ16�ｿｽi�ｿｽ�ｿｽ�ｿｽﾉ変奇ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ
		Hexa = current;
		Decimal = Long.toString(Long.parseLong(Hexa,16));




		// InflateView�ｿｽ�ｿｽ�ｿｽ謫ｾ
		LayoutInflater inflater = LayoutInflater.from(HexaActivity.this);
		View view = inflater.inflate(R.layout.alert_transed, null);

		TextView notTrans = (TextView)view.findViewById(R.id.editText0);
		notTrans.setText(Hexa);

		TextView transed = (TextView)view.findViewById(R.id.editText1);
		transed.setText(Decimal);

		Button copy = (Button)view.findViewById(R.id.copy);
		copy.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
				// �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽﾌコ�ｿｽs�ｿｽ[
				cm.setText(Decimal);
				Toast.makeText(getBaseContext(), "コピーしました。", Toast.LENGTH_SHORT).show();
				alertDialog.dismiss();
			}
		});

		Button cancel = (Button)view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				alertDialog.dismiss();
			}

		});






		// �ｿｽA�ｿｽ�ｿｽ�ｿｽ[�ｿｽg�ｿｽ_�ｿｽC�ｿｽA�ｿｽ�ｿｽ�ｿｽO�ｿｽﾌタ�ｿｽC�ｿｽg�ｿｽ�ｿｽ�ｿｽ�ｿｽﾝ定し�ｿｽﾜゑｿｽ
		adb = new AlertDialog.Builder(this);
		//adb.setTitle("16�ｿｽi�ｿｽ�ｿｽ�ｿｽ�ｿｽ10�ｿｽi�ｿｽ�ｿｽ");

		adb.setView(view);
		// �ｿｽA�ｿｽ�ｿｽ�ｿｽ[�ｿｽg�ｿｽ_�ｿｽC�ｿｽA�ｿｽ�ｿｽ�ｿｽO�ｿｽﾌキ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽZ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽﾂ能�ｿｽ�ｿｽ�ｿｽﾇゑｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽﾝ定し�ｿｽﾜゑｿｽ
		adb.setCancelable(false);
		alertDialog = adb.create();
		// �ｿｽA�ｿｽ�ｿｽ�ｿｽ[�ｿｽg�ｿｽ_�ｿｽC�ｿｽA�ｿｽ�ｿｽ�ｿｽO�ｿｽ�ｿｽ\�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽﾜゑｿｽ
		alertDialog.show();
	}

	//HOME繝懊ち繝ｳ縺ｧ邨ゆｺ�縺吶ｋ縲�
	public class HomeButtonReceive extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1){
			Log.v("onHomeDestroy","蜀崎ｵｷ蜍輔＠縺ｾ縺吶��");
			//unregisterReceiver(m_HomeButtonReceive);
			//m_HomeButtonReceive = null;
			moveTaskToBack (true);
			//finish();
		}
	}

	public void close(View view){
		//unregisterReceiver(m_HomeButtonReceive);
		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		Editor edit = mSP.edit();
		edit.putInt("lastTime",20);

		edit.commit();
		moveTaskToBack (true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 繝ｬ繧ｷ繝ｼ繝舌�ｼ縺ｮ隗｣髯､
		if(m_HomeButtonReceive != null){
			unregisterReceiver(m_HomeButtonReceive);
			m_HomeButtonReceive = null;
		}
		SharedPreferences mSP = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		Editor edit = mSP.edit();
		edit.putInt("openActivity", 0);
		edit.putInt("lastTime",20);

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
		edit.putInt("lastTime",20);

		edit.commit();
	}

}
