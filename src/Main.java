import exercize01.controller.Controller;
import exercize01.model.Matrix;
import exercize01.model.MatrixImpl;
import exercize01.view.View;

import javax.swing.*;

public class Main {
    public static void main(String... args) {

        int row = 5000;
        int col = 5000;

        Matrix matrix = new MatrixImpl(row, col);
        matrix.generateRandomMatrix();

		/*
		grid.drawGlider(10, 10);
		grid.drawGlider(100, 50);
		grid.drawBlock(30, 70);
		*/
        SwingUtilities.invokeLater(()->{
            View view = new View(800, 600, matrix);
            Controller controller = new Controller(matrix, view);
            view.addListener(controller);
            view.setVisible(true);
        });
    }
}
