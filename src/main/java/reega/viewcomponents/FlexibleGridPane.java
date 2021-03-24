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

public class FlexibleGridPane extends GridPane {

    private IntegerProperty fixedColumnsNumber;
    private IntegerProperty fixedRowsNumber;

    {
        getChildren().addListener((ListChangeListener<Node>) c -> {
           if (fixedColumnsNumberProperty().isNotEqualTo(0).get()){
               resetAllChildrenByCol(fixedColumnsNumberProperty().get());
           }
           else if (fixedRowsNumberProperty().isNotEqualTo(0).get()) {
               resetAllChildrenByRow(fixedRowsNumberProperty().get());
           }
        });
    }

    public final void setFixedColumnsNumber(int newValue) {
        if (fixedRowsNumberProperty().isNotEqualTo(0).get()) {
            throw new IllegalStateException("You cannot set a constraint on rows and columns at the same time");
        }
        double percentValue = 100.0/(newValue);
        this.getColumnConstraints().addAll(IntStream.range(0,newValue).mapToObj(elem -> {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(percentValue);
            return cc;
        }).collect(Collectors.toList()));
        fixedColumnsNumberProperty().set(newValue);
        resetAllChildrenByCol(newValue);
    }

    public final void setFixedRowsNumber(int newValue) {
        if (fixedColumnsNumberProperty().isNotEqualTo(0).get()) {
            throw new IllegalStateException("You cannot set a constraint on rows and columns at the same time");
        }
        double percentValue = 100.0/(newValue);
        this.getRowConstraints().addAll(IntStream.range(0,newValue).mapToObj(elem -> {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(percentValue);
            return rc;
        }).collect(Collectors.toList()));
        fixedRowsNumberProperty().set(newValue);
        resetAllChildrenByRow(newValue);
    }

    public final int getFixedColumnsNumber() {
        return fixedColumnsNumberProperty().get();
    }

    public final int getFixedRowsNumber() {
        return fixedRowsNumberProperty().get();
    }

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
            int colIndex = i % newColNumber;
            int rowIndex = i / newColNumber;
            this.setConstraints(currChild,colIndex,rowIndex);
        }
    }

    public final void resetAllChildrenByRow(int newRowNumber) {
        ObservableList<Node> children = getChildren();
        this.getColumnConstraints().clear();
        // Rows needed
        int neededRows = (int)Math.floor(children.size() / newRowNumber);
        // Height percentage for each row
        double percentWidth = 100.0 / neededRows;
        this.getColumnConstraints().addAll(IntStream.range(0,neededRows).mapToObj(elem -> {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(percentWidth);
            return cc;
        }).collect(Collectors.toList()));

        for (int i = 0; i < children.size(); i++) {
            Node currChild = children.get(i);
            int colIndex = i % newRowNumber;
            int rowIndex = i / newRowNumber;
            this.setConstraints(currChild,colIndex,rowIndex);
        }
    }
}

