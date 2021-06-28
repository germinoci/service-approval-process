package sk.gov.mou;


public class ServiceData {

    private String serviceId;
    private ServiceStatus status;
    
    public ServiceData() {
    }

    public ServiceData(String serviceId, ServiceStatus status) {
       this.status = status;
        this.serviceId = serviceId;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "ServiceData [serviceId=" + serviceId + ", status=" + status + "]";
    }

 


}
