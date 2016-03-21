package com.fitdogcat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main {
	
	
	public static void main(String[] args) throws IOException{
		boolean isUsedHTTP=true;
		BufferedReader in=null;
		
	    if(isUsedHTTP) {
	    	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.scb.co.th", 8080));
			Authenticator authenticator = new Authenticator() {
		        public PasswordAuthentication getPasswordAuthentication() {
		            return (new PasswordAuthentication("s45440",
		                    "Workplace@7".toCharArray()));
		        }
		    };
		    Authenticator.setDefault(authenticator);
		    //for getting a date
		    String url="http://www.thaiza.com/";
		    
		    url="http://lotto.thaiza.com/ตรวจผลสลากกินแบ่งรัฐบาล-ตรวจหวย-งวดประจำวันที่-16-สิงหาคม-2558";
			//String url="http://lotto.thaiza.com/%E0%B8%95%E0%B8%A3%E0%B8%A7%E0%B8%88%E0%B8%9C%E0%B8%A5%E0%B8%AA%E0%B8%A5%E0%B8%B2%E0%B8%81%E0%B8%81%E0%B8%B4%E0%B8%99%E0%B9%81%E0%B8%9A%E0%B9%88%E0%B8%87%E0%B8%A3%E0%B8%B1%E0%B8%90%E0%B8%9A%E0%B8%B2%E0%B8%A5-%E0%B8%95%E0%B8%A3%E0%B8%A7%E0%B8%88%E0%B8%AB%E0%B8%A7%E0%B8%A2-%E0%B8%87%E0%B8%A7%E0%B8%94%E0%B8%9B%E0%B8%A3%E0%B8%B0%E0%B8%88%E0%B8%B3%E0%B8%A7%E0%B8%B1%E0%B8%99%E0%B8%97%E0%B8%B5%E0%B9%88-16-%E0%B8%81%E0%B8%B8%E0%B8%A1%E0%B8%A0%E0%B8%B2%E0%B8%9E%E0%B8%B1%E0%B8%99%E0%B8%98%E0%B9%8C-2559";
		    //String url2="http://lotto.thaiza.com/ตรวจผลสลากกินแบ่งรัฐบาล-ตรวจหวย-งวดประจำวันที่-16-กุมภาพันธ์-2559";
			URLConnection connection = new URL(url).openConnection(proxy);
	    	in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"TIS-620"));
	    }
	    else in = new BufferedReader(new FileReader("D:/java/html/data.html"));
		
		String line = null;
		StringBuffer tmp = new StringBuffer();
		while ((line = in.readLine()) != null) {
			tmp.append(line);
		}
	    
		//Document doc = Jsoup.parse(String.valueOf(tmp));
	    Document doc = Jsoup.parse(String.valueOf(tmp));
	    //doc.outputSettings().charset("UTF-8");
		//connection.getInputStream()
		
	    //System.out.println(doc.select("#lotto_date > option").size());
	    //Element prize =doc.getElementById("prize_table");
		Element prize = doc.select("#lotto_date").first();
		//Element prize = doc.select("html > body > div.body-main > aside > section").first();
		int hasDataCount=0;
		
		System.out.println(prize.getAllElements().size());
		for(int i=0;i<prize.getAllElements().size();i++){
			//skip 0
			//if(i==0) continue;
			
			String data=doc.select(prize.getAllElements().get(i).cssSelector()).first().ownText();
			if(!"".equals(data)){
				System.out.println(prize.getAllElements().get(i).cssSelector());
				//System.out.println("Value:"+doc.select(prize.getAllElements().get(i).cssSelector()).first().html());
				System.out.println("id "+hasDataCount+": "+data);
				System.out.println("-------------------------");
				hasDataCount++;
			}
		}
	    
	    /*//get date
	    Element dateElement = doc.select("html > body > div.body-main > aside > section").first();
		List<String> dateParam=new ArrayList<String>();
		for(int i=0;i<dateElement.getAllElements().size();i++){
			String data=doc.select(dateElement.getAllElements().get(i).cssSelector()).first().ownText();
			if(!"".equals(data)) dateParam.add(data.replace(" ", "-"));
		}*/
		
		/*//get date
	    List<String> dateParam=new ArrayList<String>();
	    int dateAmount=doc.select("#lotto_date > option").size();
	    StringBuffer stringBuffer=new StringBuffer();
		for(int i=1;i<=dateAmount;i++){
		    String dateStr = doc.select("#lotto_date > option:nth-child("+i+")").first().ownText();
		    //dateParam.add(dateStr.replaceAll("\\s+",""));
		    stringBuffer.setLength(0);
		    for(char c:dateStr.toCharArray()) {
		    	//System.out.print((int)c+"_"+c+";");
		    	if((int)c==160) stringBuffer.append("-");
		    	else stringBuffer.append(c);
		    }
		    //System.out.println();
		    
			dateParam.add(stringBuffer.toString());
		}*/
		
		/*List<LotteryBean> lotteryBeans=new ArrayList<LotteryBean>();
		LotteryBean lotteryBean=null;
		String splitType="\\s*,\\s*";//get rid of whitespaces around items
		String splitType1="\\s+"; //This groups all white spaces as a delimiter. 
		
		lotteryBean=new LotteryBean(); //รางวัลที่หนึ่ง
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col1").first().ownText());
		lotteryBean.setNumber(doc.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col1.big1").first().ownText().split(splitType));
		lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col1 > span.text1").first().ownText());
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //เลขท้าย 3 ตัว
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-1").first().ownText());
		String num=doc.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-1.big2.haft > span:nth-child(1)").first().ownText();
		num+=","+doc.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-1.big2.haft > span:nth-child(2)").first().ownText();
		lotteryBean.setNumber(num.split(splitType));
		//lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-1 > span.text1").first().ownText());
		lotteryBean.setRemark("มี 2 รางวัลๆ ละ 2,000 บาท");
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //เลขหน้า 3 ตัว
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-2").first().ownText());
		String num1=doc.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-2.big2.haft > span:nth-child(1)").first().ownText();
		num1+=","+doc.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col2-2.big2.haft > span:nth-child(2)").first().ownText();
		lotteryBean.setNumber(num1.split(splitType));
		//lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col2-2 > span.text1").first().ownText());
		lotteryBean.setRemark("มี 2 รางวัลๆ ละ 2,000 บาท");
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //เลขท้าย 2 ตัว
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col3").first().ownText());
		lotteryBean.setNumber(doc.select("#prize_table > div.prize_tb > div.is-row:nth-child(2) > b.col3.big1").first().ownText().split(splitType));
		lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.bgh2:nth-child(1) > h2.col3 > span.text1").first().ownText());
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //รางวัลข้างเคียงรางวัลที่ 1 มี 2 รางวัลๆ ละ 50,000 บาท
		//lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.h40 > i.col1 > span").first().ownText());
		lotteryBean.setName("รางวัลข้างเคียงรางวัลที่ 1");
		lotteryBean.setNumber(doc.select("#prize_table > div.prize_tb > div.is-row.h40 > b.col22").first().ownText().split(splitType1));
		lotteryBean.setRemark("มี 2 รางวัลๆ ละ 50,000 บาท");
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //รางวัลที่ 2
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(4) > h2.col1.bgh2").first().ownText());
		lotteryBean.setNumber(doc.select("#prize_table > div.prize_tb > div.is-row5:nth-child(5)").first().ownText().split(splitType));
		lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(4) > i").first().ownText());
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //รางวัลที่ 3
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(6) > h2.col1.bgh2").first().ownText());
		lotteryBean.setNumber(doc.select("#prize_table > div.prize_tb > div.is-row5:nth-child(7)").first().ownText().split(splitType));
		lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(6) > i").first().ownText());
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //รางวัลที่ 4
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(8) > h2.col1.bgh2").first().ownText());
		lotteryBean.setNumber(doc.select("#prize_table > div.prize_tb > div.is-row5:nth-child(9)").first().ownText().split(splitType));
		lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(8) > i").first().ownText());
		lotteryBeans.add(lotteryBean);
		
		lotteryBean=new LotteryBean(); //รางวัลที่ 5
		lotteryBean.setName(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(10) > h2.col1.bgh2").first().ownText());
		lotteryBean.setNumber(doc.select("#prize_table > div.prize_tb > div.is-row5:nth-child(11)").first().ownText().split(splitType));
		lotteryBean.setRemark(doc.select("#prize_table > div.prize_tb > div.is-row.subhead:nth-child(10) > i").first().ownText());
		lotteryBeans.add(lotteryBean);*/
		
		/*for(String date:dateParam){
			System.out.println(date);
		}*/
		
		/*for(LotteryBean lotteryBean2:lotteryBeans){
			System.out.println(lotteryBean2);
		}*/
	}
}
