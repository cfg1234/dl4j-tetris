package perf.cfg.dl4j.tetris.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import perf.cfg.dl4j.tetris.common.Cube;
import perf.cfg.dl4j.tetris.common.CubeActionGroup;
import perf.cfg.dl4j.tetris.common.TetrisScoreCalculator;

public class Common {
	private static final Logger logger = LoggerFactory.getLogger(Common.class);
	private static List<Cube> possibleCubeList = Arrays.asList(new CubeLine(), new CubeSquare(), 
			new CubeL(), new CubeCounterL(), new CubeZ(), new CubeCounterZ(), new CubeT());
	private static Properties prop = new Properties();
	private static String configPath = System.getProperty("config.path", "config.prop");
	static {
		try(FileInputStream in = new FileInputStream(configPath)){
			prop.load(in);
		} catch (IOException e) {
			logger.error("", e);
			System.exit(1);
		}
	}


	public static File getCubeModelPath() {
		return new File(getProperty("cube.model.path", "cube-model.bin", false));
	}

	public static List<Cube> possibleCubeList() {
		return possibleCubeList;
	}
	
	public static Rectangle getTetrisArea() {
		int x = getProperty("tetris.area.left", 0, true);
		int y = getProperty("tetris.area.top", 0, true);
		return new Rectangle(x,y,getProperty("tetris.area.right", 2000, true) - x, 
				getProperty("tetris.area.bottom", 2000, true) - y);
	}
	
	public static void setTetrisArea(Point leftTop, Point rightBot) throws IOException {
		prop.setProperty("tetris.area.left", leftTop.x+"");
		prop.setProperty("tetris.area.top", leftTop.y+"");
		prop.setProperty("tetris.area.right", rightBot.x+"");
		prop.setProperty("tetris.area.bottom", rightBot.y+"");
		save();
	}

	

	public static int getTetrisWidth() {
		return getProperty("tetris.cube.cols", 10, true);
	}

	public static int getTetrisHeight() {
		return getProperty("tetris.cube.rows", 20, true);
	}

	public static Rectangle getNextCubeArea() {
		int x = getProperty("tetris.next.cube.area.left", 0, true);
		int y = getProperty("tetris.next.cube.area.top", 0, true);
		return new Rectangle(x,y,getProperty("tetris.next.cube.area.right", 2000, true) - x, 
				getProperty("tetris.next.cube.area.bottom", 2000, true) - y);
	}
	
	public static void setNextCubeArea(Point leftTop, Point rightBot) throws IOException {
		prop.setProperty("tetris.next.cube.area.left", leftTop.x+"");
		prop.setProperty("tetris.next.cube.area.top", leftTop.y+"");
		prop.setProperty("tetris.next.cube.area.right", rightBot.x+"");
		prop.setProperty("tetris.next.cube.area.bottom", rightBot.y+"");
		save();
	}

	public static int getNextCubeAreaWidth() {
		return getProperty("tetris.next.cube.cols", 4, true);
	}

	public static int getNextCubeAreaHeight() {
		return getProperty("tetris.next.cube.rows", 2, true);
	}
	
	public static int getTetrisAreaSetterInterval() {
		return getProperty("tetris.area.setter.interval", 5, false);
	}

	public static CubeActionGroup getCubeActionGroup() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String className = getProperty("cube.action.class.name", "perf.cfg.dl4j.tetris.util.YXH5UCActionGroup", true);
		Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
		return (CubeActionGroup) c.getConstructor().newInstance();
	}

	public static TetrisScoreCalculator getScoreCalculator() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String className = getProperty("cube.score.calculator.class.name", "perf.cfg.dl4j.tetris.util.DefaultScoreCalculator", false);
		Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
		return (TetrisScoreCalculator) c.getConstructor().newInstance();
	}
	
	
	private static class CubeLine extends Cube {
		CubeLine() {
			super(1, 4, new byte[] {1,1,1,1});
		}
		public synchronized void transform(boolean clockwise) {
			swapWidthAndHeight();
		}
	}
	private static class CubeSquare extends Cube {
		CubeSquare() {
			super(2, 2, new byte[] {1, 1, 1, 1});
		}
		public void transform(boolean clockwise) {}
		public Cube dup() {return this;}
		
	}
	private static class CubeL extends Cube{
		CubeL() {
			super(2, 3, new byte[] {1, 1, 1, 0, 1, 0});
		}
		
	}
	private static class CubeCounterL extends Cube {
		CubeCounterL() {
			super(2, 3, new byte[] {1, 1, 0, 1, 0, 1});
		}
		
	}
	private static class CubeZ extends Cube{
		CubeZ() {
			super(3, 2, new byte[] {0, 1, 1, 1, 1, 0});
		}
		
	}
	private static class CubeCounterZ extends Cube {
		CubeCounterZ() {
			super(3, 2, new byte[] {1, 1, 0, 0, 1, 1});
		}
		
	}
	private static class CubeT extends Cube {
		CubeT() {
			super(3, 2, new byte[] {0, 1, 0, 1, 1, 1});
		}
	}
	
	private static int getProperty(String key, int defValue, boolean warn) {
		try {
			return Integer.parseInt(prop.getProperty(key));
		} catch(RuntimeException e) {
			String msg = "Invalid or null number property '{}', use default value '{}' instead.";
			Object[] args = new Object[] {key, defValue};
			if(warn) {
				logger.warn(msg, args);
			} else {
				logger.info(msg, args);
			}
			return defValue;
		}
	}
	
	private static String getProperty(String key, String defValue, boolean warn) {
		String ret = prop.getProperty(key);
		if(ret == null) {
			ret = defValue;
			String msg = "Invalid or null property '{}', use default value '{}' instead.";
			Object[] args = new Object[] {key, defValue};
			if(warn) {
				logger.warn(msg, args);
			} else {
				logger.info(msg, args);
			}
		}
		return ret;
	}
	
	private static void save() throws IOException {
		try(FileOutputStream out = new FileOutputStream(configPath)){
			prop.store(out, "");
		}
	}
}
