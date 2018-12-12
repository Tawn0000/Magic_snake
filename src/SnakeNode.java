
import java.awt.Color;

public class SnakeNode {
	private int x;
	private int y;
	Color c;
	public SnakeNode() {}
	public SnakeNode(int x, int y, Color c) {
		this.x = x;
		this.y = y;
		this .c = c;
	} 
	public int getX(){return x;}
	public int getY(){return y;}
	public Color getColor(){return c;}
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
	public void setColor(Color c){this.c = c;}
}

