package com.studybuddies.server.web.dto.chapter;

import com.studybuddies.server.web.dto.interfaces.ChangeRequest;

public class CheckboxChangeRequest implements ChangeRequest {

    public String title;
    public boolean checked;
}
