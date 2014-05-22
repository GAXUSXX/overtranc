package rui.app.transcalc;

import rui.app.transcalc.R;
import rui.app.transcalc.TestActivity.HomeButtonReceive;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DecimalActivity extends Activity{

	// �i���o�[�f�B�X�v���C
	EditText numDis;

	// Spinner
	Spinner spinner;

	String current;		//�@�擾�p
	String added;		//�@��͗p
	String deleted;		// �폜�p

	// �ϊ��p�ϐ�
	int Decimal;
	String Hexa;


	// �e�{�^��
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
	TextView clear;
	TextView delete;
	TextView trans;

	AlertDialog.Builder adb;
	AlertDialog alertDialog;

	private rui.app.transcalc.DecimalActivity.HomeButtonReceive m_HomeButtonReceive;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_decimal);


		getObject();
		setSpinnerListener();
		setTextViewListener();

		numDis.setRawInputType(InputType.TYPE_NULL);
		//HOMEキーが押されたときのレシーバ設定
		m_HomeButtonReceive = new HomeButtonReceive();
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		this.registerReceiver(m_HomeButtonReceive, iFilter);

	}

	// �I�u�W�F�N�g����
	public void getObject(){

		// TextView�擾
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
		clear = (TextView)findViewById(R.id.clear);
		delete = (TextView)findViewById(R.id.delete);
		trans = (TextView)findViewById(R.id.trans);

		// EditText�擾
		numDis = (EditText)findViewById(R.id.numDisplay);

		// Spinner�擾
		spinner = (Spinner) findViewById(R.id.spinner);
	}

	//�@setOnItemSelectedListener
	public void setSpinnerListener(){

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// �A�C�e����ǉ�
		adapter.add(getString(R.string.decimal));
		adapter.add(getString(R.string.hexa));
		// �A�_�v�^�[��ݒ肵�܂�
		spinner.setAdapter(adapter);
		// �X�s�i�[�̃A�C�e�����I�����ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				Spinner spinner = (Spinner)parent;
				String item = (String)spinner.getSelectedItem();
				if(item.equals(getString(R.string.hexa))){
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName( "rui.app.transcalc","rui.app.transcalc.HexaActivity");
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
		clear.setOnTouchListener(touchListener);
		delete.setOnTouchListener(touchListener);
		trans.setOnTouchListener(touchListener);

	}

	// �������
	public void add(View v){

		// ��͂���Ă��镶�����擾
		current = numDis.getText().toString();

		switch(v.getId()){
		// �P��������
		case R.id.num_1:
			if(current.length()==9){
				break;
			}
			if(current.length()==1&&current.equals("0")){
				added="1";
			}else{
				added = current + "1";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �Q��������
		case R.id.num_2:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="2";
			}else{
				added = current + "2";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �R��������
		case R.id.num_3:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="3";
			}else{
				added = current + "3";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �S��������
		case R.id.num_4:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="4";
			}else{
				added = current + "4";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �T��������
		case R.id.num_5:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="5";
			}else{
				added = current + "5";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �U��������
		case R.id.num_6:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="6";
			}else{
				added = current + "6";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �V��������
		case R.id.num_7:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="7";
			}else{
				added = current + "7";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �W��������
		case R.id.num_8:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="8";
			}else{
				added = current + "8";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �X��������
		case R.id.num_9:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="9";
			}else{
				added = current + "9";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
			// �O��������
		case R.id.num_0:
			if(current.length()==9){
				break;
			}
			current = numDis.getText().toString();
			if(current.length()==1&&current.equals("0")){
				added="0";
			}else{
				added = current + "0";
			}
			numDis.setText(added);
			numDis.setSelection(added.length());
			break;
		}
	}

	// �N���A�{�^��
	public void clear(View v){
		numDis.setText("");
	}
	// �f���[�g�{�^��
	public void delete(View v){

		current = numDis.getText().toString();

		// ����������ۂ�������I���I
		if(current.equals("")){
			return;
		}

		deleted = current.substring(0,current.length()-1);
		numDis.setText(deleted);
		numDis.setSelection(deleted.length());
	}

	// �ϊ��{�^��
	public void trans(View v){
		// �������擾
		current = numDis.getText().toString();

		// ����������ۂ�������I���I
		if(current.equals("")){
			return;
		}

		// 10�i����16�i���ɕϊ�����
		Decimal = Integer.valueOf(current);
		Hexa = Integer.toHexString(Decimal).toUpperCase();


		// InflateView���擾
		LayoutInflater inflater = LayoutInflater.from(DecimalActivity.this);
		View view = inflater.inflate(R.layout.alert_transed, null);

		TextView notTrans = (TextView)view.findViewById(R.id.editText0);
		notTrans.setText(current);

		TextView transed = (TextView)view.findViewById(R.id.editText1);
		transed.setText(Hexa);

		Button copy = (Button)view.findViewById(R.id.copy);
		copy.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
				// ������̃R�s�[
				cm.setText(Hexa);
				Toast.makeText(getBaseContext(), "�R�s�[���܂���", Toast.LENGTH_SHORT).show();
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




		adb = new AlertDialog.Builder(this);
		// �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
		adb.setView(view);
		// �A���[�g�_�C�A���O�̃L�����Z�����\���ǂ�����ݒ肵�܂�
		adb.setCancelable(false);
		alertDialog = adb.create();
		// �A���[�g�_�C�A���O��\�����܂�
		alertDialog.show();
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
			// レシーバーの解除
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

	@Override
	public void onResume(){
		super.onResume();
		// アニメーションの設定
		overridePendingTransition(R.anim.in_down, R.anim.out_up);
		stopService(new Intent(this, staover.class));
	}
}
