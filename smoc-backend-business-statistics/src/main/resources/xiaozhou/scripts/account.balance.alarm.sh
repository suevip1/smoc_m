#!/bin/sh
. /etc/profile
. ~/.bash_profile

#监控账号的成功率，延迟率`
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.message.alarm.AccountBalanceAlarm >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
