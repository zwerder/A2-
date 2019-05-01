package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;


import ac.ArithmeticEncoder;
import io.OutputStreamBitSink;

public class VideoEncoder {

	public static void main(String[] args) throws IOException {
		String input_file_name = "data/out.dat";
		String output_file_name = "data/compressed.dat";

		int num_symbols = 1228800;	
		Integer[] intensities = new Integer[256];
		
		for (int i=0; i<256; i++) {
			intensities[i] = i;
		}
	
		PixelModel[] models = new PixelModel[256];
		
		for (int i=0; i<256; i++) {
			models[i] = new PixelModel(intensities);
		}

		ArithmeticEncoder<Integer> encoder = new ArithmeticEncoder<Integer>(40);
		
		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);	

		FileInputStream fis = new FileInputStream(input_file_name);
		
		PixelModel model = models[0];

		for (int i=0; i< num_symbols; i++) {
			int next_symbol = fis.read();
			encoder.encode(next_symbol, model, bit_sink);
			
			model.addToCount(next_symbol);
			
			model = models[next_symbol];
		}
		fis.close();

		encoder.emitMiddle(bit_sink);
		bit_sink.padToWord();
		fos.close();
	}
}
