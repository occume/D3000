package org.d3.util.astar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import org.d3.util.Point;

public class LLK
{

    private int level;
    private int map[][];
    int array[];
    private int restBlock;
    private Vector vector;
    private List<Point> points = new ArrayList<Point>();
//    AnimateDelete animate;
    private boolean test;

    public LLK()
    {
        level = 28;
        map = new int[10][17];
        array = new int[170];
        restBlock = level * 4;
        vector = new Vector();
        test = false;
        initMap();
    }

    public LLK(int[][] map)
    {
//        this.level = 28;
        this.map = map;
        array = new int[170];
//        restBlock = this.level * 4;
        vector = new Vector();
        test = false;
//        this.level = level;
        restBlock = level * 4;
//        initMap();
    }

    public void setTest(boolean test)
    {
        this.test = test;
    }

    public void setLevel(int level)
    {
        this.level = level;
        restBlock = level * 4;
        initMap();
    }

    private void initMap()
    {
        for(int i = 0; i < level; i++)
        {
            array[i * 4] = i + 1;
            array[i * 4 + 1] = i + 1;
            array[i * 4 + 2] = i + 1;
            array[i * 4 + 3] = i + 1;
        }

        random(array);
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 17; j++)
                map[i][j] = array[i * 17 + j];

        }

    }

    private void random(int array[])
    {
        Random random = new Random();
        for(int i = array.length; i > 0; i--)
        {
            int j = random.nextInt(i);
            int temp = array[j];
            array[j] = array[i - 1];
            array[i - 1] = temp;
        }

    }

    public void earse(Point a, Point b)
    {
        map[a.x][a.y] = 0;
        map[b.x][b.y] = 0;
        restBlock -= 2;
    }

    public int getCount()
    {
        return restBlock <= 0 ? 0 : restBlock;
    }

    public void refresh()
    {
        int count = getCount();
        if(count <= 0)
            return;
        int temp[] = new int[count];
        count = 0;
        for(int row = 0; row < 10; row++)
        {
            for(int col = 0; col < 17; col++)
                if(map[row][col] > 0)
                {
                    temp[count] = map[row][col];
                    count++;
                }

        }

        random(temp);
        count = 0;
        for(int row = 0; row < 10; row++)
        {
            for(int col = 0; col < 17; col++)
                if(map[row][col] > 0)
                {
                    map[row][col] = temp[count];
                    count++;
                }

        }

    }

    private boolean horizon(Point a, Point b, boolean recorder)
    {
        if(a.x == b.x && a.y == b.y)
            return false;
        int x_start = a.y <= b.y ? a.y : b.y;
        int x_end = a.y <= b.y ? b.y : a.y;
        for(int x = x_start + 1; x < x_end; x++)
            if(map[a.x][x] != 0)
                return false;

//        if(!test && recorder)
//            animate = new AnimateDelete(1, a, b);
        return true;
    }

    private boolean vertical(Point a, Point b, boolean recorder)
    {
        if(a.x == b.x && a.y == b.y)
            return false;
        int y_start = a.x <= b.x ? a.x : b.x;
        int y_end = a.x <= b.x ? b.x : a.x;
        for(int y = y_start + 1; y < y_end; y++)
            if(map[y][a.y] != 0)
                return false;

//        if(!test && recorder)
//            animate = new AnimateDelete(0, a, b);
        return true;
    }

    private boolean oneCorner(Point a, Point b)
    {
        Point c = new Point(a.x, b.y);
        Point d = new Point(b.x, a.y);
        if(map[c.x][c.y] == 0)
        {
            boolean method1 = horizon(a, c, false) && vertical(b, c, false);
            if(method1)
            {
//                if(!test)
//                    animate = new AnimateDelete(1, a, c, b);
            	points.add(c);
                return method1;
            }
        }
        if(map[d.x][d.y] == 0)
        {
            boolean method2 = vertical(a, d, false) && horizon(b, d, false);
//            if(method2 && !test)
//                animate = new AnimateDelete(0, a, d, b);
            points.add(d);
            return method2;
        } else
        {
            return false;
        }
    }

    private Vector scan(Point a, Point b)
    {
        Vector v = new Vector();
//        Point c = new Point(a.x, b.y);
//        Point d = new Point(b.x, a.y);
        for(int y = a.y; y >= 0; y--)
            if(map[a.x][y] == 0 && map[b.x][y] == 0 && vertical(new Point(a.x, y), new Point(b.x, y), false))
                v.add(new Line(0, new Point(a.x, y), new Point(b.x, y)));

        for(int y = a.y; y < 10; y++)
            if(map[a.x][y] == 0 && map[b.x][y] == 0 && vertical(new Point(a.x, y), new Point(b.x, y), false))
                v.add(new Line(0, new Point(a.x, y), new Point(b.x, y)));

        for(int x = a.x; x >= 0; x--)
            if(map[x][a.y] == 0 && map[x][b.y] == 0 && horizon(new Point(x, a.y), new Point(x, b.y), false))
                v.add(new Line(1, new Point(x, a.y), new Point(x, b.y)));

        for(int x = a.x; x < 10; x++)
            if(map[x][a.y] == 0 && map[x][b.y] == 0 && horizon(new Point(x, a.y), new Point(x, b.y), false))
                v.add(new Line(1, new Point(x, a.y), new Point(x, b.y)));

        return v;
    }

    private boolean twoCorner(Point a, Point b)
    {
        vector = scan(a, b);
        if(vector.isEmpty())
            return false;
        for(int index = 0; index < vector.size(); index++)
        {
            Line line = (Line)vector.elementAt(index);
            if(line.direct == 1)
            {
                if(vertical(a, line.a, false) && vertical(b, line.b, false))
                {
//                	points.add(a);
                	points.add(line.a);
//                	points.add(b);
                	points.add(line.b);
//                    if(!test)
//                        animate = new AnimateDelete(0, a, line.a, line.b, b);
                    return true;
                }
            }
            else if(horizon(a, line.a, false) && horizon(b, line.b, false))
            {
//            	points.add(a);
            	points.add(line.a);
//            	points.add(b);
            	points.add(line.b);
//                if(!test)
//                    animate = new AnimateDelete(1, a, line.a, line.b, b);
                return true;
            }
        }

        return false;
    }

    public boolean test(Point a, Point b)
    {
        if(map[a.x][a.y] != map[b.x][b.y])
            return false;
        if(a.x == b.x && horizon(a, b, true)){
            System.out.println("h");
        	return true;
        }
        if(a.y == b.y && vertical(a, b, true)){
        	System.out.println("v");
            return true;
        }
        if(oneCorner(a, b)){
        	System.out.println("1 corner");
            return true;
        }
        else{
        	System.out.println("2 corner");
            return twoCorner(a, b);
        }
    }

    public Line findNext(Point a)
    {
        Point b = new Point();
        a = findFirst(a);
        if(a.equals(new Point(-1, -1)))
            return new Line(0, a, b);
        for(; !a.equals(new Point(-1, -1)); a = findFirst(a))
            for(b = findSecond(a, b); !b.equals(new Point(-1, -1)); b = findSecond(a, b))
                if(test(a, b))
                    return new Line(1, a, b);


        return new Line(0, a, b);
    }

    private Point findFirst(Point a)
    {
        int offset = 0;
        if(a != null)
            offset = a.x * 17 + a.y;
        if(offset < 0)
            offset = -1;
        for(int x = offset + 1; x < 170; x++)
        {
            int row = Math.round(x / 17);
            int col = x - row * 17;
            if(map[row][col] != 0)
                return new Point(row, col);
        }

        return new Point(-1, -1);
    }

    private Point findSecond(Point a, Point b)
    {
        if(a == null)
            return new Point(-1, -1);
        if(a.x + a.y < 0)
            return new Point(-1, -1);
        if(b == null)
            b = new Point(0, 0);
        int offset = Math.max(a.x * 17 + a.y, b.x * 17 + b.y);
        for(int x = offset + 1; x < 170; x++)
        {
            int row = Math.round(x / 17);
            int col = x - row * 17;
            if(map[row][col] == map[a.x][a.y])
                return new Point(row, col);
        }

        return new Point(-1, -1);
    }

    public int[][] getMap()
    {
        return map;
    }
    
    public List<Point> getPoints(){
    	return points;
    }
    
    public static List<Point> search(int x1,int y1,int x2,int y2, List<String> passed){
    	int[][] map=new int[][]{// 地图数组
                {0,0,0,0,0,0,0,0,0,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,0,0,0,0,0,0,0,0,0}
        };
		
		for(String tile: passed){
			int x = Integer.valueOf(tile.split("_")[0]);
			int y = Integer.valueOf(tile.split("_")[1]);
			map[x][y] = 0;
		}
		
//		map[x1][y1] = 0;
//		map[x2][y2] = 0;
		
		LLK map1 = new LLK(map);
		boolean find = map1.test(new Point(x1, y1), new Point(x2, y2));
		if(!find){
			return null;
		}
		return map1.getPoints();
    }
    
    public static void main(String...strings){
    	int[][] mapData = new int[][]{// 地图数组
                {0,0,0,0,0,0,0,0,0,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,0,1,1,1,1,1,0},
                {0,1,1,0,0,0,0,1,1,0},
                {0,1,1,1,1,0,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,0,0,0,0,0,0,0,0,0}
        };
    	LLK map = new LLK(mapData);
    	boolean find = map.test(new Point(8, 4), new Point(8, 2));
    	System.out.println(map.getPoints());
    	System.out.println(find);
    }
}
