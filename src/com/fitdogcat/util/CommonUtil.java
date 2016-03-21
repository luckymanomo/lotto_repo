package com.fitdogcat.util;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fitdogcat.background.FetchingRunner;
import com.fitdogcat.gui.LotteryBean;

public class CommonUtil {
	public static List<LotteryBean> getLottoListBean(String dateStr){
		List<LotteryBean> lotteryBeans=null;
		try {
			String dateFileStr = FetchingRunner.dateFileStr.format(FetchingRunner.simpleDateFormat.parse(dateStr));
			JavaType type = FetchingRunner.mapper.getTypeFactory().constructCollectionType(List.class, LotteryBean.class);
			lotteryBeans=FetchingRunner.mapper.readValue(new File(FetchingRunner.lottoFileBackup.getAbsoluteFile()+File.separator+dateFileStr), type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lotteryBeans;
	}
}
