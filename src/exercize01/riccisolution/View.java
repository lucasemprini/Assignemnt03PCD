package exercize01.riccisolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class View extends JFrame implements ActionListener {

	private JButton startButton;
	private JButton stopButton;
	private JTextField nAliveCells, compStateTime;
	private ViewPanel gridPanel;
	private CellGrid grid;
	private ArrayList<InputListener> listeners;

	public View(int w, int h, CellGrid grid){
		super("Game of Life");
		this.grid = grid;
		listeners = new ArrayList<InputListener>();
		setSize(w,h);
		
		startButton = new JButton("start");
		stopButton = new JButton("stop");
		JPanel controlPanel = new JPanel();
		controlPanel.add(startButton);
		controlPanel.add(stopButton);

		gridPanel = new ViewPanel(); 
		gridPanel.setSize(w,h);

		JPanel infoPanel = new JPanel();
		nAliveCells = new JTextField(10);
		nAliveCells.setText("0");
		nAliveCells.setEditable(false);
		compStateTime = new JTextField(10);
		compStateTime.setText("0");
		compStateTime.setEditable(false);
		infoPanel.add(new JLabel("Num Alive Cells"));
		infoPanel.add(nAliveCells);
		infoPanel.add(new JLabel("CompState Time"));
		infoPanel.add(compStateTime);
		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.NORTH,controlPanel);
		cp.add(BorderLayout.CENTER,gridPanel);
		cp.add(BorderLayout.SOUTH, infoPanel);
		setContentPane(cp);		
		
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void update(int numAliveCells, long stateTime){
		try {
			SwingUtilities.invokeAndWait(() -> {
				nAliveCells.setText(""+numAliveCells);
				compStateTime.setText(""+stateTime);
				this.repaint();
			});
		} catch (Exception ex){}
	}
		
	public void actionPerformed(ActionEvent ev){
		String cmd = ev.getActionCommand(); 
		if (cmd.equals("start")){
			for (InputListener l: listeners){
				l.started();
			}
		} else if (cmd.equals("stop")){
			for (InputListener l: listeners){
				l.stopped();
			}
		}
	}

	public void addListener(InputListener l){
		listeners.add(l);
	}
	
	class ViewPanel extends JPanel implements MouseMotionListener {

		private int xfrom;
		private int yfrom;
		private static final int dx = 5;
		private static final int dy = 5;
		private Point mousePos;
		private int xFromBase;
		private int yFromBase;
		private int nCellsX, nCellsY;
			
		public ViewPanel(){
			xfrom = 0; yfrom = 0;
			this.addMouseMotionListener(this);
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setBackground(Color.WHITE);
			g2.clearRect(0, 0, this.getWidth(), this.getHeight());
			
			nCellsX = this.getWidth()/dx;
			nCellsY = this.getHeight()/dy;
						
			int x = 0;
			g2.setColor(Color.GRAY);
			for (int i = 0; i < nCellsX; i++){
				g2.drawLine(x, 0, x, this.getHeight());
				x += dx;
			}
			
			int y = 0;
			for (int j = 0; j < nCellsY; j++){				
				g2.drawLine(0, y, this.getWidth(), y);
				y += dy;
			}

			g2.setColor(Color.BLACK);
			for (int i = 0; i < nCellsX; i++){
				for (int j = 0; j < nCellsY; j++){
					if (grid.isAlive(xfrom+i, yfrom+j)){
						g2.fillRect(i*dx+1, j*dy+1, dx-2, dy-2);
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point currPos = e.getPoint();
			double deltax = currPos.getX() - mousePos.getX();
			double deltay = currPos.getY() - mousePos.getY();
			xfrom = xFromBase + (int) Math.round(deltax/dx);
			if (xfrom < 0){
				xfrom = 0;
			} else if (xfrom + nCellsX >= grid.getWidth()){
				xfrom = grid.getWidth() - nCellsX;
			}
			yfrom = yFromBase + (int) Math.round(deltay/dy);
			if (yfrom < 0){
				yfrom = 0;
			} else if (yfrom + nCellsY >= grid.getHeight()){
				yfrom = grid.getHeight() - nCellsY;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mousePos = e.getPoint();
			xFromBase = xfrom;
			yFromBase = yfrom;
		}
	}
	
}
	
