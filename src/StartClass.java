import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class StartClass {
	final static byte start = 0, stop = 1, pause = 2;
	private static Timer timer;
	private static long lastTickTime;
	static byte state = stop;
	private static long savedMinutes;
	private static long savedMillis;
	protected static long minutes;
	protected static long millis;
	protected static long seconds;
	
	public static void main(String[] args) throws FontFormatException, IOException {
		JFrame myFrame = new JFrame("Timer");
		createWindow(myFrame);
		myFrame.pack();
		myFrame.setMinimumSize(myFrame.getSize());
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
		myFrame.setLocationRelativeTo(null);
	}

	private static void createWindow(JFrame myFrame) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT,
				new File(System.getProperty("user.dir") + "/ds_digital/DS-DIGIB.TTF"));
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(font);
		font = font.deriveFont(82f);
		JLabel timeLabel = new JLabel();
		timeLabel.setFont(font);
		timeLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		timeLabel.setText(String.format("%02d:%02d.%1d ", 0, 0, 0));
		myFrame.add(timeLabel, BorderLayout.CENTER);
		JButton[] buttons = { new JButton("Start"), new JButton("Stop"), new JButton("Pause") };

		JPanel upperPanel = new JPanel();
		for (JButton button : buttons) {
			upperPanel.add(button);
		}
		buttons[stop].setEnabled(false);
		buttons[pause].setEnabled(false);
		myFrame.add(upperPanel, BorderLayout.NORTH);
		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long runningTime = System.currentTimeMillis() - lastTickTime;
				Duration duration = Duration.ofMillis(runningTime);
				long hours = duration.toHours();
				duration = duration.minusHours(hours);
				minutes = duration.toMinutes()+savedMinutes;
				duration = duration.minusMinutes(minutes);
				millis = duration.toMillis()+ savedMillis;
				seconds = millis / 1000;
				long mils = millis - (seconds * 1000);
				mils = mils / 100 ;
				timeLabel.setText(String.format("%02d:%02d.%1d", minutes, seconds, mils));
			}
		});
		buttons[start].addActionListener(l -> {
			System.out.println("Start Timer!");
			buttons[stop].setEnabled(true);
			buttons[pause].setEnabled(true);
			buttons[start].setEnabled(false);
			lastTickTime = System.currentTimeMillis();
			timer.start();
			state = start;
		});
		buttons[stop].addActionListener(l -> {
			System.out.println("Stop Timer!");
			buttons[stop].setEnabled(false);
			buttons[pause].setEnabled(false);
			buttons[start].setEnabled(true);
			timer.stop();
			savedMinutes = 0;
			savedMillis = 0;
			timeLabel.setText(String.format("%02d:%02d.%1d", 0, 0, 0));
			state = stop;
		});
		buttons[pause].addActionListener(l -> {
			System.out.println("Pause Timer!");
			buttons[stop].setEnabled(true);
			buttons[pause].setEnabled(false);
			buttons[start].setEnabled(true);
			timer.stop();
			savedMinutes = minutes;
			savedMillis = millis;
			System.out.printf("Time = %02d:%02d.%1d\n",minutes, seconds, millis);
			state = pause;
		});

	}
}
