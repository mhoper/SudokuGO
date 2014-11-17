package com.legendleo.sudokugo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameView extends View {

	//定义数独画面的宽度与高度
	private float width;
	private float height;
	
	//数独难易水平
	private String strPuzzle = Config.STR_EASY;
	private Game game = new Game();
	
	//选中的格子的对应1-9坐标
	private int selectedX, selectedY;
	

	//点击开关
	private boolean checkedClick; 
	private Button key[] = new Button[9];

	private MainActivity aty = MainActivity.getMainActivity();
	
	public GameView(Context context, AttributeSet attri) {
		super(context, attri);
		System.out.println("gamaview的构造函数被执行");
	}

	//重新开始游戏
	public void startNewGame(){

		//Time清0
		aty.setReclen(0);
		
		//清除点击方块的状态
		this.checkedClick = false;
		
		//将1-9所有数字按钮开启
		aty.setButtonEnabled();
		
		//重新new一个开始
		game.fromPuzzleString(strPuzzle);
		//game.setOriginal();
		game.calculateAllUsedTiles();
		
		//重绘
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取屏幕宽度、高度
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		int screenwidth = Math.min(dm.widthPixels, dm.heightPixels);
		//设置该View宽高
		setMeasuredDimension(screenwidth, screenwidth);
		System.out.println("onMeasure");
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	
		//取得View宽高，设置高等于宽，画出一个正方形
		//activity_main中padding=2,这里减8是为了让最右边、最下边的粗边框显示出来
		this.width = w - 8;
		this.height = h - 8;
		System.out.println("onSizeChanged");

	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		System.out.println("onDraw start");
		
		//cell宽高
		float cellWidth = (width)/9;
		float cellHeight = (height)/9;
		
		//画出背景
		Paint backgroundPaint = new Paint();
		backgroundPaint.setColor(getResources().getColor(R.color.sudoku_background));
		canvas.drawRect(0, 0, width, height, backgroundPaint);

		//原始的数字背景色
		Paint originalTileBackgroundPaint = new Paint();
		originalTileBackgroundPaint.setColor(getResources().getColor(R.color.originalTileBackgroundPaint));
		
		//填写的数字背景色
		Paint tileBackgroundPaint = new Paint();
		tileBackgroundPaint.setColor(getResources().getColor(R.color.tileBackgroundPaint));
		
		//点击开关,绘制格子背景色
		if(checkedClick == true){
			
			canvas.drawRect(cellWidth*selectedX, cellHeight*selectedY, cellWidth*(selectedX + 1), cellHeight*(selectedY + 1), tileBackgroundPaint);
		}
		
		
		//正确数字画笔
		Paint numberPaint = new Paint();
		numberPaint.setColor(Color.BLACK);
		numberPaint.setTextSize(cellHeight*0.75f);
		numberPaint.setStyle(Paint.Style.STROKE);
		numberPaint.setTextAlign(Paint.Align.CENTER);
		numberPaint.setAntiAlias(true);
		

		//错误数字画笔
		Paint wrongNumberPaint = new Paint();
		wrongNumberPaint.setColor(getResources().getColor(R.color.wrongNumber));
		wrongNumberPaint.setTextSize(cellHeight*0.75f);
		wrongNumberPaint.setStyle(Paint.Style.STROKE);
		wrongNumberPaint.setTextAlign(Paint.Align.CENTER);
		wrongNumberPaint.setAntiAlias(true);
		
		
		//计算数字的填充位置
		FontMetrics fm = numberPaint.getFontMetrics();
		float fontx = cellWidth/2;
		float fonty = cellHeight/2 - (fm.ascent + fm.descent)/2;
		
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				String o = this.game.getOriginalTileString(x, y);
				String s = this.game.getTileString(x, y);
				//绘制不为0的格子
				if(Integer.valueOf(o) != 0){
					
					//给原始数据添加背景色
					canvas.drawRect(cellWidth*x, cellHeight*y, cellWidth*(x + 1), cellHeight*(y + 1), originalTileBackgroundPaint);
					canvas.drawText(o, cellWidth*x+ fontx, cellHeight*y + fonty, numberPaint);
				}else{
					if(s != o){

						//判断填入的数字与所在X/Y轴及小9格中是否有重复的数字
						int usedNumbers[] = game.calculateUsedTiles(x, y);
						int thisNumber = game.getTile(x, y);
						boolean redFlag = false;
						for (int tile : usedNumbers) {
							if(tile == thisNumber){
								redFlag = true;
								break;
							}
						}
						
						if(redFlag){
							//给当前选中框中错误的数字颜色变成红色
							canvas.drawText(s, cellWidth*x+ fontx, cellHeight*y + fonty, wrongNumberPaint);
						}else{
							
							canvas.drawText(s, cellWidth*x+ fontx, cellHeight*y + fonty, numberPaint);
						}

						
					}
					
				}
			}
		}
		

		//放在最后再绘制线条
		//浅色线
		Paint lightPaint = new Paint();
		lightPaint.setColor(getResources().getColor(R.color.sudoku_light));
		
		//深色线
		Paint darkPaint = new Paint();
		darkPaint.setColor(getResources().getColor(R.color.sudoku_dark));
		
		Paint hilitePaint = new Paint();
		hilitePaint.setColor(getResources().getColor(R.color.sudoku_hilite));
		
		
		//技巧：相邻画两根线，一深一线，看起来有凹刻的效果
		for (int i = 0; i <= 9; i++) {
			if(i%3 == 0){
				//画出深色竖线
				canvas.drawLine(cellWidth*i, 0, cellWidth*i, height, darkPaint);
				canvas.drawLine(cellWidth*i+1, 0, cellWidth*i+1, height, hilitePaint);

				//画出深色横线
				canvas.drawLine(0, cellHeight*i, width, cellHeight*i, darkPaint);
				canvas.drawLine(0, cellHeight*i+1, width, cellHeight*i+1, hilitePaint);
			}else{
				//画出浅色竖线
				canvas.drawLine(cellWidth*i, 0, cellWidth*i, height, lightPaint);
				canvas.drawLine(cellWidth*i+1, 0, cellWidth*i+1, height, hilitePaint);
				
				//画出浅色横线
				canvas.drawLine(0, cellHeight*i, width, cellHeight*i, lightPaint);
				canvas.drawLine(0, cellHeight*i+1, width, cellHeight*i+1, hilitePaint);
			}
		}

		System.out.println("onDraw end");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//获取用户点击事件
		if(event.getAction() != MotionEvent.ACTION_DOWN){
			return super.onTouchEvent(event);
		}
		
		//屏幕空白的部分不做TouchEvent
		if(event.getY() < height){
			selectedX = (int)(event.getX()/(width/9));
			selectedY = (int)(event.getY()/(height/9));
			
			//判断方格是否为可填
			if(game.isNotOriginalNum(selectedX, selectedY)){
			
				checkedClick = true;
				invalidate();
				
				//开启Tips提示
				//获取不可用对应的数字
				int used[] = game.getUsedTilesByCoor(selectedX, selectedY);
				
				//设置可用或不可用按钮
				aty.setButtonDisabled(used);

					
				//将取得的used的数字缓存
	//			StringBuilder sb = new StringBuilder();
	//			for (int i = 0; i < used.length; i++) {
	//				sb.append(used[i]);
	//			}
				
				//创建自定义AlterDialog
		//		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		//		View layoutView = layoutInflater.inflate(R.layout.dialog, null);
		//		
		//		TextView textview = (TextView) layoutView.findViewById(R.id.textView1);
		//		textview.setText(sb.toString());
		//		
		//		AlertDialog.Builder builder = new Builder(getContext());
		//		builder.setView(layoutView);
		//		
		//		AlertDialog dialog = builder.create();
		//		dialog.show();
				
				
	//			int used[] = game.getUsedTilesByCoor(selectedX, selectedY);
	//			KeyDialog kd = new KeyDialog(getContext(), used, this);
	//			kd.show();
			
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	//调用game方法，并将选取的值set
	public void setSelectedTile(int tile){
		
		//填入正确的数字
		game.setTileIfValid(selectedX, selectedY, tile);
		//重绘
		invalidate();
		//检查是否完成
		checkComplete();
	}
	
	//是否有点击方块
	public boolean getCheckedClick() {

		return checkedClick;
	}
	
	//数独难易水平
	public void setStrPuzzle(String strPuzzle) {
		this.strPuzzle = strPuzzle;
	}
	
	//游戏退出时数据即时保存
	public void savecurrentData(){
		
		SharedPreferences sp = getContext().getSharedPreferences("puzzledata", Context.MODE_PRIVATE);
		
		String originalPuzzle = strPuzzle;
		String currentPuzzle ="";
		
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				
				currentPuzzle += game.getTileString(x, y);
			}
		}
		
		Editor e = sp.edit();
		e.putString("originalPuzzle", originalPuzzle);
		e.putString("currentPuzzle", currentPuzzle);
		e.commit();
		
	}
	
	
	private String originalPuzzle, currentPuzzle;
	//游戏加载时读取数据
	public void loadSavedData(){
		
		//setStrPuzzle(originalPuzzle);
		game.fromPuzzleString(originalPuzzle);
		
		//load后把originalPuzzle赋值给strPuzzle，因为此时并没有传入新的值给strPuzzle
		setStrPuzzle(originalPuzzle);
		
		int curPuzzle[] = new int[currentPuzzle.length()];
		for (int i = 0; i < curPuzzle.length; i++) {
			curPuzzle[i] = currentPuzzle.charAt(i) - '0';
		}
		
//		for (int y = 0; y < 9; y++) {
//			for (int x = 0; x < 9; x++) {
//				
//				game.setTileIfValid(x, y, curPuzzle[y*9 + x]);
//			}
//		}

		game.setLoadSudoku(curPuzzle);
		game.calculateAllUsedTiles();
		
	}
	
	public boolean checkLoadSavedData(){

		SharedPreferences sp = getContext().getSharedPreferences("puzzledata", Context.MODE_PRIVATE);

		originalPuzzle = sp.getString("originalPuzzle", "");
		currentPuzzle = sp.getString("currentPuzzle", "");
		
		if(originalPuzzle.equals(currentPuzzle)){
			
			return false;
		}else{
			
			return true;
		}
	}
	
	//检查游戏是否成功过关
	private void checkComplete(){

		boolean isZero = false;
		boolean redFlag = false;
		//跳出所有循环
		ALL1:
		//判断还没完成：1、只要还有空格子
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				
				if(game.getTile(x, y) == 0){
					isZero = true;
					break ALL1;
				}
			}
		}
		
		ALL2:
		//2、已满但有每行或列或小9宫格上有重复数字
		if(isZero == false){
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					
					//判断填入的数字与所在X/Y轴及小9格中是否有重复的数字
					int usedNumbers[] = game.calculateUsedTiles(x, y);
					int thisNumber = game.getTile(x, y);
					
					for (int tile : usedNumbers) {
						if(tile == thisNumber){
							redFlag = true;
							break ALL2;
						}
					}
				}
			}
		}
		
		System.out.println("isZero-redFlag:" + isZero +" " + redFlag);
		//游戏成功完成
		if(isZero == false && redFlag == false){
			Button btn = (Button) aty.getMainActivity().findViewById(R.id.restart);
			//设置提示信息
			aty.getMainActivity().setMessage("恭喜你顺利通关！再来一局？");
			
			//调用Restart按钮的点击事件
			btn.performClick();
		}
		
	}
}
