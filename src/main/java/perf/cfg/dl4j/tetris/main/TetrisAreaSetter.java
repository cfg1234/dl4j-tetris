package perf.cfg.dl4j.tetris.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import perf.cfg.dl4j.tetris.util.Common;
import perf.cfg.dl4j.tetris.util.ScreenCapture;

public class TetrisAreaSetter {

	private static ResourceBundle rb = ResourceBundle.getBundle("message.i18n.message");

	public static void main(String[] args) throws Exception {
		JOptionPane.showMessageDialog(null, rb.getString("message.tetris.area.location"));
		Thread.sleep(200);
		Rectangle tetris = ScreenCapture.captureBounds();
		if(tetris == null){
			JOptionPane.showMessageDialog(null, rb.getString("cube.area.setter.interrupt"));
			System.exit(1);
		}
		JOptionPane.showMessageDialog(null, rb.getString("message.next.cube.area.location"));
		Thread.sleep(200);
		Rectangle next = ScreenCapture.captureBounds();
		if(next == null){
			JOptionPane.showMessageDialog(null, rb.getString("cube.area.setter.interrupt"));
			System.exit(1);
		}
		Common.setTetrisArea(new Point(tetris.x, tetris.y), new Point(tetris.x + tetris.width,
				tetris.y + tetris.height));
		Common.setNextCubeArea(new Point(next.x, next.y), new Point(next.x + next.width,
				next.y + next.height));
		JOptionPane.showMessageDialog(null, rb.getString("cube.area.setter.finish"));
	}
}
