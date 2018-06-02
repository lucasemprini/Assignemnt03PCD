package exercize01.model.utility;

/**
 * Utility-Class per modellare le regole del gioco.
 */
public final class RulesUtility {

    /**
     * Costruttore privato e vuoto.
     */
    private RulesUtility(){}

    /**
     * Metodo di utility che restituisce il prossimo stato da assegnare a una cella in base al
     * numero dei suoi vicini vivi e al suo stato corrente.
     * Metodo che incorpora la regola principale del Gioco.
     *
     * Se il numero di vicini vivi (compresa la cella considerata) è:
     * - 0, 1, 2 : la cella muore / resta morta.
     * - 3 : la cella vive / resta viva.
     * - 4 : la cella resta viva se era viva e resta morta se era morta.
     * - da 5 in su : la cella muore / resta morta.
     *
     * @param aliveNeighbours il numero di vicini vivi compresa la cella considerata.
     * @param myStatus lo stato corrente della cella considerata.
     * @return il prossimo stato della cella considerata.
     */
    public static boolean nextStatus(final int aliveNeighbours, final boolean myStatus) {
        if (myStatus) {
            switch (aliveNeighbours) {
                case 0: return false;
                case 1: return false;
                case 2: return true;
                case 3: return true;
                case 4: return false;
                default: return false;
            }
        } else {
            switch (aliveNeighbours) {
                case 3: return true;
                default: return false;
            }
        }

        /*
        switch (aliveNeighbours) {
            case 0: return false;
            case 1: return false;
            case 2: return false;
            case 3: return true;
            case 4: return myStatus;
            default: return false;
        }*/
    }
}
