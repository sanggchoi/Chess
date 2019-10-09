import greenfoot.*;
import java.awt.Color;
import java.util.List;
import java.io.File;
/**
 * Write a description of class ChessBoard here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChessBoard extends World
{
    private ChessLogic logic_;
    private Label lb_;
    private boolean click1_;
    private boolean click2_;
    private int c1R_;
    private int c1C_;
    private int c2R_;
    private int c2C_;
    /**
     * Constructor for objects of class ChessBoard.
     * 
     */
    public ChessBoard(){    
        super(8,9,75); 

        GreenfootImage bg = getBackground();
        Color c = new Color(207,219,250);
        int cellSize = getCellSize();
        bg.setColor(c);
        bg.fillRect(0, (getHeight() - 1)*cellSize, getWidth()*cellSize, 2*cellSize);

        click1_ = false;
        click2_ = false;
        logic_ = new ChessLogic();
        lb_ = new Label("White's Turn",40);
        lb_.setColour(c);
        addObject(lb_,1,8);

        updatePieces();
    }
    
    public void save(){
        File f = new File("H:\\Desktop\\chessSave.txt");
        logic_.saveTo(f);
    }
    
    public void load(){
        File f = new File("H:\\Desktop\\chessSave.txt");
        logic_.loadFrom(f);
        clearBoard();
        updatePieces();
        if(logic_.turnWhite()){
            lb_.setText("White's Turn");
        }else{
            lb_.setText("Black's Turn");
        }
    }

    public void act(){
        if(Greenfoot.mouseClicked(null)){
            clickManage();
            if(click1_){
                highlightMoves();
            }
            if(click1_ && click2_){
                distinguishTurn();
                if(logic_.moved()){
                    logic_.revertInCheck();
                    if(logic_.reverted()){
                        Greenfoot.playSound("WrongMove.wav");
                    }
                    logic_.changeTurn();
                }
                clearHighlight();
                click1_ = false;
                click2_ = false;
            }
            logic_.endOfMove();
            clearBoard();
            updatePieces();
        }
        if(logic_.turnWhite()){
            lb_.setText("White's Turn");
        }else{
            lb_.setText("Black's Turn");
        }
    }

    public void clickManage(){
        MouseInfo mi = Greenfoot.getMouseInfo();
        int row = mi.getY();
        int col = mi.getX();
        if(!logic_.outOfBounds(row,col)){
            if(!click1_ && !click2_ && logic_.getPieceAt(row,col) != logic_.EMPTY){
                click1_ = true;
                c1R_ = mi.getY();
                c1C_ = mi.getX();
            }else if(click1_ && !click2_){
                click2_ = true;
                c2R_ = mi.getY();
                c2C_ = mi.getX();
            }
        }
    }

    public void distinguishTurn(){
        int piece = logic_.getPieceAt(c1R_,c1C_);
        if(logic_.turnWhite()){
            if(piece == logic_.WROOK){
                logic_.rookControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.WBISHOP){
                logic_.bishopControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.WQUEEN){
                logic_.queenControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.WKING){
                logic_.kingControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.WPAWN){
                logic_.pawnControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.WKNIGHT){
                logic_.knightControl(c1R_,c1C_,c2R_,c2C_);
            }
        }else{
            if(piece == logic_.BROOK){
                logic_.rookControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.BBISHOP){
                logic_.bishopControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.BQUEEN){
                logic_.queenControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.BKING){
                logic_.kingControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.BPAWN){
                logic_.pawnControl(c1R_,c1C_,c2R_,c2C_);
            }else if(piece == logic_.BKNIGHT){
                logic_.knightControl(c1R_,c1C_,c2R_,c2C_);
            }
        }
    }

    public int getThreat(int r,int c){
        return logic_.getThreat(r,c);
    }

    public void highlightMoves(){
        logic_.clearThreat();
        int piece = logic_.getPieceAt(c1R_,c1C_);
        if(logic_.turnWhite()){
            if(piece == logic_.WROOK){
                logic_.rookThreat(c1R_,c1C_);
            }else if(piece == logic_.WBISHOP){
                logic_.bishopThreat(c1R_,c1C_);
            }else if(piece == logic_.WQUEEN){
                logic_.queenThreat(c1R_,c1C_);
            }else if(piece == logic_.WKING){
                logic_.kingThreat(c1R_,c1C_);
            }else if(piece == logic_.WPAWN){
                logic_.pawnThreat(c1R_,c1C_);
            }else if(piece == logic_.WKNIGHT){
                logic_.knightThreat(c1R_,c1C_);
            }
        }else{
            if(piece == logic_.BROOK){
                logic_.rookThreat(c1R_,c1C_);
            }else if(piece == logic_.BBISHOP){
                logic_.bishopThreat(c1R_,c1C_);
            }else if(piece == logic_.BQUEEN){
                logic_.queenThreat(c1R_,c1C_);
            }else if(piece == logic_.BKING){
                logic_.kingThreat(c1R_,c1C_);
            }else if(piece == logic_.BPAWN){
                logic_.pawnThreat(c1R_,c1C_);
            }else if(piece == logic_.BKNIGHT){
                logic_.knightThreat(c1R_,c1C_);
            }
        }
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(logic_.getThreat(r,c) > 0){
                    addObject(new Highlight(),c,r);
                }
            }
        }
        logic_.clearThreat();
    }

    public void clearHighlight(){
        List<Highlight> hList = getObjects(Highlight.class);
        for(Highlight h : hList){
            removeObject(h);
        }
    }

    public void clearBoard(){
        List<Pieces> pList = getObjects(Pieces.class);
        for(Pieces p : pList){
            removeObject(p);
        }
    }

    public void updatePieces(){
        logic_.updateThreat();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                int piece = logic_.getPieceAt(r,c);
                if(piece == logic_.BPAWN){
                    addObject(new BlackPawn(),c,r);
                }else if(piece == logic_.BROOK){
                    addObject(new BlackRook(),c,r);
                }else if(piece == logic_.BKNIGHT){
                    addObject(new BlackKnight(),c,r);
                }else if(piece == logic_.BBISHOP){
                    addObject(new BlackBishop(),c,r);
                }else if(piece == logic_.BQUEEN){
                    addObject(new BlackQueen(),c,r);
                }else if(piece == logic_.BKING){
                    addObject(new BlackKing(),c,r);
                }else if(piece == logic_.WPAWN){
                    addObject(new WhitePawn(),c,r);
                }else if(piece == logic_.WROOK){
                    addObject(new WhiteRook(),c,r);
                }else if(piece == logic_.WKNIGHT){
                    addObject(new WhiteKnight(),c,r);
                }else if(piece == logic_.WBISHOP){
                    addObject(new WhiteBishop(),c,r);
                }else if(piece == logic_.WQUEEN){
                    addObject(new WhiteQueen(),c,r);
                }else if(piece == logic_.WKING){
                    addObject(new WhiteKing(),c,r);
                }
            }
        }
    }
}
