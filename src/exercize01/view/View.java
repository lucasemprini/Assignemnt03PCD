package exercize01.view;

import exercize01.Main;
import exercize01.model.InputListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class View extends JFrame implements ActionListener {

	private final JButton startButton = new JButton("start");
	private final JButton stopButton = new JButton("stop");
	private final JTextField nAliveCells = new JTextField(10);
	private final JTextField compStateTime = new JTextField(10);
	private final ViewPanel gridPanel = new ViewPanel();
	private final JPanel infoPanel = new JPanel();
	private final JPanel controlPanel = new JPanel();
	private final JPanel contentPane = new JPanel();
	private final LayoutManager layoutManager = new BorderLayout();

    private int numGenerations = 0;

	private final ArrayList<InputListener> listeners;

	public View(int w, int h){
		super("Game of Life");
		this.listeners = new ArrayList<>();
		this.setSize(w,h);
		this.setUpView();

		this.gridPanel.setSize(w,h);
	}

	private void setUpView() {
		this.controlPanel.add(startButton);
		this.controlPanel.add(stopButton);

		this.nAliveCells.setText("0");
		this.nAliveCells.setEditable(false);
		this.infoPanel.add(new JLabel("Num Alive Cells"));
		this.infoPanel.add(nAliveCells);


		this.compStateTime.setText("0");
		this.compStateTime.setEditable(false);
		this.infoPanel.add(new JLabel("CompState Time"));
		this.infoPanel.add(compStateTime);

		this.contentPane.setLayout(layoutManager);
		this.contentPane.add(BorderLayout.NORTH,controlPanel);
		this.contentPane.add(BorderLayout.CENTER,gridPanel);
		this.contentPane.add(BorderLayout.SOUTH, infoPanel);
		this.setContentPane(contentPane);

		this.startButton.addActionListener(this);
		this.stopButton.addActionListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void update(long numAliveCells, long stateTime){
		try {
			SwingUtilities.invokeAndWait(() -> {
			    this.numGenerations++;
				this.nAliveCells.setText(String.valueOf(numAliveCells));
                this.compStateTime.setText(String.valueOf(stateTime));

                System.out.println("Generation " + numGenerations
                        + ":\tAlive Cells: " + String.valueOf(numAliveCells)
                        + ";\t Time elapsed (in millis): " + String.valueOf(stateTime));

                this.repaint();
			});
		} catch (Exception ignored){}
	}
		
	public void actionPerformed(ActionEvent ev){
		if (ev.getActionCommand().equals("start")){
			for (InputListener l: listeners){
				l.started();
			}
		} else if (ev.getActionCommand().equals("stop")){
			for (InputListener l: listeners){
				l.stopped();
			}
		}
	}

	public void addListener(InputListener l){
		this.listeners.add(l);
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
			
		ViewPanel(){
			this.xfrom = 0;
			this.yfrom = 0;
			this.addMouseMotionListener(this);
		}

		public void paintComponent(Graphics g) {
			final Graphics2D g2 = (Graphics2D) g;
			
			g2.setBackground(Color.WHITE);
			g2.clearRect(0, 0, this.getWidth(), this.getHeight());

			this.nCellsX = this.getWidth()/dx;
			this.nCellsY = this.getHeight()/dy;

			int x = 0;
			g2.setColor(Color.GRAY);
			for (int i = 0; i < this.nCellsX; i++){
				g2.drawLine(x, 0, x, this.getHeight());
				x += dx;
			}
			
			int y = 0;
			for (int j = 0; j < this.nCellsY; j++){
				g2.drawLine(0, y, this.getWidth(), y);
				y += dy;
			}

			g2.setColor(Color.BLACK);
			for (int i = 0; i < this.nCellsX; i++){
				for (int j = 0; j < this.nCellsY; j++){
					if (Main.GAME_MATRIX.getCellAt(this.xfrom+i, this.yfrom+j)){
						g2.fillRect(i*dx+1, j*dy+1, dx-2, dy-2);
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			final Point currPos = e.getPoint();
			double deltax = currPos.getX() - this.mousePos.getX();
			double deltay = currPos.getY() - this.mousePos.getY();
			this.xfrom = this.xFromBase + (int) Math.round(deltax/dx);

			if (this.xfrom < 0){
				this.xfrom = 0;
			} else if (this.xfrom + this.nCellsX >= Main.GAME_MATRIX.getNumColumns()){
				this.xfrom = Main.GAME_MATRIX.getNumColumns() - this.nCellsX;
			}

			this.yfrom = this.yFromBase + (int) Math.round(deltay/dy);
			if (this.yfrom < 0){
				this.yfrom = 0;
			} else if (this.yfrom + this.nCellsY >= Main.GAME_MATRIX.getNumRows()){
				this.yfrom = Main.GAME_MATRIX.getNumRows() - this.nCellsY;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			this.mousePos = e.getPoint();
			this.xFromBase = this.xfrom;
			this.yFromBase = this.yfrom;
		}
	}
	
}
	
