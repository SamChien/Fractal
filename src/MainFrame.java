import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
	private class MainPanel extends JPanel {
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			
			g2d.setColor(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(0, 0, contentPane.getWidth(), contentPane.getHeight()));
			if (isDrawing) {
				g2d.drawImage(nowImg, 0, 0, null);
			} else {
				g2d.drawImage(imgList.get(nowLevel), 0, 0, null);
			}
	    }
	}
	
	private MainPanel contentPane;
	private boolean isDrawing = true;
	private Point2D nowPoint;
	private BufferedImage nowImg;
	private List<BufferedImage> imgList = new ArrayList<BufferedImage>();
	private int nowLevel = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setResizable(false);
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'f' || e.getKeyChar() == 'F') {
					isDrawing = false;
					imgList.add(nowImg);
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 800);
		contentPane = new MainPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (isDrawing) {
					nowPoint = new Point2D.Double(e.getX(), e.getY());
				} else {
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (nowLevel + 1 < imgList.size()) {
							Graphics2D g = (Graphics2D) contentPane.getGraphics();
							
							g.setColor(Color.WHITE);
							g.fill(new Rectangle2D.Double(0, 0, contentPane.getWidth(), contentPane.getHeight()));
							g.drawImage(imgList.get(nowLevel + 1), 0, 0, null);
							nowLevel++;
						} else {
							Graphics2D g = (Graphics2D) contentPane.getGraphics();
							BufferedImage tempImg = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(),
									BufferedImage.TYPE_INT_RGB);
							Graphics2D tempImgG = (Graphics2D) tempImg.getGraphics();
							
							makeSnowflake(g);
							makeSnowflake(tempImgG);
							nowImg = tempImg;
							imgList.add(tempImg);
							nowLevel++;
						}
					} else if (e.getButton() == MouseEvent.BUTTON3 && nowLevel > 0) {
						Graphics2D g = (Graphics2D) contentPane.getGraphics();
						
						g.setColor(Color.WHITE);
						g.fill(new Rectangle2D.Double(0, 0, contentPane.getWidth(), contentPane.getHeight()));
						g.drawImage(imgList.get(nowLevel - 1), 0, 0, null);
						nowLevel--;
					}
				}
			}

			private void makeSnowflake(Graphics2D g) {
				int newWidth = contentPane.getWidth() / 5;
				int newHeight = contentPane.getHeight() / 5;
				int startX = contentPane.getWidth() / 2 - newWidth / 2;
				int startY = contentPane.getHeight() / 2 - newHeight / 2;
				
				g.setColor(Color.WHITE);
				g.fill(new Rectangle2D.Double(0, 0, contentPane.getWidth(), contentPane.getHeight()));
				
				g.drawImage(nowImg, startX, startY, newWidth, newHeight, contentPane);
				
				g.drawImage(nowImg, startX, startY - newHeight, newWidth, newHeight, contentPane);
				g.drawImage(nowImg, startX, startY - 2 * newHeight, newWidth, newHeight, contentPane);
				
				g.drawImage(nowImg, startX, startY + newHeight, newWidth, newHeight, contentPane);
				g.drawImage(nowImg, startX, startY + 2 * newHeight, newWidth, newHeight, contentPane);
				
				g.drawImage(nowImg, startX - newWidth, startY - newHeight / 2, newWidth, newHeight, contentPane);
				g.drawImage(nowImg, startX - newWidth, startY + newHeight / 2, newWidth, newHeight, contentPane);
				
				g.drawImage(nowImg, startX + newWidth, startY - newHeight / 2, newWidth, newHeight, contentPane);
				g.drawImage(nowImg, startX + newWidth, startY + newHeight / 2, newWidth, newHeight, contentPane);
				
				g.drawImage(nowImg, startX - 2 * newWidth, startY - newHeight, newWidth, newHeight, contentPane);
				g.drawImage(nowImg, startX - 2 * newWidth, startY + newHeight, newWidth, newHeight, contentPane);
				
				g.drawImage(nowImg, startX + 2 * newWidth, startY - newHeight, newWidth, newHeight, contentPane);
				g.drawImage(nowImg, startX + 2 * newWidth, startY + newHeight, newWidth, newHeight, contentPane);
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDrawing) {
					Graphics2D g = (Graphics2D) contentPane.getGraphics();
					Graphics2D nowImgG = (Graphics2D) nowImg.getGraphics();
					int strokeWidth = 32;
					
					g.setColor(Color.BLUE);
					g.setStroke(new BasicStroke(strokeWidth));
					g.draw(new Line2D.Double(nowPoint.getX(), nowPoint.getY(), e.getX(), e.getY()));
					
					nowImgG.setColor(Color.BLUE);
					nowImgG.setStroke(new BasicStroke(strokeWidth));
					nowImgG.draw(new Line2D.Double(nowPoint.getX(), nowPoint.getY(), e.getX(), e.getY()));
					
					nowPoint.setLocation(e.getX(), e.getY());
				}
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		nowImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		nowImg.getGraphics().setColor(Color.WHITE);
		((Graphics2D) (nowImg.getGraphics())).fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
	}

}
