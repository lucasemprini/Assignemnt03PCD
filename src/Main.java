import exercize01.controller.Controller;
import exercize01.model.Matrix;
import exercize01.model.MatrixImpl;
import exercize01.view.View;

import javax.swing.*;

public class Main {


    private static final int NUMROWS = 5000;
    private static final int NUMCOLUMNS = 5000;
    private static final int VIEW_HEIGTH = 600;
    private static final int VIEW_WIDTH = 800;

    private static final Matrix GAME_MATRIX = new MatrixImpl(NUMROWS, NUMCOLUMNS);

    public static void main(String... args) {
        GAME_MATRIX.generateRandomMatrix();

		/*
		grid.drawGlider(10, 10);
		grid.drawGlider(100, 50);
		grid.drawBlock(30, 70);
		*/
        SwingUtilities.invokeLater(()->{
            final View view = new View(VIEW_WIDTH, VIEW_HEIGTH, GAME_MATRIX);
            final Controller controller = new Controller(GAME_MATRIX, view);
            view.addListener(controller);
            view.setVisible(true);
        });
    }
}
