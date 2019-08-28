package perf.cfg.dl4j.tetris.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import perf.cfg.dl4j.tetris.common.BoundsGenerator;
import perf.cfg.dl4j.tetris.common.CubeAction;
import perf.cfg.dl4j.tetris.common.CubeActionGroup;
import perf.cfg.dl4j.tetris.common.TetrisAreaGetter;
import perf.cfg.dl4j.tetris.common.TetrisDataGetter;
import perf.cfg.dl4j.tetris.util.Common;
import perf.cfg.dl4j.tetris.util.FixedBoundsGenerator;
import perf.cfg.dl4j.tetris.util.ScreenCaptureGetter;
import perf.cfg.dl4j.tetris.util.TetrisPrediction;


public class TetrisPlayer {
	public static void main(String[] args) throws Exception {
		MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(Common.getCubeModelPath());
		TetrisDataGetter tetrisGetter = new ScreenCaptureGetter(model, 
				new FixedBoundsGenerator(Common.getTetrisArea()), 
				Common.getTetrisWidth(), Common.getTetrisHeight());
		TetrisDataGetter nextGetter = new ScreenCaptureGetter(model, 
				new FixedBoundsGenerator(Common.getNextCubeArea()), 
				Common.getNextCubeAreaWidth(), Common.getNextCubeAreaHeight());
		CubeActionGroup cag = Common.getCubeActionGroup();
		TetrisPrediction tp = new TetrisPrediction(tetrisGetter, nextGetter, cag, Common.getScoreCalculator());
		while(true) {
			CubeAction[] ca = tp.predictNextAction();
			if(ca != null) {
				boolean lastMoveDown = false;
				for(CubeAction c:ca) {
					if(cag.actionEquals(c, cag.moveDownAction())) {
						lastMoveDown = true;
					} else if(lastMoveDown) {
						break;
					} else if(cag.actionEquals(c, cag.toBottomAction()) && ca.length != 1) {
						break;
					}
					c.action();
				}
			}
		}
	}
}
