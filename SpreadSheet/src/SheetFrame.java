import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

/**
 * ����JTable�Ĵ��ڣ�������
 * @author tgmerge
 */
@SuppressWarnings("serial")
public class SheetFrame extends JFrame implements ActionListener {
	
	static SheetFrame instance = null;
	static SheetFrame getInstance() {
		if (instance == null) {
			instance = new SheetFrame();
		}
		return instance;
	}
	
	private JTable       jtSheet;
	private JToolBar     jtbTools;
	private JButton      jbOpen;
	private JButton      jbSave;
	private JFileChooser jfcFile;
	
	private String       fileName;
	
	public SheetFrame() {
		
		// ��ֵ
		fileName = null;
		
		// ��������
		this.setTitle("SpreadSheet");
		this.setSize(800, 600);
		this.setLocation(200, 200);
		this.setName("SheetFrame");
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		
		// ���첿��
		jtSheet  = new JTable(10, 20);
		jtbTools = new JToolBar();
		jbOpen   = new JButton("��");
		jbSave   = new JButton("����");
		jfcFile  = new JFileChooser();
		
		// ���ñ������
		jtSheet.setFont(new Font("Consolas", Font.PLAIN, 12));
		jtSheet.setRowHeight(20);
		
		// �ļ�ѡ����
		jfcFile.setFileFilter(csvFileFilter());
		
		// ���ڲ���
		jtbTools.add(jbOpen, BorderLayout.NORTH);
		jtbTools.add(jbSave, BorderLayout.NORTH);
		this.add(jtbTools, BorderLayout.NORTH);
		this.add(jtSheet, BorderLayout.CENTER);
		
		// Listener
		jbOpen.addActionListener(this);
		jbSave.addActionListener(this);
		
		// ��ʾ����
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void openCsv() {
		int openVal = jfcFile.showOpenDialog(this);
		if(openVal == JFileChooser.APPROVE_OPTION) {
			File file     = jfcFile.getSelectedFile();
			this.fileName = file.getAbsolutePath();
			String[][] data   = Csv.importFile(this.fileName);
			String[]   header = Csv.getHeaders(data);
			this.getContentPane().remove(jtSheet);
			jtSheet = new JTable(data, header);
			jtSheet.setFont(new Font("Consolas", Font.PLAIN, 12));
			jtSheet.setRowHeight(20);
			this.add(jtSheet, BorderLayout.CENTER);
			this.revalidate();
		}
	}
	
	private void saveCsv() {
		int openVal = jfcFile.showSaveDialog(this);
		if(openVal == JFileChooser.APPROVE_OPTION) {
			File file     = jfcFile.getSelectedFile();
			this.fileName = file.getAbsolutePath();
		    TableModel dtm = jtSheet.getModel();
		    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
		    String[][] data = new String[nRow][nCol];
		    for (int i = 0 ; i < nRow ; i++)
		        for (int j = 0 ; j < nCol ; j++)
		            data[i][j] = (String) dtm.getValueAt(i,j);
		   
		    Csv.exportFile(this.fileName, data);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbOpen) {
			openCsv();
		}
		else if (e.getSource() == jbSave) {
			saveCsv();
		}
	}

	private FileFilter csvFileFilter() {
		return new FileFilter() {
			private final String[] okFileExtensions = new String[] {"csv"};
			public boolean accept(File file) {
				for (String extension : okFileExtensions)
					if (file.getName().toLowerCase().endsWith(extension))
						return true;
				return false;
			}
			public String getDescription() {
				return "Comma-separated values";
			}
		};
	}
	
}
