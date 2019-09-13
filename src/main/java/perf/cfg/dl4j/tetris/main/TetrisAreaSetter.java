package perf.cfg.dl4j.tetris.main;

import java.awt.Point;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import perf.cfg.dl4j.tetris.util.Common;
import perf.cfg.dl4j.tetris.util.ScreenPointGetter;

public class TetrisAreaSetter {

	private static ResourceBundle rb = ResourceBundle.getBundle("message.i18n.message");

	public static void main(String[] args) throws IOException {
		Point leftTop = ScreenPointGetter.doGetPoint(rb.getString("left.top.tetris.location")), 
				rightBot = ScreenPointGetter.doGetPoint(rb.getString("right.bot.tetris.location")), 
				leftTop2 = ScreenPointGetter.doGetPoint(rb.getString("left.top.next.cube.location")),
				rightBot2 = ScreenPointGetter.doGetPoint(rb.getString("right.bot.next.cube.location"));
		Common.setTetrisArea(leftTop, rightBot);
		Common.setNextCubeArea(leftTop2, rightBot2);
		JOptionPane.showMessageDialog(null, rb.getString("cube.area.setter.finish"));
	}
}
