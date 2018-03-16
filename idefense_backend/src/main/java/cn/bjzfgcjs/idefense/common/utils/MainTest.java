package cn.bjzfgcjs.idefense.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainTest {

    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    public static void main(String[] args) {
        new ScheduledTest().executeFileDownLoadTask();

    }
}
