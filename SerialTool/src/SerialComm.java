import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Enumeration;
import java.util.Vector;

/**
 * 串口通信用
 * @author tgmerge
 *
 */
public class SerialComm implements SerialPortEventListener {
	
	/** 单例的 **/
	static SerialComm instance = null;
	static SerialComm newInstance(String portName, String timeout, String dataRate, String dataBits, String stopBits, String parity) throws SerialException {
		instance = new SerialComm(portName, timeout, dataRate, dataBits, stopBits, parity);
		return instance;
	}
	static SerialComm getInstance() throws SerialException {
		if (instance == null)
			throw new SerialException("[SerialComm.getInstance] No opened port yet.");
		return instance;
	}
	static void closeInstance() {
		if (instance != null)
			instance.close();
		instance = null;
	}
	
	/** 得到的端口 **/
	private SerialPort serialPort;
	/** 输入流 **/
	private InputStream input;
	/** 输出流 **/
	private OutputStream output;

	public SerialComm(String portName, String timeout, String dataRate, String dataBits, String stopBits, String parity) throws SerialException {
		// 查找名称相符的端口
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(portName)) {
				portId = currPortId;
				break;
			}
		}
		
		// 找不到相应端口...
		if (portId == null) {
			throw new SerialException("[SerialComm.SerialComm]Cannot find device on " + portName);
		}
		
		// 端口配置
		int convTimeout, convDataRate, convDataBits, convStopBits, convParity;
		
		convTimeout = Integer.parseInt(timeout);
		if(convTimeout <= 0) throw new SerialException("[SerialComm.SerialComm]Invalid timeout: " + timeout);
		
		convDataRate = Integer.parseInt(dataRate);
		if(convDataRate <= 0) throw new SerialException("[SerialComm.SerialComm]Invalid dataRate: " + dataRate);
		
		if(dataBits.equals("5")) convDataBits = SerialPort.DATABITS_5;
			else if(dataBits.equals("6")) convDataBits = SerialPort.DATABITS_6;
			else if(dataBits.equals("7")) convDataBits = SerialPort.DATABITS_7;
			else if(dataBits.equals("8")) convDataBits = SerialPort.DATABITS_8;
			else throw new SerialException("[SerialComm.SerialComm]Invalid dataBits: " + dataBits);
		
		if (stopBits.equals("1")) 				convStopBits = SerialPort.STOPBITS_1;
			else if (stopBits.equals("1.5")) 	convStopBits = SerialPort.STOPBITS_1_5;
			else if (stopBits.equals("2")) 		convStopBits = SerialPort.STOPBITS_2;
			else throw new SerialException("[SerialComm.SerialComm]Invalid stopBits: " + stopBits);
		
		if (parity.equals("EVEN")) 				convParity = SerialPort.PARITY_EVEN;
			else if (parity.equals("ODD")) 		convParity = SerialPort.PARITY_ODD;
			else if (parity.equals("NONE")) 	convParity = SerialPort.PARITY_NONE;
			else throw new SerialException("[SerialComm.SerialComm]Invalid parity: " + parity);
		
		// 打开端口
		try {
			// 打开端口
			this.serialPort = (SerialPort) portId.open(this.getClass().getName(), convTimeout);
			// 设置传输参数
			this.serialPort.setSerialPortParams(convDataRate, convDataBits, convStopBits, convParity);
			// 打开输入/输出流
			this.input  = serialPort.getInputStream();
			this.output = serialPort.getOutputStream();
			// 添加事件监听
			this.serialPort.addEventListener(this);
			this.serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SerialException("[SerialComm.SerialComm]Cannot open port " + portName);
		}
		
		instance = this;
	}
	
	/** 关闭接口。在销毁前调用  **/
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	/** 处理串口有数据的事件  **/
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		// 有数据可用
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				Vector<Integer> data = new Vector<Integer>();
				while (input.available() != 0) {
					data.add((int) input.read());
				}
				Integer[] dataArr = new Integer[data.size()];
				data.toArray(dataArr);
				SerialToolWindow.getInstance().serialReceive(dataArr);
				System.out.println("[SerialComm.serialEvent]received data. 1st byte:" + (int) dataArr[0]);
			} catch (IOException e) {
				System.out.println("[SerialComm.serialEvent]IOException, ignoring");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 开始监听数据事件, 使用新的线程 **/
	public void startListening() {
		Thread t=new Thread() {
			public void run() {
				try {
					Thread.sleep(100000);
				} catch (InterruptedException ie) {
					;
				}
			}
		};
		t.start();
		System.out.println("[SerialComm.startListening]Listening started.");
	}

	/** 发送数据 **/
	public void sendData(int[] data) throws SerialException {
        try {
        	for (int b: data) {
        		this.output.write(b);
        	}
		} catch (IOException e) {
			e.printStackTrace();
			throw new SerialException("[SerialComm.sendData]Failed to send data.");
		} 
	}
}
