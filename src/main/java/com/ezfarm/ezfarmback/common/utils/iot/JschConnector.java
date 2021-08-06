package com.ezfarm.ezfarmback.common.utils.iot;


import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JschConnector {

    @Value("${property.iot.hostname}")
    private String hostname;

    @Value("${property.iot.username}")
    private String username;

    @Value("${property.iot.password}")
    private String password;

    private final int port = 22;

    private Session session = null;
    private Channel channel = null;
    private ChannelExec channelExec = null;

    public void connect() {
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(username, hostname, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            channel = session.openChannel("exec");
            channelExec = (ChannelExec) channel;
        } catch (JSchException e) {
            throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
        }
    }

    public void readLines(InputStream in, String line) throws IOException {
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                line += new String(tmp, 0, i);
            }
            if (channelExec.isClosed()) {
                break;
            }
        }
    }

    public void close() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
