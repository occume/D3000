package org.d3.core.util.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeeAgain {
    // W * H must be a even
    private static final int W = 8;
    private static final int H = 8;
    // 最外面一圈是0，这样好巡检，显示为W*H
    private static final int AW = 10;
    private static final int AH = 10;
     
    public static void main(String[] args) throws Exception {
        //Collections.shuffle(list)
        //list.addAll(new int[]{});
//        createMap();
        
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
         
        find(map, 1, 1, 8, 1);
//      Map<String, Object> map = new HashMap<String, Object>();
//      map.put("k1", new Item(12,4,6));
//      map.put("k2", new Item(12,4,6));
//      map.put("k3", new Item(12,2,6));
//     
//      System.out.println((map.get("k1").equals(map.get("k2"))) + ","+map.get("k1").equals(map.get("k3")));
    }
     
    /**
     * 矩阵插入 </br>
     * 这里默认约定src小于des, 并且插入的位置不会溢出 </br>
     * 否则会出错
     * @param src 要插入的矩阵
     * @param des 包含矩阵
     * @param x 起始x
     * @param y 起始y
     */
    public static void rectInsert(int[][] src, int[][] des, int x, int y) {
        for(int i = 0; i < src.length; i ++) {
            for(int j = 0; j < src[0].length; j ++) {
                des[i + x][j + y] = src[i][j];
            }
        }
    }
    /**
     * 创建地图
     */
    public static int[][] createMap() throws Exception {
        int[][] amap = new int[AW][AH];
        int[][] map = new int[W][H];
        List<Integer> list = new ArrayList<Integer>();
        // 生成偶数对对象
        for(int i = 0; i < W  * H; i ++) {
            list.add(i % W + 1);
            System.out.print(list.get(i) + ((i + 1) % W == 0 ? "\n" : " "));
        }
        //if(map.length > 0) return;
        // 洗牌
        Collections.shuffle(list);
        System.out.println("==============");
        int ww = 0;
        for(int i = 0; i < W * H; i ++) {
            System.out.print(list.get(i) + ((i + 1) % 8 == 0 ? "\n" : " "));
            map[i / W][ww ++] = list.get(i);
            ww = ww == W ? 0 : ww;
        }
        // 插入矩阵
        rectInsert(map, amap, 1, 1);
        System.out.println("**************");
        // begin show
        for(int i = 0; i < W; i ++ ) {
            for(int j = 0; j < W; j ++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
        for(int[] m : amap) {
            for(int a : m) {
                System.out.print(a + " ");
            }
            System.out.println();
        }
        //removed(amap, 1, 8, 0, 0);
        return amap;
        // end show
//      System.out.println("\n\n");
//      for(int i = 0; i < W; i ++) {
//          for(int j = 0; j < W; j ++) {
//              removed(amap, i, j, 0, 0);
//              show(amap);
//              Thread.sleep(2000);
//          }
//      }
        // test remove
    }
     
    public static void show(int[][] map) {
        System.out.println("\n\n====================");
         for(int[] m : map) {
            for(int a : m) {
                System.out.print(a + " ");
            }
            System.out.println();
        }
        System.out.println("====================");
    }
 
    /**
     * 寻找直线算法  </br>
     * 算法优化，见里面注释
     * @param s 要加入的集合，可以为null
     * @param map 地图
     * @param x 源位置
     * @param y
     * @param zero 是否寻找为0的
     * @param die 是否是寻找无解
     * @return 返回null则有解且能删除
     */
    private static List<Item> line(List<Item> s, int[][] map, int x, int y, int tx, int ty, boolean zero, boolean die) {
        int renVal = map[x][y], tagVal = map[tx][ty];
        // 对比的对象
        Item tagItem = new Item(tagVal, tx, ty);
        Item renItem = new Item(renVal, x, y);
         
        if(s == null)
            s = new ArrayList<Item>();
        int tempx = x, tempy = y;
        // x，y寻址
        while(-- tempx >= 0) {
            Item item = new Item(map[tempx][y], tempx, y);
            //System.out.println(item);
            if(zero) {
                if(item.val == 0) {
                    s.add(item);
                } else {
                    break;
                }
            } else {
                if(item.val != 0) {
                    if(die) {
                        if(!renItem.equals(item) && renVal == item.val) {
                            return null;
                        }
                    }
                    if(renVal == tagVal && item.equals(tagItem)) {
                        // 这里其实就找到了，可以删除，然后返回
                        // 下面同理
                        return null;
                    }
                    s.add(item);
                    break;
                }
            }
             
        }
        while(-- tempy >= 0) {
            Item item = new Item(map[x][tempy],x,tempy);
            if(zero) {
                if(item.val == 0) {
                    s.add(item);
                } else {
                    break;
                }
            } else {
                if(item.val != 0) {
                    if(die) {
                        if(!renItem.equals(item) && renVal == item.val) {
                            return null;
                        }
                    }
                    if(renVal == tagVal && item.equals(tagItem)) {
                        // 这里其实就找到了，可以删除，然后返回
                        // 下面同理
                        return null;
                    }
                    s.add(item);
                    break;
                }
            }
        }
        tempx = x;
        tempy = y;
        while( ++ tempx < AW ) {
            Item item = new Item(map[tempx][y], tempx, y);
            if(zero) {
                if(item.val == 0) {
                    s.add(item);
                } else {
                    break;
                }
            } else {
                if(item.val != 0) {
                    if(die) {
                        if(!renItem.equals(item) && renVal == item.val) {
                            return null;
                        }
                    }
                    if(renVal == tagVal && item.equals(tagItem)) {
                        // 这里其实就找到了，可以删除，然后返回
                        // 下面同理
                        return null;
                    }
                    s.add(item);
                    break;
                }
            }
        }
        while( ++ tempy < AH ) {
            Item item = new Item(map[x][tempy], x, tempy);
            if(zero) {
                if(item.val == 0) {
                    s.add(item);
                } else {
                    break;
                }
            } else {
                if(item.val != 0) {
                    if(die) {
                        if(!renItem.equals(item) && renVal == item.val) {
                            return null;
                        }
                    }
                    if(renVal == tagVal && item.equals(tagItem)) {
                        // 这里其实就找到了，可以删除，然后返回
                        // 下面同理
                        return null;
                    }
                    s.add(item);
                    break;
                }
            }
        }
        return s;
    }
    private static boolean find(int[][] map, int x, int y, int tx, int ty) {
        // x，y所所处位置的2个转角之内的所有对象集合
        List<Item> s = new ArrayList<Item>();
        // 临时集合,t是s的直线0值，t1是t的直线非0值
        List<Item> t = new ArrayList<Item>();
        List<Item> t1 = new ArrayList<Item>();
        // 开始的对象
        Item srcI = new Item(map[x][y], x, y);
        // 对比对象
        Item renItem = new Item(map[tx][ty], tx, ty);
         
        // 如果开始对象和对比对象是同一个就返回
        if(srcI.equals(renItem)) {
            return false;
        }
         
        if(map[x][y] == 0 || map[tx][ty] == 0) {
            return false;
        }
         
        if(map[x][y] != map[tx][ty]) {
            System.out.println("not equals.");
            return false;
        }
         
 
        // 得到x，y直线位置上的数据
        if(line(s, map, x, y, tx, ty, false, false) == null) {
            return true;
        }
        // 得到t，t1
        for(Item item : line(null, map, x, y, tx, ty, true, false)) {
            line(t, map, item.x, item.y, tx, ty, true, false);
        }
        for(Item item : t) {
            if(line(t1, map, item.x, item.y, tx, ty, false, false) == null)
                return true;
        }
        // add in s
        s.addAll(t1);
        for(Item item : s) {
            System.out.println(item);
        }
        /*
        for(int i = 0; i < W; i ++) {
            for(int j = 0; j < H; j ++) {
                 
            }
        }*/
        for(Item item : s) {
            if(item.val == renItem.val && item.equals(renItem)) {
                System.out.println("find");
                //map[x][y] = map[tx][ty] = 0;
                return true;
                //break;
            }
        }
        return false;
    }
    /**
     * 此方法有误，没有做处理
     * @param map
     * @return
     */
    public static boolean isDie(int[][] map) {
        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] != 0) {
                    if(line(null, map, i, j, 0, 0, false, true) == null)
                        return true;
                }
            }
        }
        return false;
    }
    /**
     * 移除指定地图上2个对象图片
     * @param map 地图
     * @param x 起始位置，根据起始位置算出目标位置是否在2个转角之内
     * @param y
     * @param tx 目标位置
     * @param ty
     */
    public static boolean removed(int[][] map, int x, int y, int tx, int ty) {
        if(find(map, x, y, tx, ty)) {
            System.out.println("removed");
            map[x][y] = map[tx][ty] = 0;
            return true;
        }
        return false;
    }
 
}
 
class Item {
    int val, x, y;
     
    public Item(int val, int x, int y) {
        this.val = val;
        this.x = x;
        this.y = y;
    }
     
    @Override
    public boolean equals(Object obj) {
        Item item = (Item) obj;
        return val == item.val && x == item.x && y == item.y;
    }
 
    @Override
    public String toString() {
        return x + " , " + y + " = " + val;
    }
     
}
