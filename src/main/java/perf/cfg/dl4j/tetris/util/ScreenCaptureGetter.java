package perf.cfg.dl4j.tetris.util;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

import perf.cfg.dl4j.tetris.common.BoundsGenerator;
import perf.cfg.dl4j.tetris.common.TetrisDataGetter;

public class ScreenCaptureGetter implements TetrisDataGetter {
	private static final int cubeSize = 20;
	private final NormImageLoader loader;
	private final INDArray captureTmp;
	private int width;
	private int height;
	private BoundsGenerator boundsGen;
	private Robot robot;
	private MultiLayerNetwork cubeModel;

	public ScreenCaptureGetter(MultiLayerNetwork model, BoundsGenerator boundsGen, int width, int height) throws AWTException {
		this.boundsGen = boundsGen;
		this.width = width;
		this.height = height;
		this.cubeModel = model;
		this.robot = new Robot();
		loader = new NormImageLoader(cubeSize*height,cubeSize*width,4);
		captureTmp = Nd4j.zeros(width * height, 3, cubeSize, cubeSize);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public byte[] dupData() {
		byte[] b = new byte[getWidth() * getHeight()];
		copyData(b);
		return b;
	}
	
	public void copyData(byte[] data) throws IllegalArgumentException {
		if(data.length != getWidth() * getHeight()) {
			throw new IllegalArgumentException("invalid data length:" + data.length);
		}
		try {
			getScreenCubeData(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void getScreenCubeData(byte[] arr) throws IOException {
		Rectangle sample = boundsGen.getBounds();
		BufferedImage img = robot.createScreenCapture(sample);
		INDArray imgMatrix = loader.asMatrix(img);
		for(int i = 0;i < arr.length;i++) {
			for(int j = 0;j < 3;j++) {
				int x = i % width;
				int y = height - 1 - i / width;
				captureTmp.put(new INDArrayIndex[] {NDArrayIndex.point(i), 
						NDArrayIndex.point(j), 
						NDArrayIndex.all(), 
						NDArrayIndex.all()}, imgMatrix.get(
								NDArrayIndex.all(), 
								NDArrayIndex.point(3 - j), 
								NDArrayIndex.interval(y * cubeSize,(y+1)*cubeSize), 
								NDArrayIndex.interval(x * cubeSize,(x+1)*cubeSize)));
			}
		}
		INDArray output = cubeModel.output(captureTmp);
		for(int i = 0;i < arr.length;i++) {
			arr[i] = (byte)(output.getDouble(i,0) > output.getDouble(i,1)?1:0);
		}
	}

}
