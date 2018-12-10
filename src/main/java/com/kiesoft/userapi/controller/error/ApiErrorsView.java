package com.kiesoft.userapi.controller.error;

import java.util.List;

public class ApiErrorsView {
    private List<ApiFieldError> fieldErrors;
    private List<ApiGlobalError> globalErrors;

    public ApiErrorsView() {
    }

    public List<ApiFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<ApiFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<ApiGlobalError> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(List<ApiGlobalError> globalErrors) {
        this.globalErrors = globalErrors;
    }

    public static final class Builder {
        private List<ApiFieldError> fieldErrors;
        private List<ApiGlobalError> globalErrors;

        public Builder fieldErrors(List<ApiFieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public Builder globalErrors(List<ApiGlobalError> globalErrors) {
            this.globalErrors = globalErrors;
            return this;
        }

        public ApiErrorsView build() {
            ApiErrorsView apiErrorsView = new ApiErrorsView();
            apiErrorsView.setFieldErrors(fieldErrors);
            apiErrorsView.setGlobalErrors(globalErrors);
            return apiErrorsView;
        }
    }
}
