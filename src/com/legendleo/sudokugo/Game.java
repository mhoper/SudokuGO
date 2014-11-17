package com.legendleo.sudokugo;

public class Game {

	public Game(){
		System.out.println("game构造函数执行");
		
	}
	
	private int[] sudoku = new int[9*9];
	
	//记录原始数字
	private int[] sudo;
	
	//存储单元格中不可用的数字
	private int[][][] used = new int[9][9][];
	
	//根据坐标计算对应的值
	public int getTile(int x, int y){
		return sudoku[y*9 + x];
	}
	
	//去掉为0的值
	public String getTileString(int x, int y){
		int v = getTile(x, y);
		return String.valueOf(v);
	}
	
	//将string中数字取出来
	public void fromPuzzleString(String str){
		sudo = new int[str.length()];
		for (int i = 0; i < sudo.length; i++) {
			sudo[i] = str.charAt(i) - '0';
		}
		
		System.arraycopy(sudo, 0, sudoku, 0, sudoku.length);
	}
	

	public int getOriginalTile(int x, int y){
		
		return sudo[y*9 + x];
	}
	
	public String getOriginalTileString(int x, int y){
		int v = getOriginalTile(x, y);
		return String.valueOf(v);
	}
	
	
	//判断点击的方格是否已填写原始数字
	public boolean isNotOriginalNum(int x, int y){
		if(sudo[y*9 + x] == 0){
			
			return true;
		}else{
			
			return false;
		}
	}
	
	//计算所有坐标上不可用的数字
	public void calculateAllUsedTiles(){
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				used[x][y] = calculateUsedTiles(x, y);
			}
		}
	}
	
	public int[] getUsedTilesByCoor(int x, int y){
		return used[x][y];
	}
	
	
	//计算某个坐标上不可用的数字
	public int[] calculateUsedTiles(int x, int y){
		int[] c = new int[9];
		
		for (int i = 0; i < 9; i++) {
			//y轴方向上的不可用数字
			if(i != y){
				int t = getTile(x, i);
				if(t != 0){
					c[t-1] = t;
				}
			}
			
			//x轴方向上的不可用数字
			if(i != x){
				int t = getTile(i, y);
				if(t != 0){
					c[t-1] = t;
				}
			}
		}
		
		int startX = (x/3)*3;
		int startY = (y/3)*3;
		for (int i = startX; i < startX + 3; i++) {
			for (int j = startY; j < startY + 3; j++) {
				if(i != x && j != y){
					int t = getTile(i, j);
					if(t != 0){
						c[t-1] = t;
					}
				}
			}
		}
		
		//compress
		int nused = 0;
		for (int i : c) {
			if(i != 0)
				nused++;
		}
		int[] c1 = new int[nused];
		nused = 0;
		for (int i : c) {
			if(i != 0)
				c1[nused++] = i;
		}
		
		return c1;
	}

	//判断填入的值是否为已使用的值
	public boolean setTileIfValid(int x, int y, int value){
		int tiles[] = getUsedTilesByCoor(x, y);
		boolean bl = true;
		
		for(int tile:tiles){
			if(tile == value){
				bl = false;
				break;
			}
		}

		setTile(x, y, value);
		calculateAllUsedTiles();
		return bl;
		
	}
	
	//设置选中的值到点击的框中
	private void setTile(int x, int y, int value){
		sudoku[y*9 + x] = value;
	}
	
//	protected void setOriginal(){
//		for (int i = 0; i < sudoku.length; i++) {
//			sudoku[i] = sudo[i];
//		}
//	}
	
	protected void setLoadSudoku(int su[]){
		for (int i = 0; i < su.length; i++) {
			sudoku[i] = su[i];
		}
	}
}
