package perf.cfg.dl4j.tetris.main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;

import javax.swing.JOptionPane;

import perf.cfg.dl4j.tetris.util.Common;

public class TetrisAreaSetter {

	public static void main(String[] args) throws IOException {
		Point leftTop = doGetPoint("�������������Ͻ�����"), 
				rightBot = doGetPoint("�������������½�����"), 
				leftTop2 = doGetPoint("��һ�������������Ͻ�����"),
				rightBot2 = doGetPoint("��һ�������������½�����");
		Common.setTetrisArea(leftTop, rightBot);
		Common.setNextCubeArea(leftTop2, rightBot2);
		JOptionPane.showMessageDialog(null, "��������ɹ������������˳�");
	}
	
	private static Point doGetPoint(String pointName) {
		while(true) {
			JOptionPane.showMessageDialog(null, String.format("%d����ʹ�����ȷ��%s!", 
					Common.getTetrisAreaSetterInterval(), pointName));
			try {
				Thread.sleep(Common.getTetrisAreaSetterInterval() * 1000);
			} catch (InterruptedException e) {
			}
			Point ret = MouseInfo.getPointerInfo().getLocation();
			int option = JOptionPane.showConfirmDialog(null, String.format("%s:%s,ȷ��?", pointName, ret), 
					"ȷ��", JOptionPane.YES_NO_CANCEL_OPTION);
			if(option == JOptionPane.YES_OPTION) return ret;
			if(option == JOptionPane.CANCEL_OPTION) System.exit(1);
		}
	}

}
