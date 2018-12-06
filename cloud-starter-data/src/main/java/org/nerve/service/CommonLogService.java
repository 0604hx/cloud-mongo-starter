package org.nerve.service;

import org.nerve.auth.Account;
import org.nerve.core.domain.Log;
import org.nerve.core.repo.LogRepo;
import org.nerve.domain.ID;
import org.nerve.enums.LogType;

public interface CommonLogService{

    /**
     * 插入新日志
     * @param op
     * @param message
     * @param entity
     * @param account
     * @return
     */
    Log insert(int op, String message, ID entity, Account account);

    default Log insert(LogType op, String message, ID entity, Account account){
        return this.insert(op.value(), message, entity, account);
    }

    /**
     *
     * @param op
     * @param message
     * @param account
     * @return
     */
    Log insert(int op, String message, Account account);

    Log insert(int op, String message, Throwable e, ID entity, Account account);
}
