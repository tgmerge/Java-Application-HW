import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


/**
 * ����
 * @author tgmerge
 */

@SuppressWarnings({ "unused", "serial" })
public class DrawPad extends JFrame implements MouseListener, MouseMotionListener{

	static public DrawPad drawPad;	// ������

	
	enum Stat		{ IDLE, SELECT, EDIT }			// �����״̬
	
	private Shape.ShapeType brushShape = Shape.ShapeType.CIRCLE;
	private Color		    brushColor = Color.BLACK;
	private Stat		    brushStat  = Stat.IDLE;
	private int 			brushEdit  = 1;						// �����е��ֱ�
	private String          brushText  = "Text to be drawn";    // Ҫ��������
	
	private Vector<Shape> shapes 		= new Vector<Shape>();	// ������״���б�
	private Shape         shapeSelected = null;					// ����ѡ���е���״
	private Image         background 	= null;					// ����ͼƬ
	private Image         image         = null;                 // ��ʾ����
	private Graphics      graphics;								// Graphics
	
	// - - -
	
	/**
	 * ���췽�����������塣
	 */
	public DrawPad() {
		drawPad = this;
		setTitle("MiniCAD");
		setSize(600, 400);
		setLocation(100, 100);
		setName("DrawPad");
		setVisible(true);
		setResizable(false);
		
		image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		graphics = image.getGraphics();
		graphics.setPaintMode();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		reDraw();
	}
	
	private void clearShapes() {
		shapes = new Vector<Shape>();
	}
	
	/**
	 * �ı仭��ĵ�ǰ״̬
	 */
	public void setStat(final Stat newStat) {
		System.out.println(newStat);
		brushStat = newStat;
	}
	public Stat getStat() {
		return brushStat;
	}
	
	
	/**
	 * �ı���һ����״����
	 */
	public void setShape(final Shape.ShapeType newShape) {
		brushShape = newShape;
	}
	
	
	
	/**
	 * �ı���һ����״��ɫ
	 */
	public void setColor(final Color newColor) {
		brushColor = newColor;
		graphics.setColor(newColor);
	}
	
	/**
	 * �ı�Ҫд������
	 */
	public void setText(final String s) {
		brushText = s;
	}
	public String getText() {
		return brushText;
	}
	
	/**
	 * ȡѡ���˵���״
	 */
	public Shape getShapeSelected() {
		return shapeSelected;
	}
	
	/**
	 * �ı䱳��ͼƬ
	 */
	public void setBackgroundImage(Image i) {
		shapeSelected = null;
		setStat(Stat.IDLE);
		clearShapes();
		background = i;
		reDraw();
	}
	public Image getImage() {
		shapeSelected = null;
		setStat(Stat.IDLE);
		reDraw();
		return image;
	}
	
	
	
	//- - -
	
	/**
	 * ��vector��˳���ػ滭�壬������ѡ��
	 */
	public void reDraw() {
		if(background != null) {
			graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		} else {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, getWidth(), getHeight());
		}
		Iterator<Shape> iter = shapes.iterator();
		while(iter.hasNext()) {
			iter.next().draw(graphics);
		}
		if(shapeSelected != null) {
			shapeSelected.drawWithResize(graphics);
		}
		this.getGraphics().drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

	
	/**
	 * �򻭰����һ����״���ػ滭��
	 */
	private void addShape(Shape s) {
		shapes.addElement(s);
	    setStat(Stat.IDLE);
	    reDraw();
	}
	
	
	/**
	 * ѡ��һ����״���ػ滭��
	 */
	private void selectShape(Shape s) {
		if(shapes.indexOf(s) == -1) {
			System.out.println("[selectShape]can't find shape");
			return;
		}
		shapeSelected = s;
		setStat(Stat.SELECT);
		reDraw();
	}
	
	
	/**
	 * ȡ����״ѡ��
	 */
	private void unselectShape() {
		shapeSelected = null;
		setStat(Stat.IDLE);
		reDraw();
	}
	
	
	/**
	 * �༭��״
	 */
	private void editShape(Shape s, int point) {
		shapeSelected = s;
		setStat(Stat.EDIT);
		brushEdit = point;
		reDraw();
	}
	
	
	/**
	 * �˳��༭״̬,�ص�ѡ��״̬
	 */
	private void uneditShape() {
		setStat(Stat.SELECT);
		reDraw();
	}
	
	
	/**
	 * �����ĳ��ʱ���Ϸ����Ǹ���״��û���򷵻�null��
	 */
	private Shape topShapeByPos(int x, int y) {
		Iterator<Shape> iter = shapes.iterator();
		Shape result = null;
		Shape s = null;
		while(iter.hasNext()) {
			s = iter.next();
			if(s.isIn(x, y)) {
				result = s;
			}
		}
		return result;
	}

	
	/**
	 * ���԰�λ��ѡ��һ����״��ʧ�ܷ���null���ص�idle���ɹ������ѡ��״̬������selected
	 */
	private Shape selectShapeByPos(int x, int y) {
		Shape s = topShapeByPos(x, y);
		if(s != null) {
			selectShape(s);
		} else {
			brushStat = Stat.IDLE;
		}
		return s;
	}
	
	
	
	// - - -
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		switch(getStat()) {
			case IDLE:
			case SELECT:
				/* Check if there's a shape under mouse.
				 *   if so, select it.
				 *   if not, unselect any shape.
				 */
				Shape s = topShapeByPos(e.getX(), e.getY());
				if(s != null) {
					selectShape(s);
				} else {
					unselectShape();
				}
				break;
			default:
				break;
		}
	}


	/**
	 * ���°����Ĵ���
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		switch(brushStat) {
			case SELECT:
				/* SELʱ���£����ֱ��ڹ���������EDIT��
				 * ���򣬸�����ѡ��״
				 */
				if(shapeSelected.isOn2(e.getX(), e.getY())) {
					editShape(shapeSelected, 2);
				} else if(shapeSelected.isOn1(e.getX(), e.getY())) {
					editShape(shapeSelected, 1);
				} else {
					selectShapeByPos(e.getX(), e.getY());
				}
			default:
				break;
		}
	}

	/**
	 * �ͷŰ����Ĵ���
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		switch(getStat()) {
		case SELECT:
			/* SELʱ�ͷţ���ֹ�ƶ�
			 */
			shapeSelected.endRepos();
			break;
		case EDIT:
			/* EDIT�ͷţ�����SEL��
			 */
			uneditShape();
			break;
		default:
			break;
		}
		
	}


	/**
	 * �϶������¼�����
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		Shape s;
		switch(getStat()) {
			case EDIT:
				/* EDIT���϶������Ĵ�С
				 */
				shapeSelected.resize(brushEdit, e.getX(), e.getY());
				reDraw();
				break;
			case IDLE:
				/* IDLE���϶�����������״���漴����EDIT���Ĵ�С
				 * => EDIT(2)
				 */
				s = Shape.newShape(e.getX(), e.getY(), brushShape, brushColor);
				addShape(s);
				selectShape(s);
				editShape(s, 2);
				break;
			case SELECT:
				/* SEL���϶��������Ѿ�ѡ���˵���״�������λ�ã�����ȡ��ѡ�����IDLE
				 */
				if(shapeSelected.isIn(e.getX(), e.getY())) {
					shapeSelected.repos(e.getX(), e.getY());
					reDraw();
				} else {
					unselectShape();
				}
				break;
			default:
				break;
		}
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO �Զ����ɵķ������	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO �Զ����ɵķ������
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO �Զ����ɵķ������
	}

}
