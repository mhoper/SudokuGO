package com.legendleo.sudokugo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private static final String ToggleButton = null;
	private int reclen = 0;
	private TextView tv, tv_tips;
	private int h, m, s;
	private String hh, mm, ss, times;
	private Button key[] = new Button[9];
	private GameView gameview;
	private static MainActivity mainActivity;
	private ToggleButton tbnTips, tbnEasyHard;
	private int used[];
	private boolean toggleTipsStatus = false;
	private boolean toggleEasyHardStatus = true;
	
	//�����������
	private RandomPuzzle randomPuzzle;
	private String strPuzzle;
	
	private String message = "ȷ������һ�֣�";

	public MainActivity() {

		mainActivity = this;
	}
	
	public static MainActivity getMainActivity(){
		
		return mainActivity;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate start");
		
		setContentView(R.layout.activity_main);
		//setContentView(new GameView(this));
		
		//��Tips���������»���
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		tv_tips.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		//��ʱ��
		tv = (TextView) findViewById(R.id.timer);
		handler.postDelayed(runnable, 1000);
		
		gameview = (GameView) findViewById(R.id.gameview);

		randomPuzzle = new RandomPuzzle(getMainActivity());
		
		findButtons();
		setListeners();

		System.out.println("onCreate end");
	}

	private void findButtons(){
		key[0] = (Button) findViewById(R.id.key_1);
		key[1] = (Button) findViewById(R.id.key_2);
		key[2] = (Button) findViewById(R.id.key_3);
		key[3] = (Button) findViewById(R.id.key_4);
		key[4] = (Button) findViewById(R.id.key_5);
		key[5] = (Button) findViewById(R.id.key_6);
		key[6] = (Button) findViewById(R.id.key_7);
		key[7] = (Button) findViewById(R.id.key_8);
		key[8] = (Button) findViewById(R.id.key_9);
	}
	
	private void setListeners(){
		for (int i = 0; i < 9; i++) {
			final int t = i + 1;
			key[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					if(gameview.getCheckedClick()){
						
						gameview.setSelectedTile(t);
					}else{
						Toast.makeText(getBaseContext(), "��ѡ��һ������", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		
		//�˸�ť
		findViewById(R.id.key_c).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(gameview.getCheckedClick()){
					//����д���������
					gameview.setSelectedTile(0);
				}
				
			}
		});
		
		findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new AlertDialog.Builder(getMainActivity())
				.setMessage(message)
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				})
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						//��ȡ�����Ѽ���
						boolean bl = tbnEasyHard.isChecked();
						
						//���ݼ򵥻�ȡ�����������
						strPuzzle = randomPuzzle.getRandomPuzzle(bl);
						gameview.setStrPuzzle(strPuzzle);
						//���¿�ʼ
						gameview.startNewGame();
						
					}
				}).show();
				
				//������Ϣ
				message = "ȷ������һ�֣�";
			}
		});
		
		//Tips���ذ�ť
		tbnTips = (ToggleButton) findViewById(R.id.toggleTips);
		tbnTips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					//������Ϊѡ��״̬ʱ������Tipsʱͬʱ���ò����õİ�ť
					if(gameview.getCheckedClick()){

						setButtonDisabled(used);
					}
					//System.out.println("TipsOn");
				}else{
					//�ر�Tipsʱ���������п���
					setButtonEnabled();
					//System.out.println("TipsOff");
				}
			}
		});
		
		//�����Ѽ���
		tbnEasyHard = (ToggleButton) findViewById(R.id.toggleEasyHard);
		tbnEasyHard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					//���ݼ򵥻�ȡ�����������
					strPuzzle = randomPuzzle.getRandomPuzzle(true);
					gameview.setStrPuzzle(strPuzzle);
					gameview.startNewGame();
				}else{
					if(toggleEasyHardStatus){
						//�������ѻ�ȡ�����������
						strPuzzle = randomPuzzle.getRandomPuzzle(false);
						gameview.setStrPuzzle(strPuzzle);
						gameview.startNewGame();
					}
					toggleEasyHardStatus = true; //���Ϊfalse��load֮��Ż���֣�������if֮�����ΪĬ��true
				}
			}
		});
		
		//������½�Tips��������͸��˵����
		findViewById(R.id.tv_tips).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				GameAbout ga = new GameAbout(getMainActivity(), R.style.ThemeDialog);
				ga.show();
			}
		});
	}
	
	//���ò����ð�ť
	public void setButtonDisabled(int used[]){
		//��used�������ݼ�¼����
		this.used = used;
		
		//�ж�Tips����״̬
		if(tbnTips.isChecked()){
			setButtonEnabled();
			for (int i = 0; i < used.length; i++) {
				
				key[used[i]-1].setEnabled(false);
			}
		}else{
			
			setButtonEnabled();
		}
	}
	
	//�������а�ť
	public void setButtonEnabled(){
		
		for (int i = 0; i < 9; i++) {
			
			key[i].setEnabled(true);
		}
	}
	
	//��ʱ��
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			reclen++;

			s = reclen%60;
			m = reclen/60%60;
			h = reclen/60/60;
			
			hh = h > 9 ? (h + "") : ("0" + h);
			mm = m > 9 ? (m + "") : ("0" + m);
			ss = s > 9 ? (s + "") : ("0" + s);
			if(h > 0){
				times = hh + ":" + mm + ":" + ss;
			}else{
				times = mm + ":" + ss;
			}
			tv.setText(times);
			handler.postDelayed(this, 1000);
		}
	};
	
	//set��ʱ��ʱ��
	public void setReclen(int reclen) {
		this.reclen = reclen;
	}
	
	//set��ʾ��Ϣ
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//�˳�ʱ���浱ǰ����
		gameview.savecurrentData();
		
		//����ʱ�估����ť״̬
		SharedPreferences sp = getSharedPreferences("puzzledata", Context.MODE_PRIVATE);

		int time = this.reclen;
		boolean toggleTipsStatus = this.tbnTips.isChecked();
		boolean toggleEasyHardStatus = this.tbnEasyHard.isChecked();
		
		Editor e = sp.edit();
		e.putInt("time", time);
		e.putBoolean("toggleTipsStatus", toggleTipsStatus);
		e.putBoolean("toggleEasyHardStatus", toggleEasyHardStatus);
		e.commit();
	};
	
	@Override
	protected void onResume() {
		super.onResume();

		System.out.println("onResume start");
		
		//�����������д�������
		if(gameview.checkLoadSavedData()){

			System.out.println("loadData start");
			
			//����ʱ�䡢Tips��EasyHard��ť״̬
			SharedPreferences sp = getSharedPreferences("puzzledata", Context.MODE_PRIVATE);
			
			reclen = sp.getInt("time", 0);
			toggleTipsStatus = sp.getBoolean("toggleTipsStatus", false);
			toggleEasyHardStatus = sp.getBoolean("toggleEasyHardStatus", true);
			
			tbnTips.setChecked(toggleTipsStatus);
			tbnEasyHard.setChecked(toggleEasyHardStatus);
			
			//������������
			gameview.loadSavedData();
			
			System.out.println("loadData end");
			
		}else{

			//���ݼ򵥻����ѻ�ȡ�����������
			strPuzzle = randomPuzzle.getRandomPuzzle(true);
			gameview.setStrPuzzle(strPuzzle);
			gameview.startNewGame();
		}
		
		
		System.out.println("onResume end");
		
	}
}
