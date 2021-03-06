/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.web.alarm;

import com.navercorp.pinpoint.web.alarm.checker.AlarmChecker;
import com.navercorp.pinpoint.web.alarm.utils.WxNotifyUtils;
import com.navercorp.pinpoint.web.service.UserGroupService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author minwoo.jung
 */
public class DefaultAlarmMessageSender implements AlarmMessageSender {

//    private final MailSender mailSender;
//    private final SmsSender smsSender;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserGroupService userGroupService;

    /*
    @Autowired
    public DefaultAlarmMessageSender(MailSender mailSender, Optional<SmsSender> smsSender) {
        this.mailSender = Objects.requireNonNull(mailSender, "mailSender");
        this.smsSender = smsSender.orElseGet(EmptySmsSender::new);
    }
    */

    @Override
    public void sendSms(AlarmChecker checker, int sequenceCount, StepExecution stepExecution) {
//        this.smsSender.sendSms(checker, sequenceCount, stepExecution);
        List<String> receivers = userGroupService.selectPhoneNumberOfMember(checker.getUserGroupId());
        if (receivers.size() == 0) {
            return;
        }
        List<String> sms = checker.getSmsMessage();
        for (String id : receivers) {
            for (String message : sms) {
                String applicationId = "";
                try {
                    applicationId = checker.getRule().getApplicationId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.error("send SMS : {} {}", applicationId, message);
                // TODO Implement logic for sending SMS
                try {
                    WxNotifyUtils.sendMsg(id, applicationId + " " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void sendEmail(AlarmChecker checker, int sequenceCount, StepExecution stepExecution) {
        // this.mailSender.sendEmail(checker, sequenceCount, stepExecution);
    }
}
