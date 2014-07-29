package org.d3.core.util.astar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;



public class Astar2{
	
	public static int[][] map = new int[][]{
        // x 0 1 2 3 4 5 6 7 8 9    y
            {0,0,0,0,0,0,0,0,0,0},//0
            {0,1,1,1,1,1,1,1,1,0},//1
            {0,1,1,1,1,1,1,1,1,0},//2
            {0,1,1,1,1,1,1,1,1,0},//3
            {0,1,1,1,1,1,1,1,1,0},//4
            {0,1,1,1,1,1,1,1,1,0},//5
            {0,1,1,1,1,1,1,1,1,0},//6
            {0,1,1,1,1,1,1,1,1,0},//7
            {0,1,1,1,1,1,1,1,1,0},//8
            {0,0,0,0,0,0,0,0,0,0} //9

    }; //虚拟地图

    public List<Point> meet(Point p1,Point p2){
//        if(p1.equals(p2) || !Asqare.fonts[p1.y-1][p1.x-1].equals(Asqare.fonts[p2.y-1][p2.x-1])) return null;
        int index = 0;
        boolean left = true,right = true,up = true,down = true;
        //分 上下左右四个方向查找 每次+1 移动的找有障碍物返回
        while(left || right || up || down){

            if(right && meetCols(p1,new Point(p1.x+index,p1.y))==0){
                if(meetCols(p2,new Point(p1.x+index,p2.y))==0 && 
                        meetRows(new Point(p1.x+index,p1.y),new Point(p1.x+index,p2.y)) == 0){
                    return surveyPointer(p1,p2,new Point(p1.x+index,p1.y),new Point(p1.x+index,p2.y));
                }
            }else{
                right = false;
            }

            if(left && meetCols(p1,new Point(p1.x-index,p1.y))==0){
                if(meetCols(p2,new Point(p1.x-index,p2.y))==0 && 
                        meetRows(new Point(p1.x-index,p1.y),new Point(p1.x-index,p2.y)) == 0){
                    return surveyPointer(p1,p2,new Point(p1.x-index,p1.y),new Point(p1.x-index,p2.y));
                }
            }else{
                left = false;
            }

            if(down && meetRows(p1,new Point(p1.x,p1.y+index))==0){
                if(meetRows(p2,new Point(p2.x,p1.y+index))==0 && 
                        meetCols(new Point(p1.x,p1.y+index),new Point(p2.x,p1.y+index)) == 0){
                    return surveyPointer(p1,p2,new Point(p1.x,p1.y+index),new Point(p2.x,p1.y+index));
                }
            }else{
                down = false;
            }

            if(up && meetRows(p1,new Point(p1.x,p1.y-index))==0){
                if(meetRows(p2,new Point(p2.x,p1.y-index))==0 && 
                        meetCols(new Point(p1.x,p1.y-index),new Point(p2.x,p1.y-index)) == 0){
                    return surveyPointer(p1,p2,new Point(p1.x,p1.y-index),new Point(p2.x,p1.y-index));
                }
            }else{
                up = false;
            }

            index ++;
        }
        return null;

    }

    private int meetCols(Point p1,Point p2){
        int start = p1.x < p2.x ? p1.x : p2.x;
        int end = p1.x < p2.x ? p2.x : p1.x;

        if(start < 0 || end >=map[0].length) return 1;

        for (int i = start; i <= end ; i++) {
            if(map[p1.y][i]==1)
                return map[p1.y][i];
        }
        return 0;
    }

    private int meetRows(Point p1,Point p2){
        int start = p1.y < p2.y ? p1.y : p2.y;
        int end = p1.y < p2.y ? p2.y : p1.y;

        if(start < 0 || end >= map.length) return 1;

        for (int i = start; i <= end ; i++) {
            if(map[i][p1.x]==1)
                return map[i][p1.x];
        }
        return 0;
    }

    private List<Point> surveyPointer(Point p1,Point p2,Point proxy1,Point proxy2){
        List<Point> pList = new ArrayList<Point>();
        pList.add(p1);
        if(proxy1.equals(p1) && proxy2.equals(p2)){
        }else if(proxy1.equals(p1)){
            pList.add(proxy2);
        }else if(proxy2.equals(p2)){
            pList.add(proxy1);
        }else{
            pList.add(proxy1);
            pList.add(proxy2);
        }
        pList.add(p2);
        return pList;
    }
    
    public static void main(String...strings){
    	Astar2 a = new Astar2();
    	List<Point> ret = a.meet(new Point(0, 0), new Point(6, 0));
    	System.out.println(ret);
    }
}