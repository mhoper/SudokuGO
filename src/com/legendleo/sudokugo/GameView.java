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

	//������������Ŀ����߶�
	private float width;
	private float height;
	
	//��������ˮƽ
	private String strPuzzle = Config.STR_EASY;
	private Game game = new Game();
	
	//ѡ�еĸ��ӵĶ�Ӧ1-9����
	private int selectedX, selectedY;
	

	//�������
	private boolean checkedClick; 
	private Button key[] = new Button[9];

	private MainActivity aty = MainActivity.getMainActivity();
	
	public GameView(Context context, AttributeSet attri) {
		super(context, attri);
		System.out.println("gamaview�Ĺ��캯����ִ��");
	}

	//���¿�ʼ��Ϸ
	public void startNewGame(){

		//Time��0
		aty.setReclen(0);
		
		//�����������״̬
		this.checkedClick = false;
		
		//��1-9�������ְ�ť����
		aty.setButtonEnabled();
		
		//����newһ����ʼ
		game.fromPuzzleString(strPuzzle);
		//game.setOriginal();
		game.calculateAllUsedTiles();
		
		//�ػ�
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ��ȡ��Ļ��ȡ��߶�
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		int screenwidth = Math.min(dm.widthPixels, dm.heightPixels);
		//���ø�View���
		setMeasuredDimension(screenwidth, screenwidth);
		System.out.println("onMeasure");
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	
		//ȡ��View��ߣ����øߵ��ڿ�����һ��������
		//activity_main��padding=2,�����8��Ϊ�������ұߡ����±ߵĴֱ߿���ʾ����
		this.width = w - 8;
		this.height = h - 8;
		System.out.println("onSizeChanged");

	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		System.out.println("onDraw start");
		
		//cell���
		float cellWidth = (width)/9;
		float cellHeight = (height)/9;
		
		//��������
		Paint backgroundPaint = new Paint();
		backgroundPaint.setColor(getResources().getColor(R.color.sudoku_background));
		canvas.drawRect(0, 0, width, height, backgroundPaint);

		//ԭʼ�����ֱ���ɫ
		Paint originalTileBackgroundPaint = new Paint();
		originalTileBackgroundPaint.setColor(getResources().getColor(R.color.originalTileBackgroundPaint));
		
		//��д�����ֱ���ɫ
		Paint tileBackgroundPaint = new Paint();
		tileBackgroundPaint.setColor(getResources().getColor(R.color.tileBackgroundPaint));
		
		//�������,���Ƹ��ӱ���ɫ
		if(checkedClick == true){
			
			canvas.drawRect(cellWidth*selectedX, cellHeight*selectedY, cellWidth*(selectedX + 1), cellHeight*(selectedY + 1), tileBackgroundPaint);
		}
		
		
		//��ȷ���ֻ���
		Paint numberPaint = new Paint();
		numberPaint.setColor(Color.BLACK);
		numberPaint.setTextSize(cellHeight*0.75f);
		numberPaint.setStyle(Paint.Style.STROKE);
		numberPaint.setTextAlign(Paint.Align.CENTER);
		numberPaint.setAntiAlias(true);
		

		//�������ֻ���
		Paint wrongNumberPaint = new Paint();
		wrongNumberPaint.setColor(getResources().getColor(R.color.wrongNumber));
		wrongNumberPaint.setTextSize(cellHeight*0.75f);
		wrongNumberPaint.setStyle(Paint.Style.STROKE);
		wrongNumberPaint.setTextAlign(Paint.Align.CENTER);
		wrongNumberPaint.setAntiAlias(true);
		
		
		//�������ֵ����λ��
		FontMetrics fm = numberPaint.getFontMetrics();
		float fontx = cellWidth/2;
		float fonty = cellHeight/2 - (fm.ascent + fm.descent)/2;
		
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				String o = this.game.getOriginalTileString(x, y);
				String s = this.game.getTileString(x, y);
				//���Ʋ�Ϊ0�ĸ���
				if(Integer.valueOf(o) != 0){
					
					//��ԭʼ������ӱ���ɫ
					canvas.drawRect(cellWidth*x, cellHeight*y, cellWidth*(x + 1), cellHeight*(y + 1), originalTileBackgroundPaint);
					canvas.drawText(o, cellWidth*x+ fontx, cellHeight*y + fonty, numberPaint);
				}else{
					if(s != o){

						//�ж����������������X/Y�ἰС9�����Ƿ����ظ�������
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
							//����ǰѡ�п��д����������ɫ��ɺ�ɫ
							canvas.drawText(s, cellWidth*x+ fontx, cellHeight*y + fonty, wrongNumberPaint);
						}else{
							
							canvas.drawText(s, cellWidth*x+ fontx, cellHeight*y + fonty, numberPaint);
						}

						
					}
					
				}
			}
		}
		

		//��������ٻ�������
		//ǳɫ��
		Paint lightPaint = new Paint();
		lightPaint.setColor(getResources().getColor(R.color.sudoku_light));
		
		//��ɫ��
		Paint darkPaint = new Paint();
		darkPaint.setColor(getResources().getColor(R.color.sudoku_dark));
		
		Paint hilitePaint = new Paint();
		hilitePaint.setColor(getResources().getColor(R.color.sudoku_hilite));
		
		
		//���ɣ����ڻ������ߣ�һ��һ�ߣ��������а��̵�Ч��
		for (int i = 0; i <= 9; i++) {
			if(i%3 == 0){
				//������ɫ����
				canvas.drawLine(cellWidth*i, 0, cellWidth*i, height, darkPaint);
				canvas.drawLine(cellWidth*i+1, 0, cellWidth*i+1, height, hilitePaint);

				//������ɫ����
				canvas.drawLine(0, cellHeight*i, width, cellHeight*i, darkPaint);
				canvas.drawLine(0, cellHeight*i+1, width, cellHeight*i+1, hilitePaint);
			}else{
				//����ǳɫ����
				canvas.drawLine(cellWidth*i, 0, cellWidth*i, height, lightPaint);
				canvas.drawLine(cellWidth*i+1, 0, cellWidth*i+1, height, hilitePaint);
				
				//����ǳɫ����
				canvas.drawLine(0, cellHeight*i, width, cellHeight*i, lightPaint);
				canvas.drawLine(0, cellHeight*i+1, width, cellHeight*i+1, hilitePaint);
			}
		}

		System.out.println("onDraw end");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//��ȡ�û�����¼�
		if(event.getAction() != MotionEvent.ACTION_DOWN){
			return super.onTouchEvent(event);
		}
		
		//��Ļ�հ׵Ĳ��ֲ���TouchEvent
		if(event.getY() < height){
			selectedX = (int)(event.getX()/(width/9));
			selectedY = (int)(event.getY()/(height/9));
			
			//�жϷ����Ƿ�Ϊ����
			if(game.isNotOriginalNum(selectedX, selectedY)){
			
				checkedClick = true;
				invalidate();
				
				//����Tips��ʾ
				//��ȡ�����ö�Ӧ������
				int used[] = game.getUsedTilesByCoor(selectedX, selectedY);
				
				//���ÿ��û򲻿��ð�ť
				aty.setButtonDisabled(used);

					
				//��ȡ�õ�used�����ֻ���
	//			StringBuilder sb = new StringBuilder();
	//			for (int i = 0; i < used.length; i++) {
	//				sb.append(used[i]);
	//			}
				
				//�����Զ���AlterDialog
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
	
	//����game����������ѡȡ��ֵset
	public void setSelectedTile(int tile){
		
		//������ȷ������
		game.setTileIfValid(selectedX, selectedY, tile);
		//�ػ�
		invalidate();
		//����Ƿ����
		checkComplete();
	}
	
	//�Ƿ��е������
	public boolean getCheckedClick() {

		return checkedClick;
	}
	
	//��������ˮƽ
	public void setStrPuzzle(String strPuzzle) {
		this.strPuzzle = strPuzzle;
	}
	
	//��Ϸ�˳�ʱ���ݼ�ʱ����
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
	//��Ϸ����ʱ��ȡ����
	public void loadSavedData(){
		
		//setStrPuzzle(originalPuzzle);
		game.fromPuzzleString(originalPuzzle);
		
		//load���originalPuzzle��ֵ��strPuzzle����Ϊ��ʱ��û�д����µ�ֵ��strPuzzle
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
	
	//�����Ϸ�Ƿ�ɹ�����
	private void checkComplete(){

		boolean isZero = false;
		boolean redFlag = false;
		//��������ѭ��
		ALL1:
		//�жϻ�û��ɣ�1��ֻҪ���пո���
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				
				if(game.getTile(x, y) == 0){
					isZero = true;
					break ALL1;
				}
			}
		}
		
		ALL2:
		//2����������ÿ�л��л�С9���������ظ�����
		if(isZero == false){
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					
					//�ж����������������X/Y�ἰС9�����Ƿ����ظ�������
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
		//��Ϸ�ɹ����
		if(isZero == false && redFlag == false){
			Button btn = (Button) aty.getMainActivity().findViewById(R.id.restart);
			//������ʾ��Ϣ
			aty.getMainActivity().setMessage("��ϲ��˳��ͨ�أ�����һ�֣�");
			
			//����Restart��ť�ĵ���¼�
			btn.performClick();
		}
		
	}
}
