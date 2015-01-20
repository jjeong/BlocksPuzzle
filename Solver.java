package blocksPuzzle;

import java.io.*;
import java.util.*;


public class Solver {
    
    private Board given, goal, solution;
    public boolean solved = false;
    private static int ROWS, COLS; // board dimension
    private static boolean debug;
    
    // checks whether a board is tested or not
    private HashSet<Board> boardHistory = new HashSet<Board>();
    
    // stores boards that needs testing
    private Stack<Board> boardStack = new Stack<Board>(); // breadth-first
    
    
    public void setGiven (Board g) {
    	given = g;
    }
    
    
    public void setGoal (Board g) {
    	goal = g;
    }
    
    
    public void start(){
    	
    	long startTime, finishTime;
        startTime = System.currentTimeMillis();
        
    	
        boardStack.push(given);
        
        // try all possible boards
        while(! boardStack.isEmpty() && ! solved)
            tryMoving();
        
        finishTime = System.currentTimeMillis ( );
        
        if (! solved) {
            System.out.println("no solution");
            System.exit(1);
        }
        
        showSteps();
        solution.visualize();
        
        System.out.println("Time taken to solve the puzzle: " 
	            + (finishTime - startTime) + " milliseconds.");
    }
    
    
    public void tryMoving() {
        
        debugPrint("\ntrying");
        
        Board b = boardStack.pop();
        
        // if it matches the desired outcome, return true
        if (isSolved(b)) {
            System.out.println("Already solved!");
            solution = b;
            System.exit(1);
        }
        
        // check if the board has already been seen.
        // if so, skip the steps below
        if (!boardHistory.contains(b)) {
            
            boardHistory.add(b);
            
            debugPrint("oldBoard:\n" + b.toString());
            
            LinkedList<Block> blks = b.cloneBlocks();
            for (Block blk : blks) {
                for (int dir = 0; dir < 4; dir++) {
                	Move newMove = new Move(new Block(blk), dir);
                    Board newBoard = b.makeMove(blk, dir);
                    
                    // skip if move not possible
                    if (newBoard == null)
                        continue;
                    
                    newBoard = new Board(newBoard);
                    
                    // new board has reached the goal
                    if(isSolved(newBoard)) {
                    	// update relevent info
                        solution = newBoard;
                        solution.prevBoard = b;
                        solution.setLastMove(newMove);
                        solved = true;
                    }
                    
                    // the layout has not been seen
                    else if (! boardHistory.contains(newBoard)) {
                    	
                        debugPrint("board added");
                        debugPrint("newBoard:\n"+newBoard.toString());
                        
                        newBoard.prevBoard = b;
                        newBoard.setLastMove(newMove);
                        
                        boardStack.push(newBoard);
                    }
                    b.unmove(blk, dir);
                    
                    if (solved)
                    	break;
                    
                } // all four directions tested
            } // all blocks tested
        }
    }
    
    
    // checks whether the puzzle matches the goal
    public boolean isSolved(Board p) {
        
        if (p == null)
            return false;
        
        for (Block blk: goal.blocks)
        	if (! p.blocks.contains(blk))
        		return false;
        return true;
    }
    
    
    // show the moves towards the goal
    public void showSteps() {
        Stack<Move> steps = new Stack<Move>();
        Board current, previous;
        
        if (! solved)
            System.out.println("no solution availabe");
        
        else {
        	
            current = solution;
            steps.push(current.lastMove);
            
            previous = current.prevBoard;
            
            // already solved, so need to show steps
            if (previous == null)
            	System.exit(1);
            
            while (previous != null) {
            	current = previous;
            	previous = previous.prevBoard;
            	if (current.lastMove == null)
            		break;
            	steps.push(current.lastMove);
            }
            int numSteps = steps.size();
            while (! steps.isEmpty())
            	System.out.println(steps.pop());
            System.out.println("Total moves: " + numSteps);
        }
    }
    
    
    // parses a puzzle txt file into a Board object
    private Board parsePuzzle(String fileName){
        LinkedList<Block> blks = new LinkedList<Block>();
        FileReader file;
        BufferedReader fileIn = null;
        String oneLine;
        StringTokenizer str;
        
        try {
            file = new FileReader(fileName);
            fileIn = new BufferedReader(file);
            oneLine = fileIn.readLine();
            str = new StringTokenizer(oneLine);
            ROWS = Integer.parseInt(str.nextToken());
            COLS = Integer.parseInt(str.nextToken());
            while ((oneLine = fileIn.readLine()) != null) {
                str = new StringTokenizer(oneLine);
                Block blk = new Block(Integer.parseInt(str.nextToken()),
                                    Integer.parseInt(str.nextToken()),
                                    Integer.parseInt(str.nextToken()),
                                    Integer.parseInt(str.nextToken()));
                blks.add(blk);
            }
        } catch(IOException e){
            System.out.println(e);
        } finally {
            try {
                if (fileIn != null)
                    fileIn.close();
            } catch(IOException e){
                
            }
        }
        Board b = new Board(ROWS, COLS);
        for (Block blk: blks) 
        	b.addBlock(blk);
        return b;
    }
    
    
    // parses a goal txt file into a Board object
    private Board parseSol(String fileName){
        FileReader file;
        BufferedReader fileIn = null;
        String oneLine;
        StringTokenizer str;
        Board b = new Board(ROWS, COLS);
        
        try {
            file = new FileReader(fileName);
            fileIn = new BufferedReader(file);
            while ((oneLine = fileIn.readLine()) != null) {
                str = new StringTokenizer(oneLine);
                Block blk = new Block(Integer.parseInt(str.nextToken()),
                                    Integer.parseInt(str.nextToken()),
                                    Integer.parseInt(str.nextToken()),
                                    Integer.parseInt(str.nextToken()));
                b.addBlock(blk);
            }
        } catch(IOException e){
            System.out.println(e);
        } finally {
            try {
                if (fileIn != null)
                    fileIn.close();
            } catch(IOException e){
                
            }
        }
        return b;
    }
    
    
    public static void debugPrint(String str) {
        if (debug)
            System.out.println(str);
    }
    
    
    public static void main(String[] args) {
    	
        if (args.length < 2) {
            System.err.println("Wrong number of inputs");
            System.exit(1);
        }
        int debugging = 0;
        
        if (args[0].substring(0, 2).equals("-o")) {
            debugging = 1;
            if (args[0].substring(2).equals("debug"))
                debug = true;
        }
        
        Solver puzzle = new Solver();
        
        Board given = puzzle.parsePuzzle(args[0 + debugging]);
        puzzle.setGiven(given);
        
        Board goal = puzzle.parseSol(args[1 + debugging]);
        puzzle.setGoal(goal);
        
        if (! puzzle.isSolved(given))
	        puzzle.start();
    }
}

