package blocksPuzzle;

import java.util.HashSet;


// object representing each piece of block
class Block {
    
    private int height, width;
    private int row, col;
    
    public Block(int h, int w, int r, int c) {
        height = h;
        width = w;
        row = r;
        col = c;
    }
    
    public Block (Block blk) {
    	this(blk.height, blk.width, blk.row, blk.col);
    }
    
    
    public int getHeight(){
        return height;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public void setLoc(int r, int c) {
        row = r;
        col = c;
    }
    
    public String toString() {
        return new String("Block at Location: ("+ getRow() +", "+ getCol()
        				 + "), Size: " + getHeight() + " x " + getWidth());
    }
    
    @Override
    public int hashCode() {
    	
    	int h = getHeight();
    	int w = getWidth();
    	while (w > 0) {
    		h *= 10;
    		w = (int) w / 10;
    	}
    	return h + width;
    }
    
    @Override
    public boolean equals (Object other) {
    	if (other == null || getClass() != other.getClass())
    		return false;
    	Block b = (Block) other;
    	return b.height == height && b.width == width &&
    			b.row == row && b.col == col;
    }
    
    public static void main (String[] args) {
    	
    	Block blk1, blk2, blk3;
    	
    	blk1 = new Block(1, 2, 4, 4);
    	blk2 = new Block (blk1);
    	blk1.setLoc(0, 0);
    	blk3 = new Block (2, 3, 4, 5);
    	
    	System.out.println("blk1>" + blk1);
    	System.out.println(blk1.hashCode()); 
    	
    	System.out.println("blk2> " + blk2);
    	System.out.println(blk2.hashCode());
    	
    	System.out.println("blk3> " + blk3);
    	System.out.println(blk3.hashCode() + "\n");
    	
    	
    	HashSet<Block> hashes = new HashSet<Block>();
    	hashes.add(blk1);
    	hashes.add(blk2);
    	hashes.add(blk3);
    	System.out.println(hashes.hashCode() + "\n");
    	
    	for (Block hash : hashes)
    		System.out.println(hash.hashCode());
    	
    }
    
}
