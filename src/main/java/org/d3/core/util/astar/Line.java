package org.d3.core.util.astar;

import org.d3.core.util.Point;


public class Line
{

    public Point a;
    public Point b;
    public int direct;

    public Line()
    {
    }

    public Line(int direct, Point a, Point b)
    {
        this.direct = direct;
        this.a = a;
        this.b = b;
    }

	@Override
	public String toString() {
		return "Line [a=" + a + ", b=" + b + "]";
	}
}
