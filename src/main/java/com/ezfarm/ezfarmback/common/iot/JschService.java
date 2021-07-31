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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class JschService {

  private String hostname;

  private String username;

  private String password;

  private int port = 22;

  Session session = null;
  Channel channel = null;
  ChannelExec channelExec = null;
  String line = "";

  public JschService( String hostname, String username, String password) {
    this.hostname = hostname;
    this.username = username;
    this.password = password;
  }

  public void connect() {
    try {
      JSch jSch = new JSch();
      session = jSch.getSession(username, hostname, port);
      session.setConfig("StrictHostKeyChecking", "no");
      session.setPassword(password);
      session.connect();

      channelExec = (ChannelExec) channel;
    } catch (JSchException e) {
      new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  public boolean updateRemote() {
    connect();
    try {
      log.info("Connect to {}", hostname);

      channelExec.setCommand("./test.py 1 2 3 4");
      InputStream in = channelExec.getInputStream();
      channelExec.connect();

      //결과값 읽음
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
    } catch (Exception e) {
      new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    } finally {
      close();

      if (line.equals("1")) {
        return true;
      } else {
        return false;
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

}
