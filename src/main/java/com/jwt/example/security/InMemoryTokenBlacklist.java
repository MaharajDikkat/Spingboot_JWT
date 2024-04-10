package com.jwt.example.security;

import com.jwt.example.controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
@Service
public class InMemoryTokenBlacklist implements TokenBlacklist {
    private Set<String> blacklist = new HashSet<>();

    private Logger logger = LoggerFactory.getLogger(InMemoryTokenBlacklist.class);

    @Override
    public void addToBlacklist(String token) {

        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {

        return blacklist.contains(token);
    }

    @Scheduled(cron = "0 15 10 * * *")
    public  void emptyBlackList(){   // here we are empty black list every day at 10:15 AM

        blacklist = null;

        logger.info(" Black List :  {}",blacklist );
     }
}
