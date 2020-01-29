package cellsociety;

import javafx.scene.paint.Color;

import java.util.*;

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
    void planUpdate(Cell[] neighbors, Queue<Cell> emptyQueue) {
        Collections.shuffle((LinkedList<Cell>)emptyQueue);
        if (getState() != 0) {
            if (!happy(neighbors)){
                Cell empty = emptyQueue.remove();
                empty.nextState = state;
                nextState = 0;
                emptyQueue.add(this);
                return;
            }
            else {
                nextState = state;
                return;
            }
        }
        if (nextState == -1) {
            nextState = 0;
        }
    }

    private boolean happy(Cell[] neighbors) {
        double total = 0;
        double same = 0;
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i].getState()>0) total++;
            if (neighbors[i].getState()==getState()) same++;
        }
        return total == 0 || (same/total)>getParam("happinessThresh");
    }
}
