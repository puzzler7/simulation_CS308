package cellsociety.simulation.cell;

import java.util.Queue;
import java.util.Random;


/**
 * @author Maverick Chung, mc608
 *
 * Purpose: Represents fire in a forest. Fire cells can set fire to adjacent tree cells, turning them into
 * empty cells. Fire burns out after 1 timestep, becoming an empty cell.
 *
 * Assumptions: See Cell. Additionally, assumes states are 0-2.
 *
 * Dependencies: Cell
 */
public class FireCell extends Cell {

  public static final String PROB_CATCH = "probCatch";
  public static final String PROB_GROW = "probGrow";

  public FireCell() {
    super();
    defaultEdge = 0;
    setMask(new int[]{1, 0, 1, 0, 1, 0, 1, 0});
  }

  public FireCell(double probCatch) {
    this();
    setParam(PROB_CATCH, probCatch);
  }

  @Override
  protected void setParams() {
    params = new String[]{PROB_CATCH};
  }

  @Override
  protected void planUpdate(Cell[] neighbors, Queue<Cell> emptyQueue) {
    if (pointingUp) {
      neighbors = rotateNeighbors(neighbors);
    }
    if (getState() == 0) {
      nextState = 0;
    }
    if (getState() == 2) {
      nextState = 0;
    }
    if (getState() == 1) {
      boolean canBurn = false;
      for (int i = 0; i < neighbors.length; i++) {
        if (neighbors[i].getState() == 2) {
          canBurn = true;
        }
      }
      Random rand = new Random();
      if (canBurn && rand.nextDouble() < getParam(PROB_CATCH)) {
        nextState = 2;
      } else {
        nextState = 1;
      }
    }
  }
}
