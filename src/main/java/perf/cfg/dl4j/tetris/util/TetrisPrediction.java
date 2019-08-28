package perf.cfg.dl4j.tetris.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import perf.cfg.dl4j.tetris.common.Cube;
import perf.cfg.dl4j.tetris.common.CubeAction;
import perf.cfg.dl4j.tetris.common.CubeActionGroup;
import perf.cfg.dl4j.tetris.common.TetrisDataGetter;
import perf.cfg.dl4j.tetris.common.TetrisScoreCalculator;
import perf.cfg.dl4j.tetris.util.Common;

public class TetrisPrediction {
	private TetrisDataGetter tetrisDataGetter;
	private TetrisDataGetter nextCubeGetter;
	private CubeActionGroup cubeActionGroup;
	private byte[] lastStep;
	private byte[] currStep;
	private byte[] cubeTmp;
	private byte[] nextCubeTmp;
	private final ArrayList<int[]> stepCubeList = new ArrayList<int[]>();
	private final int width, height, nextWidth, nextHeight;
	private final byte[][] scoreStepTmp = new byte[2][];
	private BestTetrisStatus bestTetrisStatus;
	private TetrisScoreCalculator calculator;

	
	public TetrisPrediction(TetrisDataGetter tetrisDataGetter, TetrisDataGetter nextCubeGetter, 
			CubeActionGroup cubeActionGroup, TetrisScoreCalculator calculator) {
		this.tetrisDataGetter = tetrisDataGetter;
		this.nextCubeGetter = nextCubeGetter;
		this.cubeActionGroup = cubeActionGroup;
		this.calculator = calculator;
		width = tetrisDataGetter.getWidth();
		height = tetrisDataGetter.getHeight();
		nextWidth = nextCubeGetter.getWidth();
		nextHeight = nextCubeGetter.getHeight();
		stepCubeList.add(null);
		stepCubeList.add(null);
		lastStep = new byte[width * height];
		currStep = new byte[lastStep.length];
		cubeTmp = new byte[lastStep.length];
		nextCubeTmp = new byte[nextWidth * nextHeight];
		scoreStepTmp[0] = new byte[currStep.length];
		scoreStepTmp[1] = new byte[currStep.length];
		bestTetrisStatus = new BestTetrisStatus();
		bestTetrisStatus.bestStatus = new byte[currStep.length];
	}

	public CubeAction[] predictNextAction() {
		byte[] tmp = currStep;
		currStep = lastStep;
		lastStep = tmp;
		tetrisDataGetter.copyData(currStep);
		nextCubeGetter.copyData(nextCubeTmp);
		stepCubeList.set(0, getCube());
		if(stepCubeList.get(0) == null) {
			return null;
		}
		stepCubeList.set(1, getNextCube());
		if(stepCubeList.get(1) == null) {
			return null;
		}
		
		LinkedList<CubeAction> bestRoute = findRouteByBestStatus();
		if(bestRoute == null) {
 			if((bestRoute = findRouteByNewBestStatus()) == null) {
				return null;
			}
		}
		
		if(cubeActionGroup.toBottomAction() != null) {
			while(!bestRoute.isEmpty()) {
				if(cubeActionGroup.actionEquals(bestRoute.getLast(), cubeActionGroup.moveDownAction())) {
					bestRoute.removeLast();
				} else {
					break;
				}
			}
			if(bestRoute.isEmpty()) {
				bestRoute.add(cubeActionGroup.toBottomAction());
			}
		}
		return bestRoute.toArray(new CubeAction[bestRoute.size()]);
	}
	
	private LinkedList<CubeAction> findRouteByBestStatus() {
		int srcIndexes[] = stepCubeList.get(0);
		int dstIndexes[] = bestTetrisStatus.cubeIndexes;
		if(dstIndexes == null) return null;
		for(int idx:dstIndexes) {
			if(bestTetrisStatus.bestStatus[idx] != 0) return null;
		}
		System.arraycopy(currStep, 0, cubeTmp, 0, currStep.length);
		for(int idx:srcIndexes) {
			cubeTmp[idx] = 0;
		}
		if(!Arrays.equals(cubeTmp, bestTetrisStatus.bestStatus)) {
			return null;
		}
		if(bestTetrisStatus.turnNum > 0) {
			LinkedList<CubeAction> ret = new LinkedList<CubeAction>();
			for(int i = 0;i < bestTetrisStatus.turnNum;i++) {
				ret.add(cubeActionGroup.transformAction());
			}
			bestTetrisStatus.turnNum = 0;
			return ret;
		}
		Integer xBias = null, yBias = null;
		if(srcIndexes.length != dstIndexes.length || srcIndexes.length == 0) return null;
		for(int i = 0;i < srcIndexes.length;i++) {
			int tmpX = (dstIndexes[i] % width) - (srcIndexes[i] % width);
			if(xBias == null) {
				xBias = tmpX;
			} else if(xBias != tmpX) {
				return new LinkedList<>(Arrays.asList(cubeActionGroup.transformAction()));
			}
			int tmpY = (dstIndexes[i] / width) - (srcIndexes[i] / width);
			if(yBias == null) {
				yBias = tmpY;
			} else if(yBias != tmpY) {
				return new LinkedList<>(Arrays.asList(cubeActionGroup.transformAction()));
			}
		}
		return getRoute(bestTetrisStatus.bestStatus, srcIndexes, 
				xBias.intValue(), yBias.intValue());
	}

	private LinkedList<CubeAction> findRouteByNewBestStatus() {
		RouteAndScore bestRoute = null;
		for(int turnNum = 0;turnNum < 4;turnNum++) {
			RouteAndScore route = getBestRoute(currStep, turnNum, 0);
			if(route == null) {
				continue;
			}
			if(bestRoute == null?true:route.score < bestRoute.score) {
				bestRoute = route;
				bestTetrisStatus.turnNum = turnNum;
			}
		}
		if(bestRoute == null) return null;
		System.arraycopy(currStep, 0, bestTetrisStatus.bestStatus, 0, currStep.length);
		int[] cube = stepCubeList.get(0);
		for(int idx:cube) {
			bestTetrisStatus.bestStatus[idx] = 0;
		}
		bestTetrisStatus.cubeIndexes = bestRoute.cubeIndexes;
		return findRouteByBestStatus();
	}
	
	private boolean checkIndexValid(int[] indexes) {
		if(indexes == null) return false;
		for(Cube possibleCube:Common.possibleCubeList()) {
			Cube cube = possibleCube.dup();
			int cubeIndexLen = 0;
			for(int i = 0;i < cube.getWidth();i++) {
				for(int j = 0;j < cube.getHeight();j++) {
					if(cube.getData(i, j) > 0) cubeIndexLen++;
				}
			}
			if(cubeIndexLen == indexes.length) return true;
		}
		return false;
	}


	static LinkedList<Integer> list = new LinkedList<Integer>();
	private int[] getCube() {
		int[] ret;
		list.clear();
		for(int i = 0;i < cubeTmp.length;i++) {
			cubeTmp[i] = (byte) (currStep[i] * 2 + lastStep[i]);
			if(cubeTmp[i] == 2) {
				list.add(i);
			}
		}
		ret = doGetCube(1);
		if(ret == null) ret = doGetCube(-1);
		if(ret == null) ret = doGetCube(width);
		return checkIndexValid(ret)?ret:null;
	}
	
	private int[] getNextCube() {
		LinkedList<Integer> ret = new LinkedList<Integer>();
		int xBias = (width - nextWidth) / 2;
		int yBias = height - nextHeight;
		for(int i = 0;i < nextCubeTmp.length;i++) {
			if(nextCubeTmp[i] != 0) {
				int x = i % nextWidth;
				int y = i / nextWidth;
				ret.add(((yBias + y) * width) + xBias + x);
			}
		}
		return ret.isEmpty()?null:listToIntArr(ret, 0);
	}
	
	private final LinkedList<Integer> tmpDoGetCube = new LinkedList<Integer>();
	private int[] doGetCube(int addIdx){
		tmpDoGetCube.clear();
		LinkedList<Integer> ret = new LinkedList<Integer>();
		tmpDoGetCube.addAll(list);
		while(!tmpDoGetCube.isEmpty()) {
			int val = tmpDoGetCube.removeFirst();
			int ind = val + addIdx;
			if(ind < 0 || ind >= cubeTmp.length) {
				return null;
			}
			if(cubeTmp[ind] == 0 || cubeTmp[ind] == 2) {
				return null;
			}
			ret.add(val);
			if(cubeTmp[ind] == 3) {
				tmpDoGetCube.add(ind);
			}
		}
		return ret.isEmpty()?null:listToIntArr(ret, 0);
	}
	
	private RouteAndScore getBestRoute(byte[] arr, int turnNum, int stepDepth) {
		RouteAndScore ret = new RouteAndScore();
		ret.score = Double.MAX_VALUE;
		int indexes[] = dup(stepCubeList.get(stepDepth));
		if(stepDepth == 0) {
			ret.cubeIndexes = new int[indexes.length];
		}
		for(int i = 0;i < turnNum;i++) {
			int xCenter = (getLeft(stepCubeList.get(stepDepth)) + getRight(stepCubeList.get(stepDepth))) / 2;
			int yCenter = (getTop(stepCubeList.get(stepDepth)) + getBot(stepCubeList.get(stepDepth))) / 2;
			for(int j = 0;j < indexes.length;j++) {
				int xSub = (indexes[j] % width) - xCenter;
				int ySub = indexes[j] / width - yCenter;
				int newX = xCenter + ySub, newY = yCenter - xSub;
				indexes[j] = newY * width + newX;
				if(newX < 0) {
					xCenter-=newX;
					for(int k = 0;k < indexes.length;k++) indexes[k]-=newX;
				}
				if(newX >= width) {
					xCenter-=newX - width - 1;
					for(int k = 0;k < indexes.length;k++) indexes[k]-=newX - width - 1;
				}
			}
		}
		for(int i = 0;i < indexes.length;i++) {
			int tmp = indexes[i];
			if(tmp >= width * height) {
				for(int j = 0;j < indexes.length;j++) {
					indexes[j] -= ((tmp - width * height) / width + 1) * width;
				}
			} else if(tmp < 0) {
				indexes = dup(stepCubeList.get(stepDepth));
				break;
			}
		}
		int left = -getLeft(indexes);
		int right = width - getRight(indexes) - 1;
		int bot = -getBot(indexes);
		resetScoreTmp(arr, scoreStepTmp[stepDepth], stepCubeList.get(stepDepth));
		for(int i = left;i <= right;i++) {
			for(int j = bot;j <= 0;j++) {
				if(checkValid(scoreStepTmp[stepDepth], width, height, indexes, i, j)) {
					if(checkValid(scoreStepTmp[stepDepth], width, height, indexes, i, j-1)) {
						break;
					}
					LinkedList<CubeAction> route = getRoute(scoreStepTmp[stepDepth], indexes, i, j);
					if(route == null)  continue;
					setValue(scoreStepTmp[stepDepth], indexes, i, j);
					if(stepDepth == scoreStepTmp.length - 1) {
						double score = calculator.getScore(scoreStepTmp[stepDepth], width, height);
						if(ret.route == null || score < ret.score) {
							ret.route = route;
							ret.score = score;
							
						}
					} else {
						for(int k = 0;k < 4;k++) {
							RouteAndScore ras = getBestRoute(scoreStepTmp[stepDepth], k, stepDepth + 1);
							if(ras != null) {
								if(ret.route == null || ras.score < ret.score) {
									ret.route = route;
									ret.score = ras.score;
									if(stepDepth == 0) {
										for(int a = 0;a < ret.cubeIndexes.length;a++) {
											ret.cubeIndexes[a] = indexes[a] + width*j+i;
										}
										Arrays.sort(ret.cubeIndexes);
									}
								}
							}
						}
					}
					resetScoreTmp(arr, scoreStepTmp[stepDepth], stepCubeList.get(stepDepth));
				}
			}
		}
		return ret.route == null?null:ret;
	}
	
	private LinkedList<CubeAction> getRoute(byte[] tetrisData, int[] indexes, int xBias, int yBias) {
		LinkedList<CubeAction> ret = new LinkedList<CubeAction>();
		return doGetRoute(cubeActionGroup, tetrisData, width, height, indexes, xBias, yBias, xBias, yBias, ret, 0)?ret:null;
	}

	private static boolean doGetRoute(CubeActionGroup cubeActionGroup, byte[] tetrisData, int width, int height, 
			int[] indexes, int xBias, int yBias,
			int lastXBias, int lastYBias, LinkedList<CubeAction> list, int depth) {
		if(xBias == 0 && yBias == 0) {
			return true;
		}
		if(!checkValid(tetrisData, width, height, indexes, xBias, yBias)) {
			return false;
		}
		if(depth >= height * 2) return false;
		if(yBias < 0) {
			if(doGetRoute(cubeActionGroup, tetrisData, width, height, indexes, xBias, yBias + 1, xBias, yBias, list, depth+1)) {
				list.add(cubeActionGroup.moveDownAction());
				return true;
			}
		}
		int firstDirection = xBias > 0?-1:1;
		if(lastXBias != xBias + firstDirection || lastYBias != yBias) {
			if(doGetRoute(cubeActionGroup, tetrisData, width, height, indexes, xBias + firstDirection, yBias, xBias, yBias, list, depth+1)) {
				list.add(firstDirection == 1?cubeActionGroup.moveLeftAction():
					cubeActionGroup.moveRightAction());
				return true;
			}
		}
		if(lastXBias != xBias - firstDirection || lastYBias != yBias) {
			if(doGetRoute(cubeActionGroup, tetrisData, width, height, indexes, xBias - firstDirection, yBias, xBias, yBias, list, depth+1)) {
				list.add(firstDirection == -1?cubeActionGroup.moveLeftAction():
					cubeActionGroup.moveRightAction());
				return true;
			}
		}
		return false;
	}

	private static boolean checkValid(byte[] tetrisArr, int width, int height, 
			int[] indexes, int xBias, int yBias) {
		for(int i:indexes) {
			int x = i % width;
			int y = i / width;
			if(x + xBias < 0 || x + xBias >= width || y + yBias < 0 || y + yBias >= height) {
				return false;
			}
			i += xBias+yBias*width;
			if(tetrisArr[i] != 0){
				return false;
			}
		}
		return true;
	}
	
	private void setValue(byte[] tetrisArr, int[] indexes, int xBias, int yBias) {
		for(int i:indexes) {
			tetrisArr[i+xBias+yBias*width] = 1;
		}
		l1:for(int i = 0;i < height;i++) {
			for(int j = 0;j < width;j++) {
				if(tetrisArr[i * width + j] == 0) {
					continue l1;
				}
			}
			System.arraycopy(tetrisArr, (i+1)*width, tetrisArr, i*width, tetrisArr.length - (i+1)*width);
			for(int j = 0;j < width;j++) {
				tetrisArr[height * width - 1 - j] = 0;
			}
		}
	}
	
	private static int[] listToIntArr(List<Integer> list, int bias) {
		int indexes[] = new int[list.size()];
		for(int i = 0;i < indexes.length;i++) {
			int val = list.get(i).intValue();
			indexes[i] = val + bias;
		}
		Arrays.sort(indexes);
		return indexes;
	}
	
	private void resetScoreTmp(byte[] arr, byte[] tmpArr, int[] cubeList) {
		System.arraycopy(arr, 0, tmpArr, 0, arr.length);
		for(Integer idx:cubeList) {
			tmpArr[idx] = 0;
		}
	}
	
	private int getLeft(int[] list) {
		int min = width - 1;
		for(int i:list) {
			if(i % width < min) {
				min = i % width;
			}
		}
		return min;
	}
	
	private int getBot(int[] list) {
		int min = height - 1;
		for(int i:list) {
			if(i / width < min) {
				min = i / width;
			}
		}
		return min;
	}
	
	private int getRight(int[] list) {
		int max = 0;
		for(int i:list) {
			if(i % width > max) {
				max = i % width;
			}
		}
		return max;
	}
	
	private int getTop(int[] list) {
		int max = 0;
		for(Integer i:list) {
			if(i / width > max) {
				max = i / width;
			}
		}
		return max;
	}
	
	private int[] dup(int[] src) {
		int ret[] = new int[src.length];
		System.arraycopy(src, 0, ret, 0, src.length);
		return ret;
	}
}
class RouteAndScore {
	LinkedList<CubeAction> route;
	double score = Double.MAX_VALUE;
	int[] cubeIndexes;
}
class BestTetrisStatus{
	byte[] bestStatus;
	int[] cubeIndexes;
	int turnNum;
}
