import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Mandelbrot_Frame extends JFrame 
implements ActionListener, MouseListener, MouseMotionListener, ChangeListener{

	MyPanel panel;
	Mandelbrot_Calculate Cal;
	
	int period_Calc = 1000/60;
	Timer timer_Calc = new Timer(period_Calc, this);

	int X = 800, Y = 600;

	
	JLabel how = new JLabel("배율 : 1.0 * 10 ^ 0");
	JPanel controlPanel=new JPanel();
	JButton btn_init = new JButton("초기화");
	JButton btn_time = new JButton("정지");
	JSlider slider;

	
	
	Mandelbrot_Frame() {
		setTitle("Mandelbrot");
		
		how.setFont(how.getFont().deriveFont(18.0f));
		slider=new JSlider(0,100,10);
		slider.setMajorTickSpacing(20); //큰 눈금 간격
		slider.setMinorTickSpacing(10);  // 작은 눈금 간격
		slider.setPaintTicks(true);  // 눈금 표시
		slider.setPaintLabels(true);  // 값을 레이블로 표시함
		
		slider.addChangeListener(this);
		
//		controlPanel.add(slider);
		controlPanel.add(how);
		controlPanel.add(btn_init);
		controlPanel.add(btn_time);
		btn_init.addActionListener(this);
		btn_time.addActionListener(this);
		
		
		add(controlPanel,BorderLayout.SOUTH);

		
		panel = new MyPanel(X, Y);
		add(panel);
		
		setSize(X, Y+22+40);
				
//		timer_move   .start();
		
		Cal = new Mandelbrot_Calculate(X, Y);
		Cal.set_change_max(10);
		timer_Calc.start();

		addMouseListener(this);
		addMouseMotionListener(this);
		
		setVisible(true);
	}
	
	
	
	Boolean draw_sq = false;
	Point P0 = new Point(-1, -1);
	Point P1 = new Point(-1, -1);

	public void draw_sqaure(Graphics g) {
		if(draw_sq == false) return;
		
		int max_X = Math.max(P0.x, P1.x);
		int min_X = Math.min(P0.x, P1.x);
		int max_Y = Math.max(P0.y, P1.y);
		int min_Y = Math.min(P0.y, P1.y);
		
		if( (max_Y - min_Y)*4 < (max_X - min_X)*3 ) {
			if(P0.x == max_X) {
				min_X = max_X - ( (max_Y - min_Y)/3 * 4 );
				P1.x = min_X;
			}else{
				max_X = min_X + ( (max_Y - min_Y)/3 * 4 );				
				P1.x = max_X;
			}
		}else{
			if(P0.y == max_Y) {
				min_Y = max_Y - ( (max_X - min_X)/4 * 3 );
				P1.y = min_Y;  
			}else{
				max_Y = min_Y + ( (max_X - min_X)/4 * 3 );				
				P1.y = max_Y;
			}
		}
		

		g.setColor(Color.BLUE);
		
		g.drawLine(max_X, min_Y-22, max_X, max_Y-22);
		g.drawLine(min_X, min_Y-22, min_X, max_Y-22);
		g.drawLine(min_X, min_Y-22, max_X, min_Y-22);
		g.drawLine(min_X, max_Y-22, max_X, max_Y-22);
	}
	
	class MyPanel extends JPanel{
		MyPanel(int windowX, int windowY) {
			setSize(windowX, windowY);
		}	
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
//			g.setColor(Color.RED);
//			g.fillOval(0, 0, 240, 240);

			setBackground(Color.WHITE);
			
			
			Cal.draw(g);
			
			draw_sqaure(g);
			
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Mandelbrot_Frame();
//		new Graph();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == timer_Calc) {
			Cal.Change_CalcK(10);
			repaint();
		}
		

		if(e.getSource() == btn_init) {
			Cal.initXY();
		}
		if(e.getSource() == btn_time) {
			if(timer_Calc.isRunning()) {
				timer_Calc.stop();
				btn_time.setText("재생");
			}else{
				timer_Calc.start();
				btn_time.setText("정지");
			}
		}
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		P0 = e.getPoint();
		P1 = e.getPoint();
		draw_sq = true;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		P1 = e.getPoint();
		draw_sq = false;
		repaint();
		
		Cal.ChangeXY(P0, P1, how);
		repaint();
		
		P0.x = P0.y = P1.x = P1.y = -1;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		P1 = e.getPoint();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source=(JSlider)e.getSource();
		if(!source.getValueIsAdjusting()){
			int value=(int)slider.getValue();
			Cal.set_change_max(value);
		}	

	}
	

}
