package test;

import java.util.Arrays;

public class Word {
private Tile[] tiles;
private int row;
private int col;
private boolean vertical;


public Word(Tile[] tiles, int row, int col, boolean vertical) {
    this.tiles = tiles;
    this.row = row; //שורה
    this.col = col; //עמודה
    this.vertical = vertical;
}


public Tile[] getTiles() {
    return tiles;
}


public int getRow() {
    return row;
}


public int getCol() {
    return col;
}


public boolean isVertical() {
    return vertical;
}


@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Word other = (Word) obj;
    if (!Arrays.equals(tiles, other.tiles))
        return false;
    if (row != other.row)
        return false;
    if (col != other.col)
        return false;
    if (vertical != other.vertical)
        return false;
    return true;
}

	
}
