package reega.viewcomponents;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * {@link GridPane} that contains a fixed number of rows or columns and a variable number of columns or rows. <para>If
 * columns are fixed than rows are variable.</para> <para>If rows are fixed than columns are variable.</para> <para>The
 * size of each variable is a percentage equal to the floor of the size of {@link #getChildren()} divided by the number
 * of rows/columns.</para> <para>The children are positioned based on the constraint.</para> <para>If columns are fixed
 * than all the columns needs to be filled before a new row is created.</para> <para>If rows are fixed than all the rows
 * needs to be filled before a new column is created.</para>
 */
public class FlexibleGridPane extends GridPane {

    private IntegerProperty fixedColumnsNumber;
    private IntegerProperty fixedRowsNumber;

    /**
     * Instance initializer
     */
    {
        // Add a listener that resets columns/rows whenever the children collection is modified
        this.getChildren().addListener((ListChangeListener<Node>) c -> {
            if (this.fixedColumnsNumberProperty().isNotEqualTo(0).get()) {
                this.resetAllChildrenByCol(this.fixedColumnsNumberProperty().get());
            } else if (this.fixedRowsNumberProperty().isNotEqualTo(0).get()) {
                this.resetAllChildrenByRow(this.fixedRowsNumberProperty().get());
            }
        });
    }

    /**
     * Set the fixed columns number
     *
     * @param newValue new value for the number of columns
     * @throws IllegalStateException if {@link #fixedRowsNumberProperty()} is not equal to 0
     */
    public final void setFixedColumnsNumber(final int newValue) {
        if (this.fixedRowsNumberProperty().isNotEqualTo(0).get()) {
            throw new IllegalStateException("You cannot set a constraint on rows and columns at the same time");
        }
        this.getColumnConstraints().clear();
        if (newValue == 0) {
            this.fixedColumnsNumberProperty().set(newValue);
            return;
        }
        // Find the size percentage of the columns
        final double percentValue = 100.0 / (newValue);
        this.getColumnConstraints().addAll(IntStream.range(0, newValue).mapToObj(elem -> {
            final ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(percentValue);
            return cc;
        }).collect(Collectors.toList()));
        this.fixedColumnsNumberProperty().set(newValue);
        this.resetAllChildrenByCol(newValue);
    }

    /**
     * Set the fixed rows number
     *
     * @param newValue new value for the number of rows
     * @throws IllegalStateException if {@link #fixedColumnsNumberProperty()} is not equal to 0
     */
    public final void setFixedRowsNumber(final int newValue) {
        if (this.fixedColumnsNumberProperty().isNotEqualTo(0).get()) {
            throw new IllegalStateException("You cannot set a constraint on rows and columns at the same time");
        }
        this.getRowConstraints().clear();
        if (newValue == 0) {
            this.fixedRowsNumberProperty().set(newValue);
            return;
        }
        // Find the size percentage of the rows
        final double percentValue = 100.0 / (newValue);
        this.getRowConstraints().addAll(IntStream.range(0, newValue).mapToObj(elem -> {
            final RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(percentValue);
            return rc;
        }).collect(Collectors.toList()));
        this.fixedRowsNumberProperty().set(newValue);
        this.resetAllChildrenByRow(newValue);

    }

    /**
     * Get the fixed columns number
     *
     * @return the fixed columns number
     */
    public final int getFixedColumnsNumber() {
        return this.fixedColumnsNumberProperty().get();
    }

    /**
     * Get the fixed rows number
     *
     * @return the fixed rows number
     */
    public final int getFixedRowsNumber() {
        return this.fixedRowsNumberProperty().get();
    }

    /**
     * Get the fixed columns number property
     *
     * @return the fixed columns number property
     */
    public final IntegerProperty fixedColumnsNumberProperty() {
        if (this.fixedColumnsNumber == null) {
            this.fixedColumnsNumber = new IntegerPropertyBase(0) {

                @Override
                protected void invalidated() {
                    FlexibleGridPane.this.requestLayout();
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
        return this.fixedColumnsNumber;
    }

    /**
     * Get the fixed rows number property
     *
     * @return the fixed rows number property
     */
    public final IntegerProperty fixedRowsNumberProperty() {
        if (this.fixedRowsNumber == null) {
            this.fixedRowsNumber = new IntegerPropertyBase(0) {

                @Override
                protected void invalidated() {
                    FlexibleGridPane.this.requestLayout();
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
        return this.fixedRowsNumber;
    }

    /**
     * Reset all the children when the {@link #fixedColumnsNumberProperty()} is set
     *
     * @param newColNumber new columns number
     */
    public final void resetAllChildrenByCol(final int newColNumber) {
        final ObservableList<Node> children = this.getChildren();
        this.getRowConstraints().clear();
        // Rows needed
        final int neededRows = (int) Math.floor(children.size() / newColNumber);
        // Height percentage for each row
        final double percentHeight = 100.0 / neededRows;
        this.getRowConstraints().addAll(IntStream.range(0, neededRows).mapToObj(elem -> {
            final RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(percentHeight);
            return rc;
        }).collect(Collectors.toList()));

        for (int i = 0; i < children.size(); i++) {
            final Node currChild = children.get(i);
            // Find the column index of this node
            final int colIndex = i % newColNumber;
            // Find the row index of this node
            final int rowIndex = i / newColNumber;
            GridPane.setConstraints(currChild, colIndex, rowIndex);
        }
    }

    /**
     * Reset all the children when the {@link #fixedRowsNumberProperty()} is set
     *
     * @param newRowNumber new columns number
     */
    public final void resetAllChildrenByRow(final int newRowNumber) {
        final ObservableList<Node> children = this.getChildren();
        this.getColumnConstraints().clear();
        // Rows needed
        final int neededRows = (int) Math.floor(children.size() / newRowNumber);
        // Height percentage for each column
        final double percentWidth = 100.0 / neededRows;
        // Add all the column constraints
        this.getColumnConstraints().addAll(IntStream.range(0, neededRows).mapToObj(elem -> {
            final ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(percentWidth);
            return cc;
        }).collect(Collectors.toList()));

        for (int i = 0; i < children.size(); i++) {
            final Node currChild = children.get(i);
            // Find the row index of this node
            final int rowIndex = i % newRowNumber;
            // Find the column index of this node
            final int colIndex = i / newRowNumber;
            GridPane.setConstraints(currChild, colIndex, rowIndex);
        }
    }
}
