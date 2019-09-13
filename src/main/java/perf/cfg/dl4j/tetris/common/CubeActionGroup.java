package perf.cfg.dl4j.tetris.common;

public interface CubeActionGroup {
	public boolean execute(CubeActionType type);
	public boolean isImplemented(CubeActionType type);
}
