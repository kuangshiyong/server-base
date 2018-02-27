package cn.bjzfgcjs.idefense.service;

import cn.bjzfgcjs.idefense.device.bean.HikHandlerBean;
import org.redisson.api.RDeque;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PubMessage implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PubMessage.class);

    @Autowired
    private RedissonClient client;

    // 发布雷达ARPA功能的TTM
    public RTopic<String> ttmMessage() { return client.getTopic("nema|ttm"); }

    // 发布系统机位故障信息
//    public RTopic<SysFaultBean> sysFaultMessage() {return client.getTopic("sys|fault"); }

    public RMap<String, HikHandlerBean> hikHandls() { return client.getMap("hikhandlers"); }

    public RDeque<String> soundHandle() { return client.getDeque("sounds");}

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

