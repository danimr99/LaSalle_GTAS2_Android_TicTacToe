package com.tictactoe.model;

import android.widget.ImageView;

public class Cell {
    private ImageView widget;
    private CellStatus status;

    public Cell(ImageView widget) {
        this.widget = widget;
        this.status = CellStatus.EMPTY_CELL;
    }

    public ImageView getWidget() {
        return widget;
    }

    public boolean isEmptyCell() {
        return this.status == CellStatus.EMPTY_CELL;
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }
}