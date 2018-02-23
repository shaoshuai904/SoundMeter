package com.maple.audiometry.utils.permission;


public interface PermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(String[] deniedPermissions);

    void onPermissionDeniedDotAgain(String[] deniedPermissions);
}
