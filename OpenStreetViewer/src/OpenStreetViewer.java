import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

/**
 * 主程序
 * @author tgmerge
 */
@SuppressWarnings("serial")
public class OpenStreetViewer extends JFrame {

	JPanel jpMain;
	Image image;
	
	/** 主程序部分 **/
	public OpenStreetViewer() {
		// JFrame jfWindow
		this.setSize(800, 600);
		this.setTitle("OpenStreetMap Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		// JPanel jpMain
		jpMain = new JPanel();
		jpMain.setSize(800, 600);
		this.add(jpMain);
		
		// show main window
		this.setVisible(true);
		
		// prepare image
		Container contentPanel = this.getContentPane();
		image = new BufferedImage(contentPanel.getWidth(), contentPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
		image.getGraphics().fillRect(0, 0, 800, 600);
		
		// open file and draw
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		jfc.setFileFilter(this.OsmFileFilter());
		int openVal = jfc.showOpenDialog(this);
		if(openVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			jpMain.getGraphics().drawString("Processing...", 10, 10);
			@SuppressWarnings("unused") Osm o = new Osm(file.getPath(), image);
		} else {
			System.exit(0);
		}

		// draw to JPanel
		jpMain.getGraphics().drawImage(image, 0, 0, jpMain.getWidth(), jpMain.getHeight(), Color.WHITE, null);
	}
	
	public void paint(Graphics g) {
        super.paint(g);
		jpMain.getGraphics().drawImage(image, 0, 0, jpMain.getWidth(), jpMain.getHeight(), Color.WHITE, null);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused") OpenStreetViewer me = new OpenStreetViewer();
	}
	
	private FileFilter OsmFileFilter() {
		return new FileFilter() {
			private final String[] okFileExtensions = new String[] {"osm"};
			public boolean accept(File file) {
				for (String extension : okFileExtensions)
					if (file.getName().toLowerCase().endsWith(extension))
						return true;
				return false;
			}
			public String getDescription() {
				return "OpenStreetMap osm";
			}
		};
	}
}
