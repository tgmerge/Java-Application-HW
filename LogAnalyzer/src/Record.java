import java.util.Date;

/**
 * LOG中一行的数据解析后的包装形式
 * @author tgmerge
 * 
 * Object[]的内容
 * IP identity userid [Time] "method uri" returnCode length
 * 0  1        2       3             4    5          6
 */
public class Record {
	public Object[] data;
	
	public Record() {
		data = new Object[7];
	}
	
	public void setIP(String ip) {
		data[0] = ip;
	}
	public String getIP() {
		return (String) data[0];
	}
	
	public void setIdentity(String identity) {
		data[1] = identity;
	}
	public String getIdentity() {
		return (String) data[1];
	}
	
	public void setUserid(String userId) {
		data[2] = userId;
	}
	public String getUserid() {
		return (String) data[2];
	}
	
	public void setTime(Date time) {
		data[3] = time;
	}
	public Date getTime() {
		return (Date) data[3];
	}
	
	public void setURL(String url) {
		data[4] = url;
	}
	public String getURL() {
		return (String) data[4];
	}
	
	public void setStatcode(int statcode) {
		data[5] = statcode;
	}
	public int getStatcode() {
		return (int) data[5];
	}
	
	void setLength(int length) {
		data[6] = length;
	}
	int getLength() {
		return (int) data[6];
	}
}
