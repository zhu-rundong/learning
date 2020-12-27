package com.zrd.ss.springbootssatomikosxa;

import com.zrd.ss.springbootssatomikosxa.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootSsAtomikosXaApplicationTests {

    @Autowired
    private OrderService orderService;

    /**
     * @Description: 测试插入订单类
     * @Author: ZRD
     * @Date: 2020/12/27 
     * @return: void
     **/
    @Test
    void TestAddOrder() {
        orderService.addOrder();
    }

}
