package com.ezfarm.ezfarmback.common.iot;


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
public class Jsch {

    @Value("${app.iot.port}")
    private int port;

    @Value("${app.iot.hostname}")
    private String hostname;

    @Value("${app.iot.username}")
    private String username;

    @Value("${app.iot.password}")
    private String password;

    @Value("${app.iot.remote-file}")
    private String remoteFile;

    @Value("${app.iot.live-screen-file}")
    private String liveScreenFile;

    @Value("${app.iot.live-facility-file}")
    private String liveFacility;

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
            throw new CustomException(ErrorCode.IOT_SERVER_CONNECTION_ERROR);
        }
    }

    public InputStream executeCommand(String path) throws Exception {
        channelExec.setCommand(path);
        InputStream in = channelExec.getInputStream();
        channelExec.connect();
        return in;
    }

    public String readLines(InputStream in) throws IOException {
        byte[] tmp = new byte[1024];
        StringBuilder outputBuffer = new StringBuilder();

        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                outputBuffer.append(new String(tmp, 0, i));
            }
            if (channelExec.isClosed()) {
                break;
            }
        }
        return outputBuffer.toString();
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
