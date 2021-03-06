package cellsociety.visualizer;

import cellsociety.simulation.grid.Grid;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

/**
 * @author: axo
 */
public class TriVisualizer extends Visualizer {

  /**
   * Constructor for Triangle Visualizer. Calls parent Visualizer constructor with the same grid object
   * Same assumptions as parent Visualizer class.
   * @param grid
   */
  public TriVisualizer(Grid grid) {
    super(grid);
  }

  /**
   * Extended form of InstantiateCellGrid. Creates Triangles with on-click actions and rendered colors and places them inside an AnchorPane
   * @return an AnchorPane containing the desired size and configuration of Triangles to be rendered in the scene.
   */
  @Override
  public Node instantiateCellGrid() {
    AnchorPane anchorPane = new AnchorPane();
    cellGrid = new ArrayList<ArrayList<Shape>>();
    Color[][] colorgrid = getColorGrid();
    double horizEdge;
    if (colorgrid[0].length % 2 == 0) {
      horizEdge = SIZE / (colorgrid[0].length / 2 + .5);
    } else {
      horizEdge = SIZE / (colorgrid[0].length / 2 + 1);
    }
    double height = SIZE / (colorgrid.length);
    double yCoord = 0.0;
    double xCoord = 0.0;
    for (int i = 0; i < colorgrid.length; i++) {
      cellGrid.add(new ArrayList<Shape>());
      for (int j = 0; j < colorgrid[i].length; j++) {
        Polygon tri = new Polygon();
        int start = j % 2 + i % 2;
        if (start % 2 == 0) {
          tri.getPoints().addAll(new Double[]{
              xCoord + horizEdge / 2, yCoord,
              xCoord + horizEdge, yCoord + height,
              xCoord, yCoord + height,
          });
        } else {
          tri.getPoints().addAll(new Double[]{
              xCoord, yCoord,
              xCoord + horizEdge, yCoord,
              xCoord + horizEdge / 2, yCoord + height,
          });
        }
        tri.setFill(colorgrid[i][j]);
        if(gridLines) {
          tri.setStrokeType(StrokeType.INSIDE);
          tri.setStroke(Color.GRAY);
          tri.setStrokeWidth(.5);
        }
        final int r = i; //FIXME extract method into abstract class
        final int c = j;
        tri.setOnMouseClicked(e -> {
          myGrid.incrementCellState(r, c);
          drawGrid();
        });
        cellGrid.get(i).add(tri);
        anchorPane.getChildren().add(tri);
        xCoord += horizEdge / 2;
      }
      yCoord += height;
      xCoord = 0;
    }
    return anchorPane;
  }
}
