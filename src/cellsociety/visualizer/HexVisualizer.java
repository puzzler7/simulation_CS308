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
public class HexVisualizer extends Visualizer {

  /**
   * Constructor for Hexagon Visualizer. Calls parent Visualizer constructor with the same grid object
   * Same assumptions as parent Visualizer class.
   * @param grid
   */
  public HexVisualizer(Grid grid) {
    super(grid);
  }

  /**
   * Extended form of InstantiateCellGrid. Creates Hexagons with on-click actions and rendered colors and places them inside an AnchorPane
   * @return an AnchorPane containing the desired size and configuration of Hexagons to be rendered in the scene.
   */
  @Override
  public Node instantiateCellGrid() {
    AnchorPane anchorPane = new AnchorPane();
    cellGrid = new ArrayList<ArrayList<Shape>>();
    Color[][] colorgrid = getColorGrid();
    double horizEdge = SIZE / (colorgrid[0].length * 1.5);
    double height = SIZE / (colorgrid.length + .5);
    double yCoord = 0.0;
    double xCoord = 0.0;
    for (int i = 0; i < colorgrid.length; i++) {
      cellGrid.add(new ArrayList<Shape>());
      for (int j = 0; j < colorgrid[i].length; j++) {
        Polygon hex = new Polygon();
        if (j % 2 == 0) {
          hex.getPoints().addAll(new Double[]{
              xCoord + horizEdge / 2, yCoord + height / 2,
              xCoord + horizEdge * 1.5, yCoord + height / 2,
              xCoord + horizEdge * 2, yCoord + height,
              xCoord + horizEdge * 1.5, yCoord + height * 1.5,
              xCoord + horizEdge / 2, yCoord + height * 1.5,
              xCoord, yCoord + height
          });
        } else {
          hex.getPoints().addAll(new Double[]{
              xCoord + horizEdge / 2, yCoord,
              xCoord + horizEdge * 1.5, yCoord,
              xCoord + horizEdge * 2, yCoord + height / 2,
              xCoord + horizEdge * 1.5, yCoord + height,
              xCoord + horizEdge / 2, yCoord + height,
              xCoord, yCoord + height / 2
          });
        }
        hex.setFill(colorgrid[i][j]);
        if(gridLines) {
          hex.setStrokeType(StrokeType.INSIDE);
          hex.setStroke(Color.GRAY);
          hex.setStrokeWidth(.5);
        }
        final int r = i; //FIXME extract method into abstract class
        final int c = j;
        hex.setOnMouseClicked(e -> {
          myGrid.incrementCellState(r, c);
          drawGrid();
        });
        cellGrid.get(i).add(hex);
        anchorPane.getChildren().add(hex);
        xCoord += horizEdge * 1.5;
      }
      yCoord += height;
      xCoord = 0;
    }
    return anchorPane;
  }
}
