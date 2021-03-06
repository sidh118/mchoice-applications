/*
 *   (C) Copyright 2010-2011 hSenid Software International (Pvt) Limited.
 *   All Rights Reserved.
 *
 *   These materials are unpublished, proprietary, confidential source code of
 *   hSenid Software International (Pvt) Limited and constitute a TRADE SECRET
 *   of hSenid Software International (Pvt) Limited.
 *
 *   hSenid Software International (Pvt) Limited retains all title to and intellectual
 *   property rights in these materials.
 *
 */
package hms.ctap.simulator.ui.services;

import com.vaadin.ui.Label;
import hms.ctap.simulator.sms.SmsMessageReceiver;
import hms.ctap.simulator.sms.SmsMessageSender;
import hms.ctap.simulator.ussd.UssdMessageSender;
import hms.sdp.ussd.impl.UssdAoRequestMessage;
import hms.ctap.simulator.sms.SmsAoRequestMessage;

import java.io.IOException;
import java.util.List;

import static hms.ctap.simulator.ussd.UssdMessageReceiver.getReceivedMessages;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public enum NcsService {

    SMS {
        @Override
        public void sendMessage(final String url, final String address, final String message) throws IOException {
            SmsMessageSender.getInstance().sendMessage(url, address, message);
        }

        @Override
        public List receivedMessages() {
            return SmsMessageReceiver.getReceivedMessages();
        }

        @Override
        public void updatePhoneView(Label phoneNum, Label message, Object val) {
            SmsAoRequestMessage smsAoRequestMessage = (SmsAoRequestMessage) val;
            phoneNum.setValue(smsAoRequestMessage.getAddress());
            message.setValue(smsAoRequestMessage.getMessage());
        }
    },

    USSD {
        @Override
        public void sendMessage(final String url, final String address, final String message) throws IOException {
            UssdMessageSender.getInstance().sendMessage(url, address, message);
        }

        @Override
        public List receivedMessages() {
            return getReceivedMessages();
        }

        @Override
        public void updatePhoneView(Label phoneNum, Label message, Object val) {
            UssdAoRequestMessage ussdAoRequestMessage = (UssdAoRequestMessage) val;
            phoneNum.setValue(ussdAoRequestMessage.getAddress());
            message.setValue(ussdAoRequestMessage.getMessage());
        }
    };

    public abstract void sendMessage(final String url, final String address, final String message) throws IOException;

    public abstract List receivedMessages();

    public abstract void updatePhoneView(Label phoneNum, Label message, Object val);
}
