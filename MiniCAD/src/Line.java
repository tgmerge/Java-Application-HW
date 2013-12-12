import java.awt.Color;
import java.awt.Graphics;

public class Line extends Shape {
	public Line(int xa, int ya, int xb, int yb, Color c) {
		super(xa, ya, xb, yb, c);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
	}
}
