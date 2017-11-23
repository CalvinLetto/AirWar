package cn.xpu.hcp.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import cn.xpu.hcp.entity.Plane;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.MyFrame;
import cn.xpu.hcp.tools.PlaySound;

public class GameFrame extends MyFrame {
	static boolean begin=true;//��ʼ��־
	static int yPos = -646;
	
	private static final long serialVersionUID = 1L;
	//ȡ�ÿ�ʼ����
	Image beginBg = GameImage.getImage("resources/startbg1.jpg");
	//ȡ����Ϸ����
	Image gameBg = GameImage.getImage("resources/background1.bmp");
	
	//�����ҷ��ɻ�
	Plane myplane = new Plane(Constant.GAME_WIDTH/2,650,10,true,this);
	
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
	    }  
	  
	    @Override  
	    public void keyReleased(KeyEvent e) {  
	        myplane.Release(e);//��Plane���д�����������Ӧ�����ͷ�ʱ�Ĳ���  
	    }  
	      
	}  
	public static void main(String[] args) {
		GameFrame game = new GameFrame();
		game.launchFrame();
		new BgThread().start();
		new PlaySound("bgmusic.mp3", true).start();
		game.addKeyListener(game.new KeyMonitor());//��Ӽ��̼���
	}

}
