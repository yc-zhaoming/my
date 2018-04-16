package com.ming.L2048;

public class BackArray {
		
		private int arrays[][]=new int[4][4];
		
		public BackArray() {
			
		}
		public int[][] getArrays() {
			return arrays;
		}
		public void setArrays(int[][] arrays) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					this.arrays[i][j]=arrays[i][j];
				}
			}
		}
		

}
