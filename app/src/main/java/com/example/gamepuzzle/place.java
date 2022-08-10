package com.example.gamepuzzle;

public class place {
    private int x, y;
    private tile Tile;
    private board Board;

    public place(int x, int y, board Board){
        this.x = x;
        this.y = y;
        this.Board = Board;
    }
    public place(int x, int y, int num, board Board){
        this(x, y, Board);
        Tile = new tile(num);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean hasTile(){
        return Tile != null;
    }
    public tile getTile() { return Tile; }
    public void setTile(tile t){
        Tile = t;
    }
    public boolean slideable() { return hasTile() && Board.slidable(this);}
    public void slide() {Board.slide(getTile());}
}
