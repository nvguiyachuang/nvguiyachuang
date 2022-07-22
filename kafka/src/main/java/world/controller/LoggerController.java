package world.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@Slf4j
//@RequestMapping("kafka")
public class LoggerController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    @RequestMapping("/test")
//    public void log(@RequestBody String logJson) {
//        if (logJson.contains("start")) {
//            kafkaTemplate.send("GMALL_START", logJson);
//        } else {
//            kafkaTemplate.send("GMALL_EVENT", logJson);
//        }
//    }

    @RequestMapping("/applog")
    public void applog(@RequestBody JSONObject jsonObject) {
        String logJson = jsonObject.toJSONString();
//        log.info(logJson);
        System.out.println(logJson);
        if (jsonObject.getString("start") != null) {
            kafkaTemplate.send("GMALL_START", logJson);
        } else {
            kafkaTemplate.send("GMALL_EVENT", logJson);

        }
    }

    @PostConstruct
    public void test() {
        System.out.println("test PostConstruct");
    }

}