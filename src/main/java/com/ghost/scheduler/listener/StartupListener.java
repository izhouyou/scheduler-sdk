package com.ghost.scheduler.listener;

import com.ghost.scheduler.task.MyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * <p>Description: [springboot上下文加载完成监听]</p>
 * @version 1.0
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    /** 任务对象 */
    @Autowired
    MyTask myTask;


    /**
     * Discription:[应用启动事件]
     * @param event springboot上下文加载完成事件
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        myTask.taskStart();
    }
}
