/**
 * 一个Node的信息
 * @author tgmerge
 */
public class NodeData {
	double lat;
	double lon;
	
	public NodeData(long id, double lon, double lat) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getLat() { return lat; }
	public double getLon() { return lon; }
	public int getX(Ratio r) { return r.getX(lon, lat); }
	public int getY(Ratio r) { return r.getY(lon, lat); }
}
