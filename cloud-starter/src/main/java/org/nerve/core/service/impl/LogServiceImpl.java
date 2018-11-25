package org.nerve.core.service.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nerve.auth.Account;
import org.nerve.core.domain.Log;
import org.nerve.core.repo.LogRepo;
import org.nerve.core.service.LogService;
import org.nerve.domain.ID;
import org.nerve.service.CommonServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends CommonServiceImpl<Log, LogRepo> implements LogService {

    @Async
    @Override
    public Log insert(int op, String message, ID entity, Account account) {
        return repo.save(new Log(entity, op,account,message));
    }

//    @Async
    @Override
    public Log insert(int op, String message, Account account) {
        return repo.save(new Log(null, op, account, message));
    }

    @Async
    @Override
    public Log insert(int op, String message, Throwable e, ID entity, Account account) {
        Log l = new Log(entity,op, account, message);
        l.setE(ExceptionUtils.getStackTrace(e));
        return repo.save(l);
    }
}
