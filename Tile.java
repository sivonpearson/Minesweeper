public class Tile {
    private boolean isMine;
    private int numMinesTouching = 0;
    private boolean visible = false; //if the info is visible to the player
    private boolean isFlagged = false;
  
    public Tile(boolean isMine) {
        this.isMine = isMine;
    }

    //maybe just add a toggle
    public void setFlagged() {
        isFlagged = true;
    }

    public void setUnflagged() {
        isFlagged = false;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setNumMinesTouching(int numMinesTouching) {
        this.numMinesTouching = numMinesTouching;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible() {
        visible = true;
    }

    public int getNumMinesTouching() {
        return numMinesTouching;
    }
}
