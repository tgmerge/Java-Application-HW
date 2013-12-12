import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Text extends Shape {
	
	String text;
	
	public Text(int xa, int ya, int xb, int yb, Color c) {
		super(xa, ya, xb, yb, c);
		text = DrawPad.drawPad.getText();
	}

	@Override
	public void draw(Graphics g) {
		int fontSize = Math.abs(y2-y1)<40 ? Math.abs(y2-y1) : 40;
		Font font = new Font("Consolas", Font.PLAIN, fontSize );
		g.setColor(color);
		g.setFont(font);
		g.drawString(text, x1, (y1+y2+fontSize)/2);
	}

}
