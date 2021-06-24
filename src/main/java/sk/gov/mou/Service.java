package sk.gov.mou;


public class Service {

    private ServiceStatus status;
    private String serviceId;
    
    public Service() {
    }

    public Service(ServiceStatus status, String serviceId) {
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
        return "Service [serviceId=" + serviceId + ", status=" + status + "]";
    }

}
