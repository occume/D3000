package org.d3.core.util.astar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Asqare extends JFrame{
    public static final int SQUARE_SIDE = 30; //每个格的大小
    public static final int JFRAME_WIDTH = SQUARE_SIDE * 10; //窗休宽
    public static final int JFRAME_HEIGHT = SQUARE_SIDE * 10; //窗体高
    public static final String[] data = new String[]{
        "田","畕","畾","土","圭","垚","十","卄","卅","卌",
        "屮","艸","芔","茻","水","沝","淼","皕","兟","兢",
        "競","夶","棗","棘","玨","竝","臸","玆","臦","林"};  //所有显示的数据 "字"
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

    public static String[][] fonts = new String[map.length-2][map[0].length-2]; //实际抽出来的 "字" 
    public static List<Point> coordlist = new ArrayList<Point>(4); //存放已经找到的点
    public static List<Point> sticklist = new ArrayList<Point>(2); //存放两个比较点

    private GameArithmetic gameArithmetic = new GameArithmetic(); //查找点的算
    private SquareMap square; //图形类
    private Timer tirmer = new Timer(); //定时器
    public static int totalTime = 60000*5; //游戏时间

    public Asqare(){
        //启动定时器来监听时间
        TimerTask task = new TimerTask(){
             @Override
              public void run() {
                 totalTime -= 1000;
                 square.repaint();
              }
        };
        tirmer.scheduleAtFixedRate(task, 1000, 1000);

        //初始化代码
        square = new SquareMap();
        square.addMouseListener(new MouseListener());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(JFRAME_WIDTH+5, JFRAME_HEIGHT + 60);
        setTitle("Asqare");
        setLocationRelativeTo(null);
        setResizable(false);
        JMenuBar menu = new JMenuBar();
        JMenu game = new JMenu("游戏");   
        JMenuItem newgame = game.add("新游戏");
        JMenuItem pause = game.add("暂停");
        JMenuItem goon = game.add("继续");
        JMenuItem exit = game.add("退出");
        JMenu help = new JMenu("帮助");
        JMenuItem about = help.add("关于");
        menu.add(game);
        menu.add(help);
        setJMenuBar(menu);

        add(square);
        setVisible(true);
        init();
    }
    //初始化数据
    public void init(){
        boolean[] fontFalgs = new boolean[data.length];
        int length = ((map.length-2)*(map[0].length-2))/4;
        Random rn = new Random();
        for(int i = 0; i < length; i++){
            int num = 0;
            do{
                num = rn.nextInt(data.length);
            }while(fontFalgs[num]);
            String str = data[num];
            fontFalgs[num] = true;

            int colIndex = 0,rowIndex = 0;
            for (int j = 0; j < 4; j++) { //随机放到4个不同位置
                do{
                    rowIndex = rn.nextInt(fonts.length);
                    colIndex = rn.nextInt(fonts[0].length);
                }while(fonts[rowIndex][colIndex] != null);
                fonts[rowIndex][colIndex] = str;
            }
        }
    }
    //鼠标监听
    class MouseListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getX() < SQUARE_SIDE || e.getX() > SQUARE_SIDE * 9 ||
                    e.getY() < SQUARE_SIDE || e.getY()> SQUARE_SIDE * 9 || 
                    map[(e.getY()/SQUARE_SIDE)][(e.getX()/SQUARE_SIDE)] == 0){
                return;
            } // 出了实际地图界限和已经找到就反回
            //System.out.println("x : " + (e.getX()/SQUARE_SIDE) + " y : " + (e.getY()/SQUARE_SIDE));

            if(sticklist.size() > 0){ //比较两个点
                Point p1 = new Point(sticklist.get(0)); 
                Point p2 = new Point((e.getX()/SQUARE_SIDE),(e.getY()/SQUARE_SIDE));
                map[p1.y][p1.x] = 2; //比较两个点时把这两个点设成其它标识
                map[p2.y][p2.x] = 2;
                //查询两点是否相通如果找到 设成该两点为0
                List<Point> ps = gameArithmetic.meet(p1.x < p2.x ? p1 : p2, p2.x > p1.x ? p2 : p1);
                if(ps == null){
                    sticklist.clear();
                    sticklist.add(new Point((e.getX()/SQUARE_SIDE),(e.getY()/SQUARE_SIDE)));
                    map[p1.y][p1.x] = 1;
                    map[p2.y][p2.x] = 1;
                }else{
                	System.out.println(ps);
                    coordlist.addAll(ps);
                    sticklist.add(p2);
                    square.repaint();
                    map[p1.y][p1.x] = 0;
                    map[p2.y][p2.x] = 0;
                }
            }else {
                sticklist.add(new Point((e.getX()/SQUARE_SIDE),(e.getY()/SQUARE_SIDE)));
            }
            square.repaint();
        }
    }

    public static void main(String[] args) {
        new Asqare();
    }
}
//图形类
class SquareMap extends JPanel{
    Date startTime = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        draw(g);
        g.setColor(c);
    }

    //画显示在面板上的图形与时间
    private void draw(Graphics g){
        Font font = new Font("宋体",Font.PLAIN,25);
        g.setFont(font);
        for(int i = Asqare.SQUARE_SIDE ; i < Asqare.JFRAME_HEIGHT-Asqare.SQUARE_SIDE ; i+=Asqare.SQUARE_SIDE){
            for (int j = Asqare.SQUARE_SIDE; j < Asqare.JFRAME_WIDTH - Asqare.SQUARE_SIDE; j+=Asqare.SQUARE_SIDE) {
                if(Asqare.map[i/30][j/30]!=0){
                    g.draw3DRect(j, i, Asqare.SQUARE_SIDE, Asqare.SQUARE_SIDE, true);
                    g.drawString(Asqare.fonts[(i/Asqare.SQUARE_SIDE)-1][(j/Asqare.SQUARE_SIDE)-1], j+2, i+25);
                }
            }
        }
        if(Asqare.sticklist.size() > 0){
            g.setColor(Color.RED);
            for (int j2 = 0; j2 < Asqare.sticklist.size(); j2++) {
                Point p = Asqare.sticklist.get(j2); 
                g.draw3DRect(p.x * (Asqare.SQUARE_SIDE),(p.y *Asqare.SQUARE_SIDE) , Asqare.SQUARE_SIDE, Asqare.SQUARE_SIDE, true);
            }
            if(Asqare.sticklist.size() >= 2){ 
                Asqare.sticklist.clear();
            }
        }
        if(Asqare.coordlist.size() > 0){
            g.setColor(Color.RED);
            int num = Asqare.SQUARE_SIDE/2;
            for (int j2 = 1; j2 < Asqare.coordlist.size(); j2++) {
                Point p1 = Asqare.coordlist.get(j2 - 1); 
                Point p2 = Asqare.coordlist.get(j2);
                g.drawLine((p1.x * Asqare.SQUARE_SIDE + num) , (p1.y * Asqare.SQUARE_SIDE + num) 
                        , (p2.x * Asqare.SQUARE_SIDE + num) , (p2.y * Asqare.SQUARE_SIDE + num));
            }
            if(Asqare.coordlist.size() >= 2){ 
                Asqare.coordlist.clear();
            }
        }
        g.setColor(Color.GRAY);
        font = new Font("宋体",Font.PLAIN,15);
        g.setFont(font);
        startTime.setTime(Asqare.totalTime);
        g.drawString(sdf.format(startTime), Asqare.JFRAME_WIDTH-Asqare.SQUARE_SIDE*2-5, 12);
    }

}
//算法类
class GameArithmetic{

    public List<Point> meet(Point p1,Point p2){
        if(p1.equals(p2) || !Asqare.fonts[p1.y-1][p1.x-1].equals(Asqare.fonts[p2.y-1][p2.x-1])) return null;
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

        if(start < 0 || end >= Asqare.map[0].length) return 1;

        for (int i = start; i <= end ; i++) {
            if(Asqare.map[p1.y][i]==1)
                return Asqare.map[p1.y][i];
        }
        return 0;
    }

    private int meetRows(Point p1,Point p2){
        int start = p1.y < p2.y ? p1.y : p2.y;
        int end = p1.y < p2.y ? p2.y : p1.y;

        if(start < 0 || end >= Asqare.map.length) return 1;

        for (int i = start; i <= end ; i++) {
            if(Asqare.map[i][p1.x]==1)
                return Asqare.map[i][p1.x];
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
}