package kz.nic.nc;

import kz.nic.nc.core.CallMsg;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallMsgService {
    private final CallMsgRepository callMsgRespository;

    public List<CallMsg> findUnDoneLast(int connectedLineNum) {        
        return callMsgRespository.findUnDoneLast(connectedLineNum);
    }
    
    public List<CallMsg> findAll() {
        return callMsgRespository.findAll();
    }

    public Optional<CallMsg> findById(Long id) {
        return callMsgRespository.findById(id);
    }

    public CallMsg save(CallMsg stock) {
        return callMsgRespository.save(stock);
    }

    public void deleteById(Long id) {
        callMsgRespository.deleteById(id);
    }
}
