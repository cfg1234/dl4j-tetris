package perf.cfg.dl4j.tetris.common;

public interface CubeActionGroup {
	public CubeAction transformAction();
	public CubeAction moveLeftAction();
	public CubeAction moveRightAction();
	public CubeAction moveDownAction();
	public CubeAction toBottomAction();
	public boolean actionEquals(CubeAction a1, CubeAction a2);
}
