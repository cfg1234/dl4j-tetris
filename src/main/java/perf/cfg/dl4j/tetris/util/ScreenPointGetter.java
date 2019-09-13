package perf.cfg.dl4j.tetris.util;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class ScreenPointGetter {
	
	private static ResourceBundle rb = ResourceBundle.getBundle("message.i18n.message");
	
	private static final int interval = Common.getScreenPointGetterInterval();
	
	public static Point doGetPoint(String pointDesc) {
		while(true) {
			JOptionPane.showMessageDialog(null,MessageFormat.format(
					rb.getString("notice.confirm.location"), interval, pointDesc));
			try {
				Thread.sleep(interval * 1000);
			} catch (InterruptedException e) {
			}
			Point ret = MouseInfo.getPointerInfo().getLocation();
			int option = JOptionPane.showConfirmDialog(null, String.format("%s:%s,%s?", pointDesc,
					ret, rb.getString("notice.confirm")), 
					rb.getString("notice.confirm"), JOptionPane.YES_NO_CANCEL_OPTION);
			if(option == JOptionPane.YES_OPTION) return ret;
			if(option == JOptionPane.CANCEL_OPTION) System.exit(1);
		}
	}
	
	public static Rectangle getScreenRect() {
		Point p1 = doGetPoint(rb.getString("notice.left.top"));
		Point p2 = doGetPoint(rb.getString("notice.right.bot"));
		return new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), 
				Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
	}
}
