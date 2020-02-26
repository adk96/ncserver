package kz.nic.nc;

import kz.nic.nc.core.CallMsg;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/callmsg")
@RequiredArgsConstructor
public class CallMsgAPI {

    private final CallMsgService callMsgService;

    @GetMapping("/findUnDoneLast/{connectedLineNum}")
    public ResponseEntity<List<CallMsg>> findUnDoneLast(@PathVariable int connectedLineNum) {
        return ResponseEntity.ok(callMsgService.findUnDoneLast(connectedLineNum));
    }
    
    @GetMapping("/setDone/{id}")
    public ResponseEntity<CallMsg> update(@PathVariable Long id) {
        Optional<CallMsg> callMsg = callMsgService.findById(id);
        if (!callMsg.isPresent()) {            
            ResponseEntity.badRequest().build();
        }
        CallMsg obj = callMsg.get();
        obj.setDone((short)1);
        return ResponseEntity.ok(callMsgService.save(obj));
    }

}
