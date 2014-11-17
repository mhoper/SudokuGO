package com.legendleo.sudokugo;

import android.content.Context;

public class RandomPuzzle {
	
	private Context context;
	private String[] items;
	
	public RandomPuzzle(Context c){
		
		this.context = c;
	}
	
	//���ݼ򵥻����ѻ�ȡ�����������
	public String getRandomPuzzle(boolean iseasy){
		//trueΪ�򵥣�falseΪ����
		if(iseasy){
			System.out.println("Easy:");
			items = context.getResources().getStringArray(R.array.easy_puzzles);
		}else{

			System.out.println("Hard:");
			items = context.getResources().getStringArray(R.array.hard_puzzles);
		}
		
		//�������һ��0��30֮�����������
		int rd = (int)(Math.random()*items.length);
		String strPuzzle = items[rd];
		
		System.out.println(strPuzzle);
		//�����������������
		return strPuzzle;
		
	}
}
