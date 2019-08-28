package perf.cfg.dl4j.tetris.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import perf.cfg.dl4j.tetris.common.CubeAction;
import perf.cfg.dl4j.tetris.common.CubeActionGroup;

public class YXH5UCActionGroup implements CubeActionGroup {
	
	private Robot robot;
	private long pressInterval = 100;
	

	public YXH5UCActionGroup() throws AWTException {
		robot = new Robot();
	}

	private CubeAction moveRight = new CubeAction() {
		public void action() {
			robot.keyPress(KeyEvent.VK_RIGHT);
			sleep(pressInterval);
			robot.keyRelease(KeyEvent.VK_RIGHT);
		}
		public String toString() {return "moveRight";}
	};
	
	private CubeAction moveLeft = new CubeAction() {
		public void action() {
			robot.keyPress(KeyEvent.VK_LEFT);
			sleep(pressInterval);
			robot.keyRelease(KeyEvent.VK_LEFT);
		}
		public String toString() {return "moveLeft";}
	};
	
	private CubeAction transform = new CubeAction() {
		public void action() {
			robot.keyPress(KeyEvent.VK_UP);
			sleep(pressInterval);
			robot.keyRelease(KeyEvent.VK_UP);
		}
		public String toString() {return "transform";}
	};
	
	private CubeAction toBottom = new CubeAction() {
		public void action() {
			robot.keyPress(KeyEvent.VK_DOWN);
			sleep(200);
			robot.keyRelease(KeyEvent.VK_DOWN);
		}
		public String toString() {return "toBottom";}
	};
	
	private CubeAction moveDown = new CubeAction() {
		public void action() {
		}
		public String toString() {return "moveDown";}
	};
	public CubeAction transformAction() {
		return transform;
	}
	
	public CubeAction toBottomAction() {
		return toBottom;
	}
	
	public CubeAction moveRightAction() {
		return moveRight;
	}
	
	public CubeAction moveLeftAction() {
		return moveLeft;
	}
	
	public CubeAction moveDownAction() {
		return moveDown;
	}
	
	public boolean actionEquals(CubeAction a1, CubeAction a2) {
		return a1 == a2;
	}
	
	private static void sleep(long time) {
		if(time <= 0) return;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
