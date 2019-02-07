package com.uiu.helper.KidsHelper.mvp.ui.slides.reminder;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ReminderEntity {

    @SerializedName("id")
    public String id;

    @SerializedName("slide_id")
    public String slide_id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("helper_id")
    public String helper_id;

    @SerializedName("title")
    public String title;

    @SerializedName("note")
    public String note;

    @SerializedName("time")
    public String time;

    @SerializedName("date")
    public String date;

    @SerializedName("file")
    public String reminderNote;

    @SerializedName("file_link")
    public String reminderNoteLink;

    @SerializedName("is_repeated")
    public boolean is_repeated;

    public boolean isActive;

    public Date connverteddate;

    public String getReminderNoteLink() {
        return reminderNoteLink;
    }
    public Date getdate() {
        return connverteddate;
    }

    public void setdate(Date calendar) {
        this.connverteddate = calendar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlide_id() {
        return slide_id;
    }

    public void setSlide_id(String slide_id) {
        this.slide_id = slide_id;
    }

    public String getReminderNote() {
        return reminderNote;
    }

    public void setReminderNote(String reminderNote) {
        this.reminderNote = reminderNote;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHelper_id() {
        return helper_id;
    }

    public void setHelper_id(String helper_id) {
        this.helper_id = helper_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isActiveReminder() {
        return isActive;
    }
    public void setIsActiveReminder(boolean active) {
        isActive = active;
    }

    public boolean getIs_repeated() {
        return is_repeated;
    }

    public void setIs_repeated(boolean is_repeated) {
        this.is_repeated = is_repeated;
    }
}
