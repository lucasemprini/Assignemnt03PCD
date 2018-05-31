package exercize01.riccisolution;

import javax.swing.*;

/**
 * PCD Assignment 01 - Game of Life 
 * 
 * Concurrent version. 
 * 
 * @author aricci
 *
 */
public class GameOfLife {
	public static void main(String[] args) {
		
		int w = 5000;
		int h = 5000;
		int frameRate = 100;
		
		CellGrid grid = new CellGrid(w,h);
		grid.initRandom(2500000);
		
		/*
		grid.drawGlider(10, 10);
		grid.drawGlider(100, 50);
		grid.drawBlock(30, 70);
		*/
		SwingUtilities.invokeLater(()->{
			View view = new View(800, 600, grid);
			Controller controller = new Controller(grid, view, frameRate);
			view.addListener(controller);
			view.setVisible(true);
		});
	}

}
