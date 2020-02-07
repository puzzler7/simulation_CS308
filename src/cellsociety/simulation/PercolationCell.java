package cellsociety.simulation;

import java.util.LinkedList;

public class PercolationCell extends Cell {

  public PercolationCell() {
    super();
    addStates(new int[]{0, 1, 2});
    defaultEdge = 3;
  }

  @Override
  void planUpdate(Cell[] neighbors, LinkedList<Cell> emptyQueue) {
    if (neighbors[0].getState() == 3) {
      neighbors[0].setState(2); //this is okay because cells with state 3 are dummy cells
    }
    if (getState() == 1) {
      for (int i = 0; i < neighbors.length; i += 1) {
        if (neighbors[i].getState() == 2) {
          nextState = 2;
          return;
        }
      }
      nextState = 1;
    } else {
      if (state == 0) {
        nextState = 0;
      }
      if (state == 2) {
        nextState = 2;
      }
    }
  }
}