package cellsociety.simulation.cell;

import java.util.Queue;
import java.util.Random;

public class RPSCell extends Cell {

  public static final String RPS_THRESHHOLD = "RPSThreshhold";
  public static final String RANDOM_THRESHHOLD = "randomThreshhold";

  public RPSCell() {
    super();
    defaultEdge = 0;
    setParam(RPS_THRESHHOLD, 3.0);
    setParam(RANDOM_THRESHHOLD, 0.0);
  }

  protected void setParams() {
    params = new String[]{RPS_THRESHHOLD, RANDOM_THRESHHOLD};
  }

  @Override
  void planUpdate(Cell[] neighbors, Queue<Cell> cellQueue) {
    int max = state;
    for (Cell cell : neighbors) {
      max = Math.max(max, cell.state);
    }
    int[] counts = new int[max + 1];
    for (Cell cell : neighbors) {
      counts[cell.state]++;
    }
    max = 0;
    int newVal = -1;
    Random rand = new Random();
    for (int i = 0; i < counts.length; i++) {
      if (counts[i] == max && i != 0 && rand.nextDouble() > 0.5) {
        max = counts[i];
        newVal = i;
      } else if (counts[i] > max && i != 0) {
        max = counts[i];
        newVal = i;
      }
    }

    double thresh = getParam(RPS_THRESHHOLD);
    thresh += (rand.nextDouble() - .5) * 2 * getParam(RANDOM_THRESHHOLD);
    if (newVal != -1 && (max >= thresh || state == 0)) {
      nextState = newVal;
    } else {
      nextState = state;
    }
  }
}