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
import perf.cfg.dl4j.tetris.common.TetrisDataGetter;
import perf.cfg.dl4j.tetris.common.TetrisScoreCalculator;

public class Common {
	private static final Logger logger = LoggerFactory.getLogger(Common.class);
	private static List<Cube> possibleCubeList = Arrays.asList(new CubeLine(), new CubeSquare(), 
			new CubeL(), new CubeCounterL(), new CubeZ(), new CubeCounterZ(), new CubeT());
	private static Properties prop = new Properties();
	private static File configFile = new File(System.getProperty("config.path", "config.prop"));
	static {
		try(FileInputStream in = new FileInputStream(configFile)){
			prop.load(in);
		} catch (IOException e) {
			logger.error("", e);
			System.exit(1);
		}
	}


	public static File getCubeModelPath() {
		return new File(getProperty("cube.model.path", null));
	}

	public static List<Cube> possibleCubeList() {
		return possibleCubeList;
	}
	
	public static Rectangle getTetrisArea() {
		int x = getIntProperty("tetris.area.left", null);
		int y = getIntProperty("tetris.area.top", null);
		return new Rectangle(x,y,getIntProperty("tetris.area.right", null) - x,
				getIntProperty("tetris.area.bottom", null) - y);
	}
	
	public static void setTetrisArea(Point leftTop, Point rightBot) throws IOException {
		prop.setProperty("tetris.area.left", leftTop.x+"");
		prop.setProperty("tetris.area.top", leftTop.y+"");
		prop.setProperty("tetris.area.right", rightBot.x+"");
		prop.setProperty("tetris.area.bottom", rightBot.y+"");
		save();
	}

	

	public static int getTetrisWidth() {
		return getIntProperty("tetris.cube.cols", 10);
	}

	public static int getTetrisHeight() {
		return getIntProperty("tetris.cube.rows", 20);
	}

	public static Rectangle getNextCubeArea() {
		int x = getIntProperty("tetris.next.cube.area.left", null);
		int y = getIntProperty("tetris.next.cube.area.top", null);
		return new Rectangle(x,y,getIntProperty("tetris.next.cube.area.right", null) - x,
				getIntProperty("tetris.next.cube.area.bottom", null) - y);
	}
	
	public static void setNextCubeArea(Point leftTop, Point rightBot) throws IOException {
		prop.setProperty("tetris.next.cube.area.left", leftTop.x+"");
		prop.setProperty("tetris.next.cube.area.top", leftTop.y+"");
		prop.setProperty("tetris.next.cube.area.right", rightBot.x+"");
		prop.setProperty("tetris.next.cube.area.bottom", rightBot.y+"");
		save();
	}

	public static int getNextCubeAreaWidth() {
		return getIntProperty("tetris.next.cube.cols", 4);
	}

	public static int getNextCubeAreaHeight() {
		return getIntProperty("tetris.next.cube.rows", 2);
	}
	
	public static int getScreenPointGetterInterval() {
		return getIntProperty("screen.point.getter.interval", 5);
	}

	public static CubeActionGroup getCubeActionGroup() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String className = getProperty("cube.action.class.name", null);
		return (CubeActionGroup) getInstance(className);
	}

	public static TetrisScoreCalculator getScoreCalculator() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String className = getProperty("cube.score.calculator.class.name", null);
		return (TetrisScoreCalculator) getInstance(className);
	}

	public static TetrisDataGetter getNextCubeGetter() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String className = getProperty("next.cube.getter.class.name",null);
		return (TetrisDataGetter) getInstance(className);
	}

	private static Object getInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
		return c.getConstructor().newInstance();
	}

	public static String getTetrisLoggerPath() {
		return getProperty("tetris.logger.path", "log");
	}

	public static int getLandingHeightParam() {
		return getIntProperty("pierre.dellacheris.height.param", 100);
	}

	public static double getBoardRowTransitionsParam() {
		return getIntProperty("pierre.dellacheris.board.row.transition.param", 32);
	}

	public static double getBoardColTransitionsParam() {
		return getIntProperty("pierre.dellacheris.board.col.transition.param", 93);
	}

	public static double getBoardBuriedHolesParam() {
		return getIntProperty("pierre.dellacheris.board.buried.holes.param", 79);
	}

	public static double getBoardWellsParam() {
		return getIntProperty("pierre.dellacheris.board.wells.param", 34);
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
	
	private static int getIntProperty(String key, Integer defValue) {
		try {
			return Integer.parseInt(prop.getProperty(key));
		} catch(RuntimeException e) {
			if(defValue == null) {
				throw new RuntimeException(String.format("property '%s' not set or is not an integer"
						+ " in config file '%s'.", key, configFile.getAbsolutePath()));
			}
			logger.warn("Invalid or null number property '{}' in config file '{}', use default value"
					+ " '{}' instead.", key, configFile.getAbsolutePath(), defValue);
			return defValue;
		}
	}
	
	private static String getProperty(String key, String defValue) {
		String ret = prop.getProperty(key);
		if(ret == null) {
			if(defValue == null) {
				throw new RuntimeException(String.format("property '%s' not set int config file '%s'.",
						key, configFile.getAbsolutePath()));
			}
			ret = defValue;
			logger.warn("Invalid or null property '{}'in config file '{}', use default value '{}'"
					+ " instead.", key, configFile.getAbsolutePath(), defValue);
		}
		return ret;
	}
	
	private static void save() throws IOException {
		try(FileOutputStream out = new FileOutputStream(configFile)){
			prop.store(out, "");
		}
	}

	public static String getNextCubeTypeModel() {
		return getProperty("tetirs.ultimate.next.cube.type.model.path", null);
	}


}
