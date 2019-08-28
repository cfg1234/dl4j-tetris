package perf.cfg.dl4j.tetris.common;

public interface TetrisAreaGetter {
	public BoundsGenerator getTetrisArea();
	public BoundsGenerator getNextCubeArea();
}
