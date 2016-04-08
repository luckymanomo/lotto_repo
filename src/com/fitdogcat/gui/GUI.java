package com.fitdogcat.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import com.fitdogcat.util.CommonUtil;
import com.fitdogcat.util.DataCollection;

public class GUI extends JFrame{
	/**
	 * 1 ก.ย. 2558 เปลี่ยนจากเลขท้าย 3 ตัว 4 รางวัลมาใช้เลขท้าย 3 ตัวและเลขหน้า 3 ตัวอย่างละ 2 รางวัลแทน
	 */
	private static final long serialVersionUID = -4317258709497545043L;
	private JProgressBar progressBar;
	private List<String> dateList;
	private static GUI mainInstance;
	private JDialog dialog;
	private JComboBox comboBox;
	private JList jList;
	public GUI(){
		setTitle("Lotto");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(300,300);
		setLocationRelativeTo(null);
		
		DataCollection.secondTimeout=5;
		//DataCollection.initAuthenticator();
		
		// ProgressBar
		progressBar=new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BackgroundWorker().execute();
			}
		});
		final JTextArea jTextArea=new JTextArea();
		final JButton button=new JButton("Get Data");
		final JScrollPane jScrollPane=new JScrollPane(jTextArea);
		add(jScrollPane,BorderLayout.CENTER);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//if(jList!=null) mainInstance.remove(jList);
				
				new Thread(new Runnable() {
					public void run() {
						//button.setEnabled(false);
						
						while(true){
							add(jScrollPane,BorderLayout.CENTER);
						
							String dateStr=comboBox.getSelectedItem().toString();
							List<LotteryBean> lotteryBeans=CommonUtil.getLottoListBean(dateStr);
							System.out.println("Retrieve from a file");
							
							if(lotteryBeans==null) {
								String url="http://lotto.thaiza.com/ตรวจผลสลากกินแบ่งรัฐบาล-ตรวจหวย-งวดประจำวันที่-"+dateStr;
								lotteryBeans=DataCollection.collectLotteryBean(url);
								System.out.println("Retrieve from the server");
							}
							/*jList=new JList(lotteryBeans.toArray());
							mainInstance.add(jList,BorderLayout.CENTER);*/
							StringBuilder builder=new StringBuilder();
							
							builder.append("Last Sync: "+new Date()+"\n");
							
							for(LotteryBean lotteryBean:lotteryBeans){
								builder.append(lotteryBean);
								builder.append("\n");
							}
							jTextArea.setText(builder.toString());
							
							
							mainInstance.validate();
							mainInstance.repaint();
							
							try {
								Thread.sleep(15000);
								mainInstance.remove(jScrollPane);
							} catch (Exception e) {}
							
							
						}
					}
				}).start();
				
				
			}
		});
		add(button,BorderLayout.SOUTH);
		
	}
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				mainInstance = new GUI();
				
			}
		});
	}
	public class BackgroundWorker extends SwingWorker<Void, Void>{
		@Override
		protected Void doInBackground() throws Exception {
			dialog=new JDialog(mainInstance);
			dialog.add(progressBar);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			//dateList=DataCollection.collectDate("http://lotto.thaiza.com",progressBar);
			dateList=DataCollection.collectDate("http://www.thaiza.com",progressBar);
			
			return null;
		}
		@Override
		protected void done() {
			try{
				dialog.dispose();
				comboBox=new JComboBox(dateList.toArray());
				add(comboBox,BorderLayout.NORTH);
				mainInstance.setVisible(true);
			}catch (Exception e) {
				System.exit(0);
			}
		}
	}
	
	/*public Point getCenterScreen(Component component){
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - component.getWidth()) / 2;
		final int y = (screenSize.height - component.getHeight()) / 2;
		return new Point(x, y);
	}*/
}
