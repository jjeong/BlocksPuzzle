package blocksPuzzle;

import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Graphics;

// object that stores an entire layout of the puzzle
// holds info for all the puzzle blocks, including their w, l,
// and position indices
class Board {
	
	public HashSet<Block> blocks;
    public boolean occupied[][];
    private int ROWS, COLS;
    public Move lastMove;
    public Board prevBoard;
    

    // empty Board constructor
    public Board(int r, int c) {
        ROWS = r;
        COLS = c;
        prevBoard = null;
        blocks = new HashSet<Block> (ROWS * COLS);
        occupied = new boolean[ROWS][COLS];
        
    }
    
    
    // deep copy constructor
    public Board (Board b) {

        ROWS = b.getRowSize();
        COLS = b.getColSize();
        prevBoard = b.prevBoard;
        blocks = new HashSet<Block>(ROWS * COLS);

        for (Block blk : b.blocks)
        	blocks.add(new Block(blk));

        if (b.lastMove != null)
        	lastMove = b.lastMove.clone();
        occupied = new boolean[ROWS][COLS];
        
        // fill an empty board with blocks
        for (Block blk : blocks) {
            occupy(new Block(blk));
        }
    }
    
    
    public boolean addBlock(Block blk) {
    	
    	if (blk == null) // empty block
    		return false;
		
    	if (blocks.contains(blk)) // block already exists
    		return false;
    	
    	// check if out of board
    	if (blk.getHeight() + blk.getRow() > ROWS ||
    			blk.getWidth() + blk.getCol() > COLS)
    		return false;
    	
    	// check if space occupied
    	for(int i = blk.getRow(); i < blk.getRow() + blk.getHeight(); i++)
            for (int j = blk.getCol(); j < blk.getCol() + blk.getWidth(); j++)
                if (occupied[i][j]) 
                    return false;
    	
    	// add block and update occupied space layout
    	blocks.add(blk);
    	occupy(blk);
    	return true;
    	
    }
    
    
    public boolean removeBlock(Block blk) {
    	
    	if (! blocks.contains(blk)) // no such block
    		return false;
    	
    	blocks.remove(blk);
    	return true;
    }
    
    
    public int getRowSize() {
        return ROWS;
    }
    
    
    public int getColSize() {
        return COLS;
    }
    
    
    // updates which cells are filled after a block moves
    private void occupy(Block blk) {
        for(int i = blk.getRow(); i < blk.getRow() + blk.getHeight(); i++)
            for (int j = blk.getCol(); j < blk.getCol() + blk.getWidth(); j++)
                occupied[i][j] = true;
    }
    
    
    // updates which cells will be empty after a block is removed
    private void unoccupy(Block blk) {
        for(int i = blk.getRow(); i < blk.getRow() + blk.getHeight(); i++)
            for (int j = blk.getCol(); j < blk.getCol()+blk.getWidth(); j++)
                occupied[i][j] = false;
    }
    
    
    // clones a list of blocks
    public LinkedList<Block> cloneBlocks() {
        LinkedList<Block> clones = new LinkedList<Block> ();
        for (Block blk: blocks){
            clones.add(new Block (blk));
        }
        return clones;
    }
    
    // moves a block to an adjacent block if possible
    public Board makeMove(Block blk, int dir) {
    	
        if (blk == null || ! blocks.contains(blk))
            return null;

        Block movedBlock = blk;
        
        // get block location info
        int oldR = movedBlock.getRow();
        int oldC = movedBlock.getCol();
        
        int newR, newC; // new location
        
        if (dir == 0) {
            // direction = "down";
            newR = oldR + 1;
            newC = oldC;
        } else if (dir == 1) {
            // direction = "left";
            newR = oldR;
            newC = oldC - 1;
        } else if (dir == 2) {
            // direction = "up";
            newR = oldR - 1;
            newC = oldC;
        }  else {
            // direction = "right";
            newR = oldR;
            newC = oldC + 1;
        }
        
        int height = movedBlock.getHeight();
        int width = movedBlock.getWidth();
        
        //temporarily remove old block
        unoccupy(blk);
        
        // if the move is impossible, return null
        if (! isPossible(newR, newC, height, width)) {
            occupy(blk);
            return null;
        }
        
        blocks.remove(blk);
        
        // update location info of the block before moving
        movedBlock.setLoc(newR, newC);
        
        blocks.add(movedBlock);
        occupy(movedBlock);

        return this;
    }

    
    public Board unmove(Block blk, int prevDir) {
        
        int oppositeDir = (prevDir + 2) % 4;
        this.makeMove(blk, oppositeDir);
        
        return this;
    }

    
    // checks whether the move is possible
    private boolean isPossible (int newR, int newC, int height, int width) {
        
        // check index bounds
        if (newR + height > ROWS || newC + width > COLS)
            return false;
        if (newR < 0 || newC < 0)
            return false;
        
        // checks whether the space is occupied by another block
        for (int i = newR; i < newR + height; i++)
            for (int j = newC; j < newC + width; j++)
                if (occupied[i][j])
                    return false;
        return true;
    }
    
    public void setLastMove(Move m) {
    	lastMove = m;
    }
    
    
    // prints the board into string
    public String toString() {
        String result = new String ();
        for (Block blk : blocks)
            result += blk.getHeight() + " " + blk.getWidth() + " "
            + blk.getRow() + " " + blk.getCol() + "\n";
        return result;
    }
    
    class drawBlocks extends JComponent {
    	
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
            for (Block blk : blocks) {
                g.drawRect(blk.getCol()*100 + 30, blk.getRow()*100 + 30,
                           blk.getWidth()*100 - 30, blk.getHeight()*100 - 30);
            }
        }
    }
    
    
    // displays visual representation of the board
    public void visualize() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, COLS * 100 + 30, ROWS * 100 + 30);
        window.getContentPane().add(new drawBlocks());
        window.setVisible(true);
    }
    
        
    @Override
    public int hashCode(){
    	int hash = 0;
    	for (Block blk : blocks) {
    		// hash += block * 31^(location index)
    		int val = 1;
    		for (int i = 0; i < COLS * blk.getRow() + blk.getCol(); i++)
    			val *= 961;
    		hash += blk.hashCode() * val;
    	}
    	return hash;
    }
    
    
    @Override
    public boolean equals (Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        return blocks.equals(((Board) other).blocks);
    }
    
    
    public static void main(String[] args) {
    	
    	HashSet<Board> hash = new HashSet<Board>();
    	
    	Block blk1, blk2;
        blk1 = new Block(1, 1, 3, 1);
        blk2 = new Block(2, 3, 1, 1);
        
        Board b = new Board(5, 5);
        b.addBlock(blk1);
        System.out.println("b hash: " + b.hashCode()); // 0?
        b.addBlock(blk2);
        System.out.println("b hash: " + b.hashCode());
        b.addBlock(blk2); // trying to add same block twice returns false
        
        System.out.println("\nBoard b of size: " + b.getRowSize() +
                           " x "+ b.getColSize());
        
        System.out.println(b.toString());
        
        hash.add(b);
        System.out.println("b added to hash\n");
        
        b.makeMove(blk1, 0); // down
        b.makeMove(blk2, 3); // right
        
        System.out.println("b after moving:");
        System.out.println(b.toString());
        
        System.out.println("HashSet contains modified b? " + hash.contains(b));
        
        // deep copy constructor
        Board b2 = new Board (b);
        System.out.println("\nb deep-copied to b2");
        System.out.println("\nb2: \n" + b2.toString());
        
        b.unmove(blk2, 3); // move back up
        b.unmove(blk1, 0); // move back left
        
        System.out.println("b unmoved:");
        System.out.println(b.toString());
        System.out.println("b hash: " + b.hashCode());
        
        System.out.println("HashSet contains b? " + hash.contains(b) + "\n");
        System.out.println("HashSet contains b2? " + hash.contains(b2) + "\n");

        //trying to add impossible block should do nothing
        b.addBlock(new Block(1, 2, 3, 1)); // two pieces overlay
        
        //trying to add block out of range
        b.addBlock(new Block(1, 1, 10, 10));
        
        // add new block
        b.addBlock(new Block(1, 1, 0, 0));
        
        System.out.println("b:\n" + b.toString());
        System.out.println("b2:\n" + b2);
        System.out.println("b hash: " + b.hashCode());
        System.out.println("b2 hash: " + b2.hashCode());
        
    }
} // end of Board class

