import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

//Params
def topicName = "meu-topico";

//Methods
public byte[] decodeBase64(String data){
    try {
          byte[] decodedString = Base64.getDecoder().decode(new String(data).getBytes("UTF-8"));          
          return decodedString
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
          e.printStackTrace();
    }
}

//Configs
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

//Header
List<Header> headers = new ArrayList<Header>();
headers.add(new RecordHeader("schemaVersion", "1.0".getBytes()));

//Record
def iteration = vars.getIteration();
def action = vars.get('action');
def data = vars.get('data');
def decodedBytes = decodeBase64(data);
log.debug("Interation: "+ String.valueOf(iteration));
log.debug("Action: "+ action);
log.debug("Data: "+ data);
log.debug("decoded bytes : " +  decodedBytes );
log.debug("decoded string: " + new String(decodedBytes));
Date latestdate = new Date();
int partition = 0;
String message = decodedBytes;
ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topicName, partition, latestdate.getTime(), action, message, headers);

//Producer
KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
producer.send(producerRecord);
producer.close();