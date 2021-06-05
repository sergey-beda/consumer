package myapp.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import myapp.service.common.enums.OpType;
import org.apache.kafka.clients.admin.AlterConfigOp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class AbstractSystemDto implements Serializable {
    private static final long serialVersionUID = 3264776038824516237L;

    @JsonProperty("OP_TYPE")
    @Enumerated(EnumType.STRING)
    OpType opType;

    @JsonProperty("OP_TS")
    Instant operationTime;

    @JsonProperty("TABLE")
    String table;
}
