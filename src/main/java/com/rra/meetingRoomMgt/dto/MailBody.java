package com.rra.meetingRoomMgt.dto;

import lombok.Builder;

import java.util.Date;
@Builder
public record MailBody(String to , String subject, String text , Date sentDate) {
}
