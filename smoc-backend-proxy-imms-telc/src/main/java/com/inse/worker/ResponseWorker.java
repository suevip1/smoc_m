/**
 * @desc
 * 
 */
package com.inse.worker;
import java.util.concurrent.BlockingQueue;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;


public class ResponseWorker extends SuperQueueWorker<BusinessRouteValue>{
	private String channelID;
	private BlockingQueue<BusinessRouteValue> responseQueue;

	ResponseWorker(String channelID, String index, BlockingQueue<BusinessRouteValue> responseQueue) {
		this.responseQueue = responseQueue;
		this.channelID = channelID;
		this.setName(new StringBuilder(channelID).append("-").append(index).toString());
		this.start();
	}

	@Override
	protected void doRun() throws Exception {
		// 取出响应消息
		BusinessRouteValue businessRouteValue = responseQueue.take();
		if (businessRouteValue == null) {
			logger.info("响应信息为空");
			return;
		}
		if ("1000".equals(businessRouteValue.getNextNodeCode())) {
			businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.SUCCESS_CODE);
			businessRouteValue.setChannelTotal(1);
			businessRouteValue.setChannelIndex(1);

		} else {
			businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.FAIL_CODE);
			businessRouteValue.setChannelTotal(1);
			businessRouteValue.setChannelIndex(1);

		}
		businessRouteValue.setChannelSRCID(ChannelInfoManager.getInstance().getChannelSRCID(channelID));
		logger.info(new StringBuilder().append("响应信息")      
			      .append("{}phoneNumber={}")
			      .append("{}messageContent={}")
			      .append("{}nextNodeCode={}")
			      .append("{}channelID={}")
			      .append("{}channelMessageID={}").toString(),
			      
			      FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
			      FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
			      FixedConstant.SPLICER,businessRouteValue.getNextNodeCode(),
			      FixedConstant.SPLICER,businessRouteValue.getChannelID(),
			      FixedConstant.SPLICER,businessRouteValue.getChannelMessageID());                  
			      CacheBaseService.saveResponseToMiddlewareCache(businessRouteValue);
	}

	public void exit() {
		super.exit();
	}

	public void add(BusinessRouteValue e) {
		responseQueue.add(e);
	}

}
