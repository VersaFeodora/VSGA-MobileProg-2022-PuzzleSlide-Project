package com.example.gamepuzzle;
import java.util.*;

public class board {
    public int size, moveNum;
    private List<place> places;
    private List<boardListeners> listeners;
    private static Random random = new Random();

    public board(int size) {
        listeners = new ArrayList<boardListeners>();
        this.size = size;
        places = new ArrayList<place>(size * size);
        for (int x = 1; x <= size; x++) {
            for (int y = 1; y <= size; y++) {
                places.add(x == size && y == size ?
                        new place(x, y, this)
                        : new place(x, y, (y - 1)* size + x, this));
            }
        }
        moveNum = 0;
    }

    public void rearrange() {
        moveNum = 0;
        for (int i = 0; i < size*size; i++) {
            swapTiles();
        }
        do {
            swapTiles();
        } while (!solvable() || solved());
    }

    private void swapTiles() {
        place p1 = at(random.nextInt(size) + 1, random.nextInt(size) + 1);
        place p2 = at(random.nextInt(size) + 1, random.nextInt(size) + 1);
        if (p1 != p2) {
            tile t = p1.getTile();
            p1.setTile(p2.getTile());
            p2.setTile(t);
        }
    }
    private boolean solvable() {
        // alg. from: http://www.cs.bham.ac.uk/~mdr/teaching/modules04/
        //                 java2/TilesSolvability.html
        //
        // count the number of inversions, where an inversion is when
        // a tile precedes another tile with a lower number on it.
        int inversion = 0;
        for (place p: places) {
            tile pt = p.getTile();
            for (place q: places) {
                tile qt = q.getTile();
                if (p != q && pt != null && qt != null &&
                        indexOf(p) < indexOf(q) &&
                        pt.number() > qt.number()) {
                    inversion++;
                }
            }
        }
        final boolean isEvenSize = size % 2 == 0;
        final boolean isEvenInversion = inversion % 2 == 0;
        boolean isBlankOnOddRow = blank().getY() % 2 == 1;
        // from the bottom
        isBlankOnOddRow = isEvenSize ? !isBlankOnOddRow : isBlankOnOddRow;
        return (!isEvenSize && isEvenInversion) ||
                (isEvenSize && isBlankOnOddRow == isEvenInversion);
    }
    private int indexOf(place p) {
        return (p.getY() - 1) * size + p.getX();

    }
    public boolean solved() {
        boolean result = true;
        for (place p: places) {
            result = result &&
                    ((p.getX() == size && p.getY() == size) ||
                            (p.getTile() != null &&
                                    p.getTile().number() == indexOf(p)));
        }
        return result;
    }
    public void slide(tile tile) {
        for (place p: places) {
            if (p.getTile() == tile) {
                final place to = blank();
                to.setTile(tile);
                p.setTile(null);
                moveNum++;
                notifyTileSliding(p, to, moveNum);
                if (solved()) {
                    notifyPuzzleSolved(moveNum);
                }
                return;
            }
        }
    }
    public boolean slidable(place p) {
        int x = p.getX();
        int y = p.getY();
        return isBlank(x - 1, y) || isBlank(x + 1, y)
                || isBlank(x, y - 1) || isBlank(x, y + 1);
    }
    private boolean isBlank(int x, int y) {
        return (0 < x && x <= size) && (0 < y && y <= size)
                && at(x,y).getTile() == null;
    }
    public place blank() {
        for (place p: places) {
            if (p.getTile() == null) {
                return p;
            }
        }
        return null;
    }
    public Iterable<place> places() {
        return places;
    }
    public place at(int x, int y) {
        for (place p: places) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        //assert false : "precondition violation!";
        return null;
    }
    public int size() {
        return size;
    }
    public int numOfMoves() {
        return moveNum;
    }
    public void addBoardChangeListener(boardListeners listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    public void removeBoardChangeListener(boardListeners listener) {
        listeners.remove(listener);
    }

    private void notifyTileSliding(place from, place to, int numOfMove) {
        for (boardListeners listener: listeners) {
            listener.tileSlid(from, to, moveNum);
        }
    }

    private void notifyPuzzleSolved(int numOfMoves) {
        for (boardListeners listener: listeners) {
            listener.solved(numOfMoves);
        }
    }
    public interface boardListeners {
        void tileSlid(place from, place to, int numOfMoves);
        void solved(int numOfMoves);
    }

}
