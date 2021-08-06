package com.ezfarm.ezfarmback.common.utils.iot;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.jcraft.jsch.ChannelExec;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IotUtils {

    private final JschConnector jschConnector;

    public boolean updateRemote() {
        String line = "";
        jschConnector.connect();
        try {
            log.info("Connect to {}", jschConnector.getHostname());

            ChannelExec channelExec = jschConnector.getChannelExec();
            channelExec.setCommand("./test.py 1 2 3 4");
            InputStream in = channelExec.getInputStream();
            channelExec.connect();

            jschConnector.readLines(in, line);
        } catch (Exception e) {
            log.error("Connect Error : Cant connect to {}", jschConnector.getHostname());
            throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
        } finally {
            jschConnector.close();
            if (line.equals("1")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String getLiveScreen() {
        String measureTime = "";
        jschConnector.connect();
        try {
            log.info("Connect to {}", jschConnector.getHostname());

            ChannelExec channelExec = jschConnector.getChannelExec();
            channelExec.setCommand("./test.py 1 2 3 4");
            InputStream in = channelExec.getInputStream();
            channelExec.connect();

            jschConnector.readLines(in, measureTime);

        } catch (Exception e) {
            log.error("Connect Error : Cant connect to {}", jschConnector.getHostname());
            throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
        } finally {
            jschConnector.close();
            if (measureTime.equals("error")) {
                throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
            } else {
                return measureTime;
            }
        }
    }
}
