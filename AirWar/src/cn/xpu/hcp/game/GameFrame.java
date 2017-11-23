package cn.xpu.hcp.game;

import java.awt.Cursor;
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
	static Image gameBg = GameImage.getImage("resources/background1.bmp");
	
	static boolean begin=true;//��ʼ��־
	static int yPos = -1*(gameBg.getHeight(null)-Constant.GAME_HEIGHT);
	static boolean useMouse = true;
	static PlaySound pbg = new PlaySound("bgmusic.mp3", true);
	
	private static final long serialVersionUID = 1L;
	//ȡ�ÿ�ʼ����
	Image beginBg = GameImage.getImage("resources/startbg1.jpg");
	
	//�����ҷ��ɻ�
	Plane myplane = new Plane(Constant.GAME_WIDTH/2,650,10,true,this);
	//�����з��ɻ�����
	public  List<Plane> es = new LinkedList<Plane>(); 
	//�����ӵ�����
	public List<Bullet> bs = new LinkedList<Bullet>();
	//������ը����
	public List<Explode> explodes = new LinkedList<Explode>();
	
	public void paint(Graphics g){
		new CreateEnemyThread(es, game).start();//��������л�����
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
		g.drawImage(gameBg, 0, yPos-gameBg.getHeight(null), null);//����ͼƬ����
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
		for(int i=0; i<es.size(); i++) {//�������е��ӵ������Ƴ���  
	        Plane p = es.get(i);  
	        p.draw(g);  
	    }
		//���Ʊ�ը
		for(int i=0; i<explodes.size(); i++) {  
		    Explode e = explodes.get(i);  
		    e.draw(g);  
		    new PlaySound("BombSound_solider.mp3", false).start();
		}
	}
	
	static class BgThread extends Thread{//����BgThread�࣬ר�����ڸ���yPosʹ����ͼƬ����
		@Override
		public void run() {
			while(true){
				if(yPos==764){
					yPos = gameBg.getHeight(null)-Constant.GAME_HEIGHT;
				}else{
					if(begin==false)//����������Ϸ�ſ�ʼ����
						yPos += 2;
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
		System.out.println();
	}

}
