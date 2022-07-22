package com.hello.world.java.cdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.Map;
import java.util.stream.Collectors;

public class Cust2JsonDebeziumDeserializationSchema implements DebeziumDeserializationSchema {

    @Override
    public void deserialize(SourceRecord record, Collector out) throws Exception {
        Envelope.Operation op = Envelope.operationFor(record);
        Struct value = (Struct) record.value();
        Schema valueSchema = record.valueSchema();
        Tuple2<Boolean, String> result;
        if (op == Envelope.Operation.CREATE || op == Envelope.Operation.READ) {
            String insert = extractAfterRow(value, valueSchema);
            result = new Tuple2<>(true, insert);
        } else if (op == Envelope.Operation.DELETE) {
            String delete = extractBeforeRow(value, valueSchema);
            result = new Tuple2<>(false, delete);
        } else {
            String after = extractAfterRow(value, valueSchema);
            result = new Tuple2<>(true, after);
        }
        out.collect(result);
    }


    private Map<String, Object> getRowMap(Struct after) {
        return after.schema().fields().stream().collect(Collectors.toMap(Field::name, after::get));
    }


    private String extractAfterRow(Struct value, Schema valueSchema) throws Exception {
        Struct after = value.getStruct(Envelope.FieldName.AFTER);
        Map<String, Object> rowMap = getRowMap(after);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(rowMap);
    }

    private String extractBeforeRow(Struct value, Schema valueSchema) throws Exception {
        Struct after = value.getStruct(Envelope.FieldName.BEFORE);
        Map<String, Object> rowMap = getRowMap(after);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(rowMap);
    }


    @Override
    public TypeInformation getProducedType() {
        return TypeInformation.of(new TypeHint<Tuple2<Boolean, String>>() {
        });
    }
}
