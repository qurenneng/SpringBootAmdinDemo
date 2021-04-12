package com.example.demo.configuration;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.CompositeNotifier;
import de.codecentric.boot.admin.server.notify.Notifier;
import de.codecentric.boot.admin.server.notify.RemindingNotifier;
import de.codecentric.boot.admin.server.notify.filter.FilteringNotifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author qrn
 * @version 1.0
 * @date 2021/4/9 17:53
 * 邮箱事件配置: 通知提醒 每10分钟发送一次提醒
 */
@Configuration(proxyBeanMethods = false)
public class NotifierConfiguration {

    private final InstanceRepository repository;

    private final ObjectProvider<List<Notifier>> otherNotifiers;


    public NotifierConfiguration(InstanceRepository repository, ObjectProvider<List<Notifier>> otherNotifiers) {
        this.repository = repository;
        this.otherNotifiers = otherNotifiers;
    }

    @Bean
    public FilteringNotifier filteringNotifier() { // <1>
        CompositeNotifier delegate = new CompositeNotifier(this.otherNotifiers.getIfAvailable(Collections::emptyList));
        return new FilteringNotifier(delegate, this.repository);
    }

    /**
     * 重启/离线应用程序发送提醒
     * @return
     */
    @Primary
    @Bean(initMethod = "start", destroyMethod = "stop")
    public RemindingNotifier remindingNotifier() { // <2>
        RemindingNotifier notifier = new RemindingNotifier(filteringNotifier(), this.repository);
        notifier.setReminderPeriod(Duration.ofMinutes(10)); //提醒将每10分钟发送一次
        notifier.setCheckReminderInverval(Duration.ofSeconds(10)); //安排每10秒发送一次到期提醒
        return notifier;
    }
}
