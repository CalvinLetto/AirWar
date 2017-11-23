package cn.xpu.hcp.game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import cn.xpu.hcp.entity.Bullet;
import cn.xpu.hcp.entity.CreateEnemyThread;
import cn.xpu.hcp.entity.Explode;
import cn.xpu.hcp.entity.Plane;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.MyFrame;
import cn.xpu.hcp.tools.PlaySound;

public class GameFrame extends MyFrame {
	//ȡ����Ϸ����
	public static Image gameBg = GameImage.getImage("resources/background1.bmp");
	
	public  long start = System.currentTimeMillis();
	
	public boolean success = false;
	
	//��ȡѪ��
	Image bloodImg = GameImage.getImage("resources/blood.png");
	
	static boolean begin=true;//��ʼ��־
	static int yPos = -1*(gameBg.getHeight(null)-Constant.GAME_HEIGHT)+1;
	static int yPos2 = yPos - gameBg.getHeight(null);
	
	static boolean useMouse = true;
	public static PlaySound pbg = new PlaySound("bgmusic.mp3", true);
	
	private static final long serialVersionUID = 1L;
	//ȡ�ÿ�ʼ����
	Image beginBg = GameImage.getImage("resources/startbg1.jpg");
	
	//�����ҷ��ɻ�
	public Plane myplane = new Plane(Constant.GAME_WIDTH/2,650,10,true,false,this);
	//�����з��ɻ�����
	public  List<Plane> es = new LinkedList<Plane>(); 
	//�����ӵ�����
	public List<Bullet> bs = new LinkedList<Bullet>();
	//������ը����
	public List<Explode> explodes = new LinkedList<Explode>();
	public Plane boss = new Plane(-100,-100,0,false,true,this);
	public Plane temp = boss;
	
	public void paint(Graphics g){
		
		if(begin){
			g.drawImage(beginBg, 0, 0, null);
			try {
				Thread.sleep(3000);
				begin = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		g.drawImage(gameBg, 0, yPos, null);
		g.drawImage(gameBg, 0, yPos2, null);//����ͼƬ����
//		g.drawImage(gameBg, 0, yPos - 2*gameBg.getHeight(null), null);
//		g.drawString("�ӵ�������"+bs.size(), 100, 100);
//		g.setColor(Color.red);
//		g.drawString("�л�������"+es.size(), 100, 100); 
		
		myplane.draw(g);
		//�����ӵ�
		for(int i=0; i<bs.size(); i++) {//�������е��ӵ������Ƴ���  
	        Bullet b = bs.get(i);  
	        b.draw(g);  
	        b.hitPlanes(es);
	        b.hitPlane(myplane);
	    }
		//���Ƶл�
		for(int i=0; i<es.size(); i++) {  
	        Plane p = es.get(i);
	        if(p.isBoss()){
	        	boss = p;
				g.setColor(Color.blue);
				g.fillRect(48, 30, (int)Math.round(214*(p.getLife()*0.001)), 10);
				g.setColor(Color.white);
				if(p.getLife()<=0){
					p.setLife(0);
				}
				g.drawString(p.getLife()+"", 155, 40);
			}
	        p.draw(g);  
	    }
		//���Ʊ�ը
		for(int i=0; i<explodes.size(); i++) {  
		    Explode e = explodes.get(i);  
		    e.draw(g);  
		    new PlaySound("BombSound_solider.mp3", false).start();
		}
		
		g.setColor(Color.RED);
		g.fillRect(48, 55, (int)Math.round(214*(myplane.getLife()*0.01)), 10);
		g.drawImage(bloodImg, 0, 30, null);
		g.setColor(Color.white);
		if(myplane.getLife()<=0){
			myplane.setLife(0);
		}
		g.drawString(myplane.getLife()+"", 155, 65);
		if(boss.getLife()==0){
			g.drawImage(GameImage.getImage("resources/win.png"), 210, 330, null);
			for(long i=0;i<=200000;i++){
				
			}
			success = true;//�ɹ�ͨ��
		}
		
	}
	
	static class BgThread extends Thread{//����BgThread�࣬ר�����ڸ���yPosʹ����ͼƬ����
		@Override
		public void run() {
			while(true){
				if(yPos>=Constant.GAME_HEIGHT){
					yPos = yPos2 -gameBg.getHeight(null);
				}else{
					if(yPos2>=Constant.GAME_HEIGHT){
						yPos2 = yPos - gameBg.getHeight(null);
					}else{
						if(begin==false){//����������Ϸ�ſ�ʼ����
							yPos += 2;
							yPos2 +=2;
						}
					}
				}
				try {
					Thread.sleep(50);//�����ٶȵ��趨
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//���̼�����  
	//����Ϊ�ڲ��࣬����ʹ���ⲿ�����ͨ����  
	private class KeyMonitor extends KeyAdapter{  
	    @Override  
	    public void keyPressed(KeyEvent e) {  
	        myplane.Press(e);//��Plane���д�����������Ӧ���̰���ʱ�Ĳ���  
	        if(e.getKeyCode()==e.VK_F2){
	        	useMouse = !useMouse;
	        }
	    }  
	  
	    @Override  
	    public void keyReleased(KeyEvent e) {  
	        myplane.Release(e);//��Plane���д�����������Ӧ�����ͷ�ʱ�Ĳ���  
	    }  
	      
	}  
	
	//��������
	private class MouseMonitor extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getModifiers()==16)
			 myplane.fire();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(useMouse){
				myplane.setX(e.getX());
				myplane.setY(e.getY());
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(useMouse){
				if(e.getModifiers()==16){
					myplane.fire(0);
				}
				myplane.setX(e.getX());
				myplane.setY(e.getY());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if(useMouse){
				//���������������ʧ
				//���������ʧ�ķ������ǽ��������Ϊһ��͸����ͼƬ
				Toolkit  tk =Toolkit.getDefaultToolkit() ;
				Image img = GameImage.getImage("resources/cur.png");
				Cursor  ret =tk.createCustomCursor(img, new Point( 10,10) ,"mycur"); 
				game.setCursor(ret);
			}
		}
	}
	
	static GameFrame game = new GameFrame();
	
	public static void main(String[] args) {
		game.launchFrame();
		new BgThread().start();
		pbg.start();
		
		game.addKeyListener(game.new KeyMonitor());//��Ӽ��̼���
		game.setCursor(null);
		game.addMouseListener(game.new MouseMonitor());
		game.addMouseMotionListener(game.new MouseMonitor());//���������
		new CreateEnemyThread(game).start();//��������̣߳����л�����
	}

}
