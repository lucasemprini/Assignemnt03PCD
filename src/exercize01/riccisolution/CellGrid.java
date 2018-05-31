package exercize01.riccisolution;

import java.util.Random;

public class CellGrid {

	private int w,h;
	private boolean cells[][];
	private boolean next[][];
	
	public CellGrid(int w, int h){
		this.w = w;
		this.h = h;
		cells = new boolean[h][w];
		next = new boolean[h][w]; 
	}
	
	public void swap(){
		boolean[][] aux = cells;
		cells = next;
		next = aux;
	}
	
	public int getWidth(){
		return w;
	}
	
	public int getHeight(){
		return h;
	}
	
	public boolean isAlive(int x, int y){
		return cells[y][x];
	}
	
	
	public boolean computeNextCellState(int x, int y){
		int nAlives = 0;
		int xPrev = x == 0 ? w - 1 : x - 1;
		int xNext = (x + 1) % w;
		int yPrev = y == 0 ? h - 1 : y - 1;
		int yNext = (y + 1) % h;
		if (cells[yPrev][xPrev]){
			nAlives++;
		}
		if (cells[yPrev][x]){
			nAlives++;
		}
		if (cells[yPrev][xNext]){
			nAlives++;
		}
		if (cells[y][xPrev]){
			nAlives++;
		}
		if (cells[y][xNext]){
			nAlives++;
		}
		if (cells[yNext][xPrev]){
			nAlives++;
		}
		if (cells[yNext][x]){
			nAlives++;
		}
		if (cells[yNext][xNext]){
			nAlives++;
		}
		
		if (cells[y][x]){
			next[y][x] = nAlives > 1 && nAlives < 4;
		} else {
			next[y][x] = nAlives == 3;
		}		
		return next[y][x];
	}
	
	public void initRandom(int n){
		for (int i = 0; i < w; i++){
			for (int j = 0; j < h; j++){
				cells[j][i] = false;
			}
		}
		
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < n; i++){
			int x = rand.nextInt(w);
			int y = rand.nextInt(h);
			cells[y][x] = true;
		}
	}
	
	public void drawGlider(int x, int y){
		cells[y][x] = false;
		cells[y][(x+1)%w] = true;
		cells[y][(x+2)%w] = false;
		cells[(y+1)%h][x] = false;
		cells[(y+1)%h][(x+1)%w] = false;
		cells[(y+1)%h][(x+2)%w] = true;
		cells[(y+2)%h][x] = true;
		cells[(y+2)%h][(x+1)%w] = true;
		cells[(y+2)%h][(x+2)%w] = true;
	}

	public void drawBlock(int x, int y){
		drawConf(x,y,new boolean[][]
				{{false, false, false, false},
			     {false, true, true, false},
			     {false, true, true, false},
			     {false, false, false, false}});
	}
	
	private void drawConf(int x, int y, boolean[][] conf){
		for (int i = 0; i < conf.length; i++){
			for (int j = 0; j < conf[i].length; j++){
				cells[(y+i) % h][(x+j) % w] = conf[i][j];
			}
		}
	}
}
