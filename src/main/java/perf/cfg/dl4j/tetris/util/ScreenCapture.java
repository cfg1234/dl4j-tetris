package perf.cfg.dl4j.tetris.util;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ScreenCapture {
	
	private static int x = -1,y = -1, frameX = -1, frameY = -1, width = -1, height = -1;
	private volatile static boolean captured = false;
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static Robot r;
	
	static {
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static void reset(Component frame) {
		x = -1;
		y = -1;
		frameX = -1;
		frameY = -1;
		width = -1;
		height = -1;
		captured = false;
		Graphics g = frame.getGraphics();
		g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
	}

	private static void doCapture() {
		final Image desktop = r.createScreenCapture(new Rectangle(0,0,d.width,d.height));
		final JFrame frame = new JFrame();
		frame.setUndecorated(true);
		GraphicsEnvironment.getLocalGraphicsEnvironment().
		getDefaultScreenDevice().setFullScreenWindow(frame); 
		final JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(desktop, 0, 0, getWidth(), getHeight(), null);
			}
		};
		panel.addMouseListener(new MouseAdapter() {
			private JPopupMenu menu = null;
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON1 || x < 0 || y < 0) {
					reset(panel);
					return;
				} 
				Point p = MouseInfo.getPointerInfo().getLocation();
				width = Math.abs(p.x - x);
				height = Math.abs(p.y - y);
				x = Math.min(p.x, x);
				y = Math.min(p.y, y);
				showPopMenu(e.getX(), e.getY());
			}
			
			private void showPopMenu(int x, int y) {
				if(menu == null) {
					menu = new JPopupMenu();
					JMenuItem confirmItem = new JMenuItem("confirm");
					JMenuItem cancelItem = new JMenuItem("cancel");
					confirmItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							frame.dispose();
							captured = true;
						}
					});
					cancelItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ScreenCapture.x = -1;
							ScreenCapture.y = -1;
							frame.dispose();
							captured = true;
						}
					});
					menu.add(confirmItem);
					menu.add(cancelItem);
					menu.setOpaque(false);
				}
				menu.show(panel, x, y);
			}
			
			public void mousePressed(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON1) {
					reset(panel);
					return;
				}
				Point p = MouseInfo.getPointerInfo().getLocation();
				x = p.x;
				y = p.y;
				frameX = e.getX();
				frameY = e.getY();
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(x > 0 && y > 0) {
					Graphics2D g = (Graphics2D) panel.getGraphics();
					g.setColor(Color.RED);
					g.drawImage(desktop, 0, 0, panel.getWidth(), panel.getHeight(), null);
					g.drawRect(Math.min(frameX, e.getX()), Math.min(frameY, e.getY()), 
							Math.abs(e.getX() - frameX), Math.abs(e.getY() - frameY));
				}
			}
		});
		frame.add(panel);
		frame.setVisible(true);
		reset(frame);
	}
	
	public synchronized static Rectangle captureBounds() {
		Rectangle ret = new Rectangle();
		doCapture();
		while(!captured) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
		}
		if(x < 0 || y < 0) return null;
		ret.setBounds(x, y, width, height);
		System.out.println(ret);
		return ret;
	}
	
	

}