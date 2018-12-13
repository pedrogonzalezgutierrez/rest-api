package com.kiesoft.userapi.controller.error;

import java.util.List;

public class ApiErrorsView {
    private List<ApiFieldError> fieldErrors;

    public ApiErrorsView() {
    }

    public List<ApiFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<ApiFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public static final class Builder {
        private List<ApiFieldError> fieldErrors;

        public Builder fieldErrors(List<ApiFieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public ApiErrorsView build() {
            ApiErrorsView apiErrorsView = new ApiErrorsView();
            apiErrorsView.setFieldErrors(fieldErrors);
            return apiErrorsView;
        }
    }

}
