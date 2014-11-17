package com.legendleo.sudokugo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class KeyDialog extends Dialog {
	
	private final View keys[] = new View[9];
	private final int used[];
	private GameView gameView;

	//构造函数，这里很关键，要把gameView传入进来初始化，不能去new一个gameView，否则数据有问题。
	public KeyDialog(Context context, int[] used, GameView gameView) {
		super(context);
		this.used = used;
		this.gameView = gameView;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle("Tips");
		//设置Dialog透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.8f;
		getWindow().setAttributes(lp);
		
		setContentView(R.layout.keypad);
		findViews();
		for (int i = 0; i < used.length; i++) {
			if(used[i] !=0){
				keys[used[i]-1].setVisibility(View.INVISIBLE);
			}
		}
		setListeners();
	}

	private void findViews(){
		keys[0] = findViewById(R.id.keypad_1);
		keys[1] = findViewById(R.id.keypad_2);
		keys[2] = findViewById(R.id.keypad_3);
		keys[3] = findViewById(R.id.keypad_4);
		keys[4] = findViewById(R.id.keypad_5);
		keys[5] = findViewById(R.id.keypad_6);
		keys[6] = findViewById(R.id.keypad_7);
		keys[7] = findViewById(R.id.keypad_8);
		keys[8] = findViewById(R.id.keypad_9);
	}
	
	private void returnResults(int tile){
		gameView.setSelectedTile(tile);
		//取消对话框
		dismiss();
	}
	
	private void setListeners(){
		for (int i = 0; i < keys.length; i++) {
			final int t = i + 1;
			keys[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					returnResults(t);
				}
			});
		}
	}
}
