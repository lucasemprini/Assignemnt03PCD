package exercize01.model;

import exercize01.model.utility.DebugUtility;
import exercize01.model.utility.RulesUtility;

import java.util.Random;

public class MatrixImpl implements Matrix {

    private int numRows;
    private int numColumns;

    private long aliveCells = 0;

    private boolean cells[][];
    private boolean next[][];

    private static boolean DEBUG = false;

    public MatrixImpl(final int numRows, final int numColumns) {
        this.numColumns = numColumns;
        this.numRows = numRows;
        cells = new boolean[numRows][numColumns];
        next = new boolean[numRows][numColumns];
    }

    @Override
    public void generateRandomMatrix() {
        final Random r = new Random();
        for(int i=0; i<numRows; i++) {
            for(int j=0; j<numColumns; j++) {
                this.cells[i][j] = r.nextBoolean();
                if(cells[i][j]) aliveCells++;
            }
        }
    }

    /**
     * Metodo che controlla che una coordinata x o y sia compresa nella matrice.
     * Da usare nel metodo che controlla i vicini.
     * @param x la coordinata x: riga o colonna.
     * @param isRow true se sto considerando le righe, false altimenti.
     * @return true se è compresa nella matrice, false altrimenti.
     */
    private boolean isInBound(final int x, final boolean isRow) {
        return isRow ? x >= 0 && x < numRows : x >= 0 && x < numColumns;
    }

    /**
     * Metodo che restituisce il numero di vicini vivi per la cella specificata.
     * @param x la coordinata x della cella.
     * @param y la coordinata y della cella.
     * @return il numero dei vicini vivi compresa la cella stessa.
     */
    private int getNumNeighboursAlive(int x, int y) {
        int counterAlive = 0;
        for(int i = x - 1; i <= x + 1; i++) {
            if(isInBound(i, true)) {
                for(int j = y - 1; j <= y + 1; j++) {
                    if(isInBound(j, false)) {
                        if(cells[i][j]) {
                            counterAlive++;
                        }
                    }
                }
            }
        }
        return counterAlive;
    }

    @Override
    public int getNumRows() {
        return this.numRows;
    }

    @Override
    public int getNumColumns() {
        return this.numColumns;
    }

    @Override
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    @Override
    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    @Override
    public void setDebugMode(final boolean isDebug) {
        DEBUG = isDebug;
    }

    @Override
    public long getAliveCells() {
        return this.aliveCells;
    }

    @Override
    public void update(final int startRow, final int stopRow,
                       final int startColumn, final int stopColumn) {
        this.aliveCells = 0;
        for(int i = startRow; i < stopRow; i++) {
            for(int j = startColumn; j < stopColumn; j++) {
                this.updateCellAt(i, j);
                if(cells[i][j]) this.aliveCells++;
                if(DEBUG) {
                    DebugUtility.printCell(getCellAt(i, j), j, stopColumn);
                }
            }
        }
    }

    @Override
    public void updateCellAt(int x, int y) {
        this.next[x][y] = RulesUtility.nextStatus( this.getNumNeighboursAlive(x, y), this.cells[x][y]);
    }

    @Override
    public boolean getCellAt(int x, int y) {
        return this.cells[x][y];
    }


    @Override
    public void computeUpdate() {
        boolean[][] tmp = cells;
        cells = next;
        next = tmp;
    }
}
