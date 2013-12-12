import java.awt.Color;
import java.awt.Graphics;

public class Circle extends Shape {
	public Circle(int xa, int ya, int xb, int yb, Color c) {
		super(xa, ya, xb, yb, c);
	}

	@Override
	public void draw(Graphics g) {
		int x = (x1<=x2) ? x1 : x2;
		int y = (y1<=y2) ? y1 : y2;
		int w = Math.abs(x1-x2);
		int h = Math.abs(y1-y2);
		g.setColor(color);
		g.drawOval(x, y, w, h);
		g.fillOval(x, y, w, h);
	}
	
}
