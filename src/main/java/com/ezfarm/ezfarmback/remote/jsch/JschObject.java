package com.ezfarm.ezfarmback.remote.jsch;


import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class JschObject {

  private String hostname;

  private String username;

  private String password;

  private int port = 22;

  Session session = null;
  Channel channel = null;

  public JschObject( String hostname, String username, String password) {
    this.hostname = hostname;
    this.username = username;
    this.password = password;
  }

  public void connect() {
    try {
      JSch jSch = new JSch();
      session = jSch.getSession(username, hostname, port);
      session.setPassword(password);
      session.connect();

      channel = session.openChannel("exec");
      ChannelExec channelExec = (ChannelExec) channel;

      log.info("Connect to {}", hostname);
      channelExec.setCommand("touch /home/kms1/test.txt");
      channelExec.connect();

    } catch (JSchException e) {
      new CustomException(ErrorCode.ACCESS_DENIED);
    } finally {
      if (channel != null) {
        channel.disconnect();
      }
      if (session != null) {
        session.disconnect();
      }
    }
  }

}
