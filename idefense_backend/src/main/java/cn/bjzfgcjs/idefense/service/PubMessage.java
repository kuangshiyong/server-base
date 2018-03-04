package cn.bjzfgcjs.idefense.service;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PubMessage implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PubMessage.class);

    @Resource
    private RedissonClient client;

    /*****************************  通知订阅消息通道 ***************************************/
    // 雷达使用NEMA0183协议，主要是TTM
    public RTopic<String> radarMessage() { return client.getTopic("radar:ttm"); }

    public RTopic<String> devStatus() {return client.getTopic("dev:status");}

    // 使用之前定义的表“智防监控事件日志表” 的字段来报告
    public RTopic<String> defenseEvent() {return client.getTopic("radar:ttm");}

    public RTopic<String> cameraEvent() {return client.getTopic("warn:camera");}


    /*****************************  通知订阅消息发布 ***************************************/
    public void reportRadar(DeviceInfo deviceInfo, String nemaSentence) {
        radarMessage().publish(nemaSentence);
    }



    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

