package org.d3.util.astar;

public class Point {
	
	int x;
	int y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Point(Point r) {
		this.x = r.x;
		this.y = r.y;
	}
	public String toString(){
		return" [" + x + "_" + y + "]";
	}
}
