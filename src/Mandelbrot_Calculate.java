import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;


public class Mandelbrot_Calculate {
	double[][][] first;
	double[][][] value;
	int[][] inter;
	
	double First_Xmin = -2.7, First_Xmax = 1.3;
	double First_Ymin = -1.5, First_Ymax = 1.5;
	
	double Xmin = -2.7, Xmax = 1.3;
	double Ymin = -1.5, Ymax = 1.5;
	
	int CalcK = 200;
	
	int X, Y;
	
	int change_max = 1;
	public void set_change_max(int change) {
		change_max = change;
		if(change_max <= 0)
			change_max = 1;
	}
	
	Mandelbrot_Calculate(int windowX, int windowY) {
		X = windowX;
		Y = windowY;
		first = new double[X][Y][2];
		value = new double[X][Y][2];
		inter = new    int[X][Y];
		init();
	}
	
	public void initXY() {
		Xmin = First_Xmin;	Xmax = First_Xmax;
		Ymin = First_Ymin;	Ymax = First_Ymax;
		init();
	}
	
	public void init() {
		for(int i=0; i<X; i++) for(int j=0; j<Y; j++) {
			first[i][j][0] = value[i][j][0] =
					(Xmax-Xmin)*(double)i/(double)X + Xmin;
			first[i][j][1] = value[i][j][1] =
					(Ymax-Ymin)*(double)j/(double)Y + Ymin;
			inter[i][j] = 0;
		}
		Calculate();
	}
	
	public void Calculate() {
		for(int i=0; i<X; i++) for(int j=0; j<Y; j++) {
			for(int k=0; k<CalcK; k++) {
				double scale1 = 
						value[i][j][0] * value[i][j][0]
					+	value[i][j][1] * value[i][j][1];

				if(scale1 > 1000)
					break;
				
				double temp1 = 
						value[i][j][0] * value[i][j][0]
					-	value[i][j][1] * value[i][j][1]
					+	first  [i][j][0];
				double temp2 =
					2 *	value[i][j][0] * value[i][j][1]
					+	first  [i][j][1];
				
				value[i][j][0] = temp1;
				value[i][j][1] = temp2;
				
				double scale2 = 
						value[i][j][0] * value[i][j][0]
					+	value[i][j][1] * value[i][j][1];
				
				inter[i][j] = inter[i][j] + 1;
				
				if(scale2 > 1000)
					break;
			}
		}
	}
	
	public double get_value(int i, int j) {
		double scale = 
				value[i][j][0] * value[i][j][0]
			+	value[i][j][1] * value[i][j][1];
		
		return scale;
	}
	
	public void draw(Graphics g) {
		
		int max_inter = 1;
		for(int i=0; i<X; i++) for(int j=0; j<Y; j++) {
			if(max_inter < inter[i][j])
				max_inter = inter[i][j];
		}
		for(int i=0; i<X; i++) for(int j=0; j<Y; j++) {
			double now_value = get_value(i, j);
			if(now_value<1000) {
				g.setColor(Color.BLACK);
				g.fillRect(i, j, 1, 1);
			}else{
				float h1 = (float)inter[i][j]/((float)max_inter/(float)change_max);
				float h = (float) Math.log10( (float)inter[i][j]/(float)max_inter * (float)10 );
				if(h > (float)1.0) h = (float)1.0;
				g.setColor(Color.getHSBColor(h, (float)1.0, (float)1.0));
				g.fillRect(i, j, 1, 1);
			}
		}
	}
	
	public void ChangeXY(Point P0, Point P1, JLabel how) {
		
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
		
		double next_Xmax = (Xmax-Xmin)*(double)max_X/(double)X + Xmin;
		double next_Xmin = (Xmax-Xmin)*(double)min_X/(double)X + Xmin;
		double next_Ymax = (Ymax-Ymin)*(double)(max_Y-22)/(double)Y + Ymin;
		double next_Ymin = (Ymax-Ymin)*(double)(min_Y-22)/(double)Y + Ymin;

		Xmax = next_Xmax;		Xmin = next_Xmin;
		Ymax = next_Ymax;		Ymin = next_Ymin;

		init();
		
		double settext = (First_Xmax - First_Xmin) / (Xmax - Xmin);
		double power1 = 9.99;
		double power2 = 1;
		for(int i=0; i<8; i++) {
			if(settext < power1) {
				String first = String.format("%.1f", settext / power2);
				how.setText(first + "*" + "10^" + i);
				break;
			}
			power1 = power1 * 10;
			power2 = power2 * 10;
		}
	}
	
	public void Change_CalcK(int K) {
		CalcK = K;
		Calculate();
	}
}
