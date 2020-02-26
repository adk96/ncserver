package kz.nic.nc;

import kz.nic.nc.core.CallMsg;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class AmiListenerThread implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(AmiListenerThread.class);

    @Value("${ami.host}")
    private String amiHost;
    @Value("${ami.user}")
    private String amiUser;
    @Value("${ami.password}")
    private String amiPassword;
    
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private CallMsgService callMsgService;

    private DefaultManagerConnection managerConnection;

    /**
     *
     */
    @PostConstruct
    public void startThread() {
        taskExecutor.execute(this);               
    }

    @Override
    public void run() {

        LOGGER.info("AmiListenerThread started!");

        try {

            while (true) {
                try {

                    managerConnection = new DefaultManagerConnection(amiHost, amiUser, amiPassword);
                    
                    managerConnection.addEventListener(new ManagerEventListener() {
                        @Override
                        public void onManagerEvent(ManagerEvent event) {

                            if (event instanceof org.asteriskjava.manager.event.NewConnectedLineEvent) {
                                org.asteriskjava.manager.event.NewConnectedLineEvent e = (org.asteriskjava.manager.event.NewConnectedLineEvent) event;

                                if (e.getConnectedLineNum() != null) {
                                    CallMsg callMsg = new CallMsg();
                                    callMsg.setType("call_ring");
                                    callMsg.setCallerId(e.getCallerIdNum());
                                    callMsg.setCallerIdName(e.getCallerIdName());
                                    callMsg.setConnectedLineNum(e.getConnectedLineNum());
                                    callMsg.setConnectedLineName(e.getConnectedLineName());
                                    callMsg.setDone((short) 0);
                                    callMsgService.save(callMsg);
                                }
                            }

                            if (event instanceof org.asteriskjava.manager.event.SoftHangupRequestEvent) {
                                org.asteriskjava.manager.event.SoftHangupRequestEvent e = (org.asteriskjava.manager.event.SoftHangupRequestEvent) event;

                                if (e.getConnectedLineNum() != null) {
                                    CallMsg callMsg = new CallMsg();
                                    callMsg.setType("call_end");
                                    callMsg.setCallerId(e.getCallerIdNum());
                                    callMsg.setCallerIdName(e.getCallerIdName());
                                    callMsg.setConnectedLineNum(e.getConnectedLineNum());
                                    callMsg.setConnectedLineName(e.getConnectedLineName());
                                    callMsg.setDone((short) 0);
                                    callMsgService.save(callMsg);
                                }

                                /*if (e.getConnectedLineNum() != null) {
                                    if (e.getConnectedLineNum().equals("1132")) {
                                        System.out.println(e.getConnectedLineName() + " конец разговора ");
                                    }
                                }*/
                            }

                        }
                    });

                    managerConnection.login();

                    Thread.sleep(1000 * 60 * 60);

                    managerConnection.logoff();

                } catch (AuthenticationFailedException | TimeoutException | IOException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

}
