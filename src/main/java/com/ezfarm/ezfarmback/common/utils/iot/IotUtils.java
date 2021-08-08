package com.ezfarm.ezfarmback.common.utils.iot;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
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

    public boolean updateRemote(RemoteRequest remoteRequest) {
        String path = getUpdateRemotePath(remoteRequest);
        jschConnector.connect();
        try {
            log.info("Connect to {}", jschConnector.getHostname());

            ChannelExec channelExec = jschConnector.getChannelExec();
            channelExec.setCommand(path);
            InputStream in = channelExec.getInputStream();
            channelExec.connect();

            String output = jschConnector.readLines(in).substring(0, 1);
            jschConnector.close();
            log.info("updateRemote output = {}", output);

            return output.equals("1");
        } catch (Exception e) {
            log.error("Connect Error : Cant connect to {}", jschConnector.getHostname());
            throw new CustomException(ErrorCode.IOT_SERVER_CONNECTION_ERROR);
        }
    }

    private String getUpdateRemotePath(RemoteRequest remoteRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("./").append(jschConnector.getRemoteFile())
            .append(" ").append(remoteRequest.getRemoteId())
            .append(" ").append(remoteRequest.getWater())
            .append(" ").append(remoteRequest.getTemperature())
            .append(" ").append(remoteRequest.getIlluminance())
            .append(" ").append(remoteRequest.getCo2());
        log.info("updateRemote path = {}", sb);
        return sb.toString();
    }

    public String getLiveScreen(Long farmId) {
        String path = getLiveScreenPath(farmId);
        jschConnector.connect();
        try {
            log.info("Connect to {}", jschConnector.getHostname());

            ChannelExec channelExec = jschConnector.getChannelExec();
            channelExec.setCommand(path);
            InputStream in = channelExec.getInputStream();
            channelExec.connect();

            String output = jschConnector.readLines(in);
            jschConnector.close();

            log.info("getLiveScreen output = {}", output);

            if (output.equals("0")) {
                throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
            } else {
                return output;
            }
        } catch (Exception e) {
            log.error("Connect Error : Cant connect to {}", jschConnector.getHostname());
            throw new CustomException(ErrorCode.IOT_SERVER_CONNECTION_ERROR);
        }
    }

    private String getLiveScreenPath(Long farmId) {
        StringBuilder sb = new StringBuilder();
        sb.append("./").append(jschConnector.getLiveScreenFile())
            .append(" ").append(farmId);
        log.info("liveScreen path = {}", sb);
        return sb.toString();
    }
}
