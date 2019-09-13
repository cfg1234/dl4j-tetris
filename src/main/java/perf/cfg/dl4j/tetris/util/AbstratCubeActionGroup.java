package perf.cfg.dl4j.tetris.util;

import java.util.HashMap;

import perf.cfg.dl4j.tetris.common.CubeActionGroup;
import perf.cfg.dl4j.tetris.common.CubeActionType;

public abstract class AbstratCubeActionGroup implements CubeActionGroup {
	private final HashMap<CubeActionType, Runnable> cubeActionMap
		= new HashMap<CubeActionType, Runnable>();
	

	public AbstratCubeActionGroup() {
		cubeActionMap.put(CubeActionType.ACTION_MOVE_DOWN, moveDownAction());
		cubeActionMap.put(CubeActionType.ACTION_MOVE_LEFT, moveLeftAction());
		cubeActionMap.put(CubeActionType.ACTION_MOVE_RIGHT, moveRightAction());
		cubeActionMap.put(CubeActionType.ACTION_TRANSFORM, transformAction());
		cubeActionMap.put(CubeActionType.ACTION_TRANSFORM_REVERSE, transformReverseAction());
		cubeActionMap.put(CubeActionType.ACTION_TO_BOTTOM, toBottomAction());
	}


	protected abstract Runnable moveDownAction();
	protected abstract Runnable moveLeftAction();
	protected abstract Runnable moveRightAction();
	protected abstract Runnable transformAction();
	protected abstract Runnable transformReverseAction();
	protected abstract Runnable toBottomAction();


	@Override
	public boolean execute(CubeActionType type) {
		if(!isImplemented(type)) return false;
		cubeActionMap.get(type).run();
		return true;
	}


	@Override
	public boolean isImplemented(CubeActionType type) {
		return cubeActionMap.get(type) != null;
	}
}
