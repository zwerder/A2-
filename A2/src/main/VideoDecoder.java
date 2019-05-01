package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import ac.ArithmeticDecoder;
import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class VideoDecoder {

	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		String input_file_name = "data/compressed.dat";
		String output_file_name = "data/uncompressed.dat";

		FileInputStream fis = new FileInputStream(input_file_name);

		InputStreamBitSource bit_source = new InputStreamBitSource(fis);

		Integer[] intensities = new Integer[256];
		
		for (int i=0; i<256; i++) {
			intensities[i] = i;
		}

		PixelModel[] models = new PixelModel[256];
		
		for (int i=0; i<256; i++) {
			models[i] = new PixelModel(intensities);
		}

		int num_symbols = 1228800;
		int range_bit_width = 40; 
		
		ArithmeticDecoder<Integer> decoder = new ArithmeticDecoder<Integer>(range_bit_width);
		
		FileOutputStream fos = new FileOutputStream(output_file_name);

		PixelModel model = models[0];

		for (int i=0; i<num_symbols; i++) {
			int sym = decoder.decode(model, bit_source);
			fos.write(sym);
			
			model.addToCount(sym);
			
			model = models[sym];
		}

		fos.flush();
		fos.close();
		fis.close();
	}
}
