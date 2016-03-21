package com.fitdogcat.util;

import javax.swing.SwingWorker;

public class BackgroundWorker extends SwingWorker<Void, Void> {

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/*public GUI mainInstance;
	private int processNo;
	public static final int COLLECT_DATE=0;
	public static final int COLLECT_LOTTERY_BEAN=1;
	public BackgroundWorker(GUI mainInstance,int processNo){
		this.mainInstance=mainInstance;
		this.processNo=processNo;
	}
	public BackgroundWorker() {
		addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				mainInstance.progressBar.setValue(getProgress());
				System.out.println("set_");
			}

		});
	}

	@Override
	protected void done() {
		JOptionPane.showMessageDialog(null,"Import Data Successfully");
		btnStart.setEnabled(true);
	}
	@Override
	protected Void doInBackground() throws Exception {
		switch(processNo){
			case COLLECT_DATE:mainInstance.dateList=DataCollection.collectDate("http://lotto.thaiza.com",this);break;
			case COLLECT_LOTTERY_BEAN:break;
		}
		return null;
		
	}
	
	public void setProgress_(int processNo){
		setProgress(processNo);
		System.out.println("set_processNo:"+processNo);
	}*/
	
}
