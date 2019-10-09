import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class Label here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Label extends Actor
{
    private String text_;
    private Color colour_;
    private int size_;
    public Label(String text, int size){
        super();
        text_ = text;
        size_ = size;
        colour_ = Color.WHITE;
        
        updateImage();
    }
    
    public void setColour(Color c){
        colour_ = c;
        updateImage();
    }
    
    public void setText(String text){
        text_ = text;
        updateImage();
    }

    public void setSize(int size){
        size_ = size;
        updateImage();
    }
    
    private void updateImage(){
        GreenfootImage image = new GreenfootImage(text_, size_, Color.BLACK,colour_);
        setImage(image);
    }
}
