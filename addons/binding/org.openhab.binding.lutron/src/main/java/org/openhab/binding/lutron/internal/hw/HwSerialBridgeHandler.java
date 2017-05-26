/**
 *
 */
package org.openhab.binding.lutron.internal.hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.TooManyListenersException;

import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * @author andrew
 *
 */
public class HwSerialBridgeHandler extends BaseBridgeHandler implements SerialPortEventListener {
    private Logger logger = LoggerFactory.getLogger(HwSerialBridgeHandler.class);

    private boolean connected = false;
    private String serialPortName = "";
    private int baudRate;

    private SerialPort serialPort = null;
    private OutputStreamWriter serialOutput = null;
    private BufferedReader serialInput = null;

    private HwDiscoveryService discService;
    private ServiceRegistration<DiscoveryService> discReg;

    public HwSerialBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing the Lutron HomeWorks RS232 bridge handler");
        HwSerialBridgeConfig configuration = getConfigAs(HwSerialBridgeConfig.class);
        serialPortName = configuration.serialPort;

        this.discService = new HwDiscoveryService(this);
        this.discReg = bundleContext.registerService(DiscoveryService.class, discService, null);

        if (serialPortName != null) {
            baudRate = configuration.baudRate.intValue();

            logger.debug("Lutron HomeWorks RS232 Bridge Handler Initializing.");
            logger.debug("   Serial Port: {},", serialPortName);
            logger.debug("   Baud:        {},", baudRate);

            updateStatus(ThingStatus.OFFLINE);
            openConnection();
        }
    }

    private void openConnection() {
        try {
            logger.debug("openConnection(): Connecting to Lutron HomeWorks");
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
            serialPort = portIdentifier.open(this.getClass().getName(), 2000);
            int db = SerialPort.DATABITS_8, sb = SerialPort.STOPBITS_1, p = SerialPort.PARITY_NONE;
            serialPort.setSerialPortParams(baudRate, db, sb, p);
            serialPort.enableReceiveThreshold(1);
            serialPort.disableReceiveTimeout();
            serialOutput = new OutputStreamWriter(serialPort.getOutputStream(), "US-ASCII");
            serialInput = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            setSerialEventHandler(this);
            setConnected(true);

            sendCommand("PROMPTOFF");
            sendCommand("KPMOFF");
            sendCommand("KLMOFF");
            sendCommand("GSMOFF");
            sendCommand("DLMON"); // Turn on dimmer monitoring

            updateStatus(ThingStatus.ONLINE);
        } catch (NoSuchPortException e) {
            logger.error("openConnection(): No Such Port Exception: {}", e.getMessage());
            setConnected(false);
        } catch (PortInUseException portInUseException) {
            logger.error("openConnection(): Port in Use Exception: {}", portInUseException.getMessage());
            setConnected(false);
        } catch (UnsupportedCommOperationException e) {
            logger.error("openConnection(): Unsupported Comm Operation Exception: {}", e.getMessage());
            setConnected(false);
        } catch (UnsupportedEncodingException e) {
            logger.error("openConnection(): Unsupported Encoding Exception: {}", e.getMessage());
            setConnected(false);
        } catch (IOException ioException) {
            logger.error("openConnection(): IO Exception: {}", ioException.getMessage());
            setConnected(false);
        }
    }

    /**
     * Set the serial event handler.
     *
     * @param serialPortEventListenser
     */
    private void setSerialEventHandler(SerialPortEventListener serialPortEventListenser) {
        try {
            // Add the serial port event listener
            serialPort.addEventListener(serialPortEventListenser);
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            logger.error("setSerialEventHandler(): Too Many Listeners Exception: {}", e.getMessage());
        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // TODO Auto-generated method stub
    }

    private void handleIncomingMessage(String line) {
        if (line == null || line.isEmpty()) {
            return;
        }

        logger.info("Incoming message: " + line);
        String[] data = line.replaceAll("\\s", "").toUpperCase().split(",");
        if ("DL".equals(data[0])) {
            try {
                String address = data[1];
                Integer level = Integer.parseInt(data[2]);
                HwDimmerHandler handler = findHandler(address);
                if (handler == null) {
                    discService.declareUnknownDimmer(address);
                } else {
                    handler.handleLevelChange(level);
                }
            } catch (final Exception e) {
                logger.error("Error parsing incoming message", e);
            }
        }

    }

    private HwDimmerHandler findHandler(String address) {
        for (Thing thing : getThing().getThings()) {
            if (thing.getHandler() instanceof HwDimmerHandler) {
                HwDimmerHandler handler = (HwDimmerHandler) thing.getHandler();
                if (address.equals(handler.getAddress())) {
                    return handler;
                }
            }
        }
        return null;
    }

    /**
     * Receives Serial Port Events and reads Serial Port Data.
     *
     * @param serialPortEvent
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                while (true) {
                    String messageLine = serialInput.readLine();
                    if (messageLine == null) {
                        break;
                    }
                    handleIncomingMessage(messageLine);
                }
            } catch (Exception ioException) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "IO Exception: " + ioException.getMessage());
                logger.error("write(): {}", ioException.getMessage());
            }
        }
    }

    public void sendCommand(String command) {
        try {
            logger.info("About to send command: " + command);
            serialOutput.write(command.toString() + "\r");
            serialOutput.flush();
        } catch (IOException ioException) {
            logger.error("write(): {}", ioException.getMessage());
            setConnected(false);
        } catch (Exception exception) {
            logger.error("write(): Unable to write to serial port: {} ", exception.getMessage(), exception);
            setConnected(false);
        }
    }

    @Override
    public void dispose() {
        serialPort.close();
        serialPort = null;
        serialInput = null;
        serialOutput = null;

        if (this.discReg != null) {
            this.discReg.unregister();
            this.discReg = null;
        }
    }

}
