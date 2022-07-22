import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import world.KafkaApp;

@SpringBootTest(classes = KafkaApp.class)
@RunWith(SpringRunner.class)
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testKafka() {
        int start = 40;
        int end = start + 20;

        System.out.println("测试发送数据到kafka------------------------");
        for (int i = start; i < end; i++) {
            kafkaTemplate.send("spark2", "test--" + i);
        }
    }
}
