package perf.cfg.dl4j.tetris.util;

import java.io.File;
import java.io.IOException;

import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;

public class NormImageLoader {
	
	private NativeImageLoader imageLoader;

	private NormImageLoader(NativeImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}
	
	public NormImageLoader() {
		this(new NativeImageLoader());
	}
	
	public NormImageLoader(int height, int width, int channels) {
		this(new NativeImageLoader(height, width, channels));
	}
	
	public INDArray asMatrix(File f) throws IOException {return norm(imageLoader.asMatrix(f));};
	public INDArray asMatrix(Object o) throws IOException {return norm(imageLoader.asMatrix(o));};
	
	private INDArray norm(INDArray src) {
		return src.divi(256);
	}

}
