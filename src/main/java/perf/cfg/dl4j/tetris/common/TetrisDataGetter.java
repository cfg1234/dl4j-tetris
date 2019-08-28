package perf.cfg.dl4j.tetris.common;

public interface TetrisDataGetter {
	public void copyData(byte[] data) throws IllegalArgumentException;
	public int getWidth();
	public int getHeight();
}
