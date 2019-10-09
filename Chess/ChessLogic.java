import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
/**
 * The logic for the game Chess.
 * 
 * @author David Choi
 * @version 10/02/2016
 */
public class ChessLogic  
{
    public static final int EMPTY = 0;
    public static final int WPAWN = 1;
    public static final int WKNIGHT = 2;
    public static final int WBISHOP = 3;
    public static final int WROOK = 4;
    public static final int WQUEEN = 5;
    public static final int WKING = 6;
    public static final int BPAWN = 11;
    public static final int BKNIGHT = 12;
    public static final int BBISHOP = 13;
    public static final int BROOK = 14;
    public static final int BQUEEN = 15;
    public static final int BKING = 16;
    public static final int WTHREAT = 1;
    public static final int BTHREAT = 2;
    public static final int WBTHREAT = 3;
    public static final int PAWNMOVABLE = 4;

    private boolean turnWhite_;
    private boolean moved_;
    private boolean canMove_;
    private boolean blocked_;
    private boolean reverted_;
    private final int dimensions_ = 8;
    private int[] wKing_;
    private int[] bKing_;
    private int[] lastMoved_;
    private int[] lastCaptured_;
    private int[][] chessBoard_;
    private int[][] threat_;
    /**
     * Constructor for objects of class ChessLogic.
     */
    public ChessLogic(){
        turnWhite_ = true;
        moved_ = false;
        canMove_ = true;
        wKing_ = new int[2];
        bKing_ = new int[2];
        wKing_[0]  = 7;
        wKing_[1] = 4;
        bKing_[0] = 0;
        bKing_[1] = 4;
        chessBoard_ = new int[8][8];
        threat_ = new int[8][8];
        lastCaptured_ = new int[3];
        lastMoved_ = new int[3];

        initialize();
    }

    /**
     * Initializes the board.
     */
    public void initialize(){
        for(int count = 0; count < 8; count++){
            chessBoard_[1][count] = BPAWN;
            chessBoard_[6][count] = WPAWN; 
        } 

        chessBoard_[0][0] = BROOK;
        chessBoard_[0][1] = BKNIGHT;
        chessBoard_[0][2] = BBISHOP;
        chessBoard_[0][3] = BQUEEN;
        chessBoard_[bKing_[0]][bKing_[1]] = BKING;
        chessBoard_[0][5] = BBISHOP;
        chessBoard_[0][6] = BKNIGHT;
        chessBoard_[0][7] = BROOK;

        chessBoard_[7][0] = WROOK;
        chessBoard_[7][1] = WKNIGHT;
        chessBoard_[7][2] = WBISHOP;
        chessBoard_[7][3] = WQUEEN;

        chessBoard_[wKing_[0]][wKing_[1]] = WKING;
        chessBoard_[7][5] = WBISHOP;
        chessBoard_[7][6] = WKNIGHT;

        chessBoard_[7][7] = WROOK;;
    }

    /**
     * Returns true if the square at the given row and column is out of the board.
     * 
     * @return True if the square at the given row and column is out of the board.
     */
    public boolean outOfBounds(int r,int c){
        if(r >= 0 && r <= 7 && c >= 0 && c <= 7){
            return false;
        }
        return true;
    }

    /**
     * Returns true if a given piece is white.
     * 
     * @param piece = The piece that you wish to identify.
     * @return True if a given piece is white.
     */
    public boolean isWhite(int piece){
        if(piece > 0 && piece < 7){
            return true;
        }
        return false;
    }

    /**
     * Returns true if a given piece is black.
     * 
     * @param piece = The piece that you wish to identify.
     * @return True if a given piece is black.
     */
    public boolean isBlack(int piece){
        if(piece > 10 && piece < 17){
            return true;
        }
        return false;
    }

    /**
     * Returns true if the white king is in check.
     * 
     * @return True if the white king is in check.
     */
    public boolean wKingCheck(){
        if(threat_[wKing_[0]][wKing_[1]] == BTHREAT || threat_[wKing_[0]][wKing_[1]] == WBTHREAT){
            return true;
        }
        return false;
    }

    /**
     * Returns true if the black king is in check.
     * 
     * @return True if the black king is in check.
     */
    public boolean bKingCheck(){
        if(threat_[bKing_[0]][bKing_[1]] == WTHREAT || threat_[bKing_[0]][bKing_[1]] == WBTHREAT){
            return true;
        }
        return false;
    }

    /**
     * Returns true if it is the white player's turn.
     * 
     * @return True if it is the white player's turn.
     */
    public boolean turnWhite(){
        return turnWhite_;
    }

    /**
     * Returns true if a move has been reverted.
     * 
     * @return True if a move has been reverted.
     */
    public boolean reverted(){
        return reverted_;
    }

    /**
     * Returns true if a piece has actually moved.
     * 
     * @return True if a piece has actually moved.
     */
    public boolean moved(){
        return moved_;
    }

    /**
     * Returns true if the square can be taken by the given piece.
     * 
     * @param row = The row of the target square.
     * @param col = The column of the target square.
     * @param piece = The piece you wish to examine the validity of.
     */
    public boolean checkSquare(int row,int col,int piece){
        int tSquare = chessBoard_[row][col];
        if(isBlack(piece)){
            if(isWhite(tSquare) || tSquare == EMPTY){
                return true;
            }else{
                return false;
            }
        }else if(isWhite(piece)){
            if(isBlack(tSquare) || tSquare == EMPTY){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    /**
     * Checks if a piece of the opponent is blocking the given square.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param piece = Your piece that you wish to check the blocked state of.
     */
    public boolean checkBlock(int r,int c,int piece){
        if(isBlack(piece) && isWhite(chessBoard_[r][c])){
            return true;
        }else if(isWhite(piece) && isBlack(chessBoard_[r][c])){
            return true;
        }
        return false;
    }

    /**
     * Returns the piece at the given square.
     * 
     * @param r = The row of the square.
     * @param c = The column of the square.
     * @return The piece at the given square.
     */
    public int getPieceAt(int r,int c){
        return chessBoard_[r][c];
    }

    /**
     * Returns the threat at a given square on the board.
     * 
     * @param r = The row of the square.
     * @param c = The column of the square.
     */
    public int getThreat(int r,int c){
        return threat_[r][c];
    }

    /**
     * Resets the player's moved state so that the player that should move can move.
     */
    public void endOfMove(){
        moved_ = false;
        reverted_ = false;
        canMove_ = true;
        blocked_ = false;
    }

    /**
     * Reverts the previous move done in the case of a check(an invalid move).
     */
    public void revertInCheck(){
        updateThreat();
        if(turnWhite_ && wKingCheck()){
            manualRevert();
        }else if(!turnWhite_ && bKingCheck()){
            manualRevert();
        }
        clearThreat();
    }

    public void manualRevert(){
        chessBoard_[lastMoved_[1]][lastMoved_[2]] = lastMoved_[0];
        chessBoard_[lastCaptured_[1]][lastCaptured_[2]] = lastCaptured_[0];
        reverted_ = true;
    }

    /**
     * Makes the piece at the original square take the target square.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     */
    public void takeSquare(int oR,int oC,int r,int c){
        lastMoved_[1] = oR;
        lastMoved_[2] = oC;
        lastMoved_[0] = chessBoard_[oR][oC];
        lastCaptured_[0] = chessBoard_[r][c];
        lastCaptured_[1] = r;
        lastCaptured_[2] = c;
        chessBoard_[oR][oC] = EMPTY;
        chessBoard_[r][c] = EMPTY;
        chessBoard_[r][c] = lastMoved_[0];
        moved_ = true;
    }

    /**
     * Changes the turn if a move hasn't been reverted.
     */
    public void changeTurn(){
        if(!reverted_){
            if(turnWhite_ == true){            
                turnWhite_ = false;
            }else{
                turnWhite_ = true;
            }
        }
    }

    /**
     * Moves the piece at the original square as a knight.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     */
    public void knightControl(int oR,int oC,int r,int c){
        if(!outOfBounds(oR,oC) && !outOfBounds(r,c)){
            int piece = chessBoard_[oR][oC];
            int colMoved = Math.abs(c-oC);
            int rowMoved = Math.abs(r-oR);
            if(rowMoved == 2 || colMoved == 2){
                if(rowMoved == 1|| colMoved ==1){
                    checkForMove(r,c,piece);
                    if(canMove_){
                        takeSquare(oR,oC,r,c);
                    }
                }
            }
        }
    }

    /**
     * Moves the piece at the original square as a pawn.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     */
    public void pawnControl(int oR,int oC,int r,int c){
        if(!outOfBounds(oR,oC) && !outOfBounds(r,c)){
            int piece = chessBoard_[oR][oC];
            int colMoved = Math.abs(c-oC);
            int rowMoved = r-oR;
            if(piece == WPAWN){
                if(oC == c){
                    if(oR-r == 1 && chessBoard_[r][oC] == EMPTY){
                        takeSquare(oR,oC,r,c);
                    }else if(oR-r == 2 && oR == 6 && chessBoard_[r][oC] == EMPTY){
                        takeSquare(oR,oC,r,c);
                    }
                }else if(colMoved == 1 && rowMoved == -1 && isBlack(chessBoard_[r][c])){
                    bishopControl(oR,oC,r,c);
                }
            }else if(piece == BPAWN){
                if(oC == c){
                    if(oR-r == -1 && chessBoard_[r][oC] == EMPTY){
                        takeSquare(oR,oC,r,c);
                    }else if(oR-r == -2 && oR == 1 && chessBoard_[r][oC] == EMPTY){
                        takeSquare(oR,oC,r,c);
                    }
                }else if(colMoved == 1 && rowMoved == 1 && isWhite(chessBoard_[r][c])){
                    bishopControl(oR,oC,r,c);
                }
            }
        }
    } 

    /**
     * Moves the piece at the original square as a king.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     */
    public void kingControl(int oR,int oC,int r,int c){
        if(!outOfBounds(oR,oC) && !outOfBounds(r,c)){
            int piece = chessBoard_[oR][oC];
            int colMoved = Math.abs(c-oC);
            int rowMoved = Math.abs(r-oR);
            if(oR == r && colMoved == 1){
                rookControl(oR,oC,r,c);
            }else if(oC == c && rowMoved == 1){
                rookControl(oR,oC,r,c);
            }else if(colMoved == 1 && rowMoved == 1){
                bishopControl(oR,oC,r,c);
            }
            if(moved_){
                if(piece == WKING){
                    wKing_[0] = r;
                    wKing_[1] = c;
                }else if(piece == BKING){
                    bKing_[0] = r;
                    bKing_[1] = c;
                }
            }
        }
    }

    /**
     * Moves the piece at the original square as a queen.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     */
    public void queenControl(int oR,int oC,int r,int c){
        bishopControl(oR,oC,r,c);
        rookControl(oR,oC,r,c);
    }

    /**
     * Moves the piece at the original square as a bishop.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     */
    public void bishopControl(int oR,int oC,int r,int c){     
        if(!outOfBounds(oR,oC) && !outOfBounds(r,c)){
            if(Math.abs(oC-c) == Math.abs(oR-r) && oR != r && oC != c){
                int hvDir = (r-oR)/Math.abs(r-oR);
                int spaces = Math.abs(r-oR);
                if(r-oR == c-oC){
                    bRMoves(oR,oC,hvDir,hvDir,spaces);
                }else{
                    bRMoves(oR,oC,hvDir,hvDir*-1,spaces);
                }
                if(canMove_){
                    takeSquare(oR,oC,r,c);
                }
            }
        }
    }

    /**
     * Moves the piece at the original square as a rook.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     */
    public void rookControl(int oR,int oC,int r,int c){
        int count = 1;
        if(!outOfBounds(oR,oC) && !outOfBounds(r,c)){
            if(oR == r ^ oC == c){
                if(oR != r && oC == c){
                    int vDir = (r-oR)/Math.abs(r-oR);
                    int spaces = Math.abs(r-oR);
                    bRMoves(oR,oC,vDir,0,spaces);
                }else if(oR == r && oC != c){
                    int hDir = (c-oC)/Math.abs(c-oC);
                    int spaces = Math.abs(c-oC);
                    bRMoves(oR,oC,0,hDir,spaces);
                } 
                if(canMove_){
                    takeSquare(oR,oC,r,c);
                }
            }
        }
    }

    /**
     * Checks if a piece at a square can move in a given direction.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param rInterval = The interval at which you check the row movement.
     * @param rInterval = The interval at which you check the column movement.
     * @param spaces = The amount of spaces the piece will move.
     */
    public void bRMoves(int oR,int oC,int rInterval,int cInterval,int spaces){
        int piece = chessBoard_[oR][oC];
        int count = 1;
        while(count <= spaces && canMove_){
            checkForMove(oR+(count*rInterval),oC+(count*cInterval),piece);
            count++;
        }
    }

    /**
     * Checks if a given piece can move into a certain square.
     * 
     * @param r = The row of the target square.
     * @param r = The column of the target square.
     * @param piece = The piece that you wish to check for the movement.
     */
    public void checkForMove(int r,int c,int piece){
        if(blocked_){
            canMove_ = false;
        }else if(!checkSquare(r,c,piece)){
            canMove_ = false;
        }else{
            if(checkBlock(r,c,piece)){
                blocked_ = true;
            }
        }
    }

    /**
     * Puts the squares in a line from the original square in threat.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     * @param rInterval = The interval at which you threaten the rows.
     * @param rInterval = The interval at which you threaten the columns.
     */
    public void bRThreat(int oR,int oC,int rInterval,int cInterval){
        boolean blocked = false;
        int piece = chessBoard_[oR][oC];
        int count = 1;
        while(!blocked){
            int newR = oR+(count*rInterval);
            int newC = oC+(count*cInterval);
            if(!outOfBounds(newR,newC)){
                int dPiece = chessBoard_[newR][newC];
                if(dPiece != EMPTY){
                    blocked = true;
                }
                threatSquare(newR,newC,piece);
            }else{
                blocked = true;
            }
            count++;
        }
    }

    /**
     * Sets the threat at the given square if the square has an opponent's piece.
     * Also sets the threat if the given square is empty and the given piece is not a pawn.
     * 
     * @param r = The row of the target square.
     * @param c = The column of the target square.
     * @param piece = The piece you wish to set the threat by.
     */
    public void threatSquare(int r,int c,int piece){
        if(!outOfBounds(r,c)){
            int dPiece = chessBoard_[r][c];
            if(isWhite(piece) && isBlack(dPiece)){
                setThreat(r,c,piece);
            }else if(isBlack(piece) && isWhite(dPiece)){
                setThreat(r,c,piece);
            }
            if(dPiece == EMPTY && piece != BPAWN && piece != WPAWN){
                setThreat(r,c,piece);
            }
        }
    }

    /**
     * Sets the threat as a king.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     */
    public void kingThreat(int oR,int oC){
        int piece = chessBoard_[oR][oC];
        threatSquare(oR-1,oC-1,piece);
        threatSquare(oR-1,oC,piece);
        threatSquare(oR-1,oC+1,piece);
        threatSquare(oR,oC+1,piece);
        threatSquare(oR+1,oC+1,piece);
        threatSquare(oR+1,oC,piece);
        threatSquare(oR+1,oC-1,piece);
        threatSquare(oR,oC-1,piece);
    }

    /**
     * Sets the threat of a pawn.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     */
    public void pawnThreat(int oR,int oC){
        int piece = chessBoard_[oR][oC];
        if(piece == WPAWN){
            threatSquare(oR-1,oC-1,piece);
            threatSquare(oR-1,oC+1,piece);
            if(!outOfBounds(oR-1,oC)){
                if(chessBoard_[oR-1][oC] == EMPTY){
                    threat_[oR-1][oC] += PAWNMOVABLE;
                    if(oR == 6 && chessBoard_[oR-2][oC] == EMPTY){
                        threat_[oR-2][oC] += PAWNMOVABLE;
                    }
                }
            }
        }else if(piece == BPAWN){
            threatSquare(oR+1,oC-1,piece);
            threatSquare(oR+1,oC+1,piece);
            if(!outOfBounds(oR+1,oC)){
                if(chessBoard_[oR+1][oC] == EMPTY){
                    threat_[oR+1][oC] += PAWNMOVABLE;
                    if(oR == 1 && chessBoard_[oR+2][oC] == EMPTY){
                        threat_[oR+2][oC] += PAWNMOVABLE;
                    }
                }
            }
        }
    }

    /**
     * Sets the threat as a knight.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     */
    public void knightThreat(int oR,int oC){
        int piece = chessBoard_[oR][oC];
        threatSquare(oR-1,oC-2,piece);
        threatSquare(oR-2,oC-1,piece);
        threatSquare(oR-2,oC+1,piece);
        threatSquare(oR-1,oC+2,piece);
        threatSquare(oR+1,oC+2,piece);
        threatSquare(oR+2,oC+1,piece);
        threatSquare(oR+2,oC-1,piece);
        threatSquare(oR+1,oC-2,piece);
    }

    /**
     * Sets the threat as a bishop.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     */
    public void bishopThreat(int oR,int oC){
        bRThreat(oR,oC,-1,-1);
        bRThreat(oR,oC,1,-1);
        bRThreat(oR,oC,-1,1);
        bRThreat(oR,oC,1,1);
    }

    /**
     * Sets the threat as a rook.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     */
    public void rookThreat(int oR,int oC){  
        bRThreat(oR,oC,1,0);
        bRThreat(oR,oC,-1,0);
        bRThreat(oR,oC,0,-1);
        bRThreat(oR,oC,0,1);
    }

    /**
     * Sets the threat as a queen.
     * 
     * @param oR = The row of the original square.
     * @param oC = The column of the original square.
     */
    public void queenThreat(int oR,int oC){
        rookThreat(oR,oC);
        bishopThreat(oR,oC);
    }

    /**
     * Clears the all of the threats currently on the board.
     */
    public void clearThreat(){
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                threat_[r][c] = EMPTY;
            }
        }
    }

    /**
     * Sets the threat at the given square according to the given piece.
     * 
     * @param row = The row of the target square.
     * @param col = The column of the target square.
     * @param piece = The piece you wish to set the threat by.
     */
    public void setThreat(int row,int col,int piece){
        int tSquare = threat_[row][col];
        if(isWhite(piece)){
            if(tSquare != WTHREAT && tSquare != WBTHREAT && tSquare != WTHREAT+PAWNMOVABLE && tSquare != WBTHREAT+PAWNMOVABLE){
                threat_[row][col] += WTHREAT;
            }
        }else if(isBlack(piece)){
            if(tSquare != BTHREAT && tSquare != WBTHREAT && tSquare != BTHREAT+PAWNMOVABLE && tSquare != WBTHREAT+PAWNMOVABLE){
                threat_[row][col] += BTHREAT;
            }
        }
    }

    /**
     * Updates the threat on the entire board.
     */
    public void updateThreat(){
        clearThreat();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                int piece = chessBoard_[r][c];
                if(piece == BROOK || piece == WROOK){
                    rookThreat(r,c);
                }else if(piece == BBISHOP || piece == WBISHOP){
                    bishopThreat(r,c);
                }else if(piece == BQUEEN || piece == WQUEEN){
                    queenThreat(r,c);
                }else if(piece == BKNIGHT || piece == WKNIGHT){
                    knightThreat(r,c);
                }else if(piece == BPAWN || piece == WPAWN){
                    pawnThreat(r,c);
                }else if(piece == BKING || piece == WKING){
                    kingThreat(r,c);
                }
            }
        }        
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                while(threat_[r][c] >= PAWNMOVABLE){
                    threat_[r][c] -= PAWNMOVABLE;
                }
            }
        }
    }

    public void saveTo(File file){
        try{
            FileWriter fw = new FileWriter(file);
            for(int r = 0;r < dimensions_;r++){
                for(int c = 0;c < dimensions_;c++){
                    fw.write(String.valueOf(chessBoard_[r][c]));
                    if(c < dimensions_-1){
                        fw.write(",");
                    }
                }
                fw.write("\r\n");
            }
            if(turnWhite_){
                fw.write("white");
            }else{
                fw.write("black");
            }
            fw.close();
        }catch(Exception e){
            System.out.println("Failed to save");
        }
    }

    public void loadFrom(File file){
        try{
            FileReader fr = new FileReader(file);
            Scanner sc = new Scanner(fr);
            for(int r = 0;r < dimensions_;r++){
                String[] line = sc.nextLine().split(",");
                for(int c = 0;c < dimensions_;c++){
                    chessBoard_[r][c] = Integer.parseInt(line[c]);
                }
            }
            if(sc.nextLine().compareTo("white") == 0){
                turnWhite_ = true;
            }else{
                turnWhite_ = false;
            }
        }catch(Exception e){
            System.out.println("Failed to load");
        }
    }
}
