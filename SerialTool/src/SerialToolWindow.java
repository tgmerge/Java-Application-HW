import gnu.io.CommPortIdentifier;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * 主程序窗口
 * @author tgmerge
 */
@SuppressWarnings("serial")
public class SerialToolWindow extends JFrame implements ActionListener, KeyListener, WindowListener {

	/** 单例的 **/
	static SerialToolWindow instance = null;
	public static SerialToolWindow getInstance() {
		return instance;
	}
	
	// UI Components
	JPanel jpCom;
		JComboBox<String> jcbPort;		String[] jcbPortData   = {"COM1", "COM2", "COM3", "COM4", "COM5", "COM6"};
		JComboBox<String> jcbBand;		String[] jcbBandData   = {"9600", "1200", "2400", "4800", "19200", "38400", "57600", "115200"};
		JComboBox<String> jcbData;		String[] jcbDataData   = {"8", "7", "6", "5"};
		JComboBox<String> jcbStop;		String[] jcbStopData   = {"1", "1.5", "2"};
		JComboBox<String> jcbParity;	String[] jcbParityData = {"NONE", "EVEN", "ODD"};
	JPanel jpControl;
		JLabel  jlStat;
		JButton jbStart;
		JButton jbStop;
		JButton jbClear;
	JPanel jpReceive;
		JTextArea jtaReceiveAddr;
		JTextArea jtaReceiveHex;
		JTextArea jtaReceiveChar;
	JPanel jpSend;
		JButton jbSend;
		JTextField jtfSend;
	
	int receivedLength = 0;
	
	final int TEXT_HEIGHT = 15;
	
	/** 初始化UI **/
	private void initGui() {
		setUIFont (new javax.swing.plaf.FontUIResource("Monospaced",Font.PLAIN,14));
		
		// this
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(660, 500);
		this.setResizable(false);
		this.setTitle("SerialTool");
		
		// jpSend
		jpSend = new JPanel();
		jpSend.setLayout(new FlowLayout());
		
		jbSend = new JButton("Send hex");
		jbSend.setPreferredSize(new Dimension(100, 30));
		jbSend.setEnabled(false);
		jpSend.add(jbSend);
		
		jtfSend = new JTextField(65);
		jtfSend.setPreferredSize(new Dimension(100, 30));
		jpSend.add(jtfSend);
		
		// jpReceive
		jpReceive = new JPanel();
		jpReceive.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		jtaReceiveAddr = new JTextArea("00000000  ", TEXT_HEIGHT, 10);
		jtaReceiveAddr.setPreferredSize(jtaReceiveAddr.getSize());
		jtaReceiveAddr.setEditable(false);
		
		jtaReceiveHex = new JTextArea("00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00  ", TEXT_HEIGHT, 50);
		jtaReceiveHex.setPreferredSize(jtaReceiveHex.getSize());
		jtaReceiveHex.setEditable(false);

		jtaReceiveChar = new JTextArea("00000000 00000000", TEXT_HEIGHT, 17);
		jtaReceiveChar.setPreferredSize(jtaReceiveChar.getSize());
		jtaReceiveChar.setEditable(false);
		
		JLabel jlReceive = new JLabel("Position   0  1  2  3  4  5  6  7   8  9  A  B  C  D  E  F  01234567 89ABCDEF");
		jlReceive.setPreferredSize(new Dimension(jtaReceiveAddr.getPreferredSize().width + jtaReceiveHex.getPreferredSize().width + jtaReceiveChar.getPreferredSize().width, 20));
		jlReceive.setAlignmentX(LEFT_ALIGNMENT);
		jlReceive.setAlignmentY(BOTTOM_ALIGNMENT);
		
		jpReceive.setMinimumSize(new Dimension(jlReceive.getPreferredSize().width, 0));
		jpReceive.setMaximumSize(new Dimension(jlReceive.getPreferredSize().width, 1000));
		jpReceive.add(jlReceive);
		
		jpReceive.add(jtaReceiveAddr);
		jpReceive.add(jtaReceiveHex);
		jpReceive.add(jtaReceiveChar);
		
		// jpControl
		jpControl = new JPanel();
		jpControl.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
		
		jlStat = new JLabel("STOPPED");
		jlStat.setBorder(BorderFactory.createLineBorder(Color.black));
		jlStat.setHorizontalAlignment(SwingConstants.CENTER);;
		jlStat.setPreferredSize(new Dimension(100, 30));
		jpControl.add(jlStat);
		
		jbStart = new JButton("Start");
		jbStart.setPreferredSize(new Dimension(100, 30));
		jpControl.add(jbStart);
		
		jbStop = new JButton("Stop");
		jbStop.setPreferredSize(new Dimension(100, 30));
		jpControl.add(jbStop);
		
		jbClear = new JButton("Clear");
		jbClear.setPreferredSize(new Dimension(100, 30));
		jpControl.add(jbClear);
		
		// jpCom
		jpCom = new JPanel();
		jpCom.setLayout(new FlowLayout());
		
		
		Vector<String> portList = new Vector<String>();
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			portList.add(currPortId.getName());
		}
		if(portList.isEmpty()) {
			jcbPort = new JComboBox<String>(jcbPortData);
		} else {
			jcbPort = new JComboBox<String>(portList);
		}
		jcbPort.setPreferredSize(new Dimension(80, 25));
		jpCom.add(new JLabel("Port"));
		jpCom.add(jcbPort);
		
		jcbBand = new JComboBox<String>(jcbBandData);
		jcbBand.setPreferredSize(new Dimension(80, 25));
		jpCom.add(new JLabel("Band"));
		jpCom.add(jcbBand);
		
		jcbData = new JComboBox<String>(jcbDataData);
		jcbData.setPreferredSize(new Dimension(80, 25));
		jpCom.add(new JLabel("Data"));
		jpCom.add(jcbData);
		
		jcbStop = new JComboBox<String>(jcbStopData);
		jcbStop.setPreferredSize(new Dimension(80, 25));
		jpCom.add(new JLabel("Stop"));
		jpCom.add(jcbStop);
		
		jcbParity = new JComboBox<String>(jcbParityData);
		jcbParity.setPreferredSize(new Dimension(80, 25));
		jpCom.add(new JLabel("Parity"));
		jpCom.add(jcbParity);
		
		// this.getContentPane
		Container thisPanel = this.getContentPane();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		thisPanel.add(jpCom);
		thisPanel.add(jpControl);
		thisPanel.add(jpReceive);
		thisPanel.add(jpSend);
		
		this.serialClear();
		
		System.out.println("[SerialToolWindow.initGui]Finished.");
	}
	
	/** 绑定事件 **/
	private void initListener() {
		jbStart.addActionListener(this);
		jbStop.addActionListener(this);
		jbClear.addActionListener(this);
		jbSend.addActionListener(this);
		jtfSend.addKeyListener(this);
		this.addWindowListener(this);
		
		System.out.println("[SerialToolWindow.initListener]Finished.");
	}
	
	/** 设置全局字体 **/
	@SuppressWarnings("rawtypes")
	public static void setUIFont (javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put (key, f);
		}
	}  
	
	public SerialToolWindow() {
		SerialToolWindow.instance = this;
		this.initGui();
		this.initListener();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == jbStart) {
				this.serialStart();
			} else if(e.getSource() == jbStop) {
				this.serialStop();
			} else if(e.getSource() == jbSend) {
				this.serialSend();
			} else if(e.getSource() == jbClear) {
				this.serialClear();
			}
		} catch (SerialException e1) {
			JOptionPane.showMessageDialog(null, e1.toString());
		}
	}
	
	/** START按钮事件 **/
	private void serialStart() throws SerialException {
		String portName = (String) jcbPort.getSelectedItem();
		String timeout = "200";
		String dataRate = (String) jcbBand.getSelectedItem();
		String dataBits = (String) jcbData.getSelectedItem();
		String dataStop = (String) jcbStop.getSelectedItem();
		String parity = (String) jcbParity.getSelectedItem();
		
		// 初始化端口
		SerialComm.newInstance(portName, timeout, dataRate, dataBits, dataStop, parity);
		SerialComm.getInstance().startListening();
		
		// 禁用/启用组件
		for(Component item: jpCom.getComponents()) {
			item.setEnabled(false);
		}
		jlStat.setText("LISTENING");
		jbSend.setEnabled(true);
		
		System.out.println("[SerialToolWindow.serialStart]Start listening.");
	}
	
	/** STOP按钮事件 **/
	private void serialStop() {
		// 关闭端口
		SerialComm.closeInstance();
		
		// 禁用/启用组件
		for(Component item: jpCom.getComponents()) {
			item.setEnabled(true);
		}
		jlStat.setText("STOPPED");
		jbSend.setEnabled(false);
	
		System.out.println("[SerialToolWindow.serialStop]Stop listening.");
	}
	
	/** SEND按钮事件 **/
	private void serialSend() throws SerialException {
		String[] strArr = jtfSend.getText().split(",");
		int[] data = new int[strArr.length];
		for (int i = 0; i < strArr.length; i ++) {
			try {
				data[i] += Integer.parseInt(strArr[i], 16);
			} catch(NumberFormatException e) {
				throw new SerialException("[SerialToolWindow.serialSend]Invalid data: " + strArr[i]);
			}
		}
		SerialComm.getInstance().sendData(data);
		System.out.println("[SerialToolWindow.serialSend]Send: " + jtfSend.getText());
	}
	
	/** CLEAR按钮事件 **/
	private void serialClear() {
		receiveClear();
		System.out.println("[SerialToolWindow.serialClear]Cleared");
	}
	
	/** 从接口收到数据 **/
	public synchronized void serialReceive(Integer[] dataArr) {
		receiveAppend(dataArr);
		System.out.println("[SerialToolWindow.serialReceive]Received data." );
	}
	
	/** 清除数据记录 **/
	private void receiveClear() {
		this.receivedLength = 0;
		jtaReceiveAddr.setText("00000000");
		jtaReceiveHex.setText("");
		jtaReceiveChar.setText("");
	}
	
	/** 添加数据到显示中 **/
	private synchronized void receiveAppend(Integer[] data) {
		int pos = receivedLength - 1;
		
		for (int b : data) {
			pos ++;
			String bStr = Integer.toHexString(b & 0xFF);
			if(bStr.length() < 2) bStr = "0" + bStr;
			String bChr = "" + ((b>32 && b<128) ? (char) b : ".");
			jtaReceiveHex.append(bStr + " ");
			jtaReceiveChar.append(bChr);
			if (pos % 16 == 7) {
				jtaReceiveHex.append(" ");
				jtaReceiveChar.append(" ");
			}
			if (pos % 16 == 15) {
				String posStr = Integer.toHexString(pos+1);
				while(posStr.length() < 8) posStr = "0" + posStr;
				jtaReceiveAddr.append("\n" + posStr);
				jtaReceiveHex.append(" \n");
				jtaReceiveChar.append("\n");
			}
		}
		
		// 不使用JScrollPanel的滚动
		while(jtaReceiveAddr.getText().split("\n").length > TEXT_HEIGHT)
			jtaReceiveAddr.setText(jtaReceiveAddr.getText().replaceFirst("^.*\n", ""));
		while(jtaReceiveHex.getText().split("\n").length > TEXT_HEIGHT)
			jtaReceiveHex.setText(jtaReceiveHex.getText().replaceFirst("^.*\n", ""));
		while(jtaReceiveChar.getText().split("\n").length > TEXT_HEIGHT)
			jtaReceiveChar.setText(jtaReceiveChar.getText().replaceFirst("^.*\n", ""));
		
		receivedLength += data.length;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// 发送框输入后自动分组
		if (e.getSource() == jtfSend) {
			String str = jtfSend.getText().replaceAll(",", "").toUpperCase();
			str = str.replaceAll("([^,][^,])(?=[^,])", "$1,");
			jtfSend.setText(str);
			jtaReceiveAddr.scrollRectToVisible(new Rectangle(0, jtaReceiveAddr.getHeight(), 0, 0));
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// 退出前关闭端口
		serialStop();
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
