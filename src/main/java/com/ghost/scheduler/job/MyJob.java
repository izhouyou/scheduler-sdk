package com.ghost.scheduler.job;


import com.ghost.scheduler.utils.HttpClientUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: [时调度任务]</p>
 * @version 1.0
 */
public class MyJob implements Job {
	
	@Value("${env}")
	private String env;

    /** 日志 */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MyJob.class);

    /**
     * Discription:[任务执行操作]
     * @param jobExecutionContext job任务执行上下文
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap data = jobExecutionContext.getMergedJobDataMap();
        String apiName = data.get("name") + "";
        String apiUrl = data.get("url") + "";
        String apiMethod = data.get("method") + "";
        Map<String, Object> apiParams = (Map<String, Object>) data.get("params");
        String result = sendRequest(apiName, apiUrl, apiMethod, apiParams);
        //记录日志
        LOGGER.info("\n 方法[{}]，入参：[{}]", "MyJob-execute", "apiName:" + apiName + ",apiUrl:" + apiUrl + ",apiMethod:" + apiMethod + ",apiParams:" + apiParams);
        //输出提示信息
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = format.format(date);
        LOGGER.info("定时任务系统于" + timeStr + "调用:" + apiName+"  "+apiUrl+"	"+result);
    }

    /**
     * Discription:[发送Restful请求]
     * @param apiName 请求接口名
     * @param apiUrl 请求接口地址
     * @param apiMethod 请求接口方式（GET、POST）
     * @param apiParams 请求参数
     * @return 接口响应结果
     */
    private String sendRequest(String apiName, String apiUrl, String apiMethod, Map<String, Object> apiParams) {
        LOGGER.info("\n 方法[{}]，入参：[{}]", "MyJob-sendRequest", "apiName:" + apiName + ",apiUrl:" + apiUrl + ",apiMethod:" + apiMethod + ",apiParams:" + apiParams);
        String paramStr = "";
        if (apiParams != null) {
            for (Map.Entry<String, Object> entry : apiParams.entrySet()) {
            	if (entry.getValue() != null && "${yestoday}".equals(entry.getValue())) {
            		//获取昨天的日期
            		Calendar cal= Calendar.getInstance();
                    cal.add(Calendar.DATE,-1);
                    Date d=cal.getTime();
                    SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
                    paramStr=sp.format(d);//获取昨天日期
            	}
            	
                if (entry.getValue() != null && "${now}".equals(entry.getValue())) {
                    paramStr += entry.getKey() + "=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "&";
                } else {
                    paramStr += entry.getKey() + "=" + entry.getValue() + "&";
                }
            }
        }
        if ("GET".equals(apiMethod.toUpperCase())) {
            return HttpClientUtils.sendGet(apiUrl, paramStr);
        } else if ("POST".equals(apiMethod.toUpperCase())) {
            Map<String, String> headers = new HashMap<>();
            return HttpClientUtils.sendPost(apiUrl, paramStr, headers);
        }else {
        	LOGGER.error("调用方式错误：" + apiMethod);
        }
        return null;
    }
}
