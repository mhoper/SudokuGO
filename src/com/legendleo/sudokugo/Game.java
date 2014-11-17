package com.legendleo.sudokugo;

public class Game {

	public Game(){
		System.out.println("game���캯��ִ��");
		
	}
	
	private int[] sudoku = new int[9*9];
	
	//��¼ԭʼ����
	private int[] sudo;
	
	//�洢��Ԫ���в����õ�����
	private int[][][] used = new int[9][9][];
	
	//������������Ӧ��ֵ
	public int getTile(int x, int y){
		return sudoku[y*9 + x];
	}
	
	//ȥ��Ϊ0��ֵ
	public String getTileString(int x, int y){
		int v = getTile(x, y);
		return String.valueOf(v);
	}
	
	//��string������ȡ����
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
	
	
	//�жϵ���ķ����Ƿ�����дԭʼ����
	public boolean isNotOriginalNum(int x, int y){
		if(sudo[y*9 + x] == 0){
			
			return true;
		}else{
			
			return false;
		}
	}
	
	//�������������ϲ����õ�����
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
	
	
	//����ĳ�������ϲ����õ�����
	public int[] calculateUsedTiles(int x, int y){
		int[] c = new int[9];
		
		for (int i = 0; i < 9; i++) {
			//y�᷽���ϵĲ���������
			if(i != y){
				int t = getTile(x, i);
				if(t != 0){
					c[t-1] = t;
				}
			}
			
			//x�᷽���ϵĲ���������
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

	//�ж������ֵ�Ƿ�Ϊ��ʹ�õ�ֵ
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
	
	//����ѡ�е�ֵ������Ŀ���
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
