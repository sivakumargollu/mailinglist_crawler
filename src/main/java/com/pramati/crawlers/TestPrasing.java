package com.pramati.crawlers;

import java.io.IOException;

public class TestPrasing {

	public static void main(String args[]) throws IOException {
		if(args.length > 0){
			System.out.println("Program have arguments");
			
		}
		else{
			System.out.println("Program doesnt have arguments");
		}
		for (String s: args) {
            System.out.println(s);
        }
	}
}
