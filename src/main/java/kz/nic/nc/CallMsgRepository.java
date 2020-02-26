package kz.nic.nc;

import kz.nic.nc.core.CallMsg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CallMsgRepository extends JpaRepository<CallMsg, Long>{

    @Query(value = "SELECT * FROM call_msg m WHERE m.connected_line_num = ?1 AND m.done = 0 AND m.created > ((NOW() - INTERVAL 1 MINUTE))", nativeQuery = true)
    public List<CallMsg> findUnDoneLast(int connectedLineNum);
}
