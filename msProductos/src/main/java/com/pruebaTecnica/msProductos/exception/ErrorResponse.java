package com.pruebaTecnica.msProductos.exception;

public class ErrorResponse {
    private String status;
    private String title;
    private String detail;

    public ErrorResponse(String status, String title, String detail) {
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}