/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CALL_RING;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CARRIER_INFO_IMSI_ENCRYPTION;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CDMA_CALL_WAITING;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CDMA_INFO_REC;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CDMA_OTA_PROVISION_STATUS;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_CELL_INFO_LIST;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_DATA_CALL_LIST_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_EMERGENCY_NUMBER_LIST;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_HARDWARE_CONFIG_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_KEEPALIVE_STATUS;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_LCEDATA_RECV;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_MODEM_RESTART;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_NETWORK_SCAN_RESULT;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_NITZ_TIME_RECEIVED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_ON_SS;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_ON_USSD;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_PCO_DATA;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_PHYSICAL_CHANNEL_CONFIG;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RADIO_CAPABILITY;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESEND_INCALL_MUTE;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_CDMA_NEW_SMS;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_NETWORK_STATE_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_NEW_SMS;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_NEW_SMS_ON_SIM;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_NEW_SMS_STATUS_REPORT;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RESTRICTED_STATE_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RIL_CONNECTED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RINGBACK_TONE;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_SIGNAL_STRENGTH;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_SIM_REFRESH;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_SIM_SMS_STORAGE_FULL;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_SRVCC_STATE_NOTIFY;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_STK_CALL_SETUP;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_STK_CC_ALPHA_NOTIFY;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_STK_EVENT_NOTIFY;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_STK_PROACTIVE_COMMAND;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_STK_SESSION_END;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_SUPP_SVC_NOTIFICATION;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_UICC_SUBSCRIPTION_STATUS_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOL_VOICE_RADIO_TECH_CHANGED;
import static com.android.internal.telephony.RILConstants.RIL_UNSOl_CDMA_PRL_CHANGED;

import android.hardware.radio.V1_0.CdmaCallWaiting;
import android.hardware.radio.V1_0.CdmaInformationRecord;
import android.hardware.radio.V1_0.CdmaLineControlInfoRecord;
import android.hardware.radio.V1_0.CdmaNumberInfoRecord;
import android.hardware.radio.V1_0.CdmaRedirectingNumberInfoRecord;
import android.hardware.radio.V1_0.CdmaSignalInfoRecord;
import android.hardware.radio.V1_0.CdmaSmsMessage;
import android.hardware.radio.V1_0.CdmaT53AudioControlInfoRecord;
import android.hardware.radio.V1_0.CfData;
import android.hardware.radio.V1_0.LceDataInfo;
import android.hardware.radio.V1_0.PcoDataInfo;
import android.hardware.radio.V1_0.SimRefreshResult;
import android.hardware.radio.V1_0.SsInfoData;
import android.hardware.radio.V1_0.StkCcUnsolSsResult;
import android.hardware.radio.V1_0.SuppSvcNotification;
import android.hardware.radio.V1_2.CellConnectionStatus;
import android.hardware.radio.V1_4.IRadioIndication;
import android.hardware.radio.V1_4.RadioFrequencyInfo.hidl_discriminator;
import android.os.AsyncResult;
import android.os.SystemProperties;
import android.telephony.CellInfo;
import android.telephony.PcoData;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.data.DataCallResponse;
import android.telephony.emergency.EmergencyNumber;

import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;
import com.android.internal.telephony.cdma.SmsMessageConverter;
import com.android.internal.telephony.dataconnection.KeepaliveStatus;
import com.android.internal.telephony.gsm.SsData;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;

import java.util.ArrayList;
import java.util.List;

public class RadioIndication extends IRadioIndication.Stub {
    RIL mRil;

    public RadioIndication(RIL ril) {
        mRil = ril;
    }

    /**
     * Indicates when radio state changes.
     * @param indicationType RadioIndicationType
     * @param radioState android.hardware.radio.V1_0.RadioState
     */
    public void radioStateChanged(int indicationType, int radioState) {
        mRil.processIndication(indicationType);

        int state = getRadioStateFromInt(radioState);
        if (RIL.RILJ_LOGD) {
            mRil.unsljLogMore(RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED, "radioStateChanged: " +
                    state);
        }

        mRil.setRadioState(state, false /* forceNotifyRegistrants */);
    }

    public void callStateChanged(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED);

        mRil.mCallStateRegistrants.notifyRegistrants();
    }

    /**
     * Indicates when either voice or data network state changed
     * @param indicationType RadioIndicationType
     */
    public void networkStateChanged(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_NETWORK_STATE_CHANGED);

        mRil.mNetworkStateRegistrants.notifyRegistrants();
    }

    public void newSms(int indicationType, ArrayList<Byte> pdu) {
        mRil.processIndication(indicationType);

        byte[] pduArray = RIL.arrayListToPrimitiveArray(pdu);
        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_NEW_SMS);

        SmsMessage sms = SmsMessage.newFromCMT(pduArray);
        if (mRil.mGsmSmsRegistrant != null) {
            mRil.mGsmSmsRegistrant.notifyRegistrant(new AsyncResult(null, sms, null));
        }
    }

    public void newSmsStatusReport(int indicationType, ArrayList<Byte> pdu) {
        mRil.processIndication(indicationType);

        byte[] pduArray = RIL.arrayListToPrimitiveArray(pdu);
        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_NEW_SMS_STATUS_REPORT);

        if (mRil.mSmsStatusRegistrant != null) {
            mRil.mSmsStatusRegistrant.notifyRegistrant(new AsyncResult(null, pduArray, null));
        }
    }

    public void newSmsOnSim(int indicationType, int recordNumber) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_NEW_SMS_ON_SIM);

        if (mRil.mSmsOnSimRegistrant != null) {
            mRil.mSmsOnSimRegistrant.notifyRegistrant(new AsyncResult(null, recordNumber, null));
        }
    }

    public void onUssd(int indicationType, int ussdModeType, String msg) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogMore(RIL_UNSOL_ON_USSD, "" + ussdModeType);

        // todo: Clean this up with a parcelable class for better self-documentation
        String[] resp = new String[2];
        resp[0] = "" + ussdModeType;
        resp[1] = msg;
        if (mRil.mUSSDRegistrant != null) {
            mRil.mUSSDRegistrant.notifyRegistrant(new AsyncResult (null, resp, null));
        }
    }

    public void nitzTimeReceived(int indicationType, String nitzTime, long receivedTime) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_NITZ_TIME_RECEIVED, nitzTime);

        // todo: Clean this up with a parcelable class for better self-documentation
        Object[] result = new Object[2];
        result[0] = nitzTime;
        result[1] = receivedTime;

        boolean ignoreNitz = SystemProperties.getBoolean(
                TelephonyProperties.PROPERTY_IGNORE_NITZ, false);

        if (ignoreNitz) {
            if (RIL.RILJ_LOGD) mRil.riljLog("ignoring UNSOL_NITZ_TIME_RECEIVED");
        } else {
            if (mRil.mNITZTimeRegistrant != null) {
                mRil.mNITZTimeRegistrant.notifyRegistrant(new AsyncResult (null, result, null));
            }
            // in case NITZ time registrant isn't registered yet, or a new registrant
            // registers later
            mRil.mLastNITZTimeInfo = result;
        }
    }

    public void currentSignalStrength(int indicationType,
                                      android.hardware.radio.V1_0.SignalStrength signalStrength) {
        mRil.processIndication(indicationType);

        SignalStrength ss = new SignalStrength(signalStrength);
        // Note this is set to "verbose" because it happens frequently
        if (RIL.RILJ_LOGV) mRil.unsljLogvRet(RIL_UNSOL_SIGNAL_STRENGTH, ss);

        if (mRil.mSignalStrengthRegistrant != null) {
            mRil.mSignalStrengthRegistrant.notifyRegistrant(new AsyncResult (null, ss, null));
        }
    }

    /**
     * Indicates current link capacity estimate.
     */
    public void currentLinkCapacityEstimate(int indicationType,
                                            android.hardware.radio.V1_2.LinkCapacityEstimate lce) {
        mRil.processIndication(indicationType);

        LinkCapacityEstimate response = RIL.convertHalLceData(lce, mRil);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_LCEDATA_RECV, response);

        if (mRil.mLceInfoRegistrants != null) {
            mRil.mLceInfoRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
        }
    }

    /**
     * Indicates the current signal strength of the camped or primary serving cell.
     */
    public void currentSignalStrength_1_2(int indicationType,
                                      android.hardware.radio.V1_2.SignalStrength signalStrength) {
        mRil.processIndication(indicationType);

        SignalStrength ss = new SignalStrength(signalStrength);
        // Note this is set to "verbose" because it happens frequently
        if (RIL.RILJ_LOGV) mRil.unsljLogvRet(RIL_UNSOL_SIGNAL_STRENGTH, ss);

        if (mRil.mSignalStrengthRegistrant != null) {
            mRil.mSignalStrengthRegistrant.notifyRegistrant(new AsyncResult(null, ss, null));
        }
    }

    /**
     * Indicates the current signal strength of the camped or primary serving cell.
     */
    public void currentSignalStrength_1_4(int indicationType,
            android.hardware.radio.V1_4.SignalStrength signalStrength) {

        mRil.processIndication(indicationType);

        SignalStrength ss = new SignalStrength(signalStrength);

        if (RIL.RILJ_LOGV) mRil.unsljLogvRet(RIL_UNSOL_SIGNAL_STRENGTH, ss);

        if (mRil.mSignalStrengthRegistrant != null) {
            mRil.mSignalStrengthRegistrant.notifyRegistrant(new AsyncResult(null, ss, null));
        }
    }

    /**
     * Indicates current physical channel configuration.
     */
    public void currentPhysicalChannelConfigs_1_4(int indicationType,
            ArrayList<android.hardware.radio.V1_4.PhysicalChannelConfig> configs) {
        mRil.processIndication(indicationType);
        physicalChannelConfigsIndication(configs);
    }

    /**
     * Indicates current physical channel configuration.
     */
    public void currentPhysicalChannelConfigs(int indicationType,
            ArrayList<android.hardware.radio.V1_2.PhysicalChannelConfig> configs) {
        mRil.processIndication(indicationType);
        physicalChannelConfigsIndication(configs);
    }

    /**
     * Indicates current emergency number list.
     */
    public void currentEmergencyNumberList(int indicationType,
            ArrayList<android.hardware.radio.V1_4.EmergencyNumber> emergencyNumberList) {
        List<EmergencyNumber> response = new ArrayList<>(emergencyNumberList.size());

        for (android.hardware.radio.V1_4.EmergencyNumber emergencyNumberHal
                : emergencyNumberList) {
            EmergencyNumber emergencyNumber = new EmergencyNumber(emergencyNumberHal.number,
                    MccTable.countryCodeForMcc(emergencyNumberHal.mcc), emergencyNumberHal.mnc,
                    emergencyNumberHal.categories, emergencyNumberHal.urns,
                    emergencyNumberHal.sources, EmergencyNumber.EMERGENCY_CALL_ROUTING_UNKNOWN);
            response.add(emergencyNumber);
        }

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_EMERGENCY_NUMBER_LIST, response);

        mRil.mEmergencyNumberListRegistrants.notifyRegistrants(
                new AsyncResult(null, response, null));
    }

    /** Indicates current data call list. */
    public void dataCallListChanged(int indicationType,
            ArrayList<android.hardware.radio.V1_0.SetupDataCallResult> dcList) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_DATA_CALL_LIST_CHANGED, dcList);

        ArrayList<DataCallResponse> response = RIL.convertDataCallResultList(dcList);
        mRil.mDataCallListChangedRegistrants.notifyRegistrants(
                new AsyncResult(null, response, null));
    }

    /** Indicates current data call list with radio HAL 1.4. */
    public void dataCallListChanged_1_4(int indicationType,
            ArrayList<android.hardware.radio.V1_4.SetupDataCallResult> dcList) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_DATA_CALL_LIST_CHANGED, dcList);

        ArrayList<DataCallResponse> response = RIL.convertDataCallResultList(dcList);
        mRil.mDataCallListChangedRegistrants.notifyRegistrants(
                new AsyncResult(null, response, null));
    }

    public void suppSvcNotify(int indicationType, SuppSvcNotification suppSvcNotification) {
        mRil.processIndication(indicationType);

        SuppServiceNotification notification = new SuppServiceNotification();
        notification.notificationType = suppSvcNotification.isMT ? 1 : 0;
        notification.code = suppSvcNotification.code;
        notification.index = suppSvcNotification.index;
        notification.type = suppSvcNotification.type;
        notification.number = suppSvcNotification.number;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_SUPP_SVC_NOTIFICATION, notification);

        if (mRil.mSsnRegistrant != null) {
            mRil.mSsnRegistrant.notifyRegistrant(new AsyncResult (null, notification, null));
        }
    }

    public void stkSessionEnd(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_STK_SESSION_END);

        if (mRil.mCatSessionEndRegistrant != null) {
            mRil.mCatSessionEndRegistrant.notifyRegistrant(new AsyncResult (null, null, null));
        }
    }

    public void stkProactiveCommand(int indicationType, String cmd) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_STK_PROACTIVE_COMMAND);

        if (mRil.mCatProCmdRegistrant != null) {
            mRil.mCatProCmdRegistrant.notifyRegistrant(new AsyncResult (null, cmd, null));
        }
    }

    public void stkEventNotify(int indicationType, String cmd) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_STK_EVENT_NOTIFY);

        if (mRil.mCatEventRegistrant != null) {
            mRil.mCatEventRegistrant.notifyRegistrant(new AsyncResult (null, cmd, null));
        }
    }

    public void stkCallSetup(int indicationType, long timeout) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_STK_CALL_SETUP, timeout);

        if (mRil.mCatCallSetUpRegistrant != null) {
            mRil.mCatCallSetUpRegistrant.notifyRegistrant(new AsyncResult (null, timeout, null));
        }
    }

    public void simSmsStorageFull(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_SIM_SMS_STORAGE_FULL);

        if (mRil.mIccSmsFullRegistrant != null) {
            mRil.mIccSmsFullRegistrant.notifyRegistrant();
        }
    }

    public void simRefresh(int indicationType, SimRefreshResult refreshResult) {
        mRil.processIndication(indicationType);

        IccRefreshResponse response = new IccRefreshResponse();
        response.refreshResult = refreshResult.type;
        response.efId = refreshResult.efId;
        response.aid = refreshResult.aid;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_SIM_REFRESH, response);

        mRil.mIccRefreshRegistrants.notifyRegistrants(new AsyncResult (null, response, null));
    }

    public void callRing(int indicationType, boolean isGsm, CdmaSignalInfoRecord record) {
        mRil.processIndication(indicationType);

        char response[] = null;

        // Ignore record for gsm
        if (!isGsm) {
            // todo: Clean this up with a parcelable class for better self-documentation
            response = new char[4];
            response[0] = (char) (record.isPresent ? 1 : 0);
            response[1] = (char) record.signalType;
            response[2] = (char) record.alertPitch;
            response[3] = (char) record.signal;
            mRil.writeMetricsCallRing(response);
        }

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CALL_RING, response);

        if (mRil.mRingRegistrant != null) {
            mRil.mRingRegistrant.notifyRegistrant(new AsyncResult (null, response, null));
        }
    }

    public void simStatusChanged(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED);

        mRil.mIccStatusChangedRegistrants.notifyRegistrants();
    }

    public void cdmaNewSms(int indicationType, CdmaSmsMessage msg) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_CDMA_NEW_SMS);

        // todo: conversion from CdmaSmsMessage to SmsMessage should be contained in this class so
        // that usage of auto-generated HAL classes is limited to this file
        SmsMessage sms = SmsMessageConverter.newSmsMessageFromCdmaSmsMessage(msg);
        if (mRil.mCdmaSmsRegistrant != null) {
            mRil.mCdmaSmsRegistrant.notifyRegistrant(new AsyncResult(null, sms, null));
        }
    }

    public void newBroadcastSms(int indicationType, ArrayList<Byte> data) {
        mRil.processIndication(indicationType);

        byte response[] = RIL.arrayListToPrimitiveArray(data);
        if (RIL.RILJ_LOGD) {
            mRil.unsljLogvRet(RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS,
                    IccUtils.bytesToHexString(response));
        }

        if (mRil.mGsmBroadcastSmsRegistrant != null) {
            mRil.mGsmBroadcastSmsRegistrant.notifyRegistrant(new AsyncResult(null, response, null));
        }
    }

    public void cdmaRuimSmsStorageFull(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL);

        if (mRil.mIccSmsFullRegistrant != null) {
            mRil.mIccSmsFullRegistrant.notifyRegistrant();
        }
    }

    public void restrictedStateChanged(int indicationType, int state) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogvRet(RIL_UNSOL_RESTRICTED_STATE_CHANGED, state);

        if (mRil.mRestrictedStateRegistrant != null) {
            mRil.mRestrictedStateRegistrant.notifyRegistrant(new AsyncResult (null, state, null));
        }
    }

    public void enterEmergencyCallbackMode(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE);

        if (mRil.mEmergencyCallbackModeRegistrant != null) {
            mRil.mEmergencyCallbackModeRegistrant.notifyRegistrant();
        }
    }

    public void cdmaCallWaiting(int indicationType, CdmaCallWaiting callWaitingRecord) {
        mRil.processIndication(indicationType);

        // todo: create a CdmaCallWaitingNotification constructor that takes in these fields to make
        // sure no fields are missing
        CdmaCallWaitingNotification notification = new CdmaCallWaitingNotification();
        notification.number = callWaitingRecord.number;
        notification.numberPresentation = CdmaCallWaitingNotification.presentationFromCLIP(
                callWaitingRecord.numberPresentation);
        notification.name = callWaitingRecord.name;
        notification.namePresentation = notification.numberPresentation;
        notification.isPresent = callWaitingRecord.signalInfoRecord.isPresent ? 1 : 0;
        notification.signalType = callWaitingRecord.signalInfoRecord.signalType;
        notification.alertPitch = callWaitingRecord.signalInfoRecord.alertPitch;
        notification.signal = callWaitingRecord.signalInfoRecord.signal;
        notification.numberType = callWaitingRecord.numberType;
        notification.numberPlan = callWaitingRecord.numberPlan;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CDMA_CALL_WAITING, notification);

        mRil.mCallWaitingInfoRegistrants.notifyRegistrants(
                new AsyncResult (null, notification, null));
    }

    public void cdmaOtaProvisionStatus(int indicationType, int status) {
        mRil.processIndication(indicationType);

        int response[] = new int[1];
        response[0] = status;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CDMA_OTA_PROVISION_STATUS, response);

        mRil.mOtaProvisionRegistrants.notifyRegistrants(new AsyncResult (null, response, null));
    }

    public void cdmaInfoRec(int indicationType,
                            android.hardware.radio.V1_0.CdmaInformationRecords records) {
        mRil.processIndication(indicationType);

        int numberOfInfoRecs = records.infoRec.size();
        for (int i = 0; i < numberOfInfoRecs; i++) {
            CdmaInformationRecord record = records.infoRec.get(i);
            int id = record.name;
            CdmaInformationRecords cdmaInformationRecords;
            switch (id) {
                case CdmaInformationRecords.RIL_CDMA_DISPLAY_INFO_REC:
                case CdmaInformationRecords.RIL_CDMA_EXTENDED_DISPLAY_INFO_REC:
                    CdmaInformationRecords.CdmaDisplayInfoRec cdmaDisplayInfoRec =
                            new CdmaInformationRecords.CdmaDisplayInfoRec(id,
                            record.display.get(0).alphaBuf);
                    cdmaInformationRecords = new CdmaInformationRecords(cdmaDisplayInfoRec);
                    break;

                case CdmaInformationRecords.RIL_CDMA_CALLED_PARTY_NUMBER_INFO_REC:
                case CdmaInformationRecords.RIL_CDMA_CALLING_PARTY_NUMBER_INFO_REC:
                case CdmaInformationRecords.RIL_CDMA_CONNECTED_NUMBER_INFO_REC:
                    CdmaNumberInfoRecord numInfoRecord = record.number.get(0);
                    CdmaInformationRecords.CdmaNumberInfoRec cdmaNumberInfoRec =
                            new CdmaInformationRecords.CdmaNumberInfoRec(id,
                            numInfoRecord.number,
                            numInfoRecord.numberType,
                            numInfoRecord.numberPlan,
                            numInfoRecord.pi,
                            numInfoRecord.si);
                    cdmaInformationRecords = new CdmaInformationRecords(cdmaNumberInfoRec);
                    break;

                case CdmaInformationRecords.RIL_CDMA_SIGNAL_INFO_REC:
                    CdmaSignalInfoRecord signalInfoRecord = record.signal.get(0);
                    CdmaInformationRecords.CdmaSignalInfoRec cdmaSignalInfoRec =
                            new CdmaInformationRecords.CdmaSignalInfoRec(
                            signalInfoRecord.isPresent ? 1 : 0,
                            signalInfoRecord.signalType,
                            signalInfoRecord.alertPitch,
                            signalInfoRecord.signal);
                    cdmaInformationRecords = new CdmaInformationRecords(cdmaSignalInfoRec);
                    break;

                case CdmaInformationRecords.RIL_CDMA_REDIRECTING_NUMBER_INFO_REC:
                    CdmaRedirectingNumberInfoRecord redirectingNumberInfoRecord =
                            record.redir.get(0);
                    CdmaInformationRecords.CdmaRedirectingNumberInfoRec
                            cdmaRedirectingNumberInfoRec =
                            new CdmaInformationRecords.CdmaRedirectingNumberInfoRec(
                            redirectingNumberInfoRecord.redirectingNumber.number,
                            redirectingNumberInfoRecord.redirectingNumber.numberType,
                            redirectingNumberInfoRecord.redirectingNumber.numberPlan,
                            redirectingNumberInfoRecord.redirectingNumber.pi,
                            redirectingNumberInfoRecord.redirectingNumber.si,
                            redirectingNumberInfoRecord.redirectingReason);
                    cdmaInformationRecords = new CdmaInformationRecords(
                            cdmaRedirectingNumberInfoRec);
                    break;

                case CdmaInformationRecords.RIL_CDMA_LINE_CONTROL_INFO_REC:
                    CdmaLineControlInfoRecord lineControlInfoRecord = record.lineCtrl.get(0);
                    CdmaInformationRecords.CdmaLineControlInfoRec cdmaLineControlInfoRec =
                            new CdmaInformationRecords.CdmaLineControlInfoRec(
                            lineControlInfoRecord.lineCtrlPolarityIncluded,
                            lineControlInfoRecord.lineCtrlToggle,
                            lineControlInfoRecord.lineCtrlReverse,
                            lineControlInfoRecord.lineCtrlPowerDenial);
                    cdmaInformationRecords = new CdmaInformationRecords(cdmaLineControlInfoRec);
                    break;

                case CdmaInformationRecords.RIL_CDMA_T53_CLIR_INFO_REC:
                    CdmaInformationRecords.CdmaT53ClirInfoRec cdmaT53ClirInfoRec =
                            new CdmaInformationRecords.CdmaT53ClirInfoRec(record.clir.get(0).cause);
                    cdmaInformationRecords = new CdmaInformationRecords(cdmaT53ClirInfoRec);
                    break;

                case CdmaInformationRecords.RIL_CDMA_T53_AUDIO_CONTROL_INFO_REC:
                    CdmaT53AudioControlInfoRecord audioControlInfoRecord = record.audioCtrl.get(0);
                    CdmaInformationRecords.CdmaT53AudioControlInfoRec cdmaT53AudioControlInfoRec =
                            new CdmaInformationRecords.CdmaT53AudioControlInfoRec(
                            audioControlInfoRecord.upLink,
                            audioControlInfoRecord.downLink);
                    cdmaInformationRecords = new CdmaInformationRecords(cdmaT53AudioControlInfoRec);
                    break;

                default:
                    throw new RuntimeException("RIL_UNSOL_CDMA_INFO_REC: unsupported record. Got "
                            + CdmaInformationRecords.idToString(id) + " ");
            }

            if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CDMA_INFO_REC, cdmaInformationRecords);
            mRil.notifyRegistrantsCdmaInfoRec(cdmaInformationRecords);
        }
    }

    public void indicateRingbackTone(int indicationType, boolean start) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogvRet(RIL_UNSOL_RINGBACK_TONE, start);

        mRil.mRingbackToneRegistrants.notifyRegistrants(new AsyncResult(null, start, null));
    }

    public void resendIncallMute(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESEND_INCALL_MUTE);

        mRil.mResendIncallMuteRegistrants.notifyRegistrants();
    }

    public void cdmaSubscriptionSourceChanged(int indicationType, int cdmaSource) {
        mRil.processIndication(indicationType);

        int response[] = new int[1];
        response[0] = cdmaSource;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED, response);

        mRil.mCdmaSubscriptionChangedRegistrants.notifyRegistrants(
                new AsyncResult (null, response, null));
    }

    public void cdmaPrlChanged(int indicationType, int version) {
        mRil.processIndication(indicationType);

        int response[] = new int[1];
        response[0] = version;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOl_CDMA_PRL_CHANGED, response);

        mRil.mCdmaPrlChangedRegistrants.notifyRegistrants(
                new AsyncResult (null, response, null));
    }

    public void exitEmergencyCallbackMode(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE);

        mRil.mExitEmergencyCallbackModeRegistrants.notifyRegistrants();
    }

    public void rilConnected(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RIL_CONNECTED);

        // Initial conditions
        mRil.setRadioPower(false, null);
        mRil.setCdmaSubscriptionSource(mRil.mCdmaSubscription, null);
        // todo: this should not require a version number now. Setting it to latest RIL version for
        // now.
        mRil.notifyRegistrantsRilConnectionChanged(15);
    }

    public void voiceRadioTechChanged(int indicationType, int rat) {
        mRil.processIndication(indicationType);

        int response[] = new int[1];
        response[0] = rat;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_VOICE_RADIO_TECH_CHANGED, response);

        mRil.mVoiceRadioTechChangedRegistrants.notifyRegistrants(
                new AsyncResult (null, response, null));
    }

    /** Get unsolicited message for cellInfoList */
    public void cellInfoList(int indicationType,
                             ArrayList<android.hardware.radio.V1_0.CellInfo> records) {
        mRil.processIndication(indicationType);

        ArrayList<CellInfo> response = RIL.convertHalCellInfoList(records);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CELL_INFO_LIST, response);

        mRil.mRilCellInfoListRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
    }

    /** Get unsolicited message for cellInfoList using HAL V1_2 */
    public void cellInfoList_1_2(int indicationType,
                                 ArrayList<android.hardware.radio.V1_2.CellInfo> records) {
        mRil.processIndication(indicationType);

        ArrayList<CellInfo> response = RIL.convertHalCellInfoList_1_2(records);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CELL_INFO_LIST, response);

        mRil.mRilCellInfoListRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
    }

    /** Get unsolicited message for cellInfoList using HAL V1_4 */
    public void cellInfoList_1_4(int indicationType,
                                 ArrayList<android.hardware.radio.V1_4.CellInfo> records) {
        mRil.processIndication(indicationType);

        ArrayList<CellInfo> response = RIL.convertHalCellInfoList_1_4(records);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CELL_INFO_LIST, response);

        mRil.mRilCellInfoListRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
    }

    /** Incremental network scan results */
    public void networkScanResult(int indicationType,
                                  android.hardware.radio.V1_1.NetworkScanResult result) {
        responseNetworkScan(indicationType, result);
    }

    /** Incremental network scan results with HAL V1_2 */
    public void networkScanResult_1_2(int indicationType,
                                      android.hardware.radio.V1_2.NetworkScanResult result) {
        responseNetworkScan_1_2(indicationType, result);
    }

    /** Incremental network scan results with HAL V1_4 */
    public void networkScanResult_1_4(int indicationType,
                                      android.hardware.radio.V1_4.NetworkScanResult result) {
        responseNetworkScan_1_4(indicationType, result);
    }

    public void imsNetworkStateChanged(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLog(RIL_UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED);

        mRil.mImsNetworkStateChangedRegistrants.notifyRegistrants();
    }

    public void subscriptionStatusChanged(int indicationType, boolean activate) {
        mRil.processIndication(indicationType);

        int response[] = new int[1];
        response[0] = activate ? 1 : 0;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_UICC_SUBSCRIPTION_STATUS_CHANGED, response);

        mRil.mSubscriptionStatusRegistrants.notifyRegistrants(
                new AsyncResult (null, response, null));
    }

    public void srvccStateNotify(int indicationType, int state) {
        mRil.processIndication(indicationType);

        int response[] = new int[1];
        response[0] = state;

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_SRVCC_STATE_NOTIFY, response);

        mRil.writeMetricsSrvcc(state);

        mRil.mSrvccStateRegistrants.notifyRegistrants(
                new AsyncResult (null, response, null));
    }

    public void hardwareConfigChanged(
            int indicationType,
            ArrayList<android.hardware.radio.V1_0.HardwareConfig> configs) {
        mRil.processIndication(indicationType);

        ArrayList<HardwareConfig> response = RIL.convertHalHwConfigList(configs, mRil);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_HARDWARE_CONFIG_CHANGED, response);

        mRil.mHardwareConfigChangeRegistrants.notifyRegistrants(
                new AsyncResult (null, response, null));
    }

    public void radioCapabilityIndication(int indicationType,
                                          android.hardware.radio.V1_0.RadioCapability rc) {
        mRil.processIndication(indicationType);

        RadioCapability response = RIL.convertHalRadioCapability(rc, mRil);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_RADIO_CAPABILITY, response);

        mRil.mPhoneRadioCapabilityChangedRegistrants.notifyRegistrants(
                new AsyncResult (null, response, null));
    }

    public void onSupplementaryServiceIndication(int indicationType, StkCcUnsolSsResult ss) {
        mRil.processIndication(indicationType);

        int num;
        SsData ssData = new SsData();

        ssData.serviceType = ssData.ServiceTypeFromRILInt(ss.serviceType);
        ssData.requestType = ssData.RequestTypeFromRILInt(ss.requestType);
        ssData.teleserviceType = ssData.TeleserviceTypeFromRILInt(ss.teleserviceType);
        ssData.serviceClass = ss.serviceClass; // This is service class sent in the SS request.
        ssData.result = ss.result; // This is the result of the SS request.

        if (ssData.serviceType.isTypeCF() &&
                ssData.requestType.isTypeInterrogation()) {
            CfData cfData = ss.cfData.get(0);
            num = cfData.cfInfo.size();
            ssData.cfInfo = new CallForwardInfo[num];

            for (int i = 0; i < num; i++) {
                android.hardware.radio.V1_0.CallForwardInfo cfInfo = cfData.cfInfo.get(i);
                ssData.cfInfo[i] = new CallForwardInfo();

                ssData.cfInfo[i].status = cfInfo.status;
                ssData.cfInfo[i].reason = cfInfo.reason;
                ssData.cfInfo[i].serviceClass = cfInfo.serviceClass;
                ssData.cfInfo[i].toa = cfInfo.toa;
                ssData.cfInfo[i].number = cfInfo.number;
                ssData.cfInfo[i].timeSeconds = cfInfo.timeSeconds;

                mRil.riljLog("[SS Data] CF Info " + i + " : " +  ssData.cfInfo[i]);
            }
        } else {
            SsInfoData ssInfo = ss.ssInfo.get(0);
            num = ssInfo.ssInfo.size();
            ssData.ssInfo = new int[num];
            for (int i = 0; i < num; i++) {
                ssData.ssInfo[i] = ssInfo.ssInfo.get(i);
                mRil.riljLog("[SS Data] SS Info " + i + " : " +  ssData.ssInfo[i]);
            }
        }

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_ON_SS, ssData);

        if (mRil.mSsRegistrant != null) {
            mRil.mSsRegistrant.notifyRegistrant(new AsyncResult(null, ssData, null));
        }
    }

    public void stkCallControlAlphaNotify(int indicationType, String alpha) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_STK_CC_ALPHA_NOTIFY, alpha);

        if (mRil.mCatCcAlphaRegistrant != null) {
            mRil.mCatCcAlphaRegistrant.notifyRegistrant(new AsyncResult (null, alpha, null));
        }
    }

    public void lceData(int indicationType, LceDataInfo lce) {
        mRil.processIndication(indicationType);

        LinkCapacityEstimate response = RIL.convertHalLceData(lce, mRil);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_LCEDATA_RECV, response);

        if (mRil.mLceInfoRegistrants != null) {
            mRil.mLceInfoRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
        }
    }

    public void pcoData(int indicationType, PcoDataInfo pco) {
        mRil.processIndication(indicationType);

        PcoData response = new PcoData(pco.cid,
                pco.bearerProto,
                pco.pcoId,
                RIL.arrayListToPrimitiveArray(pco.contents));

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_PCO_DATA, response);

        mRil.mPcoDataRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
    }

    public void modemReset(int indicationType, String reason) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_MODEM_RESTART, reason);

        mRil.writeMetricsModemRestartEvent(reason);
        mRil.mModemResetRegistrants.notifyRegistrants(new AsyncResult(null, reason, null));
    }

    /**
     * Indicates when the carrier info to encrypt IMSI is being requested
     * @param indicationType RadioIndicationType
     */
    public void carrierInfoForImsiEncryption(int indicationType) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_CARRIER_INFO_IMSI_ENCRYPTION, null);

        mRil.mCarrierInfoForImsiEncryptionRegistrants.notifyRegistrants(
                new AsyncResult(null, null, null));
    }

    /**
     * Indicates a change in the status of an ongoing Keepalive session
     * @param indicationType RadioIndicationType
     * @param halStatus Status of the ongoing Keepalive session
     */
    public void keepaliveStatus(
            int indicationType, android.hardware.radio.V1_1.KeepaliveStatus halStatus) {
        mRil.processIndication(indicationType);

        if (RIL.RILJ_LOGD) {
            mRil.unsljLogRet(RIL_UNSOL_KEEPALIVE_STATUS,
                    "handle=" + halStatus.sessionHandle + " code=" +  halStatus.code);
        }

        KeepaliveStatus ks = new KeepaliveStatus(halStatus.sessionHandle, halStatus.code);
        mRil.mNattKeepaliveStatusRegistrants.notifyRegistrants(new AsyncResult(null, ks, null));
    }

    /**
     * @param stateInt
     * @return {@link TelephonyManager.RadioPowerState RadioPowerState}
     */
    private @TelephonyManager.RadioPowerState int getRadioStateFromInt(int stateInt) {
        int state;

        switch(stateInt) {
            case android.hardware.radio.V1_0.RadioState.OFF:
                state = TelephonyManager.RADIO_POWER_OFF;
                break;
            case android.hardware.radio.V1_0.RadioState.UNAVAILABLE:
                state = TelephonyManager.RADIO_POWER_UNAVAILABLE;
                break;
            case android.hardware.radio.V1_0.RadioState.ON:
                state = TelephonyManager.RADIO_POWER_ON;
                break;
            default:
                throw new RuntimeException("Unrecognized RadioState: " + stateInt);
        }
        return state;
    }


    /**
     * Set the frequency range or channel number from the physical channel config. Only one of them
     * is valid, we should set the other to the unknown value.
     * @param builder the builder of {@link PhysicalChannelConfig}.
     * @param config physical channel config from ril.
     */
    private void setFrequencyRangeOrChannelNumber(PhysicalChannelConfig.Builder builder,
            android.hardware.radio.V1_4.PhysicalChannelConfig config) {

        switch (config.rfInfo.getDiscriminator()) {
            case hidl_discriminator.range:
                builder.setFrequencyRange(config.rfInfo.range());
                break;
            case hidl_discriminator.channelNumber:
                builder.setChannelNumber(config.rfInfo.channelNumber());
                break;
            default:
                mRil.riljLoge("Unsupported frequency type " + config.rfInfo.getDiscriminator());
        }
    }

    private int convertConnectionStatusFromCellConnectionStatus(int status) {
        switch (status) {
            case CellConnectionStatus.PRIMARY_SERVING:
                return PhysicalChannelConfig.CONNECTION_PRIMARY_SERVING;
            case CellConnectionStatus.SECONDARY_SERVING:
                return PhysicalChannelConfig.CONNECTION_SECONDARY_SERVING;
            default:
                // only PRIMARY_SERVING and SECONDARY_SERVING are supported.
                mRil.riljLoge("Unsupported CellConnectionStatus in PhysicalChannelConfig: "
                        + status);
                return PhysicalChannelConfig.CONNECTION_UNKNOWN;
        }
    }

    private void physicalChannelConfigsIndication(List<? extends Object> configs) {
        List<PhysicalChannelConfig> response = new ArrayList<>(configs.size());
        for (Object obj : configs) {
            if (obj instanceof android.hardware.radio.V1_2.PhysicalChannelConfig) {
                android.hardware.radio.V1_2.PhysicalChannelConfig config =
                        (android.hardware.radio.V1_2.PhysicalChannelConfig) obj;

                response.add(new PhysicalChannelConfig.Builder()
                        .setCellConnectionStatus(
                                convertConnectionStatusFromCellConnectionStatus(config.status))
                        .setCellBandwidthDownlinkKhz(config.cellBandwidthDownlink)
                        .build());
            } else if (obj instanceof android.hardware.radio.V1_4.PhysicalChannelConfig) {
                android.hardware.radio.V1_4.PhysicalChannelConfig config =
                        (android.hardware.radio.V1_4.PhysicalChannelConfig) obj;
                PhysicalChannelConfig.Builder builder = new PhysicalChannelConfig.Builder();
                setFrequencyRangeOrChannelNumber(builder, config);
                response.add(builder.setCellConnectionStatus(
                        convertConnectionStatusFromCellConnectionStatus(config.base.status))
                        .setCellBandwidthDownlinkKhz(config.base.cellBandwidthDownlink)
                        .setRat(ServiceState.rilRadioTechnologyToNetworkType(config.rat))
                        .setPhysicalCellId(config.physicalCellId)
                        .setContextIds(config.contextIds.stream().mapToInt(x -> x).toArray())
                        .build());
            } else {
                mRil.riljLoge("Unsupported PhysicalChannelConfig " + obj);
            }
        }

        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_PHYSICAL_CHANNEL_CONFIG, response);

        mRil.mPhysicalChannelConfigurationRegistrants.notifyRegistrants(
                new AsyncResult(null, response, null));
    }

    private void responseNetworkScan(int indicationType,
                                     android.hardware.radio.V1_1.NetworkScanResult result) {
        mRil.processIndication(indicationType);

        NetworkScanResult nsr = null;
        ArrayList<CellInfo> infos = RIL.convertHalCellInfoList(result.networkInfos);
        nsr = new NetworkScanResult(result.status, result.error, infos);
        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_NETWORK_SCAN_RESULT, nsr);
        mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult(null, nsr, null));
    }

    private void responseNetworkScan_1_2(int indicationType,
                                         android.hardware.radio.V1_2.NetworkScanResult result) {
        mRil.processIndication(indicationType);

        NetworkScanResult nsr = null;
        ArrayList<CellInfo> infos = RIL.convertHalCellInfoList_1_2(result.networkInfos);
        nsr = new NetworkScanResult(result.status, result.error, infos);
        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_NETWORK_SCAN_RESULT, nsr);
        mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult(null, nsr, null));
    }

    private void responseNetworkScan_1_4(int indicationType,
                                         android.hardware.radio.V1_4.NetworkScanResult result) {
        mRil.processIndication(indicationType);

        ArrayList<CellInfo> cellInfos = RIL.convertHalCellInfoList_1_4(result.networkInfos);
        NetworkScanResult nsr = new NetworkScanResult(result.status, result.error, cellInfos);
        if (RIL.RILJ_LOGD) mRil.unsljLogRet(RIL_UNSOL_NETWORK_SCAN_RESULT, nsr);
        mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult(null, nsr, null));
    }
}
