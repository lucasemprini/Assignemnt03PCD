package exercize01.model.messages;

public class CallWorkerMsg {

    private final int row;
    private final int worker;


    public CallWorkerMsg(final int row, final int worker) {
        this.row = row;
        this.worker = worker;
    }

    public int getRow() {
        return row;
    }

    public int getWorker() {
        return worker;
    }


}
