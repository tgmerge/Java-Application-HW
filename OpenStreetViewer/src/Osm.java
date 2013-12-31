import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 读.osm并绘图到Image
 * @author tgmerge
 */
public class Osm {
	
	Image paper;
	Graphics graphics;
	String fileName;
	
	Document document;
	Ratio ratio;
	HashMap<Long, NodeData> nodes;
	
	double minlat, maxlat, minlon, maxlon;
	
	/**
	 * 构造。
	 * @param fileName .osm文件名
	 * @param img      Image
	 */
	public Osm(String fileName, Image img) {
		// set class members
		this.paper    = img;
		this.graphics = img.getGraphics();
		this.fileName = fileName;
		
		// configure graphics
		graphics.setColor(Color.BLACK);
		//graphics.setFont(new Font("Serif", Font.PLAIN, 13));
		((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// parse data and draw
		this.parseXML();
		this.fetchRatio();
		this.fetchNodes();
		this.fetchWay();
		
		//this.drawBorders();
	}
	
	/** 解析XML->Document **/
	private void parseXML() {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.document = builder.parse(new File(this.fileName));
		} catch(IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		System.out.println("[Osm.parseXML]done.");
	}
	
	/** 从XML的信息和paper的大小获取Ratio对象 **/
	private void fetchRatio() {
		// get <bounds>
		NodeList bounds = document.getElementsByTagName("bounds");
		
		// get attributes of <bounds>
		NamedNodeMap attrs = bounds.item(0).getAttributes();
		this.minlat = Double.parseDouble(attrs.getNamedItem("minlat").getNodeValue());
		this.maxlat = Double.parseDouble(attrs.getNamedItem("maxlat").getNodeValue());
		this.minlon = Double.parseDouble(attrs.getNamedItem("minlon").getNodeValue());
		this.maxlon = Double.parseDouble(attrs.getNamedItem("maxlon").getNodeValue());
		
		// get Ratio
		this.ratio = new Ratio(minlon, minlat, maxlon, maxlat, this.paper.getWidth(null), this.paper.getHeight(null));
		
		System.out.println("[Osm.fetchRatio]minlon=" + minlon + ",minlat=" + minlat + ",maxlon=" + maxlon + ",maxlat=" + maxlat);
	}
	
	/** 获取XML中全部的点并添加至nodes **/
	private void fetchNodes() {
		nodes = new HashMap<Long, NodeData>();
		
		// get all <node>
		NodeList n = document.getElementsByTagName("node");
		
		// for each <node>:
		for (int i = 0; i < n.getLength(); i ++) {
			// get its attributes
			NamedNodeMap attrs = n.item(i).getAttributes();
			
			long id = Long.parseLong(attrs.getNamedItem("id").getNodeValue());
			double lon = Double.parseDouble(attrs.getNamedItem("lon").getNodeValue());
			double lat = Double.parseDouble(attrs.getNamedItem("lat").getNodeValue());
			
			// convert to NodeData
			nodes.put(id, new NodeData(id, lon, lat));
		}
		System.out.println("[Osm.fetchNodes]done.");
	}
	
	/** 获取XML中全部的路线并绘图至graphics **/
	private void fetchWay() {
		// get all <way>
		NodeList ways = document.getElementsByTagName("way");
		
		// for each <way>:
		for (int i = 0; i < ways.getLength(); i ++) {
			// get its childs with name <nd>
			Element way = (Element) ways.item(i);
			NodeList nds = way.getElementsByTagName("nd");
			
			// for its each <nd> child:
			long[] nodeIds = new long[nds.getLength()];
			for (int j = 0; j < nds.getLength(); j ++) {
				// get its [ref]
				nodeIds[j] = Long.parseLong(nds.item(j).getAttributes().getNamedItem("ref").getNodeValue());
			}
			
			// get its childs with name <tag k="name" v="...">
			NodeList tags = way.getElementsByTagName("tag");
			String wayName = "";
			for (int k = 0; k < tags.getLength(); k ++) {
				if (tags.item(k).getAttributes().getNamedItem("k").getNodeValue().equals("name")) {
					wayName = tags.item(k).getAttributes().getNamedItem("v").getNodeValue();
					break;
				}
			}
			
			// draw it
			drawWay(nodeIds, wayName);
		}
		System.out.println("[Osm.fetchWay]done.");
	}
	
	/** 绘制一条路径 **/
	private void drawWay(long[] nodeIds, String name) {
		int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
		
		for (int i = 0; i < nodeIds.length-1; i ++) {
			NodeData n1 = nodes.get(nodeIds[i]);
			NodeData n2 = nodes.get(nodeIds[i+1]);
			int x1 = n1.getX(ratio), y1 = n1.getY(ratio);
			int x2 = n2.getX(ratio), y2 = n2.getY(ratio);
			if (x1 > xMax) xMax = x1;
			if (x1 < xMin) xMin = x1;
			if (y1 > yMax) yMax = y1;
			if (y1 < yMin) yMin = y1;
			graphics.drawLine(x1, y1, x2, y2);
		}
		
		if(Math.abs(xMax-xMin) > 80 && Math.abs(yMax-yMin) > 50) {
			graphics.drawString(name, (xMin+xMax)/2, (yMin+yMax)/2);
		}
	}
	
	@SuppressWarnings("unused")
	private void drawBorders() {
		int x1 = ratio.getX(minlon, minlat), y1 = ratio.getY(minlon, minlat);
		int x2 = ratio.getX(maxlon, maxlat), y2 = ratio.getY(maxlon, maxlat);
		int x3 = paper.getWidth(null), y3 = paper.getWidth(null);
		
		graphics.setColor(new Color(250, 246, 230));
		
		// up
		graphics.fillRect(0, 0, x3, y1);
		// down
		graphics.fillRect(0, y2, x3, y3-y2);
		// left
		graphics.fillRect(0, 0, x1, y3);
		// right
		graphics.fillRect(x2, 0, x3, y3);
		
		graphics.setColor(Color.BLACK);
		System.out.println("[Osm.drawBorders]done");
	}

}
