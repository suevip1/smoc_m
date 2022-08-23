package com.protocol.access.sgip.pdu;

import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.sgip.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.sgip.sms.PDUException;
import com.protocol.access.sgip.SgipConstant;

public class ReportResp extends Response {
	private byte result = 0;
	private String reserve = "";

	public ReportResp() {
		super(SgipConstant.SGIP_REPORT_RESP);
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SgipConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}
	
	public void setBody(ByteBuffer buffer)
		throws PDUException {
		try {
			result = buffer.removeByte();
			reserve = buffer.removeStringEx(8);
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendByte(result);
		buffer.appendString(reserve, 8);
		return buffer;
	}
	
	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public String name() {
		return "SGIP ReportResp";
	}
	
	public String dump() {
		String rt =  "\r\nDeliverResp*****************************"
					+"\r\nresult:	"+result
					+"\r\nreserve:	"+reserve
					+"\r\n*****************************DeliverResp";
		return rt;
	}
}
