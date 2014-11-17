package com.legendleo.sudokugo;

import android.content.Context;

public class RandomPuzzle {
	
	private Context context;
	private String[] items;
	
	public RandomPuzzle(Context c){
		
		this.context = c;
	}
	
	//根据简单或困难获取随机数独数据
	public String getRandomPuzzle(boolean iseasy){
		//true为简单，false为困难
		if(iseasy){
			System.out.println("Easy:");
			items = context.getResources().getStringArray(R.array.easy_puzzles);
		}else{

			System.out.println("Hard:");
			items = context.getResources().getStringArray(R.array.hard_puzzles);
		}
		
		//随机生成一个0到30之间的整型数据
		int rd = (int)(Math.random()*items.length);
		String strPuzzle = items[rd];
		
		System.out.println(strPuzzle);
		//返回随机的数独数据
		return strPuzzle;
		
	}
}
