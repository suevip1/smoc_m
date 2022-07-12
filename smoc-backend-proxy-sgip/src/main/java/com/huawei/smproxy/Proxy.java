/**
 * @desc
 * 
 */
package com.huawei.smproxy;

import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;

public abstract class Proxy {
	private int base = 100000000;
	private int sequence=(int)(5*base*Math.random()+10*base);
	
	public synchronized int getSequence(){
		if(sequence == Integer.MAX_VALUE){
			return 10*base;
		}
		return sequence++;
	}
	
	public abstract SGIPMessage onDeliver(SGIPDeliverMessage msg);
	public abstract SGIPMessage onReport(SGIPReportMessage msg);
	public abstract SGIPMessage onUserReport(SGIPUserReportMessage msg);
	
	public abstract int send(SGIPMessage message) throws Exception;
	
	/**
	 * 连接终止的处理，由API使用者实现 SMC连接终止后，需要执行动作的接口
	 */
	public abstract void onTerminate();
	
	public abstract boolean isHealth();
}

