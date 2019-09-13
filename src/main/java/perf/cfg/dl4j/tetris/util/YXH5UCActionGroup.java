package perf.cfg.dl4j.tetris.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class YXH5UCActionGroup extends AbstratCubeActionGroup {
	
	private Robot robot;
	private long pressInterval = 50;
	private long toBottomInterval = 200;
	

	public YXH5UCActionGroup() throws AWTException {
		super();
		robot = new Robot();
	}
	
	public Runnable transformAction() {
		return new Runnable() {
			public void run() {
				robot.keyPress(KeyEvent.VK_UP);
				robot.keyRelease(KeyEvent.VK_UP);
				sleep(pressInterval);
			}
		};
	}
	
	public Runnable toBottomAction() {
		return new Runnable() {
			public void run() {
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
				sleep(pressInterval + toBottomInterval);
				//sleep(toBottomInterval);
			}
		};
	}
	
	public Runnable moveRightAction() {
		return new Runnable() {
			public void run() {
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
				sleep(pressInterval);
			}
		};
	}
	
	public Runnable moveLeftAction() {
		return new Runnable() {
			public void run() {
				robot.keyPress(KeyEvent.VK_LEFT);
				robot.keyRelease(KeyEvent.VK_LEFT);
				sleep(pressInterval);
			}
		};
	}
	
	public Runnable moveDownAction() {
		return new Runnable() {
			public void run() {}
		};
	}
	
	private static void sleep(long time) {
		if(time <= 0) return;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	@Override
	protected Runnable transformReverseAction() {
		return null;
	}
}
