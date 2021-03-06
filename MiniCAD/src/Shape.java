import java.awt.Color;
import java.awt.Graphics;

/**
 * 形状
 * @author tgmerge
 *
 */
public abstract class Shape {
	
	enum ShapeType	{ CIRCLE, BOX, LINE, TEXT }     // 形状类型

	protected int x1, y1;
	protected int x2, y2;
	protected int xt, xb;
	protected int yt, yb;
	protected final int RESIZE = 4;	// 控制手柄的半径
	protected Color color;	// 形状颜色
	
	/**
	 * 构造，a,b为左上、右下角坐标
	 */
	public Shape(int xa, int ya, int xb, int yb, Color c) {
		x1 = xa;	y1 = ya;
		x2 = xb;	y2 = yb;
		xt = -1;    yt = -1;
		color = c;
	}
	
	/**
	 * 在画板中画出该形状
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * 画出边框，等待更改大小
	 */
	public void drawWithResize(Graphics g) {
		draw(g);
		g.setColor(Color.BLACK);
		g.drawOval(x1-RESIZE, y1-RESIZE, RESIZE*2, RESIZE*2);
		g.drawOval(x2-RESIZE, y2-RESIZE, RESIZE*2, RESIZE*2);
		g.setColor(Color.WHITE);
		int r1 = RESIZE - 1;
		g.drawOval(x1-r1, y1-r1, r1*2, r1*2);
		g.drawOval(x2-r1, y2-r1, r1*2, r1*2);
	}

	/**
	 * 检查x, y是否在形状内
	 */
	public boolean isIn(int x, int y) {
		if((x1-x)*(x2-x) < 0 && (y1-y)*(y2-y) < 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检查x, y是否在点1上
	 */
	public boolean isOn1(int x, int y) {
		if( (x-x1)*(x-x1) + (y-y1)*(y-y1) <= RESIZE*RESIZE )
			return true;
		else
			return false;
	}
	
	/**
	 * 检查x, y是否在点2上
	 */
	public boolean isOn2(int x, int y) {
		if( (x-x2)*(x-x2) + (y-y2)*(y-y2) <= RESIZE*RESIZE )
			return true;
		else
			return false;
	}
	
	/**
	 * 新建一个形状
	 */
	public static Shape newShape(int x, int y, ShapeType t, Color c) {
		Shape s = null;
		switch(t) {
		case BOX:
			s = new Box(x, y, x, y, c);
			break;
		case CIRCLE:
			s = new Circle(x, y, x, y, c);
			break;
		case LINE:
			s = new Line(x, y, x, y, c);
			break;
		case TEXT:
			s = new Text(x, y, x, y, c);
			break;
		default:
			System.out.print("[newShape]wrong type");
		}
		return s;
	}
	
	/**
	 * 根据x, y改变一个形状的尺寸
	 */
	public void resize(int point, int x, int y) {
		switch(point) {
		case 1:
			x1 = x;
			y1 = y;
			break;
		case 2:
			x2 = x;
			y2 = y;
			break;
		default:
			System.out.println("[reSizeShape]wrong point");
		}
	}
		
	/**
	 * 根据c改变形状颜色
	 */
	public void reColor(Color c) {
		color = c;
	}
	
	/**
	 * 根据x, y改变一个形状的位置
	 */
	public void repos(int x, int y) {
		if( xt < 0 ) {
			xt = x;  xb = x1;
			yt = y;  yb = y1;
		} else {
			int w = x2 - x1;
			int h = y2 - y1;
			x1 = xb + (x - xt);
			y1 = yb + (y - yt);
			x2 = x1+w;
			y2 = y1+h;
		}
	}
	
	
	/**
	 * 结束更改位置
	 */
	public void endRepos() {
		xt = -1;
		yt = -1;
	}
}
