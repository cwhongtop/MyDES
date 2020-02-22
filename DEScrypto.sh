#!/usr/bin/env bash
#-----------------------------------------
#name:			DEScrypto.sh
#version:		1.0
#createTime:	2019.05.13
#description:	执行DEScrypto.jar,DES加密程序
#author:		cwhong
#email:			cwhong_top@163.com
#github:		github.com/cwhongtop
#-----------------------------------------


export JAVA_HOME=/opt/jdk1.8.0_212
export JRE_HOME=/opt/jdk1.8.0_212/jre
export CLASSPATH=.:$CLASSPATH:$JAVA_HOME/lib:$JRE_HOME/lib
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin

java -jar DEScrypto.jar
