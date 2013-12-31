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
 * ����ͨ����
 * @author tgmerge
 *
 */
public class SerialComm implements SerialPortEventListener {
	
	/** ������ **/
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
	
	/** �õ��Ķ˿� **/
	private SerialPort serialPort;
	/** ������ **/
	private InputStream input;
	/** ����� **/
	private OutputStream output;

	public SerialComm(String portName, String timeout, String dataRate, String dataBits, String stopBits, String parity) throws SerialException {
		// ������������Ķ˿�
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
		
		// �Ҳ�����Ӧ�˿�...
		if (portId == null) {
			throw new SerialException("[SerialComm.SerialComm]Cannot find device on " + portName);
		}
		
		// �˿�����
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
		
		// �򿪶˿�
		try {
			// �򿪶˿�
			this.serialPort = (SerialPort) portId.open(this.getClass().getName(), convTimeout);
			// ���ô������
			this.serialPort.setSerialPortParams(convDataRate, convDataBits, convStopBits, convParity);
			// ������/�����
			this.input  = serialPort.getInputStream();
			this.output = serialPort.getOutputStream();
			// ����¼�����
			this.serialPort.addEventListener(this);
			this.serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SerialException("[SerialComm.SerialComm]Cannot open port " + portName);
		}
		
		instance = this;
	}
	
	/** �رսӿڡ�������ǰ����  **/
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	/** �����������ݵ��¼�  **/
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		// �����ݿ���
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
	
	/** ��ʼ���������¼�, ʹ���µ��߳� **/
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

	/** �������� **/
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
