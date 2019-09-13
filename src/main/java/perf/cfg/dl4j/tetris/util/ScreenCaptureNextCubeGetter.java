package perf.cfg.dl4j.tetris.util;

import java.awt.AWTException;
import java.io.IOException;

import org.deeplearning4j.util.ModelSerializer;

public class ScreenCaptureNextCubeGetter extends ScreenCaptureGetter {

	public ScreenCaptureNextCubeGetter() throws AWTException, IOException {
		super(ModelSerializer.restoreMultiLayerNetwork(Common.getCubeModelPath()), 
				new FixedBoundsGenerator(Common.getNextCubeArea()), 
				Common.getNextCubeAreaWidth(), Common.getNextCubeAreaHeight());
	}
	
}
