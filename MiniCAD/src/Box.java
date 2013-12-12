import java.awt.Color;
import java.awt.Graphics;

public class Box extends Shape {
	public Box(int xa, int ya, int xb, int yb, Color c) {
		super(xa, ya, xb, yb, c);
	}
	
	@Override
	public void draw(Graphics g) {
		int x = (x1<=x2) ? x1 : x2;
		int y = (y1<=y2) ? y1 : y2;
		int w = Math.abs(x1-x2);
		int h = Math.abs(y1-y2);
		g.setColor(color);
		g.drawRect(x, y, w, h);
		g.fillRect(x, y, w, h);
	}
	
}
