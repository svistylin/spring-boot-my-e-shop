package com.hello_world.service.impl;

import com.hello_world.entity.Code;
import com.hello_world.entity.Orders;
import com.hello_world.repository.CodeJpaRepo;
import com.hello_world.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiseImpl implements CodeService {
    @Autowired
    private CodeJpaRepo codeDao;

    @Override
    public void add(Code code) {
        codeDao.save(code);
    }

    @Override
    public int getCode(Orders orders) {
        return (int) Integer.valueOf( codeDao.findCodeByOrdersId(orders.getId()));
    }
}
