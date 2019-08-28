package perf.cfg.dl4j.tetris.common;


import java.awt.Point;

public class Cube implements Cloneable{
	
	public static final Cube CUBE_EMPTY = new Cube(0,0,new byte[0]);

	protected int width;
	protected int height;
	protected byte[] data;
	
	public Cube(int width, int height, byte[] data){
		super();
		this.width = width;
		this.height = height;
		this.data = data;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public synchronized void transform(boolean clockwise) {
		byte[] newArr = new byte[data.length];
		for(int i = 0;i < width;i++) {
			for(int j = 0;j < height;j++) {
				if(clockwise) 
					newArr[height * (width - 1 - i) + j] = getData(i, j);
				else
					newArr[height * i + (height - 1 - j)] = getData(i, j);
			}
		}
		System.arraycopy(newArr, 0, data, 0, data.length);
		swapWidthAndHeight();
	}

	protected void swapWidthAndHeight() {
		int tmp = width;
		width = height;
		height = tmp;
	}

	public byte getData(int widthIdx, int heightIdx) {
		return data[heightIdx * width + widthIdx];
	}
	
	public Cube dup() {
		Cube ret;
		try {
			ret = (Cube) this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		byte newData[] = new byte[data.length];
		System.arraycopy(data, 0, newData, 0, data.length);
		ret.data = newData;
		return ret;
		
	}
	
	public Point suggestedCenter() {
		return new Point(width / 2, height / 2 - 1);
	}

}
