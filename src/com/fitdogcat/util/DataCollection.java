package com.fitdogcat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.JProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fitdogcat.LotteryBean;

public class DataCollection {
	public static Proxy proxy;
	public static int secondTimeout;
	public static void initAuthenticator(){
		proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.scb.co.th", 8080));
		Authenticator authenticator = new Authenticator() {
	        public PasswordAuthentication getPasswordAuthentication() {
	            return (new PasswordAuthentication("s45440","Workplace@77".toCharArray()));
	        }
	    };
	    Authenticator.setDefault(authenticator);
	}
	
	public static List<String> collectDate(String url,JProgressBar progressBar){
		BufferedReader bufferedReader=null;
		List<String> collectDate=new ArrayList<String>();
		try{
			//String url="http://lotto.thaiza.com/";
			URLConnection connection=null;
			if(proxy!=null) connection = new URL(url).openConnection(proxy);
			else connection = new URL(url).openConnection();
			
			connection.setConnectTimeout(secondTimeout*1000);
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"TIS-620"));
			
			//bufferedReader = new BufferedReader(new FileReader("D:/java/html/data.html"));
			
			String line = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
		    Document document = Jsoup.parse(String.valueOf(stringBuffer));
		    int dateAmount=document.select("#lotto_date > option").size();
		    StringBuffer dateStringBuffer=new StringBuffer();
			for(int i=0;i<dateAmount;i++){
				dateStringBuffer.setLength(0);
			    String dateStr = document.select("#lotto_date > option:nth-child("+(i+1)+")").first().ownText();
			    //dateParam.add(dateStr.replaceAll("\\s+",""));
			    for(char c:dateStr.toCharArray()) {
			    	if((int)c==160) dateStringBuffer.append("-");
			    	else dateStringBuffer.append(c);
			    }
				collectDate.add(dateStringBuffer.toString());
				if(progressBar!=null) progressBar.setValue((int)((i+1)*100/dateAmount));
			}
		}catch(Exception e){e.printStackTrace();}
		finally{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return collectDate;
	}
	public static List<LotteryBean> collectLotteryBean(String url){
		BufferedReader bufferedReader=null;
		List<LotteryBean> lotteryBeans=new ArrayList<LotteryBean>();
		try{
			String[] dateStr=url.split("งวดประจำวันที่-");
			URLConnection connection=null;
			
			if(proxy!=null) connection = new URL(url).openConnection(proxy);
			else connection = new URL(url).openConnection();
			
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"TIS-620"));
			String line = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
		    Document document = Jsoup.parse(String.valueOf(stringBuffer));
			LotteryBean lotteryBean=null;
			String splitType="\\s*,\\s*";//get rid of whitespaces around items
			String splitType1="\\s+"; //This groups all white spaces as a delimiter. 
			
			lotteryBean=new LotteryBean(); //รางวัลที่หนึ่ง
			try{
				lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col1").first().ownText());
				lotteryBean.setNumber(document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col1.big1").first().ownText().split(splitType));
				lotteryBean.setRemark(document.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col1 > span.text1").first().ownText());				
			}catch (Exception e) {e.printStackTrace();}
			lotteryBeans.add(lotteryBean);
			
			if(isNew3Prize(dateStr[1])){
				lotteryBean=new LotteryBean(); //เลขท้าย 3 ตัว
				try{
					lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-1").first().ownText());
					String num=document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-1.big2.haft > span:nth-child(1)").first().ownText();
					num+=","+document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-1.big2.haft > span:nth-child(2)").first().ownText();
					lotteryBean.setNumber(num.split(splitType));
					//lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-1 > span.text1").first().ownText());
					lotteryBean.setRemark("มี 2 รางวัลๆ ละ 2,000 บาท");
					
				}catch (Exception e) {e.printStackTrace();}
				lotteryBeans.add(lotteryBean);
				
				lotteryBean=new LotteryBean(); //เลขหน้า 3 ตัว
				try{
					lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-2").first().ownText());
					String num1=document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-2.big2.haft > span:nth-child(1)").first().ownText();
					num1+=","+document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-2.big2.haft > span:nth-child(2)").first().ownText();
					lotteryBean.setNumber(num1.split(splitType));
					//lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-2 > span.text1").first().ownText());
					lotteryBean.setRemark("มี 2 รางวัลๆ ละ 2,000 บาท");
				}catch (Exception e) {e.printStackTrace();}
				lotteryBeans.add(lotteryBean);
			}else{
				//ก่อนวันที่ 1 กันยายน 2558 จะใช้ เลขท้าย 3 ตัว 4 รางวัล
				lotteryBean=new LotteryBean(); //เลขท้าย 3 ตัว
				try{
					lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2").first().ownText());
					String num=document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2.big2 > span:nth-child(1)").first().ownText();
					num+=","+document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2.big2 > span:nth-child(2)").first().ownText();
					num+=","+document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2.big2 > span:nth-child(3)").first().ownText();
					num+=","+document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2.big2 > span:nth-child(4)").first().ownText();
					lotteryBean.setNumber(num.split(splitType));
					//lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2 > span.text1").first().ownText());
					lotteryBean.setRemark("มี 2 รางวัลๆ ละ 2,000 บาท");
				}catch (Exception e) {e.printStackTrace();}
				lotteryBeans.add(lotteryBean);
			}
			
			lotteryBean=new LotteryBean(); //เลขท้าย 2 ตัว
			try{
				lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col3").first().ownText());
				lotteryBean.setNumber(document.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col3.big1").first().ownText().split(splitType));
				lotteryBean.setRemark(document.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col3 > span.text1").first().ownText());
			}catch (Exception e) {e.printStackTrace();}
			lotteryBeans.add(lotteryBean);
			
			lotteryBean=new LotteryBean(); //รางวัลข้างเคียงรางวัลที่ 1 มี 2 รางวัลๆ ละ 50,000 บาท
			try{
				//lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.h40 > i.col1 > span").first().ownText());
				lotteryBean.setName("รางวัลข้างเคียงรางวัลที่ 1");
				lotteryBean.setNumber(document.select("#prize_table > div.prize_tb > div.is-row.h40 > b.col22").first().ownText().split(splitType1));
				lotteryBean.setRemark("มี 2 รางวัลๆ ละ 50,000 บาท");
			}catch (Exception e) {e.printStackTrace();}
			lotteryBeans.add(lotteryBean);
			
			lotteryBean=new LotteryBean(); //รางวัลที่ 2
			try{
				lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(4) > h2.col1.bgh2").first().ownText());
				lotteryBean.setNumber(document.select("#prize_table > div.prize_tb > div.is-row5:nth-child(5)").first().ownText().split(splitType));
				lotteryBean.setRemark(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(4) > i").first().ownText());
			}catch (Exception e) {e.printStackTrace();}
			lotteryBeans.add(lotteryBean);
		
			lotteryBean=new LotteryBean(); //รางวัลที่ 3
			try{
				lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(6) > h2.col1.bgh2").first().ownText());
				lotteryBean.setNumber(document.select("#prize_table > div.prize_tb > div.is-row5:nth-child(7)").first().ownText().split(splitType));
				lotteryBean.setRemark(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(6) > i").first().ownText());
			}catch (Exception e) {e.printStackTrace();}
			lotteryBeans.add(lotteryBean);
			
			lotteryBean=new LotteryBean(); //รางวัลที่ 4
			try{
				lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(8) > h2.col1.bgh2").first().ownText());
				lotteryBean.setNumber(document.select("#prize_table > div.prize_tb > div.is-row5:nth-child(9)").first().ownText().split(splitType));
				lotteryBean.setRemark(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(8) > i").first().ownText());
			}catch (Exception e) {e.printStackTrace();}
			lotteryBeans.add(lotteryBean);
			
			lotteryBean=new LotteryBean(); //รางวัลที่ 5
			try{
				lotteryBean.setName(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(10) > h2.col1.bgh2").first().ownText());
				lotteryBean.setNumber(document.select("#prize_table > div.prize_tb > div.is-row5:nth-child(11)").first().ownText().split(splitType));
				lotteryBean.setRemark(document.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(10) > i").first().ownText());
			}catch (Exception e) {e.printStackTrace();}
			lotteryBeans.add(lotteryBean);
			
		}catch(Exception e){e.printStackTrace();}
		finally{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lotteryBeans;
	}
	public static boolean isNew3Prize(String dateStr){		
		boolean isNew3Prize=false;
		
		try{
			//String[] dateStrArray=dateStr.split("-");
			//int date=Integer.parseInt(dateStrArray[0]);
			//Calendar calendar=Calendar.getInstance();
			//calendar.setTime(new SimpleDateFormat("MMMM", new Locale("th","TH")).parse(dateStrArray[1]));
			//int month=calendar.get(Calendar.MONTH);
			//int year=Integer.parseInt(dateStrArray[2]);
			//System.out.println("date:"+date+",month:"+month+",year:"+year);
			//1-กันยายน-2558, date=1, month=8, year=2558
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMMM-yyyy",new Locale("th","TH"));
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(simpleDateFormat.parse(dateStr));
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(simpleDateFormat.parse("1-กันยายน-2558"));
			
			isNew3Prize=calendar.compareTo(calendar2)>=0;
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		
		return isNew3Prize;
	}
	public static void main(String[] args){
		//System.out.println(isNew3Prize("1-สิงหาคม-2558"));
	}
}
