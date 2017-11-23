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

import cn.xpu.hcp.entity.Plane;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.MyFrame;
import cn.xpu.hcp.tools.PlaySound;

public class GameFrame extends MyFrame {
	static boolean begin=true;//��ʼ��־
	static int yPos = -646;
	static boolean useMouse = true;
	
	private static final long serialVersionUID = 1L;
	//ȡ�ÿ�ʼ����
	Image beginBg = GameImage.getImage("resources/startbg1.jpg");
	//ȡ����Ϸ����
	Image gameBg = GameImage.getImage("resources/background1.bmp");
	
	//�����ҷ��ɻ�
	Plane myplane = new Plane(Constant.GAME_WIDTH/2,650,10,true,this);
	
	//�����ӵ�����
	
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
		g.drawImage(gameBg, 0, yPos-1411, null);//����ͼƬ����
		
		myplane.draw(g);
	}
	
	static class BgThread extends Thread{//����BgThread�࣬ר�����ڸ���yPosʹ����ͼƬ����
		@Override
		public void run() {
			while(true){
				if(yPos==764){
					yPos = -646;
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
			System.out.println("��갴��");
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("����ͷ�");
			//���Ϊ16���Ҽ�Ϊ4
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
				System.out.println(e.getModifiers());
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
		new PlaySound("bgmusic.mp3", true).start();
		game.addKeyListener(game.new KeyMonitor());//��Ӽ��̼���
		game.setCursor(null);
		game.addMouseListener(game.new MouseMonitor());
		game.addMouseMotionListener(game.new MouseMonitor());//���������
	}

}
