package perf.cfg.dl4j.tetris.util;

import perf.cfg.dl4j.tetris.common.TetrisScoreCalculator;

public class PierreDellacherieScoreCalculator implements TetrisScoreCalculator {
	
	private static final int[] SUM_WELLS_NUM = new int[Common.getTetrisHeight() + 1];
	
	static {
		for(int i = 0;i < SUM_WELLS_NUM.length;i++) {
			for(int j = 0;j < i;j++) {
				SUM_WELLS_NUM[i] += j+1;
			}
		}
	}
	
	private static final double landingHeightParam = Common.getLandingHeightParam();
	private static final double boardRowTransitionsParam = Common.getBoardRowTransitionsParam();
	private static final double boardColTransitionsParam = Common.getBoardColTransitionsParam();
	private static final double boardBuriedHolesParam = Common.getBoardBuriedHolesParam();
	private static final double boardWellsParam = Common.getBoardWellsParam();

	@Override
	public double getScore(byte[] tetrisData, int width, int height) {
		double landingHeight = getHeight(tetrisData, width, height);
		double boardRowTransitions = getBoardRowTransitions(tetrisData, width, height);
		double boardColTransitions = getBoardColTransitions(tetrisData, width, height);
		double boardBuriedHoles = getBoardBuriedHoles(tetrisData, width, height);
		double boardWells = getBoardWells(tetrisData, width, height);
		double score = - landingHeightParam * landingHeight
				- boardRowTransitionsParam * boardRowTransitions
				- boardColTransitionsParam * boardColTransitions
				- boardBuriedHolesParam * boardBuriedHoles
				- boardWellsParam * boardWells;
		return 100 / score;
	}
	
	private double getBoardWells(byte[] tetrisData, int width, int height) {
		int wells = 0;
		double sum = 0;
		for(int i = 0;i < width;i++) {
			boolean isWell = false;
			for(int j = height - 1;j >= 0;j--) {
				byte val = tetrisData[j * width + i];
				if(val == 0) {
					if(isWell) {
						wells++;
					} else if((i == 0 || tetrisData[j * width + i - 1] != 0) && 
							(i == width - 1 || tetrisData[j * width + i + 1] != 0)) {
						isWell = true;
						wells++;
					}
				} else {
					isWell = false;
					sum += SUM_WELLS_NUM[wells];
					wells = 0;
				}
			}
			sum += SUM_WELLS_NUM[wells];
			wells = 0;
		}
		return sum;
	}

	private double getBoardBuriedHoles(byte[] tetrisData, int width, int height) {
		int holeNum = 0;
		for(int i = 0;i < width;i++) {
			boolean hasHole = false;
			for(int j = height - 1;j >= 0;j--) {
				byte val = tetrisData[j * width + i];
				if(val > 0) {
					hasHole = true;
				} else if(hasHole) {
					holeNum++;
				}
			}
		}
		return holeNum;
	}

	private double getBoardColTransitions(byte[] tetrisData, int width, int height) {
		int transitions = 0;
		for(int i = 0;i < width;i++) {
			Byte last = null;
			for(int j = 0;j < height;j++) {
				if(last != null && last.byteValue() != tetrisData[j * width + i]) {
					transitions++;
				}
				last = tetrisData[j * width + i];
			}
		}
		return transitions;
	}

	private double getBoardRowTransitions(byte[] tetrisData, int width, int height) {
		int transitions = 0;
		for(int i = 0;i < height;i++) {
			Byte last = null;
			for(int j = 0;j < width;j++) {
				if(last != null && last.byteValue() != tetrisData[i * width + j]) {
					transitions++;
				}
				last = tetrisData[i * width + j];
			}
		}
		return transitions;
	}

	private double getHeight(byte[] tetrisData, int width, int height) {
		for(int i = tetrisData.length - 1;i >= 0;i--) {
			if(tetrisData[i] > 0) 
				return (i+1) / width + 1;
		}
		return 0;
	}

}
