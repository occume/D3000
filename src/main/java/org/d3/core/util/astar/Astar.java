package org.d3.core.util.astar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class Astar{
	 
    private int size = 20;
    private int row, column;
     
    /**
     * 0为可通过点， 1为不可通过点
     */
    private int[][] map = {
            {0,0,0,0,0,0,0,0,0,0,0},   
            {0,0,0,0,0,1,0,0,0,0,0},   
            {0,1,0,0,0,1,0,0,0,1,0},   
            {0,1,0,0,0,1,0,0,0,1,0},   
            {0,0,0,0,0,0,0,0,0,0,0},   
            {0,0,1,0,0,0,0,0,1,0,0},   
            {0,0,1,0,0,0,0,0,1,0,0}
    };
     
    /**
     * 人物位置
     */
    private int playerX, playerY = 2;
     
    /**
     * 目标位置
     */
    private int targetX, targetY;
     
     
    public Astar() {
        row = map.length;
        column = map[0].length;
        targetX = 3;
        targetY = 3;
    }
     

     
    /**
     * 人物运动
     */
    @SuppressWarnings("unchecked")
    private void playerMove() {
        int[] dirs = findPath();
    }
     
     
    @SuppressWarnings("unchecked")
    public int[] findPath()
    {
        int[] dirs = null;
         
        int x = playerX, y = playerY;
         
        while (true) {
            List aroundPos = this.getAroundPos(x, y);
            if(aroundPos.isEmpty()) //说明从x,y处无法到达终点
            {
                this.removeList(openlist, x, y); //openlist中删除位置为x, y的元素
                this.removeList(closelist, x, y); //openlist中删除位置为x, y的元素
            }else{             
                openlist.addAll(aroundPos);
            }
             
            int[] nearestPos = getNearPos();
            if(nearestPos == null)
            {
                System.out.println("找不到路径！");
                return null;
            }
            closelist.add(nearestPos);
             
            //到达了终点
            if(nearestPos[0] == targetX && nearestPos[1] == targetY)
            {
                break;
            }
            else //没有到终点，继续寻找
            {
                x = nearestPos[0];
                y = nearestPos[1];
            }
        }
         
        //打印一下
        for (int i = 0; i < closelist.size(); i++) {
            int[] data = (int[]) closelist.get(i);
            System.out.println(data[0] + ", " + data[1] + ", " + data[2]);
        }
         
        System.out.println();
         
        //得到最终的路径
        int[] temp = (int[]) closelist.get(closelist.size() - 1);
        pathList.add(temp);
        while(temp[0] != playerX || temp[1] != playerY)
        {
            temp = getPrePos(temp);
            int[] data;
            for (int i = 0; i < closelist.size(); i++) {
                data = (int[]) closelist.get(i);
                if(data[0] == temp[0] && data[1] == temp[1])
                {
                    temp = data;
                    pathList.add(temp);
                    break;
                }
            }
        }
         
         
        //打印一下
        for (int i = 0; i < pathList.size(); i++) {
            int[] data = (int[]) pathList.get(i);
            System.out.println(data[0] + ", " + data[1] + ", " + data[2]);
        }
        playerX = targetX;
        playerY = targetY;
         
         
         
        return dirs;
    }
     
    private List pathList = new LinkedList();
     
     
    private List openlist = new LinkedList();
    private List closelist = new LinkedList();
     
    private void removeList(List list, int x, int y) {
        for (int i = 0; i < list.size(); i++) {
            int[] listData = (int[]) list.get(i);
            if(listData[0] == x && listData[1] == y)
            {
                list.remove(i);
                return;
            }
        }
    }
     
    /**
     * 得到上一步的位置
     * @param temp
     * @return
     */
    private int[] getPrePos(int[] temp) {
        switch(temp[2])
        {
        case LEFT:
            return new int[]{temp[0] + 1, temp[1], temp[2]};
        case RIGHT:
            return new int[]{temp[0] - 1, temp[1], temp[2]};
        case UP:
            return new int[]{temp[0], temp[1] + 1, temp[2]};
        case DOWN:
            return new int[]{temp[0], temp[1] - 1, temp[2]};
        }
        return null;
    }
     
     
    private int[] getNearPos() {
        int distance = Integer.MAX_VALUE;
        int[] result = null;
        for (int i = 0; i < openlist.size(); i++) {
            int[] obj = (int[]) openlist.get(i);
            int temp = this.getDistance(obj[0], obj[1], targetX, targetY);
            if(temp < distance)
            {
                distance = temp;
                result = obj;
            }
        }
        return result;
    }
     
     
    /**
     * 得到x,y位置周围的位置(这些位置不在openlist和closelist中且可以通过)
     * @return
     */
    private List getAroundPos(int x, int y)
    {
        List list = new ArrayList();
         
        //左边
        if(x > 0 && map[y][x - 1] != 1)
        {
            int[] obj = new int[3];
            obj[0] = x - 1;
            obj[1] = y;
            obj[2] = LEFT;
            if(this.isExistAtList(openlist, obj) == false && this.isExistAtList(closelist, obj) == false)
            {              
                list.add(obj);
            }
        }
        //右边
        if(x < column - 1 && map[y][x + 1] != 1)
        {
            int[] obj = new int[3];
            obj[0] = x + 1;
            obj[1] = y;
            obj[2] = RIGHT;
            if(this.isExistAtList(openlist, obj) == false && this.isExistAtList(closelist, obj) == false)
            {              
                list.add(obj);
            }
        }
        //上边
        if(y > 0 && map[y - 1][x] != 1)
        {
            int[] obj = new int[3];
            obj[0] = x;
            obj[1] = y - 1;
            obj[2] = UP;
            if(this.isExistAtList(openlist, obj) == false && this.isExistAtList(closelist, obj) == false)
            {              
                list.add(obj);
            }
        }
        //下边
        if(y < row - 1 && map[y + 1][x] != 1)
        {
            int[] obj = new int[3];
            obj[0] = x;
            obj[1] = y + 1;
            obj[2] = DOWN;
            if(this.isExistAtList(openlist, obj) == false && this.isExistAtList(closelist, obj) == false)
            {              
                list.add(obj);
            }
        }
        return list;
    }
     
     
     
     
    private final int LEFT = -1, RIGHT = 1, UP = -2, DOWN = 2;
     
     
    /**
     * 判断list中是否已有了obj位置
     * @return
     */
    private boolean isExistAtList(List list, int[] obj)
    {
        for (int i = 0; i < list.size(); i++) {
            int[] listData = (int[]) list.get(i);
            if(listData[0] == obj[0] && listData[1] == obj[1])
            {
                return true;
            }
        }
        return false;
    }
     
     
     
     
    private int getDistance(int x1, int y1, int x2, int y2)
    {
        int a = (x1 - x2) * (x1 - x2);
        int b = (y1 - y2) * (y1 - y2);
        return (int) Math.sqrt(a + b);
    }
     
     
    public static void main(String[] args) {
        Astar astar = new Astar();
        astar.findPath();
    }
     
     
}