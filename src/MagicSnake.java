	
import java.util.*;
import java.awt.geom.*;
import java.awt.*;
import java.io.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.imageio.*;

public class MagicSnake extends JFrame  implements KeyListener{
	private static final long serialVersionUID = 11L;
	Dimension DEFAULT = new Dimension(1600,900); 
	Dimension d = new Dimension(90,90);
	Container c = getContentPane();
	LinkedList <SnakeNode> S = new LinkedList<SnakeNode>();//存储蛇
	LinkedList <SnakeNode> P = new LinkedList<SnakeNode>();//存储障碍物
	
	char DIR = 'R';//方向
    int foodx = 130;
    int foody = 170;
    int score = 4;
    int time = 0;
    int interval = 150;
    int speed = 0;
    boolean first = true;
    boolean pause = false;
    
	public MagicSnake() 
	{
		setTitle("贪吃蛇@by LingXiao");
		setVisible(true);
		setSize(DEFAULT);
		setResizable(false);
		setLocationRelativeTo(getOwner()); 
		addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   		
		c.setVisible(true);
		c.setBackground(Color.gray);
		c.setLayout(null);	
		c.setSize(d);
		c.setLocation(100,100);
		c.setBackground(Color.gray);
		
		get_init_Snake();//获得初始蛇身
        get_obstacle();
        getFood();
		new Thread(new MyPaintThread()).start();
	}
	
	 //解决闪烁
	 public Image offScreenImage = null;    
	  @Override
	 public void update(Graphics g)  
	  {  
	         if(offScreenImage == null)  
	            offScreenImage = this.createImage(1600, 900);       
	            Graphics gImage = offScreenImage.getGraphics();
	            paint(gImage);                         
	            g.drawImage(offScreenImage, 0, 0, null); 
	  }  
	  public void repaint() {if(first) {super.repaint();first = false;}update(getGraphics());}
	  
	 public void paint(Graphics g)
     {
		 
		Graphics2D g2d = (Graphics2D)g;
		//super.paint(g2d);
         
		//加载背景图片
     	try{
    		Image img = ImageIO.read(new File("C:\\Users\\ASUS\\Desktop\\Tawn\\Pictures\\Wallpaper\\timg.jpg"));
    		g2d.drawImage(img, null, null);   		
    		}catch(IOException e){}
     	
     	//笔尖粗细
     	BasicStroke   stoke1 = new BasicStroke(1);
        BasicStroke   stoke3 = new BasicStroke(3);
        
        //画边框
        g2d.setStroke(stoke3);
        g2d.setColor(Color.black);
     	Rectangle2D rect1 = new Rectangle2D.Double(30,70,1300,800);
     	Rectangle2D rect2 = new Rectangle2D.Double(25,65,1310,810);
     	Rectangle2D rect3 = new Rectangle2D.Double(1370,70,200,800);
    	Rectangle2D rect4 = new Rectangle2D.Double(1365,65,210,810);
     	g2d.draw(rect1);
     	g2d.draw(rect2);
     	g2d.draw(rect3);
     	g2d.draw(rect4);
     	g2d.drawLine(1370, 435, 1570, 435);
     	
       //画格子
     	g2d.setStroke(stoke1);
     	for(int i = 30; i < 30+1300; i += 20)
     	g2d.drawLine(i, 70, i, 70+800);
     	for(int j = 70; j < 80 + 800; j += 20)
     	g2d.drawLine(30, j, 30+1300, j);		
     	
     	//画障碍物
     	for(int i = P.size() - 1; i >= 0; i--)
 		{
 	  int x = P.get(i).getX();
 	  int y = P.get(i).getY();
 	  Color c = P.get(i).getColor();
 	  g2d.setColor(c);
 	  g2d.fillRect(x,y,20,20);			
     }
     	
     	
     	//画蛇
     	for(int i = S.size() - 1; i >= 0; i--)
     		{
     	  int x = S.get(i).getX();
     	  int y = S.get(i).getY();
     	  Color c = S.get(i).getColor();
     	  g2d.setColor(c);
     	  g2d.fillOval(x,y,20,20);			
         }
     	
     	//画食物
     	 g2d.setColor(RandomColor());
     	 g2d.fillOval(foodx,foody,20,20);
         
     	//画时间版、分数版
     	 Font font0=new Font("仿宋",Font.ITALIC,30); 
     	 Font font1=new Font("宋体",Font.PLAIN,50);  
     	 Font font2=new Font("宋体",Font.PLAIN,30); 
     	 Font font3=new Font("仿宋",Font.BOLD,20); 
     
     	 g2d.setColor(Color.YELLOW);
     	 g2d.setFont(font0);
     	 g2d.drawString("Greedy Snake", 1380, 120);
     	 
     	 g2d.setColor(Color.white);
     	 g2d.setFont(font1);
         g2d.drawString("Score", 1400, 200);
         g2d.drawString("Time", 1400, 350);
         g2d.setColor(Color.green);
         g2d.setFont(font2);
         g2d.drawString(""+score, 1450, 250);
         g2d.setColor(Color.yellow);
         g2d.drawString(""+time/1000, 1450-(int)Math.log10(time/10000)*20, 400);
         
        // 画操作指南
         g2d.setColor(Color.red);
     	 g2d.setFont(font3);
     	 g2d.drawString("Operation Guide", 1380, 480);
     	 
     	 g2d.setColor(RandomColor());
     	 g2d.fillOval(1380,500,20,20);
     	 g2d.setColor(Color.YELLOW);
     	 g2d.drawString("food", 1440, 515);
     	 g2d.setColor(Color.WHITE);
     	 g2d.fillRect(1380,530,20,20);
     	 g2d.setColor(Color.BLACK);
     	 g2d.fillRect(1410,530,20,20);
     	g2d.setColor(Color.YELLOW);
    	 g2d.drawString("obstacle", 1440, 545);
         
     	 
     	g2d.setColor(Color.YELLOW);
     	g2d.drawString("start/pause: ",1380,580);
     	g2d.drawString("move: ",1380,680);
     	g2d.drawString("speed: ",1380,780);
     	
     	g2d.setColor(Color.black);
     	g2d.drawString("space",1380,630);
     	g2d.drawString("up down left right",1370,730);
     	g2d.drawString("shift ",1380,830);
     	
     	//作者
     	g2d.setColor(Color.white);
     	g2d.drawString("@by LingXiao",1430,860);
     }
     
	 
	 //绘画线程
	 private class MyPaintThread implements Runnable{

	        @Override
	        public void run() {
	        	while(check_end()){	            	
	                repaint();
	                while(!pause);
	                try {
	                	time += (interval-speed);
	                	move(check_eat());   
	                    Thread.sleep(interval-speed);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                
	                
	            }

	        }

	    }
	
	 //判断是否吃到食物
	 public boolean check_eat()
	 {
		 if(S.getFirst().getX() == foodx && S.getFirst().getY() == foody)
		     {
			 score++;
			 getFood();
			 return true;
		     }
		 return false;
	 }
	 
	 //判断游戏是否结束
	 public boolean check_end()
	 {
		
		 int x = S.getFirst().getX();
		 int y = S.getFirst().getY();
		 if(check_in_obstacle(x,y)) {JOptionPane.showConfirmDialog(null, "Game Over!", "", JOptionPane.CLOSED_OPTION);return false;}
		 if((x < 30 && DIR == 'L') || (x > 20+1300 && DIR == 'R') || (y < 70 && DIR == 'U') || (y > 50 + 800 && DIR == 'D')) 
		 {JOptionPane.showConfirmDialog(null, "Game Over!", "", JOptionPane.CLOSED_OPTION);return false;}
		 for(int i = 1; i < S.size(); i++)
		 if(x == S.get(i).getX() && y == S.get(i).getY())   
		{JOptionPane.showConfirmDialog(null, "Game Over!", "", JOptionPane.CLOSED_OPTION);return false;}
		 return true;
	 }
	 	
	 //蛇朝着DIR方向移动
	public void move(boolean t)
	{
		if(!t) S.removeLast();
		S.getFirst().setColor(Color.green);
		int x = S.getFirst().getX();
		int y = S.getFirst().getY();
		if(DIR == 'U')  y -= 20;
		if(DIR == 'D')  y += 20;
		if(DIR == 'L')  x -= 20;
		if(DIR == 'R')  x += 20;
		S.addFirst(new SnakeNode(x,y,Color.red));
	}
	
	//获得初始蛇
	public void  get_init_Snake()
	{
		 int x,y;
		 Random random = new Random();
		  do{
		   x = 30 + random.nextInt(70);
		   y = 20 + random.nextInt(40);
		  }while(!(x % 2 == 1 && y % 2 == 1));
		  x *= 10;
		  y *= 10;
		S.addFirst(new SnakeNode(x,y,Color.red));
    	S.addLast(new SnakeNode(x-20,y,Color.green));
    	S.addLast(new SnakeNode(x-40,y,Color.green));
    	S.addLast(new SnakeNode(x-60,y,Color.green));
	}
	//获得障碍物
	public void get_obstacle()
	{
		
		Color c1 = Color.BLACK;
		Color c2 = Color.WHITE;
		Color c3;
		for(int i = 230; i < 1130; i+=40)
			{
			P.addLast(new SnakeNode(i,170,c1));
			P.addLast(new SnakeNode(i,750,c2));
			c3 = c1;c1 = c2;c2 = c3;
			}
	    for(int i = 270; i < 680; i += 40)
	    {
	    	P.addLast(new SnakeNode(130,i,c1));
	    	P.addLast(new SnakeNode(1210,i,c2));
	    	c3 = c1;c1 = c2;c2 = c3;
	    }
	}
	
	//判断是否在障碍物里
	public boolean check_in_obstacle(int x, int y)
	{
		for(int i = 0; i < P.size(); i++)
		{
			if(x == P.get(i).getX()  && y == P.get(i).getY())
				return true;
		}
		return false;
	}

	
	//得到食物
	public void getFood()
	{
	  Random random = new Random();
	  do{
	  foodx = 3 + random.nextInt(130);
	  foody = 7 + random.nextInt(80);
	  }while(!(foodx % 2 == 1 && foody % 2 == 1 && !check_in_obstacle(foodx*10,foody*10)));
	  foodx *= 10;
	  foody *= 10;
	 
	  
	}
	 
	//获得随机颜色，制造闪烁食物
	public Color RandomColor()
	{
		Random random = new Random();
		int x = random.nextInt(4);
		if(x == 6) return Color.BLACK;
		else if(x == 1) return Color.white;
		else if(x == 2) return Color.blue;
		else if(x == 3) return Color.red;
		else if(x == 4) return Color.ORANGE;
		else if(x == 5) return Color.PINK;
		else return Color.yellow;
	}
	
	//键盘监测
	public void keyPressed(KeyEvent e){
		
		if(e.getKeyCode()==KeyEvent.VK_RIGHT && DIR != 'L')
			{DIR = 'R';pause = true;}    		
		if(e.getKeyCode()==KeyEvent.VK_LEFT && DIR != 'R')
			{DIR = 'L';pause = true;}
		if(e.getKeyCode()==KeyEvent.VK_DOWN && DIR != 'U')
		    {DIR = 'D';pause = true;}
		if(e.getKeyCode()==KeyEvent.VK_UP && DIR != 'D')
		    {DIR = 'U';pause = true;}
		if(e.getKeyCode()==KeyEvent.VK_SPACE)  pause = !(pause); //暂停游戏
		if(e.getKeyCode()==KeyEvent.VK_SHIFT)  speed = 100; //加速
	}
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_SHIFT)  speed = 0;
	}   
	public void keyTyped(KeyEvent e){}
	
	public static void main(String args[]) 
	{
      new MagicSnake();   
	}
}
