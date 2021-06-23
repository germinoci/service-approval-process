package sk.gov.mou;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    private ServiceStatus status;
    private String serviceId;

}
