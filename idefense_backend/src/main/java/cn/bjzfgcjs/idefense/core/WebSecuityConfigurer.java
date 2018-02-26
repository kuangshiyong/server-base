package cn.bjzfgcjs.idefense.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


import java.util.List;
import java.util.stream.*;

/**
 * Created by San on 2017/6/26.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecuityConfigurer extends WebSecurityConfigurerAdapter {


    @Value("${voice-server.white-ips:127.0.0.1}")
    private String callIpWhitelist;

    private String ipFileterChain(String ipWhitelist) {
        // parse ip whitelist
        List<String> ipList = Stream.of(ipWhitelist.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        // build ip whitelist rules
        StringBuilder ipRuleChain = new StringBuilder();
        for (String ip : ipList) {
            ipRuleChain.append(" or ");
            ipRuleChain.append("hasIpAddress('").append(ip).append("')");
        }
        ipRuleChain.delete(0,3);

        return ipRuleChain.toString();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .headers().disable()
                .sessionManagement().disable()
                .requestCache().disable()
                .servletApi().disable()

                .authorizeRequests()
                .antMatchers("/call/**").access(ipFileterChain(callIpWhitelist))
                .anyRequest().hasIpAddress("0.0.0.0/0");
    }

}
