import java.awt.Color;
import java.awt.Graphics;

/**
 * ��״
 * @author tgmerge
 *
 */
public abstract class Shape {
	
	enum ShapeType	{ CIRCLE, BOX, LINE, TEXT }     // ��״����

	protected int x1, y1;
	protected int x2, y2;
	protected int xt, xb;
	protected int yt, yb;
	protected final int RESIZE = 4;	// �����ֱ��İ뾶
	protected Color color;	// ��״��ɫ
	
	/**
	 * ���죬a,bΪ���ϡ����½�����
	 */
	public Shape(int xa, int ya, int xb, int yb, Color c) {
		x1 = xa;	y1 = ya;
		x2 = xb;	y2 = yb;
		xt = -1;    yt = -1;
		color = c;
	}
	
	/**
	 * �ڻ����л�������״
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * �����߿򣬵ȴ����Ĵ�С
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
	 * ���x, y�Ƿ�����״��
	 */
	public boolean isIn(int x, int y) {
		if((x1-x)*(x2-x) < 0 && (y1-y)*(y2-y) < 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ���x, y�Ƿ��ڵ�1��
	 */
	public boolean isOn1(int x, int y) {
		if( (x-x1)*(x-x1) + (y-y1)*(y-y1) <= RESIZE*RESIZE )
			return true;
		else
			return false;
	}
	
	/**
	 * ���x, y�Ƿ��ڵ�2��
	 */
	public boolean isOn2(int x, int y) {
		if( (x-x2)*(x-x2) + (y-y2)*(y-y2) <= RESIZE*RESIZE )
			return true;
		else
			return false;
	}
	
	/**
	 * �½�һ����״
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
	 * ����x, y�ı�һ����״�ĳߴ�
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
	 * ����c�ı���״��ɫ
	 */
	public void reColor(Color c) {
		color = c;
	}
	
	/**
	 * ����x, y�ı�һ����״��λ��
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
	 * ��������λ��
	 */
	public void endRepos() {
		xt = -1;
		yt = -1;
	}
}
