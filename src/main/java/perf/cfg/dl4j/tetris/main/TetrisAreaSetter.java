package perf.cfg.dl4j.tetris.main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;

import javax.swing.JOptionPane;

import perf.cfg.dl4j.tetris.util.Common;

public class TetrisAreaSetter {

	public static void main(String[] args) throws IOException {
		Point leftTop = doGetPoint("主方块区域左上角坐标"), 
				rightBot = doGetPoint("主方块区域右下角坐标"), 
				leftTop2 = doGetPoint("下一个方块区域左上角坐标"),
				rightBot2 = doGetPoint("下一个方块区域右下角坐标");
		Common.setTetrisArea(leftTop, rightBot);
		Common.setNextCubeArea(leftTop2, rightBot2);
		JOptionPane.showMessageDialog(null, "设置坐标成功，程序正常退出");
	}
	
	private static Point doGetPoint(String pointName) {
		while(true) {
			JOptionPane.showMessageDialog(null, String.format("%d秒内使用鼠标确定%s!", 
					Common.getTetrisAreaSetterInterval(), pointName));
			try {
				Thread.sleep(Common.getTetrisAreaSetterInterval() * 1000);
			} catch (InterruptedException e) {
			}
			Point ret = MouseInfo.getPointerInfo().getLocation();
			int option = JOptionPane.showConfirmDialog(null, String.format("%s:%s,确认?", pointName, ret), 
					"确认", JOptionPane.YES_NO_CANCEL_OPTION);
			if(option == JOptionPane.YES_OPTION) return ret;
			if(option == JOptionPane.CANCEL_OPTION) System.exit(1);
		}
	}

}
