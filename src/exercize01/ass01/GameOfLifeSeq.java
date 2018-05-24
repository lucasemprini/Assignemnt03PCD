package exercize01.ass01;

/**
 * Versione sequenziale (controller/master sequenziali)
 * 
 * @author aricci
 *
 */
public class GameOfLifeSeq {
	public static void main(String[] args) {
		
		int w = 1000;
		int h = 1000;
		int frameRate = 100;
		
		CellGrid grid = new CellGrid(w,h);
		grid.initRandom(250000);
		
		/*
		grid.drawGlider(10, 10);
		grid.drawGlider(100, 50);
		grid.drawBlock(30, 70);
		*/
		
		View view = new View(800, 600, grid);
		ControllerSeq controller = new ControllerSeq(grid, view, frameRate);
		view.addListener(controller);
		view.setVisible(true);
	}

}
