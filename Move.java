package blocksPuzzle;

// move of a block
class Move {

    Block currentBlock;
    int direction;

    
    public Move (Block blk, int d) {
    	currentBlock = blk;
    	direction = d;
    	
    }
    
    public Move clone () {
    	return new Move(this.currentBlock, this.direction);
    }
    
    public String toString() {
    	String result;
    	
        // get block location info
        int oldR = currentBlock.getRow();
        int oldC = currentBlock.getCol();
        
        int newR, newC; // new location
        
        if (direction == 0) {
            // direction = "down";
            newR = oldR + 1;
            newC = oldC;
        } else if (direction == 1) {
            // direction = "left";
            newR = oldR;
            newC = oldC - 1;
        } else if (direction == 2) {
            // direction = "up";
            newR = oldR - 1;
            newC = oldC;
        }  else {
            // direction = "right";
            newR = oldR;
            newC = oldC + 1;
        }
    	
    	result = "Move block at " + oldR + ", " + oldC + " to " + newR + ", " + newC;
    	return result;
    }
    
    public static void main (String[] args) {
    	Move m = new Move(new Block(1, 1, 0, 0), 3); // move block to right
    	System.out.println(m);
    }
}
