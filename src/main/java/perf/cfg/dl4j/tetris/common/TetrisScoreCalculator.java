package perf.cfg.dl4j.tetris.common;

public interface TetrisScoreCalculator {
	public double getScore(byte[] tetrisData, int width, int height);
}
