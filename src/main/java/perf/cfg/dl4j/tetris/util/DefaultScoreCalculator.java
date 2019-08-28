package perf.cfg.dl4j.tetris.util;

import perf.cfg.dl4j.tetris.common.Cube;
import perf.cfg.dl4j.tetris.common.TetrisScoreCalculator;

public class DefaultScoreCalculator implements TetrisScoreCalculator{
	
	public double getScore(byte[] data, int width, int height) {
		double sumHeight = 0;
		int divNum = 0;
		for(Cube possibleCube:Common.possibleCubeList()) {
			Cube cube = possibleCube.dup();
			double minHeight = height;
			for(int i = 0;i < 4;i++) {
				for(int i2 = 0;i2 < width - cube.getWidth() + 1;i2++) {
					for(int i3 = 0;i3 < height - cube.getHeight() + 1;i3++) {
						if(isValid(data, width, cube, i2, i3)) {
							minHeight = Math.min(minHeight, getCubeHeight(data, width, cube, i2, i3));
							break;
						}
					}
				}
				cube.transform(true);
			}
			sumHeight += minHeight;
			divNum++;
		}
		return sumHeight / divNum;
	}

	private static byte[] tmp;
	private static int getCubeHeight(byte[] data, int width, Cube cube, int x, int y) {
		tmp = new byte[data.length];
		System.arraycopy(data, 0, tmp, 0, data.length);
		for(int i = 0;i < cube.getWidth();i++) {
			for(int j = 0;j < cube.getHeight();j++) {
				if(cube.getData(i, j) > 0) {
					int idx = (j + y) * width + (i + x);
					tmp[idx] = 1;
				}
			}
		}
		int height = tmp.length / width;
		l1:for(int i = 0;i < height;i++) {
			for(int j = 0;j < width;j++) {
				if(tmp[i * width + j] == 0) continue l1;
			}
			System.arraycopy(tmp, (i+1) * width, tmp, i*width, (height-i-1)*width);
			i--;
		}
		int ret = 0;
		for(int i = 0;i < width;i++) {
			boolean top = false;
			int colHeight = 0;
			for(int j = height - 1;j >= 0;j--) {
				if(tmp[j * width + i] > 0) {
					if(!top) {
						colHeight = j + 1;
						top = true;
					}
				} else if(top) {
					colHeight++;
				}
			}
			ret = Math.max(ret, colHeight);
		}
		if(ret == 0) {
			for(int i = 0;i < tmp.length;i++) {
				if(tmp[i] != 0)throw new RuntimeException();
			}
		}
		return ret;
	}

	private static boolean isValid(byte[] data, int width, Cube cube, int x, int y) {
		for(int i = 0;i < cube.getWidth();i++) {
			for(int j = 0;j < cube.getHeight();j++) {
				if(cube.getData(i, j) > 0 && data[(j + y) * width + (i + x)] > 0)
					return false;
			}
		}
		return true;
	}
}
