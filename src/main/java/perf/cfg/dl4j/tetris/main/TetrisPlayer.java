package perf.cfg.dl4j.tetris.main;

import java.util.LinkedList;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import perf.cfg.dl4j.tetris.common.CubeActionGroup;
import perf.cfg.dl4j.tetris.common.CubeActionType;
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
		TetrisDataGetter nextGetter = Common.getNextCubeGetter();
		CubeActionGroup cag = Common.getCubeActionGroup();
		TetrisPrediction tp = new TetrisPrediction(tetrisGetter, nextGetter, Common.getScoreCalculator());
		while(true) {
			LinkedList<CubeActionType> ca = tp.predictNextAction();
			if(ca != null) {
				handleCubeAction(ca);
				for(CubeActionType c:ca) {
					if(!cag.isImplemented(c)) {
						throw new RuntimeException("cube action type " + c + " must be implemented");
					}
					cag.execute(c);
				}
			}
		}
	}

	private static void handleCubeAction(LinkedList<CubeActionType> ca) {
		int removeStart = 0;
		for(CubeActionType c:ca) {
			if(c == CubeActionType.ACTION_MOVE_DOWN) break;
			removeStart++;
		}
		if(removeStart > 0) {
			while(ca.size() > removeStart) {
				ca.remove(removeStart);
			}
		} else {
			while(!ca.isEmpty() && ca.getFirst() == CubeActionType.ACTION_MOVE_DOWN) {
				ca.removeFirst();
			}
			if(ca.isEmpty()) {
				ca.add(CubeActionType.ACTION_TO_BOTTOM);
			} else {
				ca.clear();
			}
		}
	}
}
