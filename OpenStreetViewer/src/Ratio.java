/**
 * 转换经纬度到Image坐标
 * @author tgmerge
 */
public class Ratio {
	double minlat, minlon;
	double k;
	double dx, dy;
	int width, height;
	
	public Ratio(double minlon, double minlat, double maxlon, double maxlat, int width, int height) {
		this.minlat = minlat;
		this.minlon = minlon;
		this.width  = width;
		this.height = height;
		double kx = width / (maxlon - minlon);
		double ky = height / (maxlat - minlat);
		k = (kx < ky) ? kx : ky;
		dx = (width  - (maxlon-minlon) * k) / 2;
		dy = (height - (maxlat-minlat) * k) / 2;
		System.out.println("[Ratio.Ratio]k=" + k);
	}
	
	public int getX(double lon, double lat) {
		return (int) ((lon-minlon) * k + dx);
	}
	
	public int getY(double lon, double lat) {
		return height - (int) ((lat-minlat) * k + dy);
	}
}
