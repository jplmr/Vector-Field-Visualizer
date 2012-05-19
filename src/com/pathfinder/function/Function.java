package com.pathfinder.function;

public class Function {
	static Parser parse = new Parser();

	public static double eval(double x, double y, String function) {
		function = function.replaceAll("x", "" + x + "");
		function = function.replaceAll("y", "" + y + "");
		try {
			double answer = Double.parseDouble(parse.parse(function).substring(6));
			return answer;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(function);
		}
		return 0.0;
	}
}
