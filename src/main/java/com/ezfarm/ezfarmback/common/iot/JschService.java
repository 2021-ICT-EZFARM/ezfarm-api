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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class JschService {

  private final IotInfo iotInfo;

  private int port = 22;

  Session session = null;
  Channel channel = null;
  ChannelExec channelExec = null;

  public void connect() {
    try {
      JSch jSch = new JSch();
      session = jSch.getSession(iotInfo.getUsername(), iotInfo.getHostname(), port);
      session.setConfig("StrictHostKeyChecking", "no");
      session.setPassword(iotInfo.getPassword());
      session.connect();

      Channel channel = session.openChannel("exec");
      channelExec = (ChannelExec) channel;
    } catch (JSchException e) {
      new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
    }
  }

  public boolean updateRemote() {
    String line = "";
    connect();
    try {
      log.info("Connect to {}", iotInfo.getHostname());

      channelExec.setCommand("./test.py 1 2 3 4");
      InputStream in = channelExec.getInputStream();
      channelExec.connect();

      //결과값 읽음
      readLines(in, line);

    } catch (Exception e) {
      log.error("Connect Error : Cant connect to {}", iotInfo.getHostname());
      new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
    } finally {
      close();

      if (line.equals("1")) {
        return true;
      } else {
        return false;
      }
    }
  }

  private void readLines(InputStream in, String line) throws IOException {
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

  private void close() {
    if (channel != null) {
      channel.disconnect();
    }
    if (session != null) {
      session.disconnect();
    }
  }

  public String viewScreen() {
    String measureTime = "";
    connect();

    try {
      log.info("Connect to {}", iotInfo.getHostname());

      channelExec.setCommand("./test.py 1 2 3 4");
      InputStream in = channelExec.getInputStream();
      channelExec.connect();

      readLines(in, measureTime);

    } catch (Exception e) {
      log.error("Connect Error : Cant connect to {}", iotInfo.getHostname());
      new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
    } finally {
      close();

      if (measureTime.equals("error")) {
        throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
      } else {
        return measureTime;
      }
    }

  }
}
