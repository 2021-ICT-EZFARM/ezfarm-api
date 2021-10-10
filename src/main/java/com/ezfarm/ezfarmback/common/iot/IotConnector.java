package com.ezfarm.ezfarmback.common.iot;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IotConnector {

  private final Jsch jsch;

  public void updateRemote(RemoteRequest remoteRequest) {
    String path = getUpdateRemotePath(remoteRequest);
    jsch.connect();
    try {
      log.info("Connect to {}", jsch.getHostname());

      InputStream in = jsch.executeCommand(path);

      String output = jsch.readLines(in).substring(0, 1);
      jsch.close();
      log.info("updateRemote output = {}", output);

      if (!output.equals("1")) {
        throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
      }

    } catch (Exception e) {
      log.error("Connect Error : Cant connect to {}", jsch.getHostname());
      throw new CustomException(ErrorCode.IOT_SERVER_CONNECTION_ERROR);
    }
  }

  private String getUpdateRemotePath(RemoteRequest remoteRequest) {
    StringBuilder sb = new StringBuilder();
    sb.append("./").append(jsch.getRemoteFile())
        .append(" ").append(remoteRequest.getRemoteId())
        .append(" ").append(remoteRequest.getWater())
        .append(" ").append(remoteRequest.getTemperature())
        .append(" ").append(remoteRequest.getIlluminance())
        .append(" ").append(remoteRequest.getCo2());
    log.info("updateRemote path = {}", sb);
    return sb.toString();
  }

  public String getLiveScreen(Long farmId) {
    String path = getFarmIdPath(farmId, jsch.getLiveScreenFile());
    jsch.connect();
    try {
      log.info("Connect to {}", jsch.getHostname());

      InputStream in = jsch.executeCommand(path);

      String output = jsch.readLines(in);
      jsch.close();

      log.info("getLiveScreen output = {}", output);

      if (output.equals("0")) {
        throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
      } else {
        return output;
      }
    } catch (Exception e) {
      log.error("Connect Error : Cant connect to {}", jsch.getHostname());
      throw new CustomException(ErrorCode.IOT_SERVER_CONNECTION_ERROR);
    }
  }

  public String getLiveSensorValue(Long farmId) {
    String path = getFarmIdPath(farmId, jsch.getLiveFacility());
    jsch.connect();
    try {
      log.info("Connect to {}", jsch.getHostname());

      InputStream in = jsch.executeCommand(path);

      String output = jsch.readLines(in);
      jsch.close();

      log.info("getLiveSensorValue output = {}", output);

      if (output.equals("0")) {
        throw new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR);
      } else {
        return output;
      }
    } catch (Exception e) {
      log.error("Connect Error : Cant connect to {}", jsch.getHostname());
      throw new CustomException(ErrorCode.IOT_SERVER_CONNECTION_ERROR);
    }
  }

  private String getFarmIdPath(Long farmId, String fileName) {
    StringBuilder sb = new StringBuilder();
    sb.append("./").append(fileName)
        .append(" ").append(farmId);
    log.info("farmId path = {}", sb);
    return sb.toString();
  }
}
