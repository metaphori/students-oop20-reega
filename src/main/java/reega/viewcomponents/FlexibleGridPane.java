package reega.viewcomponents;

import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * {@link GridPane} that contains a fixed number of rows or columns and a variable number of columns or rows
 * <para>If columns are fixed than rows are variable</para>
 * <para>If rows are fixed than columns are variable</para>
 * <para>The size of each variable is a percentage equal to the floor of the size of {@link #getChildren()} divided by the number of rows/columns</para>
 * <para>The children are positioned based on the constraint</para>
 * <para>If columns are fixed than all the columns needs to be filled before a new row is created</para>
 * <para>If rows are fixed than all the rows needs to be filled before a new column is created</para>
 */
public class FlexibleGridPane extends GridPane {

    private IntegerProperty fixedColumnsNumber;
    private IntegerProperty fixedRowsNumber;

    /**
     * Instance initializer
     */
    {
        //Add a listener that resets columns/rows whenever the children collection is modified
        getChildren().addListener((ListChangeListener<Node>) c -> {
           if (fixedColumnsNumberProperty().isNotEqualTo(0).get()){
               resetAllChildrenByCol(fixedColumnsNumberProperty().get());
           }
           else if (fixedRowsNumberProperty().isNotEqualTo(0).get()) {
               resetAllChildrenByRow(fixedRowsNumberProperty().get());
           }
        });
    }

    /**
     * Set the fixed columns number
     * @param newValue new value for the number of columns
     * @throws IllegalStateException if {@link #fixedRowsNumberProperty()} is not equal to 0
     */
    public final void setFixedColumnsNumber(int newValue) {
        if (fixedRowsNumberProperty().isNotEqualTo(0).get()) {
            throw new IllegalStateException("You cannot set a constraint on rows and columns at the same time");
        }
        this.getColumnConstraints().clear();
        if (newValue == 0){
            fixedColumnsNumberProperty().set(newValue);
            return;
        }
        //Find the size percentage of the columns
        double percentValue = 100.0/(newValue);
        this.getColumnConstraints().addAll(IntStream.range(0,newValue).mapToObj(elem -> {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(percentValue);
            return cc;
        }).collect(Collectors.toList()));
        fixedColumnsNumberProperty().set(newValue);
        resetAllChildrenByCol(newValue);
    }

    /**
     * Set the fixed rows number
     * @param newValue new value for the number of rows
     * @throws IllegalStateException if {@link #fixedColumnsNumberProperty()} is not equal to 0
     */
    public final void setFixedRowsNumber(int newValue) {
        if (fixedColumnsNumberProperty().isNotEqualTo(0).get()) {
            throw new IllegalStateException("You cannot set a constraint on rows and columns at the same time");
        }
        this.getRowConstraints().clear();
        if (newValue == 0) {
            fixedRowsNumberProperty().set(newValue);
            return;
        }
        //Find the size percentage of the rows
        double percentValue = 100.0/(newValue);
        this.getRowConstraints().addAll(IntStream.range(0,newValue).mapToObj(elem -> {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(percentValue);
            return rc;
        }).collect(Collectors.toList()));
        fixedRowsNumberProperty().set(newValue);
        resetAllChildrenByRow(newValue);


    }

    /**
     * Get the fixed columns number
     * @return the fixed columns number
     */
    public final int getFixedColumnsNumber() {
        return fixedColumnsNumberProperty().get();
    }

    /**
     * Get the fixed rows number
     * @return the fixed rows number
     */
    public final int getFixedRowsNumber() {
        return fixedRowsNumberProperty().get();
    }

    /**
     * Get the fixed columns number property
     * @return the fixed columns number property
     */
    public final IntegerProperty fixedColumnsNumberProperty() {
        if (fixedColumnsNumber == null) {
            fixedColumnsNumber = new IntegerPropertyBase(0) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return FlexibleGridPane.this;
                }

                @Override
                public String getName() {
                    return "fixedColumnsNumber";
                }
            };
        }
        return fixedColumnsNumber;
    }

    /**
     * Get the fixed rows number property
     * @return the fixed rows number property
     */
    public final IntegerProperty fixedRowsNumberProperty() {
        if (fixedRowsNumber == null) {
            fixedRowsNumber = new IntegerPropertyBase(0) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return FlexibleGridPane.this;
                }

                @Override
                public String getName() {
                    return "fixedRowsNumber";
                }
            };
        }
        return fixedRowsNumber;
    }

    /**
     * Reset all the children when the {@link #fixedColumnsNumberProperty()} is set
     * @param newColNumber new columns number
     */
    public final void resetAllChildrenByCol(int newColNumber) {
        ObservableList<Node> children = getChildren();
        this.getRowConstraints().clear();
        // Rows needed
        int neededRows = (int)Math.floor(children.size() / newColNumber);
        // Height percentage for each row
        double percentHeight = 100.0 / neededRows;
        this.getRowConstraints().addAll(IntStream.range(0,neededRows).mapToObj(elem -> {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(percentHeight);
            return rc;
        }).collect(Collectors.toList()));

        for (int i = 0; i < children.size(); i++) {
            Node currChild = children.get(i);
            //Find the column index of this node
            int colIndex = i % newColNumber;
            //Find the row index of this node
            int rowIndex = i / newColNumber;
            this.setConstraints(currChild,colIndex,rowIndex);
        }
    }

    /**
     * Reset all the children when the {@link #fixedRowsNumberProperty()} is set
     * @param newRowNumber new columns number
     */
    public final void resetAllChildrenByRow(int newRowNumber) {
        ObservableList<Node> children = getChildren();
        this.getColumnConstraints().clear();
        // Rows needed
        int neededRows = (int)Math.floor(children.size() / newRowNumber);
        // Height percentage for each column
        double percentWidth = 100.0 / neededRows;
        // Add all the column constraints
        this.getColumnConstraints().addAll(IntStream.range(0,neededRows).mapToObj(elem -> {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(percentWidth);
            return cc;
        }).collect(Collectors.toList()));

        for (int i = 0; i < children.size(); i++) {
            Node currChild = children.get(i);
            //Find the row index of this node
            int rowIndex = i % newRowNumber;
            //Find the column index of this node
            int colIndex = i / newRowNumber;
            this.setConstraints(currChild,colIndex,rowIndex);
        }
    }
}

