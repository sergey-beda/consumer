package myapp.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import myapp.service.common.KafkaTopic;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@KafkaTopic("CLIENT_TOPIC")
public class ClientDto extends AbstractSystemDto {

    private  static final long serialVersionUID = 3401618112342223988L;

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("NAME")
    private String name;
}
