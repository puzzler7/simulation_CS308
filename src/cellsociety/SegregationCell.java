package cellsociety;

import javafx.scene.paint.Color;

import java.util.Queue;

public class SegregationCell extends Cell {
    public SegregationCell() {
        super();
        defaultEdge = 0;
        setStateColor(0, Color.WHITE); //Empty
        setStateColor(1, Color.RED); //Red
        setStateColor(2, Color.BLUE); //Blue
    }

    public SegregationCell(double happinessThresh) {
        this();
        setParam("happinessThresh", happinessThresh);
    }

    @Override
    void planUpdate(int[] neighbors, Queue<Cell> emptyQueue) {
        if (getState() != 0) {
            if (!happy(neighbors)){
                Cell empty = emptyQueue.remove();
                empty.setNextState(getState());
                setNextState(0);
            }
        }
        if (nextState == -1) {
            nextState = getState();
        }
    }

    private boolean happy(int[] neighbors) {
        double total = 0;
        double same = 0;
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i]>0) total++;
            if (neighbors[i]==getState()) same++;
        }
        return (same/total)>getParam("happinessThresh");
    }
}
