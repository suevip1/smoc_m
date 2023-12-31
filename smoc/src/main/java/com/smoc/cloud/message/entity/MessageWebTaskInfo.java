package com.smoc.cloud.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "message_web_task_info")
public class MessageWebTaskInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "SUBJECT", nullable = false, length = 128)
    private String subject;

    @Column(name = "TEMPLATE_ID", nullable = false, length = 32)
    private String templateId;

    @Column(name = "BUSINESS_ACCOUNT", nullable = false, length = 32)
    private String businessAccount;

    @Column(name = "BUSINESS_TYPE", nullable = false, length = 32)
    private String businessType;

    @Column(name = "INFO_TYPE", nullable = false, length = 32)
    private String infoType;

    @Column(name = "SEND_TYPE", nullable = false, length = 32)
    private String sendType;

    @Column(name = "TIMING_TIME", length = 32)
    private String timingTime;

    @Column(name = "SPLIT_NUMBER")
    private Integer splitNumber;

    @Column(name = "EXPAND_NUMBER", length = 32)
    private String expandNumber;

    @Column(name = "SUBMIT_NUMBER")
    private Integer submitNumber;

    @Column(name = "SUCCESS_NUMBER")
    private Integer successNumber;

    @Column(name = "SUCCESS_SEND_NUMBER")
    private Integer successSendNumber;

    @Column(name = "FAILURE_NUMBER")
    private Integer failureNumber;

    @Column(name = "NO_REPORT_NUMBER")
    private Integer noReportNumber;

    @Column(name = "APPLE_SEND_TIME", length = 32)
    private String appleSendTime;

    @Column(name = "SEND_TIME", length = 32)
    private String sendTime;

    @Column(name = "SEND_STATUS", nullable = false, length = 32)
    private String sendStatus;

    @Column(name = "INPUT_NUMBER", length = 511)
    private String inputNumber;

    @Column(name = "NUMBER_FILES")
    private String numberFiles;

    @Column(name = "MESSAGE_CONTENT", nullable = false, length = 511)
    private String messageContent;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    //发送手机号路径
    @Column(name = "SEND_NUMBER_ATTACHMENT", length = 255)
    private String sendNumberAttachment;
    //异常手机号路径
    @Column(name = "EXCEPTION_NUMBER_ATTACHMENT", length = 255)
    private String exceptionNumberAttachment;
    //状态报告路径
    @Column(name = "REPORT_ATTACHMENT", length = 255)
    private String reportAttachment;

    @Column(name = "ENTERPRISE_ID")
    private String enterpriseId;

    @Column(name = "MESSAGE_TYPE")
    private String messageType;

    @Column(name = "NUMBER_ATTACHMENT_SIZE")
    private Long numberAttachmentSize;

    @Column(name = "GROUP_ID")
    private String groupId;

    @Column(name = "UP_TYPE")
    private String upType;

    public Integer getSplitNumber() {
        return splitNumber;
    }

    public void setSplitNumber(Integer splitNumber) {
        this.splitNumber = splitNumber;
    }

    public String getUpType() {
        return upType;
    }

    public void setUpType(String upType) {
        this.upType = upType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getNumberAttachmentSize() {
        return numberAttachmentSize;
    }

    public void setNumberAttachmentSize(Long numberAttachmentSize) {
        this.numberAttachmentSize = numberAttachmentSize;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getSendNumberAttachment() {
        return sendNumberAttachment;
    }

    public void setSendNumberAttachment(String sendNumberAttachment) {
        this.sendNumberAttachment = sendNumberAttachment;
    }

    public String getExceptionNumberAttachment() {
        return exceptionNumberAttachment;
    }

    public void setExceptionNumberAttachment(String exceptionNumberAttachment) {
        this.exceptionNumberAttachment = exceptionNumberAttachment;
    }

    public String getReportAttachment() {
        return reportAttachment;
    }

    public void setReportAttachment(String reportAttachment) {
        this.reportAttachment = reportAttachment;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getNumberFiles() {
        return numberFiles;
    }

    public void setNumberFiles(String numberFiles) {
        this.numberFiles = numberFiles;
    }

    public String getInputNumber() {
        return inputNumber;
    }

    public void setInputNumber(String inputNumber) {
        this.inputNumber = inputNumber;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getAppleSendTime() {
        return appleSendTime;
    }

    public void setAppleSendTime(String appleSendTime) {
        this.appleSendTime = appleSendTime;
    }

    public Integer getNoReportNumber() {
        return noReportNumber;
    }

    public void setNoReportNumber(Integer noReportNumber) {
        this.noReportNumber = noReportNumber;
    }

    public Integer getFailureNumber() {
        return failureNumber;
    }

    public void setFailureNumber(Integer failureNumber) {
        this.failureNumber = failureNumber;
    }

    public Integer getSuccessSendNumber() {
        return successSendNumber;
    }

    public void setSuccessSendNumber(Integer successSendNumber) {
        this.successSendNumber = successSendNumber;
    }

    public Integer getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(Integer successNumber) {
        this.successNumber = successNumber;
    }

    public Integer getSubmitNumber() {
        return submitNumber;
    }

    public void setSubmitNumber(Integer submitNumber) {
        this.submitNumber = submitNumber;
    }

    public String getExpandNumber() {
        return expandNumber;
    }

    public void setExpandNumber(String expandNumber) {
        this.expandNumber = expandNumber;
    }

    public String getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(String timingTime) {
        this.timingTime = timingTime;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
        this.businessAccount = businessAccount;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}