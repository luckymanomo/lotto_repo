package com.fitdogcat.background;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitdogcat.gui.LotteryBean;
import com.fitdogcat.util.DataCollection;

public class FetchingRunner {
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static File lottoFileBackup=new File("d:/temp/lottoBackup");
	public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMMM-yyyy",new Locale("th","TH"));
	public static SimpleDateFormat dateFileStr=new SimpleDateFormat("yyyyMMdd",new Locale("en","EN"));
	public static String url="http://lotto.thaiza.com/ตรวจผลสลากกินแบ่งรัฐบาล-ตรวจหวย-งวดประจำวันที่-";
	public static  List<String> dateList;
	final static Logger logger = Logger.getLogger(FetchingRunner.class);
	public static void main(String[] args){
		final int second=10;
		DataCollection.secondTimeout=10;
		//DataCollection.initAuthenticator();
		
		dateList=DataCollection.collectDate("http://www.thaiza.com",null);
		
		//get all data for the first time
		initialLottoBackup(dateList);
		
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					//List<LotteryBean> lotteryBeans=CommonUtil.getLottoListBean(dateList.get(0));
					dateList=DataCollection.collectDate("http://www.thaiza.com",null);
					try {
						String fileStr = dateFileStr.format(simpleDateFormat.parse(dateList.get(0)));
						File file=new File(FetchingRunner.lottoFileBackup.getAbsoluteFile()+File.separator+fileStr);
						if(file.isFile() && file.exists()){
							if(hasPendingResult(file)){
								System.out.println(fileStr+" is pending result...");
								logger.debug(fileStr+" is pending result...");
								List<LotteryBean> lotteryBeans=DataCollection.collectLotteryBean(url+dateList.get(0));
								String dateFileName=dateFileStr.format(simpleDateFormat.parse(dateList.get(0)));
								mapper.writeValue(new File(lottoFileBackup.getAbsoluteFile()+File.separator+dateFileName), lotteryBeans);
								System.out.println(dateFileName+" was created under "+lottoFileBackup.getAbsolutePath());
								logger.debug(dateFileName+" was created under "+lottoFileBackup.getAbsolutePath());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {Thread.sleep(second*1000);}
					catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}).start();
		
	}
	public static void initialLottoBackup(List<String> dateList){
		for(String dateStr:dateList){
			try {
				String dateFileName=dateFileStr.format(simpleDateFormat.parse(dateStr));
				
				if(!lottoFileBackup.exists()) lottoFileBackup.mkdirs();
				
				if(Arrays.asList(lottoFileBackup.list()).contains(dateFileName)){
					System.out.println(dateFileName+" was found under "+lottoFileBackup.getAbsolutePath());
					logger.debug(dateFileName+" was found under "+lottoFileBackup.getAbsolutePath());
				}else{
					List<LotteryBean> lotteryBeans=DataCollection.collectLotteryBean(url+dateStr);
					mapper.writeValue(new File(lottoFileBackup.getAbsoluteFile()+File.separator+dateFileName), lotteryBeans);
					System.out.println(dateFileName+" was created under "+lottoFileBackup.getAbsolutePath());
					logger.debug(dateFileName+" was created under "+lottoFileBackup.getAbsolutePath());
				}
				
				
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static boolean hasPendingResult(File file){
		FileReader fr=null;
		BufferedReader br=null;
		boolean hasFound=false;
		try {
			fr=new FileReader(file);
			br = new BufferedReader(fr);
		    StringBuilder sb = new StringBuilder();
		    String line=null;
		    while ((line = br.readLine()) != null) {
		        sb.append(line);
		    }
		    hasFound = sb.toString().contains("xxxxxx") || sb.toString().contains("XXXXXX");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		return hasFound;
	}
	
}
