import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

@SuppressWarnings("serial")
public class DrawTools extends JFrame implements ActionListener, DocumentListener{

	static public DrawTools drawTools;	// 单例的
	
	private DrawPad drawPad;
	
	private Vector<Shape.ShapeType> vShape;
	
	private JComboBox<Shape.ShapeType> jcbShape;
	private JTextField jtfColor;
	private JTextField jtfText;
	private JButton jbOpen;
	private JButton jbSave;
	
	private JFileChooser fileChooser = new JFileChooser();
	
	public DrawTools() {
		drawTools = this;
		
		drawPad = DrawPad.drawPad;

		setTitle("MiniCAD Control");
		setSize(600, 80);
		setLocation(100, 510);
		setName("DrawTools");
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		setResizable(false);
		
		vShape = new Vector<Shape.ShapeType>();
		vShape.add(Shape.ShapeType.CIRCLE);
		vShape.add(Shape.ShapeType.BOX);
		vShape.add(Shape.ShapeType.LINE);
		vShape.add(Shape.ShapeType.TEXT);
		jcbShape = new JComboBox<Shape.ShapeType>(vShape);
		jtfColor = new JTextField(7);
		jtfColor.setText("000000");
		jtfText = new JTextField(20);
		jtfText.setText("Text to be drawn");
		jbOpen = new JButton("Open");
		jbSave = new JButton("Save");
		
		jcbShape.addActionListener(this);
		jbOpen.addActionListener(this);
		jbSave.addActionListener(this);
		jtfColor.getDocument().addDocumentListener(this);
		jtfText.getDocument().addDocumentListener(this);
		
		fileChooser.setFileFilter(ImageFileFilter());
		
		add(jcbShape);
		add(jtfColor);
		add(jtfText);
		add(jbOpen);
		add(jbSave);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jcbShape) {
			drawPad.setShape((Shape.ShapeType) jcbShape.getSelectedItem());
		}
		else if(e.getSource() == jbOpen) {
			int openVal = fileChooser.showOpenDialog(this);
			if(openVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					DrawPad.drawPad.setBackgroundImage(ImageIO.read(file));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource() == jbSave) {
			int openVal = fileChooser.showSaveDialog(this);
			if(openVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					ImageIO.write((RenderedImage) DrawPad.drawPad.getImage(), "png", file);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(e.getDocument() == jtfColor.getDocument()) {
			try {
				Document doc = e.getDocument();
				String s = "#"+doc.getText(0, doc.getLength());
				if(s.length() == 7) {
					if(drawPad.getShapeSelected() != null)
						drawPad.getShapeSelected().color = Color.decode(s);
					else
						drawPad.setColor(Color.decode(s));
					drawPad.reDraw();
				}
			} catch (BadLocationException e2) {
				e2.printStackTrace();
			}
		} else if(e.getDocument() == jtfText.getDocument()) {
			Document doc = e.getDocument();
			try {
				DrawPad.drawPad.setText(doc.getText(0, doc.getLength()));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		insertUpdate(e);
	}

	private FileFilter ImageFileFilter() {
		return new FileFilter() {
			private final String[] okFileExtensions = new String[] {"jpg", "png", "gif", "bmp", "wbmp", "jpeg"};
			public boolean accept(File file) {
				for (String extension : okFileExtensions)
					if (file.getName().toLowerCase().endsWith(extension))
						return true;
				return false;
			}
			public String getDescription() {
				return "Image file";
			}
		};
	}
	
}